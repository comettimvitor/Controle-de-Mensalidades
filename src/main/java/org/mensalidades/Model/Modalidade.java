package org.mensalidades.Model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Modalidade {
    private Long id;
    private List<String> diaSemana;
    private String nomeModalidade;
    private String descricao;
    private BigDecimal valorMensalidade;
    private Date inicioVIgencia;
    private Date fimVigencia;

    public Modalidade() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(List<String> diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getNomeModalidade() {
        return nomeModalidade;
    }

    public void setNomeModalidade(String nomeModalidade) {
        this.nomeModalidade = nomeModalidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorMensalidade() {
        return valorMensalidade;
    }

    public void setValorMensalidade(BigDecimal valorMensalidade) {
        this.valorMensalidade = valorMensalidade;
    }

    public Date getInicioVIgencia() {
        return inicioVIgencia;
    }

    public void setInicioVIgencia(Date inicioVIgencia) {
        this.inicioVIgencia = inicioVIgencia;
    }

    public Date getFimVigencia() {
        return fimVigencia;
    }

    public void setFimVigencia(Date fimVigencia) {
        this.fimVigencia = fimVigencia;
    }
}
