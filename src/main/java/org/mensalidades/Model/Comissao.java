package org.mensalidades.Model;

import java.math.BigDecimal;

public class Comissao {
    private Long id;
    private int mes_referente;
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

    public int getMes_referente() {
        return mes_referente;
    }

    public void setMes_referente(int mes_referente) {
        this.mes_referente = mes_referente;
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
