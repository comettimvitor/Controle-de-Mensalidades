package org.mensalidades.Model;

import java.math.BigDecimal;
import java.util.Date;

public class Mensalidade {
    private Long id;
    private int mesReferente;
    private Date vencimento;
    private BigDecimal valor;
    private boolean pago;
    private Turma turma;
    private Aluno aluno;

    public Mensalidade() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMesReferente() {
        return mesReferente;
    }

    public void setMesReferente(int mesReferente) {
        this.mesReferente = mesReferente;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}
