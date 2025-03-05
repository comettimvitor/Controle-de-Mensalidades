package org.mensalidades.Model;

import java.math.BigDecimal;

public class Comissao {
    private Long id;
    private int mesReferente;
    private BigDecimal valor;
    private boolean pago;
    private Instrutor instrutor;

    public Comissao() {}

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

    public Instrutor getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }
}
