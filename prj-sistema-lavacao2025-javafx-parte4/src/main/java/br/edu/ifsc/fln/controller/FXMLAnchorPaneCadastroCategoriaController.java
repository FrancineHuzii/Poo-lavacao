/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroCategoriaController implements Initializable {

    
    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;
    
    @FXML
    private Button btInserir;

    @FXML
    private Label lbNome;

    @FXML
    private Label lbCategoriaId;

    @FXML
    private TableColumn<Veiculo, String> tableColumnCategoriaDescricao;

    @FXML
    private TableView<Veiculo> tableViewCategorias;
    
    private List<Veiculo> listaCategorias;
    private ObservableList<Veiculo> observableListCategorias;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO categoriaDAO = new VeiculoDAO();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        carregarTableViewCategoria();
        
        tableViewCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCategorias(newValue));
    }     
    
    public void carregarTableViewCategoria() {
        tableColumnCategoriaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        listaCategorias = categoriaDAO.listar();
        
        observableListCategorias = FXCollections.observableArrayList(listaCategorias);
        tableViewCategorias.setItems(observableListCategorias);
    }
    
    public void selecionarItemTableViewCategorias(Veiculo categoria) {
        if (categoria != null) {
            lbCategoriaId.setText(String.valueOf(categoria.getId())); 
            lbNome.setText(categoria.getPlaca());
        } else {
            lbCategoriaId.setText(""); 
            lbNome.setText("");
        }
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Veiculo categoria = new Veiculo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCategoriaDialog(categoria);
        if (btConfirmarClicked) {
            categoriaDAO.inserir(categoria);
            carregarTableViewCategoria();
        } 
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Veiculo categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCategoriaDialog(categoria);
            if (btConfirmarClicked) {
                categoriaDAO.alterar(categoria);
                carregarTableViewCategoria();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Categoria na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Veiculo categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            categoriaDAO.remover(categoria);
            carregarTableViewCategoria();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Categoria na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroCategoriaDialog(Veiculo categoria) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroCategoriaController.class.getResource("/view/FXMLAnchorPaneCadastroCategoriaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Categoria");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o obejto categoria para o controller
        FXMLAnchorPaneCadastroCategoriaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCategoria(categoria);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
}
