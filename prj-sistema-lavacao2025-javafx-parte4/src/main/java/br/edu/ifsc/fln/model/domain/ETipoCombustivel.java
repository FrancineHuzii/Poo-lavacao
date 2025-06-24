package br.edu.ifsc.fln.model.domain;

public enum ETipoCombustivel {
    GASOLINA("Gasolina"),
    ETANOL("Etanol"),
    FLEX("Flex"),
    DIESEL("Diesel"),
    GNV("GNV"),
    OUTRO("Outro");

    private String descricao;

    ETipoCombustivel(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
