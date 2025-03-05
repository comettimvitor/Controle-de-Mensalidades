package org.mensalidades.services;

import org.mensalidades.Model.Instrutor;
import org.mensalidades.Model.Pessoa;
import org.mensalidades.Model.Turma;
import org.mensalidades.dao.AcademiaDAO;
import org.mensalidades.dao.TurmaDAO;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

public class TurmaService {
    private final TurmaDAO turmaDAO;

    public TurmaService() {
        this.turmaDAO = new TurmaDAO();
    }

    public void cadastraTurma(String horarioInicio, String horarioFim, String nome, String descricao, BigDecimal valorMensalidade, String diasSemana, Long idInstrutor) {
        turmaDAO.cadastraTurma(horarioInicio, horarioFim, nome, descricao, valorMensalidade, diasSemana, idInstrutor);
    }

    public List<Instrutor> buscaInstrutor() {
        return turmaDAO.buscaInstrutores();
    }

    public List<Turma> buscaTurmas() {
        return turmaDAO.buscaTurmas();
    }

    public void editarTurma(Long idTurma, Long idHorario, Long idModalidade, String horarioInicio, String horarioFim, String nome, String descricao, BigDecimal valorMensalidade, String diasSemana, Long idInstrutor) {
        turmaDAO.editarTurma(idTurma, idHorario, idModalidade, horarioInicio, horarioFim, nome, descricao, valorMensalidade, diasSemana, idInstrutor);
    }
}

