package org.mensalidades.dao;

import org.mensalidades.Model.Endereco;
import org.mensalidades.Model.Instrutor;
import org.mensalidades.Model.Pessoa;
import org.mensalidades.connection.ConexaoDatabase;
import org.mensalidades.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstrutorDAO {

    public void cadastraInstrutor(String rua, String bairro, String numero, String complemento, String cep, String uf, String nome, String telefone, Date dataNasc, String cpf, int comissao) {
        String criaEndereco = "INSERT into sistema.endereco(rua,bairro,numero,complemento,cep,inicio_vigencia,fim_vigencia,uf) VALUES(?,?,?,?,?, CURRENT_DATE,NULL,?) RETURNING id;";
        int idEndereco = 0;

        String criaPessoa = "INSERT INTO sistema.pessoa(id_endereco, nome, data_nascimento, telefone, cpf) VALUES(?, ?, ?, ?, ?) RETURNING id;";
        int idPessoa = 0;

        String criaInstrutor = "INSERT INTO sistema.instrutor(id_pessoa, comissao, inicio_vigencia) VALUES(?, ?, CURRENT_DATE);";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao()) {
            conn.setAutoCommit(false);

            try (PreparedStatement statementEndereco = conn.prepareStatement(criaEndereco);) {

                statementEndereco.setString(1, rua);
                statementEndereco.setString(2, bairro);
                statementEndereco.setString(3, numero);
                statementEndereco.setString(4, complemento);
                statementEndereco.setString(5, cep);
                statementEndereco.setString(6, uf);

                try (ResultSet resultSet = statementEndereco.executeQuery()) {
                    if (resultSet.next()) {
                        idEndereco = resultSet.getInt("id");
                    } else {
                        throw new DatabaseException("Erro ao obter ID do endereco.");
                    }
                }
            }

            try (PreparedStatement statementPessoa = conn.prepareStatement(criaPessoa);) {

                statementPessoa.setInt(1, idEndereco);
                statementPessoa.setString(2, nome);
                statementPessoa.setDate(3, dataNasc);
                statementPessoa.setString(4, telefone);
                statementPessoa.setString(5, cpf);

                try (ResultSet resultSet = statementPessoa.executeQuery()) {
                    if (resultSet.next()) {
                        idPessoa = resultSet.getInt("id");
                    } else {
                        throw new DatabaseException("Erro ao obter ID da Pessoa.");
                    }
                }
            }

            try (PreparedStatement statementInstrutor = conn.prepareStatement(criaInstrutor)) {
                statementInstrutor.setInt(1, idPessoa);
                statementInstrutor.setInt(2, comissao);
                statementInstrutor.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao cadastrar instrutor.");
        }
    }

    public List<Instrutor> buscarInstrutores() {
        List<Instrutor> listaInstrutores = new ArrayList<>();

        String selectInstrutores = "SELECT instrutor.id, instrutor.comissao, pessoa.id AS pessoa_id, pessoa.nome, pessoa.data_nascimento, pessoa.telefone, pessoa.cpf, endereco.id AS endereco_id, endereco.rua, endereco.bairro, endereco.numero, endereco.complemento, " +
                "endereco.cep, endereco.uf FROM sistema.instrutor instrutor " +
                "LEFT JOIN sistema.pessoa pessoa ON pessoa.id = instrutor.id_pessoa " +
                "LEFT JOIN sistema.endereco endereco ON endereco.id = pessoa.id_endereco " +
                "WHERE instrutor.fim_vigencia IS NULL;";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(selectInstrutores);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setId(resultSet.getLong("pessoa_id"));
                pessoa.setNome(resultSet.getString("nome"));
                pessoa.setDataNascimento(resultSet.getDate("data_nascimento"));
                pessoa.setTelefone(resultSet.getString("telefone"));
                pessoa.setCpf(resultSet.getString("cpf"));

                Endereco endereco = new Endereco();
                endereco.setId(resultSet.getLong("endereco_id"));
                endereco.setRua(resultSet.getString("rua"));
                endereco.setBairro(resultSet.getString("bairro"));
                endereco.setNumero(resultSet.getString("numero"));
                endereco.setComplemento(resultSet.getString("complemento"));
                endereco.setCep(resultSet.getString("cep"));
                endereco.setUf(resultSet.getString("uf"));
                pessoa.setEndereco(endereco);

                Instrutor instrutor = new Instrutor();
                instrutor.setId(resultSet.getLong("id"));
                instrutor.setComissao(resultSet.getInt("comissao"));
                instrutor.setPessoa(pessoa);

                listaInstrutores.add(instrutor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao retornar dados para tabela de instrutores.");
        }

        return listaInstrutores;
    }

    public void editarInstrutor(Long idInstrutor, String rua, String bairro, String numero, String complemento, String cep, String uf, String nome, String telefone, Date dataNasc, String cpf, int comissao) {
        String selecionaIds = "SELECT instrutor.id_pessoa, pessoa.id_endereco FROM sistema.instrutor instrutor " +
                "LEFT JOIN sistema.pessoa pessoa ON pessoa.id = instrutor.id_pessoa " +
                "WHERE instrutor.id = ?";

        String updatePessoa = "UPDATE sistema.pessoa SET nome = ?, data_nascimento = ?, telefone = ?, cpf = ? WHERE id = ?;";

        String updateEndereco = "UPDATE sistema.endereco SET rua = ?, bairro = ?, numero = ?, complemento = ?, cep = ?, uf = ? WHERE id = ?";

        String updateInstrutor = "UPDATE sistema.instrutor SET comissao = ? WHERE id = ?;";

        long idPessoa = -1;
        long idEndereco = -1;

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao()) {
            conn.setAutoCommit(false);

            try (PreparedStatement statement = conn.prepareStatement(selecionaIds)) {
                statement.setLong(1, idInstrutor);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        idPessoa = resultSet.getLong("id_pessoa");
                        idEndereco = resultSet.getLong("id_endereco");
                    }
                }
            }

            try (PreparedStatement statementPessoa = conn.prepareStatement(updatePessoa)) {
                statementPessoa.setString(1, nome);
                statementPessoa.setDate(2, dataNasc);
                statementPessoa.setString(3, telefone);
                statementPessoa.setString(4, cpf);
                statementPessoa.setLong(5, idPessoa);
                statementPessoa.executeUpdate();
            }

            try (PreparedStatement statementEndereco = conn.prepareStatement(updateEndereco)) {
                statementEndereco.setString(1, rua);
                statementEndereco.setString(2, bairro);
                statementEndereco.setString(3, numero);
                statementEndereco.setString(4, complemento);
                statementEndereco.setString(5, cep);
                statementEndereco.setString(6, uf);
                statementEndereco.setLong(7, idEndereco);
                statementEndereco.executeUpdate();
            }

            try(PreparedStatement statementInstrutor = conn.prepareStatement(updateInstrutor)) {
                statementInstrutor.setInt(1, comissao);
                statementInstrutor.setLong(2, idInstrutor);
                statementInstrutor.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao alterar dados de Instrutor para edicao.");
        }
    }
}
