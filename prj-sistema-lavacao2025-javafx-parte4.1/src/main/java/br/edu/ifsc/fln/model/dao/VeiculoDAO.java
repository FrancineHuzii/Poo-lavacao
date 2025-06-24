package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VeiculoDAO {

    private Connection connection;
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo(placa, id_modelo, id_cor, id_cliente, observacoes) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setInt(2, veiculo.getModelo().getId());
            stmt.setInt(3, veiculo.getCor().getId());
            stmt.setInt(4, veiculo.getCliente().getId());
            stmt.setString(5, veiculo.getObservacoes());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Veiculo veiculo) {
        String sqlVeiculo = "UPDATE veiculo SET placa=?, id_modelo=?, id_cor=?, id_cliente=?, observacoes=? WHERE id=?";
        String sqlModelo = "UPDATE modelo SET categoria=? WHERE id=?";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmtModelo = connection.prepareStatement(sqlModelo)) {
                stmtModelo.setString(1, veiculo.getModelo().getCategoria().name());
                stmtModelo.setInt(2, veiculo.getModelo().getId());
                stmtModelo.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(sqlVeiculo)) {
                stmt.setString(1, veiculo.getPlaca());
                stmt.setInt(2, veiculo.getModelo().getId());
                stmt.setInt(3, veiculo.getCor().getId());
                stmt.setInt(4, veiculo.getCliente().getId());
                stmt.setString(5, veiculo.getObservacoes());
                stmt.setInt(6, veiculo.getId());
                stmt.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException ex) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public boolean remover(Veiculo veiculo) {
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Veiculo> listar() {
        String sql = "SELECT v.id AS veiculo_id, v.placa AS veiculo_placa, m.id AS id_modelo, m.descricao AS modelo_descricao, c.id AS id_cor, c.nome AS cor_nome, v.observacoes AS veiculo_observacoes, cl.id AS id_cliente, cl.nome AS cliente_nome "
                +       "FROM veiculo v "
                +       "INNER JOIN modelo m ON v.id_modelo = m.id "
                +       "INNER JOIN cor c ON v.id_cor = c.id "
                +       "INNER JOIN cliente cl ON v.id_cliente = cl.id;";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<Veiculo> listagem() {
        String sql = "SELECT v.id AS veiculo_id, v.placa AS veiculo_placa, v.observacoes AS veiculo_observacoes, m.id AS modelo_id, m.descricao AS modelo_descricao, m.categoria AS modelo_categoria, m.id_marca AS modelo_id_marca, c.id AS cor_id, c.nome AS cor_nome, ma.id AS marca_id, ma.nome AS marca_nome, cl.id AS cliente_id, cl.nome AS cliente_nome, cl.celular AS cliente_celular, cl.email AS cliente_email, cl.data_cadastro AS cliente_data_cadastro, pf.cpf AS pessoa_fisica_cpf, pf.data_nascimento AS pessoa_fisica_data_nascimento, pj.cnpj AS pessoa_juridica_cnpj, pj.inscricao_estadual AS pessoa_juridica_inscricao_estadual "
                +       "FROM veiculo v "
                +       "INNER JOIN cor c ON c.id = v.id_cor "
                +       "INNER JOIN modelo m ON m.id = v.id_modelo "
                +       "INNER JOIN marca ma ON ma.id = m.id_marca "
                +       "INNER JOIN cliente cl ON cl.id = v.id_cliente "
                +       "LEFT JOIN pessoa_fisica pf ON pf.id_cliente = cl.id "
                +       "LEFT JOIN pessoa_juridica pj ON pj.id_cliente = cl.id;";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVOFull(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Veiculo buscar(Veiculo veiculo) {
        String sql = "SELECT v.id AS veiculo_id, v.placa AS veiculo_placa, m.id AS id_modelo, m.descricao AS modelo_descricao, c.id AS id_cor, c.nome AS cor_nome, v.observacoes AS veiculo_observacoes "
                +       "FROM veiculo v "
                +       "INNER JOIN modelo m ON v.id_modelo = m.id "
                +       "INNER JOIN cor c ON v.id_cor = c.id WHERE v.id = ?;";
        Veiculo retorno = new Veiculo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Veiculo populateVO(ResultSet resultado) throws SQLException {
        Veiculo veiculo = new Veiculo();
        Modelo modelo = new Modelo();
        Cor cor = new Cor();
        Marca marca = new Marca();
        veiculo.setModelo(modelo);
        veiculo.setCor(cor);

        veiculo.setId(resultado.getInt("veiculo_id"));
        veiculo.setPlaca(resultado.getString("veiculo_placa"));
        modelo.setId(resultado.getInt("modelo_id"));
        modelo.setDescricao(resultado.getString("modelo_descricao"));
        String categoriaStr = resultado.getString("modelo_categoria");
        if (categoriaStr != null) {
            modelo.setCategoria(ECategoria.valueOf(categoriaStr));
        }
        marca.setId(resultado.getInt("marca_id"));
        marca.setNome(resultado.getString("marca_nome"));
        modelo.setMarca(marca);
        cor.setId(resultado.getInt("cor_id"));
        cor.setNome(resultado.getString("cor_nome"));
        veiculo.setObservacoes((resultado.getString("veiculo_observacoes")));
        int id_cliente = resultado.getInt("id_cliente");
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.setConnection(connection);
        Cliente cliente = clienteDAO.buscar(id_cliente);
        veiculo.setCliente(cliente);

        return veiculo;
    }

    private Veiculo populateVOFull(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        Modelo modelo = new Modelo();
        Cor cor = new Cor();
        Marca marca = new Marca();
        veiculo.setModelo(modelo);
        veiculo.setCor(cor);

        veiculo.setId(rs.getInt("veiculo_id"));
        veiculo.setPlaca(rs.getString("veiculo_placa"));
        modelo.setId(rs.getInt("modelo_id"));
        modelo.setDescricao(rs.getString("modelo_descricao"));
        String categoriaStr = rs.getString("modelo_categoria");
        if (categoriaStr != null) {
            modelo.setCategoria(ECategoria.valueOf(categoriaStr));
        }
        marca.setId(rs.getInt("marca_id"));
        marca.setNome(rs.getString("marca_nome"));
        modelo.setMarca(marca);
        cor.setId(rs.getInt("cor_id"));
        cor.setNome(rs.getString("cor_nome"));
        veiculo.setObservacoes(rs.getString("veiculo_observacoes"));
        Cliente cliente;
        if (rs.getString("pessoa_juridica_cnpj") == null) {
            cliente = new PessoaFisica();
            ((PessoaFisica)cliente).setCpf(rs.getString("pessoa_fisica_cpf"));
            Date dataNascimento = rs.getDate("pessoa_fisica_data_nascimento");
            if (dataNascimento != null) {
                ((PessoaFisica)cliente).setDataNascimento(((java.sql.Date) dataNascimento).toLocalDate());
            }
        } else {
            cliente = new PessoaJuridica();
            ((PessoaJuridica)cliente).setCnpj(rs.getString("pessoa_juridica_cnpj"));
            ((PessoaJuridica)cliente).setInscricaoEstadual(rs.getString("pessoa_juridica_inscricao_estadual"));
        }
        cliente.setId(rs.getInt("cliente_id"));
        cliente.setNome(rs.getString("cliente_nome"));
        cliente.setCelular(rs.getString("cliente_celular"));
        cliente.setEmail(rs.getString("cliente_email"));
        Date dataCadastro = rs.getDate("cliente_data_cadastro");
        if(dataCadastro != null) {
            cliente.setDataCadastro(((java.sql.Date) dataCadastro).toLocalDate());
        }
        veiculo.setCliente(cliente);
        return veiculo;
    }
}
