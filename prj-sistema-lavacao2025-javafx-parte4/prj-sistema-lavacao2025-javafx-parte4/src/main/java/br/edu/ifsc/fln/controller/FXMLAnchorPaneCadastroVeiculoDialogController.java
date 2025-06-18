/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;


public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfPlaca;

    @FXML
    private ComboBox<Modelo> cbModelo;

    @FXML
    private ComboBox<Cor> cbCor;

    @FXML
    private TextArea taVeiculoObservacoes;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final CorDAO corDAO = new CorDAO();
    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Veiculo veiculo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);
        corDAO.setConnection(connection);
        carregarComboBoxModelos();
        carregarComboBoxCores();
        setFocusLostHandle();
    }

    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;

    private List<Cor> listaCores;
    private ObservableList<Cor> observableListCores;

    private void carregarComboBoxModelos() {
        listaModelos = modeloDAO.listar();
        observableListModelos = FXCollections.observableArrayList(listaModelos);
        cbModelo.setItems(observableListModelos);
    }

    private void carregarComboBoxCores() {
        listaCores = corDAO.listar();
        observableListCores = FXCollections.observableArrayList(listaCores);
        cbCor.setItems(observableListCores);
    }

    private void setFocusLostHandle() {
        tfPlaca.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if(tfPlaca.getText() == null || tfPlaca.getText().isEmpty()){
                    tfPlaca.requestFocus();
                }
            }
        });
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        if(veiculo.getId() != 0) {
            tfPlaca.setText(veiculo.getPlaca());
            taVeiculoObservacoes.setText(veiculo.getObservacoes());
            cbModelo.getSelectionModel().select(veiculo.getModelo());
            cbCor.getSelectionModel().select(veiculo.getCor());
        }
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setObservacoes(taVeiculoObservacoes.getText());
            veiculo.setModelo(cbModelo.getSelectionModel().getSelectedItem());
            veiculo.setCor(cbCor.getSelectionModel().getSelectedItem());

            btConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }
    
    //método para validar a entrada de dados
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfPlaca.getText() == null || this.tfPlaca.getText().length() == 0) {
            errorMessage += "Descrição inválida.\n";
        }

        if(cbModelo.getSelectionModel().getSelectedItem() == null){
            errorMessage += "Selecione uma marca.\n";
        }

        if(cbCor.getSelectionModel().getSelectedItem() == null){
            errorMessage += "Selecione uma marca.\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
