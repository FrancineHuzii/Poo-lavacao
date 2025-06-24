/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente(nome, celular, email, data_cadastro) VALUES(?,?,?,?)";
        String sqlPF = "INSERT INTO pessoa_fisica(id_cliente, cpf, data_nascimento) VALUES(?,?,?)";
        String sqlPJ = "INSERT INTO pessoa_juridica(id_cliente, cnpj, inscricao_estadual) VALUES(?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3, cliente.getEmail());
            if (cliente.getDataCadastro() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataCadastro()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idCliente = rs.getInt(1);
                cliente.setId(idCliente);
            } else {
                throw new SQLException("Erro ao obter o ID do cliente.");
            }

            if(cliente instanceof PessoaFisica){
                stmt = connection.prepareStatement(sqlPF);
                stmt.setInt(1, cliente.getId());
                stmt.setString(2, ((PessoaFisica) cliente).getCpf());
                LocalDate dataNascimento = ((PessoaFisica) cliente).getDataNascimento();
                if (dataNascimento != null) {
                    stmt.setDate(3, java.sql.Date.valueOf(dataNascimento));
                } else {
                    stmt.setNull(3, java.sql.Types.DATE);
                }
                stmt.executeUpdate();
            } else {
                stmt = connection.prepareStatement(sqlPJ);
                stmt.setInt(1, cliente.getId());
                stmt.setString(2,((PessoaJuridica) cliente).getCnpj());
                stmt.setString(3,((PessoaJuridica) cliente).getInscricaoEstadual());
                stmt.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);

            try {
                connection.rollback();
                System.out.println("Rollback executado com sucesso!!!");
            } catch (SQLException e) {
                System.out.println("Falha na operação rollback...");
                throw new RuntimeException(e);
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome=?, celular=?, email=?, data_cadastro=? WHERE id=?";
        String sqlPF = "UPDATE pessoa_fisica SET cpf=?, data_nascimento=? WHERE id_cliente=?";
        String sqlPJ = "UPDATE pessoa_juridica SET cnpj=?, inscricao_estadual=? WHERE id_cliente=?";
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3,cliente.getEmail());
            LocalDate dataCadastro = cliente.getDataCadastro();
            stmt.setDate(4, (dataCadastro != null ? java.sql.Date.valueOf(dataCadastro) : null));
            stmt.setInt(5, cliente.getId());
            stmt.execute();

            if(cliente instanceof PessoaFisica){
                stmt = connection.prepareStatement(sqlPF);
                stmt.setString(1, ((PessoaFisica) cliente).getCpf());
                LocalDate dataNascimento = ((PessoaFisica) cliente).getDataNascimento();
                stmt.setDate(2, (dataNascimento != null ? java.sql.Date.valueOf(dataNascimento) : null));
                stmt.setInt(3, cliente.getId());
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlPJ);
                stmt.setString(1,((PessoaJuridica) cliente).getCnpj());
                stmt.setString(2,((PessoaJuridica) cliente).getInscricaoEstadual());
                stmt.setInt(3, cliente.getId());
                stmt.execute();
            }
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean remover(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE id=?";
        try {
            if(cliente != null){
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, cliente.getId());
                stmt.execute();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM cliente c "
                      + " left join pessoa_fisica pf on pf.id_cliente = c.id "
                      + " left join pessoa_juridica pj on pj.id_cliente = c.id ";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = populateVO(resultado);
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) {
        String sql = "SELECT * FROM cliente c "
                + "left join pessoa_fisica pf on pf.id_cliente = c.id "
                + "left join pessoa_juridica pj on pj.id_cliente = c.id WHERE id=?";
        Cliente retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Cliente buscar(int id) {
        String sql = "SELECT * FROM cliente c "
                + "left join pessoa_fisica pf on pf.id_cliente = c.id "
                + "left join pessoa_juridica pj on pj.id_cliente = c.id WHERE id=?";
        Cliente retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    private Cliente populateVO(ResultSet rs) throws SQLException {
        Cliente cliente;
        String cnpj = rs.getString("cnpj");
        if(cnpj == null || cnpj.isEmpty()){
            cliente = new PessoaFisica();
            ((PessoaFisica) cliente).setCpf(rs.getString("cpf"));

            Date dataNascimento = rs.getDate("data_nascimento");
            ((PessoaFisica)cliente).setDataNascimento(dataNascimento != null ? dataNascimento.toLocalDate() : null);
        } else {
            cliente = new PessoaJuridica();
            ((PessoaJuridica) cliente).setCnpj(rs.getString("cnpj"));
            ((PessoaJuridica) cliente).setInscricaoEstadual(rs.getString("inscricao_estadual"));
        }
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCelular(rs.getString("celular"));
        cliente.setEmail(rs.getString("email"));

        Date dataCadastro = rs.getDate("data_cadastro");
        cliente.setDataCadastro(dataCadastro != null ? dataCadastro.toLocalDate() : null);
        return cliente;
    }
}
