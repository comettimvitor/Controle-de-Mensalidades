package org.mensalidades.Controller;

import org.mensalidades.Model.Instrutor;
import org.mensalidades.Model.Pessoa;
import org.mensalidades.Model.Turma;
import org.mensalidades.services.TurmaService;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

public class TurmaController {
    private TurmaService turmaService;

    public TurmaController() {
        this.turmaService = new TurmaService();
    }

    public void CadastraTurma(String horarioInicio, String horarioFim, String nome, String descricao, BigDecimal valorMensalidade, String diasSemana, Long idInstrutor) {
        turmaService.cadastraTurma(horarioInicio, horarioFim, nome, descricao, valorMensalidade, diasSemana, idInstrutor);
    }

    public List<Instrutor> buscaInstrutor() {
        return turmaService.buscaInstrutor();
    }

    public List<Turma> buscaTurmas() {
        return turmaService.buscaTurmas();
    }

    public void editarTurma(Long idTurma, Long idHorario, Long idModalidade, String horarioInicio, String horarioFim, String nome, String descricao, BigDecimal valorMensalidade, String diasSemana, Long idInstrutor) {
        turmaService.editarTurma(idTurma, idHorario, idModalidade, horarioInicio, horarioFim, nome, descricao, valorMensalidade, diasSemana, idInstrutor);
    }
}
