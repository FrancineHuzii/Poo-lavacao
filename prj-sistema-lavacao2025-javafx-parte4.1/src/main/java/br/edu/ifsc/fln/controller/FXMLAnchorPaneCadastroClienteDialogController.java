/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Cliente;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    private HBox hbTipoCliente;

    @FXML
    private RadioButton rbtPessoaFisica;

    @FXML
    private RadioButton rbtPessoaJuridica;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfId.setDisable(true);
        // Cria o grupo manualmente
        ToggleGroup tgTipoPessoa = new ToggleGroup();
        rbtPessoaFisica.setToggleGroup(tgTipoPessoa);
        rbtPessoaJuridica.setToggleGroup(tgTipoPessoa);

        // Adiciona listener de seleção
        tgTipoPessoa.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == rbtPessoaFisica) {
                dpDataNascimento.setDisable(false);
                tfInscricaoEstadual.setDisable(true);
                tfInscricaoEstadual.clear();
            } else if (newToggle == rbtPessoaJuridica) {
                dpDataNascimento.setDisable(true);
                dpDataNascimento.setValue(null);
                tfInscricaoEstadual.setDisable(false);
            } else {
                dpDataNascimento.setDisable(true);
                tfInscricaoEstadual.setDisable(true);
            }
        });

        // Desativa os campos inicialmente
        dpDataNascimento.setDisable(true);
        tfInscricaoEstadual.setDisable(true);
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
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        System.out.println(">>> setCliente chamado com ID: " + cliente.getId());
        this.cliente = cliente;
        if (cliente.getId() != 0) {
            tfId.setText(String.valueOf(cliente.getId()));
            tfNome.setText(cliente.getNome());
            tfCelular.setText(cliente.getCelular());
            tfEmail.setText(cliente.getEmail());
            dpDataCadastro.setValue(cliente.getDataCadastro());

            if (cliente instanceof PessoaFisica pf) {
                rbtPessoaFisica.setSelected(true);
                tfCpfCnpj.setText(pf.getCpf());
                dpDataNascimento.setValue(pf.getDataNascimento());
            } else if (cliente instanceof PessoaJuridica pj) {
                rbtPessoaJuridica.setSelected(true);
                tfCpfCnpj.setText(pj.getCnpj());
                tfInscricaoEstadual.setText(pj.getInscricaoEstadual());
            }
        }
        // Atualiza visibilidade dos campos conforme tipo
        if (cliente instanceof PessoaFisica) {
            dpDataNascimento.setDisable(false);
            tfInscricaoEstadual.setDisable(true);
            tfInscricaoEstadual.clear();
        } else {
            dpDataNascimento.setDisable(true);
            dpDataNascimento.setValue(null);
            tfInscricaoEstadual.setDisable(false);
        }
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            if (rbtPessoaFisica.isSelected()) {
                PessoaFisica pf;
                if (this.cliente instanceof PessoaFisica) {
                    pf = (PessoaFisica) this.cliente;
                } else {
                    pf = new PessoaFisica();
                    pf.setId(this.cliente.getId()); // mantém o ID já existente
                }
                pf.setNome(tfNome.getText());
                pf.setCelular(tfCelular.getText());
                pf.setEmail(tfEmail.getText());
                pf.setDataCadastro(dpDataCadastro.getValue() != null ? dpDataCadastro.getValue() : LocalDate.now());
                pf.setCpf(tfCpfCnpj.getText());
                pf.setDataNascimento(dpDataNascimento.getValue());

                this.cliente = pf;
            } else {
                PessoaJuridica pj;
                if (this.cliente instanceof PessoaJuridica) {
                    pj = (PessoaJuridica) this.cliente;
                } else {
                    pj = new PessoaJuridica();
                    pj.setId(this.cliente.getId());
                }
                pj.setNome(tfNome.getText());
                pj.setCelular(tfCelular.getText());
                pj.setEmail(tfEmail.getText());
                pj.setDataCadastro(dpDataCadastro.getValue() != null ? dpDataCadastro.getValue() : LocalDate.now());
                pj.setCnpj(tfCpfCnpj.getText());
                pj.setInscricaoEstadual(tfInscricaoEstadual.getText());

                this.cliente = pj;
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
    public void handleRbtPessoaFisica() {
        this.tfInscricaoEstadual.setText("");
        this.tfInscricaoEstadual.setDisable(true);
    }

    @FXML
    public void handleRbtPessoaJuridica() {
        this.dpDataNascimento.setDisable(true);
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfNome.getText() == null || this.tfNome.getText().length() == 0) {
            errorMessage += "Nome inválido.\n";
        }

        if (this.tfCpfCnpj.getText() == null || this.tfCpfCnpj.getText().length() == 0) {
            errorMessage += "CPF/CNPJ inválido.\n";
        }
        
        if (this.tfCelular.getText() == null || this.tfCelular.getText().length() == 0) {
            errorMessage += "Telefone inválido.\n";
        }

        if (this.tfEmail.getText() == null || this.tfEmail.getText().length() == 0) {
            errorMessage += "Email inválido.\n";
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
