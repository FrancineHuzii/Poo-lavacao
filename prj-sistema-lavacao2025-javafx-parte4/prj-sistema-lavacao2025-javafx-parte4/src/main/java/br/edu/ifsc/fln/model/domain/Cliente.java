/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    protected int id;
    protected String nome;;
    protected String celular;
    protected String email;
    protected LocalDate dataCadastro;

    protected List<Veiculo> veiculos;

    public Cliente() {
    }

    public Cliente(String nome, String celular, String email, LocalDate dataCadastro) {
        this.nome = nome;
        this.celular = celular;
        this.email = email;
        this.dataCadastro = dataCadastro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void add(Veiculo veiculo) {
        if (veiculos == null) {
            veiculos = new ArrayList<>();
        }
        veiculos.add(veiculo);
        veiculo.setCliente(this);
    }

    public void remove(Veiculo veiculo) {
        if (veiculos != null) {
            veiculos.remove(veiculo);
            veiculo.setCliente(null);
        }
    }

    @Override
    public String toString() {
        return nome;
    }
}
