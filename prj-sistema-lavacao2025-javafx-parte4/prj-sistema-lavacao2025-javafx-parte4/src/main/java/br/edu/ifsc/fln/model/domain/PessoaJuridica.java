package br.edu.ifsc.fln.model.domain;

public class PessoaJuridica extends Cliente{

    private String cnpj;
    private String inscricaoEstadual;

    public PessoaJuridica() {
    }

    public PessoaJuridica(String cnpj, String inscricaoEstadual) {
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public String toString() {
        return cnpj;
    }
}
