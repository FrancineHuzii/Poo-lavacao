/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Veiculo;
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

public class FXMLAnchorPaneCadastroVeiculoController implements Initializable {

    
    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;
    
    @FXML
    private Button btInserir;

    @FXML
    private Label lbVeiculoId;

    @FXML
    private Label lbVeiculoPlaca;

    @FXML
    private Label lbVeiculoCategoria;

    @FXML
    private Label lbVeiculoMarca;

    @FXML
    private Label lbVeiculoModelo;

    @FXML
    private Label lbVeiculoCor;

    @FXML
    private TextArea taVeiculoObservacoes;

    @FXML
    private Label lbVeiculoCliente;

    @FXML
    private TableColumn<Veiculo, String> tableColumnPlaca;

    @FXML
    private TableColumn<Veiculo, String> tableColumnModelo;

    @FXML
    private TableView<Veiculo> tableViewVeiculos;
    
    private List<Veiculo> listaVeiculos;
    private ObservableList<Veiculo> observableListVeiculos;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);

        carregarTableViewVeiculos();
        
        tableViewVeiculos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewVeiculos(newValue));
    }     
    
    public void carregarTableViewVeiculos() {

        tableColumnModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        
        listaVeiculos = veiculoDAO.listagem();
        
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        tableViewVeiculos.setItems(observableListVeiculos);
    }
    
    public void selecionarItemTableViewVeiculos(Veiculo veiculo) {
        if (veiculo != null) {
            lbVeiculoId.setText(Integer.toString(veiculo.getId()));
            lbVeiculoPlaca.setText(veiculo.getPlaca());
            ECategoria categoria = veiculo.getModelo().getCategoria();
            lbVeiculoCategoria.setText(categoria.toString());
            Marca marca = veiculo.getModelo().getMarca();
            lbVeiculoMarca.setText(marca.toString());
            lbVeiculoModelo.setText(veiculo.getModelo().getDescricao());
            lbVeiculoCor.setText(veiculo.getCor().getNome());
            lbVeiculoCliente.setText(veiculo.getCliente().getNome());
            taVeiculoObservacoes.setText(veiculo.getObservacoes());

        } else {
            lbVeiculoId.setText("");
            lbVeiculoPlaca.setText("");
            lbVeiculoCategoria.setText("");
            lbVeiculoMarca.setText("");
            lbVeiculoModelo.setText("");
            lbVeiculoCor.setText("");
            lbVeiculoCliente.setText("");
            taVeiculoObservacoes.setText("");
        }
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Veiculo veiculo = new Veiculo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
        if (btConfirmarClicked) {
            veiculoDAO.inserir(veiculo);
            carregarTableViewVeiculos();
        } 
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Veiculo veiculo = tableViewVeiculos.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
            if (btConfirmarClicked) {
                veiculoDAO.alterar(veiculo);
                carregarTableViewVeiculos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Veiculo na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Veiculo veiculo = tableViewVeiculos.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            veiculoDAO.remover(veiculo);
            carregarTableViewVeiculos();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Veiculo na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroVeiculoDialog(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroVeiculoController.class.getResource("/view/FXMLAnchorPaneCadastroVeiculoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Veiculo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o obejto veiculo para o controller
        FXMLAnchorPaneCadastroVeiculoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
}
