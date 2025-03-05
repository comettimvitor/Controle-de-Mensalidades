package org.mensalidades.dao;

import org.mensalidades.Model.*;
import org.mensalidades.connection.ConexaoDatabase;
import org.mensalidades.exceptions.DatabaseException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MensalidadeDAO {

    public List<Mensalidade> geraTabelaMensalidades(List<Long> idAlunos) {
        List<Mensalidade> listaMensalidades = new ArrayList<>();

        String placeholder = idAlunos.stream().map(id -> "?").collect(Collectors.joining(","));

        String selectValor = "SELECT aluno.id AS id_aluno, aluno.desconto, turma.id AS id_turma,modalidade.id AS id_modalidade, modalidade.nome_modalidade, modalidade.valor_mensalidade, pessoa.nome FROM sistema.aluno aluno " +
                "LEFT JOIN sistema.turma turma ON turma.id = aluno.id_turma " +
                "LEFT JOIN sistema.modalidade modalidade ON modalidade.id = turma.id_modalidade " +
                "LEFT JOIN sistema.pessoa pessoa ON pessoa.id = aluno.id_pessoa " +
                "WHERE aluno.id IN(" + placeholder + ");";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(selectValor);) {

            for (int i = 0; i < idAlunos.size(); i++) {
                statement.setLong(i + 1, idAlunos.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Pessoa pessoa = new Pessoa();
                    pessoa.setNome(resultSet.getString("nome"));

                    Aluno aluno = new Aluno();
                    aluno.setId(resultSet.getLong("id_aluno"));
                    aluno.setDesconto(resultSet.getInt("desconto"));
                    aluno.setPessoa(pessoa);

                    Turma turma = new Turma();
                    turma.setId(resultSet.getLong("id_turma"));
                    aluno.setTurma(turma);

                    Modalidade modalidade = new Modalidade();
                    modalidade.setId(resultSet.getLong("id_modalidade"));
                    modalidade.setNomeModalidade(resultSet.getString("nome_modalidade"));
                    modalidade.setValorMensalidade(resultSet.getBigDecimal("valor_mensalidade"));
                    turma.setModalidade(modalidade);

                    Mensalidade mensalidade = new Mensalidade();
                    mensalidade.setAluno(aluno);
                    mensalidade.setTurma(turma);

                    listaMensalidades.add(mensalidade);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao buscar dados para gerar mensalidades.");
        }

        return listaMensalidades;
    }

    public void gerarMensalidades(List<Mensalidade> listaMensalidades) {

        String insereMensalidade = "INSERT INTO sistema.mensalidade(mes_referente, vencimento, valor, id_turma, id_aluno, pago) VALUES(?, ?, ?, ?, ?, false);";

        try(Connection conn = ConexaoDatabase.getInstancia().getConexao();
            PreparedStatement statement = conn.prepareStatement(insereMensalidade)) {

            for(Mensalidade mensalidade : listaMensalidades) {
                statement.setInt(1, mensalidade.getMes_referente());
                statement.setDate(2, (Date) mensalidade.getVencimento());
                statement.setBigDecimal(3, mensalidade.getValor());
                statement.setLong(4, mensalidade.getTurma().getId());
                statement.setLong(5, mensalidade.getAluno().getId());
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao gerar mensalidades.");
        }
    }

    public List<Mensalidade> buscaMensalidades() {
        List<Mensalidade> mensalidades = new ArrayList<>();

        String selectMensalidades = "SELECT mensalidade.id, mensalidade.mes_referente, mensalidade.vencimento, mensalidade.valor, mensalidade.pago, mensalidade.id_turma, mensalidade.id_aluno, pessoa.nome FROM sistema.mensalidade mensalidade " +
                "LEFT JOIN sistema.turma turma ON turma.id = mensalidade.id_turma " +
                "LEFT JOIN sistema.aluno aluno ON aluno.id = mensalidade.id_aluno " +
                "LEFT JOIN sistema.pessoa pessoa ON pessoa.id = aluno.id_pessoa " +
                "ORDER BY mensalidade.id;";

        try(Connection conn = ConexaoDatabase.getInstancia().getConexao();
            PreparedStatement statement = conn.prepareStatement(selectMensalidades);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(resultSet.getString("nome"));

                Turma turma = new Turma();
                turma.setId(resultSet.getLong("id_turma"));

                Aluno aluno = new Aluno();
                aluno.setId(resultSet.getLong("id_aluno"));
                aluno.setPessoa(pessoa);

                Mensalidade mensalidade = new Mensalidade();
                mensalidade.setId(resultSet.getLong("id"));
                mensalidade.setMes_referente(resultSet.getInt("mes_referente"));
                mensalidade.setVencimento(resultSet.getDate("vencimento"));
                mensalidade.setValor(resultSet.getBigDecimal("valor"));
                mensalidade.setPago(resultSet.getBoolean("pago"));
                mensalidade.setTurma(turma);
                mensalidade.setAluno(aluno);

                mensalidades.add(mensalidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao buscar mensalidades.");
        }

        return mensalidades;
    }

    public void pagarMensalidade(List<Long> idMensalidade) {
        String placeholder = idMensalidade.stream().map(id -> "?").collect(Collectors.joining(","));

        String updateMensalidade = "UPDATE sistema.mensalidade SET pago = true WHERE id IN(" + placeholder + ") AND pago = false;";

        try(Connection conn = ConexaoDatabase.getInstancia().getConexao();
            PreparedStatement statement = conn.prepareStatement(updateMensalidade);) {

            for (int i = 0; i < idMensalidade.size(); i++) {
                statement.setLong(i + 1, idMensalidade.get(i));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao pagar mensalidade.");
        }
    }

    public void cancelarPagamentoMensalidade(List<Long> idMensalidade) {
        String placeholder = idMensalidade.stream().map(id -> "?").collect(Collectors.joining(","));

        String updateMensalidade = "UPDATE sistema.mensalidade SET pago = false WHERE id IN(" + placeholder + ") AND pago = true;";

        try(Connection conn = ConexaoDatabase.getInstancia().getConexao();
            PreparedStatement statement = conn.prepareStatement(updateMensalidade);) {

            for (int i = 0; i < idMensalidade.size(); i++) {
                statement.setLong(i + 1, idMensalidade.get(i));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao cancelar pagamento de mensalidade.");
        }
    }
}
