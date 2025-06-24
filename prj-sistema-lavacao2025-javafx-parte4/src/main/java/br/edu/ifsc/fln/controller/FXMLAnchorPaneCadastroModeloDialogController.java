/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Marca;
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

public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfDescricao;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private ComboBox<ECategoria> cbCategoria;

    @FXML
    private TextField tfPotencia;

    @FXML
    private ComboBox<ETipoCombustivel> cbCombustivel;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Modelo modelo;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarComboBoxMarcas();
        carregarComboBoxCategorias();
        carregarComboBoxCombustivel();
        setFocusLostHandle();
    }

    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;

    private ObservableList<ECategoria> observableListCategorias;

    private ObservableList<ETipoCombustivel> observableListCombustivel;

    private void carregarComboBoxMarcas() {
        listaMarcas = marcaDAO.listar();
        observableListMarcas = FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
    }

    private void carregarComboBoxCategorias() {
        observableListCategorias = FXCollections.observableArrayList(ECategoria.values());
        cbCategoria.setItems(observableListCategorias);
    }

    private void carregarComboBoxCombustivel() {
        observableListCombustivel = FXCollections.observableArrayList(ETipoCombustivel.values());
        cbCombustivel.setItems(observableListCombustivel);
    }

    private void setFocusLostHandle() {
        tfDescricao.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if(tfDescricao.getText() == null || tfDescricao.getText().isEmpty()){
                    tfDescricao.requestFocus();
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

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        if(modelo.getId() != 0) {
            tfDescricao.setText(modelo.getDescricao());
            cbMarca.getSelectionModel().select(modelo.getMarca());
            cbCategoria.getSelectionModel().select(modelo.getCategoria());
            tfPotencia.setText(Integer.toString(modelo.getMotor().getPotencia()));
            cbCombustivel.getSelectionModel().select(modelo.getMotor().getCombustivel());
        }
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            modelo.setDescricao(tfDescricao.getText());
            modelo.setMarca(cbMarca.getSelectionModel().getSelectedItem());
            modelo.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
            modelo.getMotor().setPotencia(Integer.parseInt(tfPotencia.getText()));
            modelo.getMotor().setCombustivel(cbCombustivel.getSelectionModel().getSelectedItem());

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
        if (this.tfDescricao.getText() == null || this.tfDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida.\n";
        }

        if(cbMarca.getSelectionModel().getSelectedItem() == null){
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
