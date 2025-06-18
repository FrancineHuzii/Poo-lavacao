/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Modelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroModeloController implements Initializable {

    
    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;
    
    @FXML
    private Button btInserir;

    @FXML
    private Label lbModeloDescricao;

    @FXML
    private Label lbModeloNomeMarca;

    @FXML
    private Label lbModeloId;

    @FXML
    private Label lbModeloMotorPotencia;

    @FXML
    private Label lbModeloMotorCombustivel;

    @FXML
    private Label lbModeloCategoria;

    @FXML
    private TableColumn<Modelo, String> tableColumnModeloDescricao;

    @FXML
    private TableView<Modelo> tableViewModelos;
    
    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);
        carregarTableViewModelo();
        
        tableViewModelos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewModelos(newValue));
    }     
    
    public void carregarTableViewModelo() {
        tableColumnModeloDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        listaModelos = modeloDAO.listar();
        
        observableListModelos = FXCollections.observableArrayList(listaModelos);
        tableViewModelos.setItems(observableListModelos);
    }
    
    public void selecionarItemTableViewModelos(Modelo modelo) {
        if (modelo != null) {
            lbModeloId.setText(Integer.toString(modelo.getId()));
            lbModeloDescricao.setText(modelo.getDescricao());
            lbModeloNomeMarca.setText(modelo.getMarca().getNome());
            lbModeloCategoria.setText(modelo.getCategoria().getDescricao());
            lbModeloMotorPotencia.setText(Integer.toString(modelo.getMotor().getPotencia()));
            lbModeloMotorCombustivel.setText(modelo.getMotor().getCombustivel().getDescricao());

        } else {
            lbModeloId.setText("");
            lbModeloDescricao.setText("");
            lbModeloNomeMarca.setText("");
            lbModeloCategoria.setText("");
            lbModeloMotorPotencia.setText("");
            lbModeloMotorCombustivel.setText("");
        }
        
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Modelo modelo = new Modelo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
        if (btConfirmarClicked) {
            modeloDAO.inserir(modelo);
            carregarTableViewModelo();
        } 
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Modelo modelo = tableViewModelos.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
            if (btConfirmarClicked) {
                modeloDAO.alterar(modelo);
                carregarTableViewModelo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Modelo na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Modelo modelo = tableViewModelos.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            modeloDAO.remover(modelo);
            carregarTableViewModelo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Modelo na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroModeloDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloController.class.getResource("/view/FXMLAnchorPaneCadastroModeloDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Modelo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o obejto modelo para o controller
        FXMLAnchorPaneCadastroModeloDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
}
