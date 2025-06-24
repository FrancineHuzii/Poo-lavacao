package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeloDAO {

    private Connection connection;
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Modelo modelo) {
        final String sql      = "INSERT INTO modelo(descricao, categoria, id_marca) VALUES(?,?,?)";
        final String sqlMotor = "INSERT INTO motor(potencia, combustivel, id_modelo) VALUES (?,?,?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, modelo.getDescricao());
            stmt.setString(2,modelo.getCategoria().name());
            stmt.setInt(3, modelo.getMarca().getId());
            stmt.execute();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                int idModelo = rs.getInt(1);
                modelo.setId(rs.getInt(1));

                stmt = connection.prepareStatement(sqlMotor);
                stmt.setInt(1, modelo.getMotor().getPotencia());
                stmt.setString(2, modelo.getMotor().getCombustivel().name());
                stmt.setInt(3, idModelo);
                stmt.execute();
                return true;
            } else {
                throw new SQLException("Falha ao obter o ID do modelo inserido.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Modelo modelo) {
        final String sql      = "UPDATE modelo SET descricao=?, categoria=?, id_marca=? WHERE id=?";
        final String sqlMotor = "UPDATE motor SET combustivel=?, potencia=? WHERE id_modelo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setString(2, modelo.getCategoria().name());
            stmt.setInt(3, modelo.getMarca().getId());
            stmt.setInt(4, modelo.getId());
            stmt.execute();
            stmt = connection.prepareStatement(sqlMotor);
            stmt.setString(1, modelo.getMotor().getCombustivel().name());
            stmt.setInt(2, modelo.getMotor().getPotencia());
            stmt.setInt(3, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Modelo modelo) {
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Modelo> listar() {
        String sql = "SELECT md.id as modelo_id, md.descricao as modelo_descricao, md.categoria as modelo_categoria, md.id_marca as modelo_marca_id, ma.nome as modelo_marca_nome, mt.potencia as motor_potencia, mt.combustivel as motor_combustivel\n"
                +       "FROM modelo md "
                +       "INNER JOIN motor mt ON md.id = mt.id_modelo "
                +       "INNER JOIN marca ma ON ma.id = md.id_marca ";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Modelo buscar(Modelo modelo) {
        return buscar(modelo.getId());
    }
    
    public Modelo buscar(int id) {
        //String sql = "SELECT mo.id as modelo_id, mo.descricao as modelo_descricao, ma.id as marca_id, ma.nome as marca_nome \n" +
        //        "FROM modelo mo INNER JOIN marca ma ON ma.id = mo.id_marca WHERE mo.id=?";
        String sql = "SELECT * FROM modelo WHERE id=?";
        Modelo retorno = new Modelo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;        
    }

    private Modelo populateVO(ResultSet resultado) throws SQLException {
        Modelo modelo = new Modelo();
        Marca marca = new Marca();
        Motor motor = new Motor();
        modelo.setMarca(marca);
        modelo.setMotor(motor);

        modelo.setId(resultado.getInt("modelo_id"));
        modelo.setDescricao(resultado.getString("modelo_descricao"));
        modelo.setCategoria(ECategoria.valueOf(resultado.getString("modelo_categoria")));

        marca.setId(resultado.getInt("modelo_marca_id"));
        marca.setNome(resultado.getString("modelo_marca_nome"));

        motor.setPotencia(resultado.getInt("motor_potencia"));
        motor.setCombustivel(ETipoCombustivel.valueOf(resultado.getString("motor_combustivel").toUpperCase()));
        return modelo;
    }
}
