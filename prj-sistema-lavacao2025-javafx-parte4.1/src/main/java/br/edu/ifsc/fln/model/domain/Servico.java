/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

public class Servico {
    private int id;
    private String descricao;
    private double valor;
    private static int pontos;
    private ECategoria categoria;

    private double valorPequeno;
    private double valorMedio;
    private double valorGrande;
    private double valorMoto;
    private double valorPadrao;

    public Servico() {
    }

    public Servico(String descricao, ECategoria categoria) {
        this.descricao = descricao;
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

    public int getPontos() {
        return pontos;
    }

    public static void setPontos(int pontos) {
        Servico.pontos = pontos;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }

    public double getValor() {
        if (categoria == null) return 0.0;

        return switch (categoria) {
            case PEQUENO -> valorPequeno;
            case MEDIO   -> valorMedio;
            case GRANDE  -> valorGrande;
            case MOTO    -> valorMoto;
            case PADRAO  -> valorPadrao;
            default -> 0.0;
        };
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValorPequeno() {
        return valorPequeno;
    }

    public void setValorPequeno(double valorPequeno) {
        this.valorPequeno = valorPequeno;
    }

    public double getValorMedio() {
        return valorMedio;
    }

    public void setValorMedio(double valorMedio) {
        this.valorMedio = valorMedio;
    }

    public double getValorGrande() {
        return valorGrande;
    }

    public void setValorGrande(double valorGrande) {
        this.valorGrande = valorGrande;
    }

    public double getValorMoto() {
        return valorMoto;
    }

    public void setValorMoto(double valorMoto) {
        this.valorMoto = valorMoto;
    }

    public double getValorPadrao() {
        return valorPadrao;
    }

    public void setValorPadrao(double valorPadrao) {
        this.valorPadrao = valorPadrao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
