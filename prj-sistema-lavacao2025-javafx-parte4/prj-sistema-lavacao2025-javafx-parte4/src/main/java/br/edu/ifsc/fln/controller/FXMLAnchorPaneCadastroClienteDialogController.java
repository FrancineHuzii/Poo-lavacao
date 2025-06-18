/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Cliente;
import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroClienteDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private DatePicker dpDataNascimento;

    @FXML
    private DatePicker dpDataCadastro;

    @FXML
    private TextField tfCpfCnpj;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfCelular;

    @FXML
    private TextField tfInscricaoEstadual;

    @FXML
    private TextField gbTipo;

    @FXML
    private RadioButton rbPessoaFisica;

    @FXML
    private RadioButton rbPessoaJuridica;
    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if(cliente.getId() != 0) {
            this.tfId.setText(String.valueOf(this.cliente.getId()));
            this.tfNome.setText(this.cliente.getNome());
            this.tfCelular.setText(this.cliente.getCelular());
            this.tfEmail.setText(this.cliente.getEmail());
            dpDataCadastro.setValue(this.cliente.getDataCadastro());
            if(cliente instanceof PessoaFisica){
                rbPessoaFisica.setSelected(true);
                this.tfCpfCnpj.setText(((PessoaFisica) this.cliente).getCpf());
                this.dpDataNascimento.setValue(((PessoaFisica) this.cliente).getDataNascimento());
            }else {
                rbPessoaJuridica.setSelected(true);
                this.tfCpfCnpj.setText(((PessoaJuridica) this.cliente).getCnpj());
                this.tfInscricaoEstadual.setText(((PessoaJuridica) this.cliente).getInscricaoEstadual());
            }
        }
        this.tfNome.requestFocus();
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(tfNome.getText());
            cliente.setCelular(tfCelular.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setDataCadastro(dpDataCadastro.getValue());
            if(rbPessoaFisica.isSelected()){
                ((PessoaFisica) cliente).setCpf(tfCpfCnpj.getText());
                ((PessoaFisica) cliente).setDataNascimento(dpDataNascimento.getValue());
            }else {
                ((PessoaJuridica) cliente).setCnpj(tfCpfCnpj.getText());
                ((PessoaJuridica) cliente).setInscricaoEstadual(tfInscricaoEstadual.getText());
            }

            btConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }

    @FXML
    public void handleRbPessoaFisica() {
        this.tfInscricaoEstadual.setText("");
        this.tfInscricaoEstadual.setDisable(true);
    }

    @FXML
    public void handleRbPessoaJuridica() {
        this.dpDataNascimento.setDisable(true);
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfNome.getText() == null || this.tfNome.getText().length() == 0) {
            errorMessage += "Nome inválido.\n";
        }
        
        if (this.tfCpfCnpj.getText() == null || this.tfCpfCnpj.getText().length() == 0) {
            errorMessage += "CPF inválido.\n";
        }
        
        if (this.tfCelular.getText() == null || this.tfCelular.getText().length() == 0) {
            errorMessage += "Telefone inválido.\n";
        }

        if (this.tfEmail.getText() == null || this.tfEmail.getText().length() == 0) {
            errorMessage += "Telefone inválido.\n";
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
