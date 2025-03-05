package org.mensalidades.dao;

import org.mensalidades.Model.*;
import org.mensalidades.connection.ConexaoDatabase;
import org.mensalidades.exceptions.ComponentException;
import org.mensalidades.exceptions.DatabaseException;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TurmaDAO {

    private Time parseHorario(String horario) {
        if (horario == null || horario.isBlank()) {
            throw new ComponentException("Horario invalido.");
        }

        if (horario.matches("\\d{2}:\\d{2}")) {
            horario += ":00";
        }

        return Time.valueOf(horario);
    }

    private boolean confereHorario(String horarioInicio, String horarioFim) {
        String confereHorario = "SELECT COUNT(1) FROM sistema.horario WHERE horario_inicio = ? AND horario_fim = ? AND fim_vigencia IS NULL;";

        try (Connection connection = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = connection.prepareStatement(confereHorario)) {

            Time horarioInicioSql = parseHorario(horarioInicio);
            Time horarioFimSql = parseHorario(horarioFim);

            statement.setTime(1, horarioInicioSql);
            statement.setTime(2, horarioFimSql);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao verificar horario.");
        }

        return false;
    }

    public void cadastraTurma(String horarioInicio, String horarioFim, String nome, String descricao, BigDecimal valorMensalidade, String diasSemana, Long idInstrutor) {
        String criaHorario = "INSERT INTO sistema.horario(horario_inicio, horario_fim, inicio_vigencia, fim_vigencia) VALUES(?, ?, CURRENT_DATE, NULL) RETURNING id;";

        int idHorario = 0;

        String criaModalidade = "INSERT INTO sistema.modalidade(nome_modalidade, descricao, valor_mensalidade, inicio_vigencia, fim_vigencia, dias_semana) VALUES(?, ?, ?, CURRENT_DATE, NULL, ?) RETURNING id;";

        int idModalidade = 0;

        String criaTurma = "INSERT INTO sistema.turma(id_modalidade, id_horario, informacoes, inicio_vigencia, fim_vigencia, id_instrutor) VALUES(?, ?, NULL, CURRENT_DATE, NULL, ?);";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();) {
            conn.setAutoCommit(false);

            try (PreparedStatement statementHorario = conn.prepareStatement(criaHorario);) {

                Time horarioInicioSql = parseHorario(horarioInicio);
                Time horarioFimSql = parseHorario(horarioFim);

                statementHorario.setTime(1, horarioInicioSql);
                statementHorario.setTime(2, horarioFimSql);

                try (ResultSet resultSetHorario = statementHorario.executeQuery()) {
                    if (resultSetHorario.next()) {
                        idHorario = resultSetHorario.getInt("id");
                    } else {
                        throw new DatabaseException("Erro ao obter ID de horario.");
                    }
                }
            }

            try (PreparedStatement statementModalidade = conn.prepareStatement(criaModalidade);) {

                statementModalidade.setString(1, nome);
                statementModalidade.setString(2, descricao);
                statementModalidade.setBigDecimal(3, valorMensalidade);
                statementModalidade.setString(4, diasSemana);

                try (ResultSet resultSetModalidade = statementModalidade.executeQuery()) {
                    if (resultSetModalidade.next()) {
                        idModalidade = resultSetModalidade.getInt("id");
                    } else {
                        throw new DatabaseException("Erro ao obter ID de modalidade.");
                    }
                }
            }

            try (PreparedStatement statementTurma = conn.prepareStatement(criaTurma)) {
                statementTurma.setInt(1, idModalidade);
                statementTurma.setInt(2, idHorario);
                statementTurma.setLong(3, idInstrutor);
                statementTurma.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao cadastrar Turma.");
        }
    }

    public List<Instrutor> buscaInstrutores() {
        List<Instrutor> listaInstrutores = new ArrayList<>();

        String buscaInstrutor = "SELECT instrutor.id, pessoa.nome FROM sistema.instrutor instrutor \n" +
                "left join sistema.pessoa pessoa on instrutor.id_pessoa = pessoa.id;";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(buscaInstrutor);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(resultSet.getString("nome"));

                listaInstrutores.add(new Instrutor(id, pessoa));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao buscar instrutor.");
        }

        return listaInstrutores;
    }

    public List<Turma> buscaTurmas() {
        List<Turma> listaTurmas = new ArrayList<>();

        String selectTurmas = "SELECT turma.id, horario.id AS id_horario, horario.horario_inicio, horario.horario_fim,modalidade.id AS id_modalidade, modalidade.nome_modalidade, modalidade.descricao, modalidade.valor_mensalidade, pessoa.nome, modalidade.dias_semana, instrutor.id AS id_instrutor FROM sistema.turma turma " +
                "LEFT JOIN sistema.modalidade modalidade ON turma.id_modalidade = modalidade.id " +
                "LEFT JOIN sistema.horario horario ON horario.id = turma.id_horario " +
                "LEFT JOIN sistema.instrutor instrutor ON instrutor.id = turma.id_instrutor " +
                "LEFT JOIN sistema.pessoa pessoa ON pessoa.id = instrutor.id_pessoa;";

        try(Connection conn = ConexaoDatabase.getInstancia().getConexao();
            PreparedStatement statement = conn.prepareStatement(selectTurmas);
            ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()) {
                Horario horario = new Horario();
                horario.setId(resultSet.getLong("id_horario"));
                horario.setHorarioInicio(resultSet.getTime("horario_inicio").toLocalTime());
                horario.setHorarioFim(resultSet.getTime("horario_fim").toLocalTime());

                Modalidade modalidade = new Modalidade();
                modalidade.setId(resultSet.getLong("id_modalidade"));
                modalidade.setNomeModalidade(resultSet.getString("nome_modalidade"));
                modalidade.setValorMensalidade(resultSet.getBigDecimal("valor_mensalidade"));
                modalidade.setDescricao(resultSet.getString("descricao"));
                String dias = resultSet.getString("dias_semana");
                List<String> diasSemana = Arrays.asList(dias.split(","));
                modalidade.setDiaSemana(diasSemana);

                Pessoa pessoa = new Pessoa();
                pessoa.setNome(resultSet.getString("nome"));

                Instrutor instrutor = new Instrutor();
                instrutor.setId(resultSet.getLong("id_instrutor"));
                instrutor.setPessoa(pessoa);

                Turma turma = new Turma();
                turma.setId(resultSet.getLong("id"));
                turma.setModalidade(modalidade);
                turma.setHorario(horario);
                turma.setInstrutor(instrutor);

                listaTurmas.add(turma);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao buscar dados para tabela Turmas");
        }

        return listaTurmas;
    }

    public void editarTurma(Long idTurma, Long idHorario, Long idModalidade, String horarioInicio, String horarioFim, String nome, String descricao, BigDecimal valorMensalidade, String diasSemana, Long idInstrutor) {
        String updateHorario = "UPDATE sistema.horario SET horario_inicio = ?, horario_fim = ? WHERE id = ?;";

        String updateModalidade = "UPDATE sistema.modalidade SET nome_modalidade = ?, descricao = ?, valor_mensalidade = ?, dias_semana = ? WHERE id = ?;";

        String updateTurma = "UPDATE sistema.turma SET id_instrutor = ? WHERE id = ?;";

        try(Connection conn = ConexaoDatabase.getInstancia().getConexao()) {
            conn.setAutoCommit(false);

            try(PreparedStatement statementHorario = conn.prepareStatement(updateHorario)) {
                Time horarioInicioSql = parseHorario(horarioInicio);
                Time horarioFimSql = parseHorario(horarioFim);

                statementHorario.setTime(1, horarioInicioSql);
                statementHorario.setTime(2, horarioFimSql);
                statementHorario.setLong(3, idHorario);
                statementHorario.executeUpdate();
            }

            try(PreparedStatement statementModalidade = conn.prepareStatement(updateModalidade)) {
                statementModalidade.setString(1, nome);
                statementModalidade.setString(2, descricao);
                statementModalidade.setBigDecimal(3, valorMensalidade);
                statementModalidade.setString(4, diasSemana);
                statementModalidade.setLong(5, idModalidade);
                statementModalidade.executeUpdate();
            }

            try(PreparedStatement statementTurma = conn.prepareStatement(updateTurma)) {
                statementTurma.setLong(1, idInstrutor);
                statementTurma.setLong(2, idTurma);
                statementTurma.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao alterar dados de Turma.");
        }

    }
}
