package org.mensalidades.Model;

import java.util.Date;

public class Instrutor {
    private Long id;
    private Pessoa pessoa;
    private Date inicioVigencia;
    private Date fimVIgencia;
    private int comissao;

    public Instrutor() {}

    public Instrutor(Long id, Pessoa pessoa) {
        this.id = id;
        this.pessoa = pessoa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(Date inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public Date getFimVIgencia() {
        return fimVIgencia;
    }

    public void setFimVIgencia(Date fimVIgencia) {
        this.fimVIgencia = fimVIgencia;
    }

    public int getComissao() {
        return comissao;
    }

    public void setComissao(int comissao) {
        this.comissao = comissao;
    }

    @Override
    public String toString() {
        return pessoa.getNome();
    }
}
