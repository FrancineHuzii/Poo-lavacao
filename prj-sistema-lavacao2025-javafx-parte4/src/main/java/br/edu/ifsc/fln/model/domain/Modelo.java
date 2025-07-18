package br.edu.ifsc.fln.model.domain;

import java.util.Objects;

public class Modelo {
    private int id;
    private String descricao;

    private ECategoria categoria;
    private Marca marca;
    private Motor motor = new Motor();

    public Modelo() {
    }

    public Modelo(String descricao, Marca marca, Motor motor, ECategoria categoria) {
        this.descricao = descricao;
        this.marca = marca;
        this.motor = motor;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String setMarca(String modeloIdMarca) {
        return null;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }

    public String setCategoria() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modelo modelo = (Modelo) o;
        return id == modelo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return descricao;
    }
}
