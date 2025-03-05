package org.mensalidades.services;

import org.mensalidades.Model.Comissao;
import org.mensalidades.dao.ComissaoDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ComissaoService {

    private ComissaoDAO comissaoDAO;

    public ComissaoService() {
        this.comissaoDAO = new ComissaoDAO();
    }

    public void gerarTabelaComissao(Date dataInicio, Date dataFim) {
        Map<Long, BigDecimal> listaTotais = comissaoDAO.retornaTotalMensalidades(dataInicio, dataFim);
        Map<Long, Integer> listaPorcentagem = comissaoDAO.retornaPorcentagemComissao();

        Map<Long, BigDecimal> listaFinal = new HashMap<>();

        for(Map.Entry<Long, BigDecimal> entry : listaTotais.entrySet()) {
            Long idInstrutor = entry.getKey();
            BigDecimal totalMensalidade = entry.getValue();

            Integer porcentagem = listaPorcentagem.get(idInstrutor);

            if(porcentagem != null) {
                BigDecimal comissao = totalMensalidade
                        .multiply(BigDecimal.valueOf(porcentagem))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

                listaFinal.put(idInstrutor, comissao);
            } else {
                listaTotais.put(idInstrutor, BigDecimal.ZERO);
            }
        }

        comissaoDAO.gerarComissao(listaFinal);
    }

    public List<Comissao> buscaComissoes() {
        return comissaoDAO.buscaComissoes();
    }

    public void pagarComissao(List<Long> idComissoes) {
        comissaoDAO.pagarComissao(idComissoes);
    }

    public void cancelarPagamentoComissao(List<Long> idComissoes) {
        comissaoDAO.cancelarPagamentoComissao(idComissoes);
    }
}
