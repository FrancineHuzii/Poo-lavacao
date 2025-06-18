/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroServicoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfServicoDescricao;

    @FXML
    private TextField tfServicoPontos;

    @FXML
    private ComboBox<ECategoria> cbServicoCategoria;

    @FXML
    private Spinner<Double> spValorPequeno;

    @FXML
    private Spinner<Double> spValorMedio;

    @FXML
    private Spinner<Double> spValorGrande;

    @FXML
    private Spinner<Double> spValorMoto;

    @FXML
    private Spinner<Double> spValorPadrao;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Servico servico;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicoDAO.setConnection(connection);
        carregarComboBoxCategorias();

        // Inicializa ComboBox com os valores do Enum
        cbServicoCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));

        // Inicializa os Spinners com valores entre 0.0 e 1000.0, step de 5.0
        SpinnerValueFactory.DoubleSpinnerValueFactory factoryPequeno =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 5.0);
        spValorPequeno.setValueFactory(factoryPequeno);

        SpinnerValueFactory.DoubleSpinnerValueFactory factoryMedio =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 5.0);
        spValorMedio.setValueFactory(factoryMedio);

        SpinnerValueFactory.DoubleSpinnerValueFactory factoryGrande =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 5.0);
        spValorGrande.setValueFactory(factoryGrande);

        SpinnerValueFactory.DoubleSpinnerValueFactory factoryMoto =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 5.0);
        spValorMoto.setValueFactory(factoryMoto);

        SpinnerValueFactory.DoubleSpinnerValueFactory factoryPadrao =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 5.0);
        spValorPadrao.setValueFactory(factoryPadrao);

        // Atualiza os spinners visíveis conforme categoria
        cbServicoCategoria.setOnAction(event -> atualizarSpinnersPorCategoria());

        // Inicializa todos como desabilitados até que uma categoria seja selecionada
        atualizarSpinnersPorCategoria();
    }

    private void atualizarSpinnersPorCategoria() {
        ECategoria categoria = cbServicoCategoria.getSelectionModel().getSelectedItem();

        if (categoria == ECategoria.TODAS) {
            spValorPequeno.setDisable(false);
            spValorMedio.setDisable(false);
            spValorGrande.setDisable(false);
            spValorMoto.setDisable(false);
            spValorPadrao.setDisable(false);
        } else {
            spValorPequeno.setDisable(categoria != ECategoria.PEQUENO);
            spValorMedio.setDisable(categoria != ECategoria.MEDIO);
            spValorGrande.setDisable(categoria != ECategoria.GRANDE);
            spValorMoto.setDisable(categoria != ECategoria.MOTO);
            spValorPadrao.setDisable(categoria != ECategoria.PADRAO);
        }
    }

    private ObservableList<ECategoria> observableListCategorias;

    private void carregarComboBoxCategorias() {
        observableListCategorias = FXCollections.observableArrayList(ECategoria.values());
        cbServicoCategoria.setItems(observableListCategorias);
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

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        this.tfServicoDescricao.setText(servico.getDescricao());
        this.tfServicoPontos.setText(String.valueOf(servico.getPontos()));
    }

    @FXML
    public void handleBtConfirmar() {
        if (servico != null) {
            servico.setDescricao(tfServicoDescricao.getText());
            servico.setPontos(Integer.parseInt(tfServicoPontos.getText()));
            servico.setCategoria(cbServicoCategoria.getValue());

            ECategoria categoria = cbServicoCategoria.getValue();
            switch (categoria) {
                case PEQUENO:
                    servico.setValorPequeno(spValorPequeno.getValue());
                    break;
                case MEDIO:
                    servico.setValorMedio(spValorMedio.getValue());
                    break;
                case GRANDE:
                    servico.setValorGrande(spValorGrande.getValue());
                    break;
                case MOTO:
                    servico.setValorMoto(spValorMoto.getValue());
                    break;
                case PADRAO:
                    servico.setValorPadrao(spValorPadrao.getValue());
                    break;
            }

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
        if (this.tfServicoDescricao.getText() == null || this.tfServicoDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida.\n";
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
