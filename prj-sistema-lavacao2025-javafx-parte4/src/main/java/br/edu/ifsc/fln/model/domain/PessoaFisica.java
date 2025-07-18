package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;

public class PessoaFisica extends Cliente{

    private String cpf;
    private LocalDate dataNascimento;

    public PessoaFisica() {
    }

    public PessoaFisica(String cpf, LocalDate dataNascimento) {
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return cpf;
    }
}
