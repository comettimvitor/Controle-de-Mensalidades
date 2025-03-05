package org.mensalidades.Controller;

import org.mensalidades.Model.Comissao;
import org.mensalidades.dao.ComissaoDAO;
import org.mensalidades.services.ComissaoService;

import java.util.Date;
import java.util.List;

public class ComissaoController {

    private ComissaoService comissaoService;

    public ComissaoController() {
        this.comissaoService = new ComissaoService();
    }

    public void gerarTabelaComissao(Date dataInicio, Date dataFim) {
        comissaoService.gerarTabelaComissao(dataInicio, dataFim);
    }

    public List<Comissao> buscaComissoes() {
        return comissaoService.buscaComissoes();
    }

    public void pagarComissao(List<Long> idComissoes) {
        comissaoService.pagarComissao(idComissoes);
    }

    public void cancelarPagamentoComissao(List<Long> idComissoes) {
        comissaoService.cancelarPagamentoComissao(idComissoes);
    }
}
