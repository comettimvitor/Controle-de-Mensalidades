package org.mensalidades.Controller;

import org.mensalidades.Model.Mensalidade;
import org.mensalidades.services.MensalidadeService;

import java.util.List;

public class MensalidadeController {

    private final MensalidadeService mensalidadeService;

    public MensalidadeController() {
        this.mensalidadeService = new MensalidadeService();
    }

    public List<Mensalidade> gerarTabelaMensalidade(List<Long> idAlunos) {
        return mensalidadeService.gerarTabelaMensalidades(idAlunos);
    }

    public void gerarMensalidades(List<Mensalidade> listaMensalidades) {
        mensalidadeService.gerarMensalidades(listaMensalidades);
    }
    public List<Mensalidade> buscaMensalidades() {
        return mensalidadeService.buscaMensalidades();
    }

    public void pagarMensalidade(List<Long> idMensalidade) {
        mensalidadeService.pagarMensalidade(idMensalidade);
    }

    public void cancelarPagamentoMensalidade(List<Long> idMensalidade) {
        mensalidadeService.cancelarPagamentoMensalidade(idMensalidade);
    }
}
