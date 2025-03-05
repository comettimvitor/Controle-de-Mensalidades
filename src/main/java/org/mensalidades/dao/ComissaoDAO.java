package org.mensalidades.dao;

import org.mensalidades.Model.Comissao;
import org.mensalidades.Model.Instrutor;
import org.mensalidades.Model.Pessoa;
import org.mensalidades.connection.ConexaoDatabase;
import org.mensalidades.exceptions.DatabaseException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ComissaoDAO {

    public Map<Long, BigDecimal> retornaTotalMensalidades(Date dataInicio, Date dataFim) {
        Long idInstrutor = 0L;
        BigDecimal totalMensalidades = BigDecimal.ZERO;
        Map<Long, BigDecimal> resultado = new HashMap<>();

        String selectMensalidades = "SELECT instrutor.id, SUM(mensalidade.valor) AS valor_mensalidade FROM sistema.mensalidade mensalidade " +
                "LEFT JOIN sistema.turma turma ON turma.id = mensalidade.id_turma " +
                "LEFT JOIN sistema.instrutor instrutor ON instrutor.id = turma.id_instrutor " +
                "LEFT JOIN sistema.pessoa pessoa ON pessoa.id = instrutor.id_pessoa " +
                "WHERE vencimento BETWEEN ? AND ? " +
                "GROUP BY instrutor.id;";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(selectMensalidades);) {

            statement.setDate(1, new java.sql.Date(dataInicio.getTime()));
            statement.setDate(2, new java.sql.Date(dataFim.getTime()));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    idInstrutor = resultSet.getLong("id");
                    totalMensalidades = resultSet.getBigDecimal("valor_mensalidade");

                    if (totalMensalidades == null) {
                        totalMensalidades = BigDecimal.ZERO;
                    }

                    resultado.put(idInstrutor, totalMensalidades);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao calcular total de mensalidades.");
        }

        return resultado;
    }

    public Map<Long, Integer> retornaPorcentagemComissao() {
        Long idInstrutor = 0L;
        int porcentagem = 0;

        Map<Long, Integer> listaPorcentagemComissoes = new HashMap<>();

        String selectPorcentagem = "SELECT id, comissao FROM sistema.instrutor WHERE fim_vigencia is null";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(selectPorcentagem);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                idInstrutor = resultSet.getLong("id");
                porcentagem = resultSet.getInt("comissao");

                listaPorcentagemComissoes.put(idInstrutor, porcentagem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao buscar porcentagem de comissoes.");
        }

        return listaPorcentagemComissoes;
    }

    public void gerarComissao(Map<Long, BigDecimal> listaCalculada) {

        String insertComissoes = "INSERT INTO sistema.comissao(mes_referente, valor, pago, id_instrutor) VALUES(?, ?, false, ?);";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(insertComissoes)) {

            for (Map.Entry<Long, BigDecimal> entry : listaCalculada.entrySet()) {
                statement.setInt(1, LocalDate.now().getMonthValue());
                statement.setBigDecimal(2, entry.getValue());
                statement.setLong(3, entry.getKey());
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao inserir comissoes.");
        }
    }

    public List<Comissao> buscaComissoes() {
        List<Comissao> comissoes = new ArrayList<>();

        String selectComissoes = "SELECT comissao.id, comissao.mes_referente, comissao.valor, comissao.pago, comissao.id_instrutor, pessoa.nome FROM sistema.comissao comissao " +
                "LEFT JOIN sistema.instrutor instrutor ON instrutor.id = comissao.id_instrutor " +
                "LEFT JOIN sistema.pessoa pessoa ON pessoa.id = instrutor.id_pessoa;";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(selectComissoes);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(resultSet.getString("nome"));

                Instrutor instrutor = new Instrutor();
                instrutor.setId(resultSet.getLong("id_instrutor"));
                instrutor.setPessoa(pessoa);

                Comissao comissao = new Comissao();
                comissao.setId(resultSet.getLong("id"));
                comissao.setMes_referente(resultSet.getInt("mes_referente"));
                comissao.setValor(resultSet.getBigDecimal("valor"));
                comissao.setPago(resultSet.getBoolean("pago"));
                comissao.setInstrutor(instrutor);

                comissoes.add(comissao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao buscar dados de comissoes.");
        }

        return comissoes;
    }

    public void pagarComissao(List<Long> idComissoes) {
        String placeholder = idComissoes.stream().map(id -> "?").collect(Collectors.joining(","));

        String updateComissao = "UPDATE sistema.comissao SET pago = true WHERE id IN(" + placeholder + ") AND pago = false;";

        try(Connection conn = ConexaoDatabase.getInstancia().getConexao();
            PreparedStatement statement = conn.prepareStatement(updateComissao);) {

            for(int i = 0; i < idComissoes.size(); i++) {
                statement.setLong(i + 1, idComissoes.get(i));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao pagar comissao.");
        }
    }

    public void cancelarPagamentoComissao(List<Long> idComissoes) {
        String placeholder = idComissoes.stream().map(id -> "?").collect(Collectors.joining(","));

        String updateComissao = "UPDATE sistema.comissao SET pago = false WHERE id IN(" + placeholder + ") AND pago = true;";

        try(Connection conn = ConexaoDatabase.getInstancia().getConexao();
            PreparedStatement statement = conn.prepareStatement(updateComissao);) {

            for(int i = 0; i < idComissoes.size(); i++) {
                statement.setLong(i + 1, idComissoes.get(i));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao cancelar pagamento de comissao.");
        }
    }
}
