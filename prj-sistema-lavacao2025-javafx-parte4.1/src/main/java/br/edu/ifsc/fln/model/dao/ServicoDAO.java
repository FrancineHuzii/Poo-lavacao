package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicoDAO {

    private Connection connection;
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Servico servico) {
        String sql = "INSERT INTO servico(descricao, valor, categoria) VALUES(?,?,?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            // Caso "TODAS": insere uma linha para cada categoria com valores distintos
            if (servico.getCategoria() == ECategoria.TODAS) {
                // PEQUENO
                stmt.setString(1, servico.getDescricao());
                stmt.setDouble(2, servico.getValorPequeno());
                stmt.setString(3, ECategoria.PEQUENO.name());
                stmt.execute();

                // MEDIO
                stmt.setString(1, servico.getDescricao());
                stmt.setDouble(2, servico.getValorMedio());
                stmt.setString(3, ECategoria.MEDIO.name());
                stmt.execute();

                // GRANDE
                stmt.setString(1, servico.getDescricao());
                stmt.setDouble(2, servico.getValorGrande());
                stmt.setString(3, ECategoria.GRANDE.name());
                stmt.execute();

                // MOTO
                stmt.setString(1, servico.getDescricao());
                stmt.setDouble(2, servico.getValorMoto());
                stmt.setString(3, ECategoria.MOTO.name());
                stmt.execute();

                // PADRAO
                stmt.setString(1, servico.getDescricao());
                stmt.setDouble(2, servico.getValorPadrao());
                stmt.setString(3, ECategoria.PADRAO.name());
                stmt.execute();
            } else {
                // Categoria única: insere apenas uma linha com o valor correto
                stmt.setString(1, servico.getDescricao());

                switch (servico.getCategoria()) {
                    case PEQUENO -> stmt.setDouble(2, servico.getValorPequeno());
                    case MEDIO -> stmt.setDouble(2, servico.getValorMedio());
                    case GRANDE -> stmt.setDouble(2, servico.getValorGrande());
                    case MOTO -> stmt.setDouble(2, servico.getValorMoto());
                    case PADRAO -> stmt.setDouble(2, servico.getValorPadrao());
                }

                stmt.setString(3, servico.getCategoria().name());
                stmt.execute();
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Servico servico) {
        String sql = "UPDATE servico SET descricao=?, categoria=?, valor=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            if (servico.getCategoria() != null) {
                stmt.setString(2, servico.getCategoria().name());
                stmt.setDouble(3, servico.getValor());
                stmt.setInt(4, servico.getId());
                stmt.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Servico servico) {
        String sql = "DELETE FROM servico WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Servico> listar() {
        String sql = "SELECT * FROM servico";
        List<Servico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Servico servico = new Servico();
                servico.setId(resultado.getInt("id"));
                servico.setDescricao(resultado.getString("descricao"));
                servico.setCategoria(ECategoria.valueOf(resultado.getString("categoria")));
                servico.setPontos(resultado.getInt("pontos"));

                double valor = resultado.getDouble("valor");
                ECategoria categoria = servico.getCategoria();
                switch (categoria) {
                    case PEQUENO -> servico.setValorPequeno(valor);
                    case MEDIO   -> servico.setValorMedio(valor);
                    case GRANDE  -> servico.setValorGrande(valor);
                    case MOTO    -> servico.setValorMoto(valor);
                    case PADRAO  -> servico.setValorPadrao(valor);
                }
                retorno.add(servico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
