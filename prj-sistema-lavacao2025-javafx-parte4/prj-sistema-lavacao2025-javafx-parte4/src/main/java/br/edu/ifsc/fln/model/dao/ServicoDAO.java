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
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValorPequeno());
            stmt.setString(3, servico.getCategoria().name());
            stmt.execute();

            stmt.setDouble(2, servico.getValorMedio());
            stmt.setString(3, servico.getCategoria().name());
            stmt.execute();

            stmt.setDouble(2, servico.getValorGrande());
            stmt.setString(3, servico.getCategoria().name());
            stmt.execute();

            stmt.setDouble(2, servico.getValorMoto());
            stmt.setString(3, servico.getCategoria().name());
            stmt.execute();

            stmt.setDouble(2, servico.getValorPadrao());
            stmt.setString(3, servico.getCategoria().name());
            stmt.execute();
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

                switch (servico.getCategoria()) {
                    case PEQUENO -> stmt.setDouble(3, servico.getValorPequeno());
                    case MEDIO   -> stmt.setDouble(3, servico.getValorMedio());
                    case GRANDE  -> stmt.setDouble(3, servico.getValorGrande());
                    case MOTO    -> stmt.setDouble(3, servico.getValorMoto());
                    case PADRAO  -> stmt.setDouble(3, servico.getValorPadrao());
                    default      -> stmt.setNull(3, java.sql.Types.DOUBLE);
                }
            } else {
                stmt.setNull(2, java.sql.Types.VARCHAR);
                stmt.setNull(3, java.sql.Types.DOUBLE);
            }

            stmt.setInt(4, servico.getId());
            stmt.executeUpdate();

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
            stmt.execute();
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
                //servico.setValor(resultado.getDouble("valor"));
                //servico.setCategoria(ECategoria.valueOf(resultado.getString("categoria")));
                servico.setPontos(resultado.getInt("pontos"));
                retorno.add(servico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Servico buscar(Servico servico) {
        Servico retorno = buscar(servico.getId());
        return retorno;
    }
    
    public Servico buscar(int id) {
        String sql = "SELECT * FROM servico WHERE id=?";
        Servico retorno = new Servico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setId(resultado.getInt("id"));
                retorno.setDescricao(resultado.getString("descricao"));
                if(retorno.getCategoria() == ECategoria.PEQUENO){
                    retorno.setValorPequeno(resultado.getDouble("valor"));
                } else if(retorno.getCategoria() == ECategoria.MEDIO){
                    retorno.setValorMedio(resultado.getDouble("valor"));
                } else if(retorno.getCategoria() == ECategoria.GRANDE){
                    retorno.setValorGrande(resultado.getDouble("valor"));
                } else if(retorno.getCategoria() == ECategoria.MOTO){
                    retorno.setValorMoto(resultado.getDouble("valor"));
                } else if(retorno.getCategoria() == ECategoria.PADRAO){
                    retorno.setValorPadrao(resultado.getDouble("valor"));
                }
                retorno.setCategoria(ECategoria.valueOf(resultado.getString("categoria")));
                retorno.setPontos(resultado.getInt("pontos"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;        
    }
}
