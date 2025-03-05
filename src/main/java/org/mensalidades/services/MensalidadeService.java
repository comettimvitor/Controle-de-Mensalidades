package org.mensalidades.services;

import org.mensalidades.Model.Aluno;
import org.mensalidades.Model.Mensalidade;
import org.mensalidades.dao.MensalidadeDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MensalidadeService {

    private final MensalidadeDAO mensalidadeDAO;

    public MensalidadeService() {
        this.mensalidadeDAO = new MensalidadeDAO();
    }

    public List<Mensalidade> gerarTabelaMensalidades(List<Long> idAlunos) {
        List<Mensalidade> listaMensalidades = mensalidadeDAO.geraTabelaMensalidades(idAlunos);

        for(Mensalidade mensalidade : listaMensalidades) {
            int porcentagem = mensalidade.getAluno().getDesconto();
            BigDecimal valor = mensalidade.getTurma().getModalidade().getValorMensalidade();
            mensalidade.setValor(calculoDesconto(porcentagem, valor));
        }

        return listaMensalidades;
    }

    private BigDecimal calculoDesconto(int porcentagem, BigDecimal valor) {
        BigDecimal desconto = valor
                .multiply(BigDecimal.valueOf(porcentagem))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);

        return valor.subtract(desconto);
    }

    public void gerarMensalidades(List<Mensalidade> listaMensalidades) {
        mensalidadeDAO.gerarMensalidades(listaMensalidades);
    }

    public List<Mensalidade> buscaMensalidades() {
        return mensalidadeDAO.buscaMensalidades();
    }

    public void pagarMensalidade(List<Long> idMensalidade) {
        mensalidadeDAO.pagarMensalidade(idMensalidade);
    }

    public void cancelarPagamentoMensalidade(List<Long> idMensalidade) {
        mensalidadeDAO.cancelarPagamentoMensalidade(idMensalidade);
    }
}
