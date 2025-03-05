package org.mensalidades.dao;

import org.mensalidades.Model.*;
import org.mensalidades.connection.ConexaoDatabase;
import org.mensalidades.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlunoDAO {

    public void cadastraAluno(String rua, String bairro, String numero, String complemento, String cep, String uf, String nome, String telefone, Date dataNasc, List<Long> turmasSelecionadas, String cpf, int desconto) {
        String criaEndereco = "INSERT into sistema.endereco(rua,bairro,numero,complemento,cep,inicio_vigencia,fim_vigencia,uf) VALUES(?,?,?,?,?,CURRENT_DATE,NULL,?) RETURNING id;";
        int idEndereco = 0;

        String criaPessoa = "INSERT INTO sistema.pessoa(id_endereco, nome, data_nascimento, telefone, cpf) VALUES(?, ?, ?, ?, ?) RETURNING id;";
        int idPessoa = 0;

        String criaAluno = "INSERT INTO sistema.aluno(inicio_vigencia, id_pessoa, id_turma, desconto) VALUES(CURRENT_DATE, ?, ?, ?);";

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

            try (PreparedStatement statementAluno = conn.prepareStatement(criaAluno)) {
                for (Long idTurma : turmasSelecionadas) {
                    statementAluno.setLong(1, idPessoa);
                    statementAluno.setLong(2, idTurma);
                    statementAluno.setInt(3, desconto);
                    statementAluno.addBatch();
                }

                statementAluno.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao cadastrar aluno.");
        }
    }

    public List<Turma> buscaTurmas() {
        List<Turma> listaTurmas = new ArrayList<>();

        String selectTurmas = "SELECT turma.id, modalidade.nome_modalidade, modalidade.valor_mensalidade, horario.horario_inicio, instrutor.id AS id_instrutor,pessoa.id AS pessoa_id, pessoa.nome, pessoa.data_nascimento, pessoa.telefone, pessoa.cpf FROM sistema.turma " +
                "LEFT JOIN sistema.modalidade modalidade ON modalidade.id = turma.id_modalidade " +
                "LEFT JOIN sistema.horario horario ON horario.id = turma.id_horario " +
                "LEFT JOIN sistema.instrutor instrutor ON instrutor.id = turma.id_instrutor " +
                "LEFT JOIN sistema.pessoa pessoa ON instrutor.id_pessoa = pessoa.id;";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(selectTurmas);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Turma turma = new Turma();
                turma.setId(resultSet.getLong("id"));

                Modalidade modalidade = new Modalidade();
                modalidade.setNomeModalidade(resultSet.getString("nome_modalidade"));
                modalidade.setValorMensalidade(resultSet.getBigDecimal("valor_mensalidade"));

                Horario horario = new Horario();
                horario.setHorarioInicio(resultSet.getTime("horario_inicio").toLocalTime());

                Instrutor instrutor = new Instrutor();
                instrutor.setId(resultSet.getLong("id_instrutor"));

                Pessoa pessoa = new Pessoa();
                pessoa.setId(resultSet.getLong("pessoa_id"));
                pessoa.setNome(resultSet.getString("nome"));
                pessoa.setDataNascimento(resultSet.getDate("data_nascimento"));
                pessoa.setTelefone(resultSet.getString("telefone"));
                pessoa.setCpf(resultSet.getString("cpf"));
                instrutor.setPessoa(pessoa);

                turma.setModalidade(modalidade);
                turma.setHorario(horario);
                turma.setInstrutor(instrutor);

                listaTurmas.add(turma);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao retornar dados para tabela de turmas.");
        }

        return listaTurmas;
    }

    public List<Aluno> buscarAlunos() {
        List<Aluno> listaAlunos = new ArrayList<>();

        String selectAlunos = "SELECT aluno.id AS id_aluno, aluno.desconto, pessoa.id AS pessoa_id, pessoa.nome, pessoa.data_nascimento, pessoa.telefone, pessoa.cpf, endereco.id AS endereco_id, endereco.rua, endereco.bairro, endereco.numero, endereco.complemento, " +
                "endereco.cep, endereco.uf, modalidade.nome_modalidade, modalidade.valor_mensalidade, horario.horario_inicio, turma.id AS id_turma FROM sistema.aluno aluno " +
                "LEFT JOIN sistema.turma turma ON turma.id = aluno.id_turma " +
                "LEFT JOIN sistema.modalidade modalidade ON modalidade.id = turma.id_modalidade " +
                "LEFT JOIN sistema.horario horario ON horario.id = turma.id_horario " +
                "LEFT JOIN sistema.pessoa pessoa ON pessoa.id = aluno.id_pessoa " +
                "LEFT JOIN sistema.endereco endereco ON endereco.id = pessoa.id_endereco " +
                "WHERE aluno.fim_vigencia IS NULL;";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(selectAlunos);
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

                Modalidade modalidade = new Modalidade();
                modalidade.setNomeModalidade(resultSet.getString("nome_modalidade"));
                modalidade.setValorMensalidade(resultSet.getBigDecimal("valor_mensalidade"));

                Horario horario = new Horario();
                horario.setHorarioInicio(resultSet.getTime("horario_inicio").toLocalTime());

                Turma turma = new Turma();
                turma.setId(resultSet.getLong("id_turma"));
                turma.setModalidade(modalidade);
                turma.setHorario(horario);

                Aluno aluno = new Aluno();
                aluno.setId(resultSet.getLong("id_aluno"));
                aluno.setDesconto(resultSet.getInt("desconto"));
                aluno.setPessoa(pessoa);
                aluno.setTurma(turma);

                listaAlunos.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao retornar dados para tabela de alunos.");
        }

        return listaAlunos;
    }

    public void editarAluno(Long idAluno, String nome, Date dataNascimento, String telefone, String cpf, String rua, String bairro, String numero, String complemento, String cep, String uf, List<Long> turmasSelecionadas, int desconto) {
        String selecionaIds = "SELECT aluno.id_pessoa, aluno.id_turma, pessoa.id_endereco FROM sistema.aluno aluno " +
                "LEFT JOIN sistema.pessoa pessoa ON pessoa.id = aluno.id_pessoa " +
                "WHERE aluno.id = ?;";

        String updatePessoa = "UPDATE sistema.pessoa SET nome = ?, data_nascimento = ?, telefone = ?, cpf = ? WHERE id = ?;";

        String updateEndereco = "UPDATE sistema.endereco SET rua = ?, bairro = ?, numero = ?, complemento = ?, cep = ?, uf = ? WHERE id = ?";

        String selectAluno = "SELECT id_turma FROM sistema.aluno WHERE id_pessoa = ? AND fim_vigencia is null;";

        String removeTurmaAluno = "UPDATE sistema.aluno SET fim_vigencia = CURRENT_DATE WHERE id_turma = ?;";

        String adicionaTurmaAluno = "INSERT INTO sistema.aluno(inicio_vigencia, fim_vigencia, id_pessoa, id_turma, desconto) VALUES(CURRENT_DATE, null, ?, ?, ?);";

        long idPessoa = -1;
        long idEndereco = -1;

        List<Long> idsTurmasatuais = new ArrayList<>();

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao()) {
            conn.setAutoCommit(false);

            try (PreparedStatement statement = conn.prepareStatement(selecionaIds);) {
                statement.setLong(1, idAluno);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        idPessoa = resultSet.getLong("id_pessoa");
                        idEndereco = resultSet.getLong("id_endereco");
                    }
                }
            }

            try (PreparedStatement statementPessoa = conn.prepareStatement(updatePessoa)) {
                statementPessoa.setString(1, nome);
                statementPessoa.setDate(2, dataNascimento);
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

            try (PreparedStatement statementAluno = conn.prepareStatement(selectAluno)) {
                statementAluno.setLong(1, idPessoa);

                try (ResultSet resultSetAluno = statementAluno.executeQuery();) {
                    while (resultSetAluno.next()) {
                        idsTurmasatuais.add(resultSetAluno.getLong("id_turma"));
                    }
                }
            }

            List<Long> turmasParaAdicionar = new ArrayList<>(turmasSelecionadas);
            turmasParaAdicionar.removeAll(idsTurmasatuais);

            List<Long> turmasParaRemover = new ArrayList<>(idsTurmasatuais);
            turmasParaRemover.removeAll(turmasSelecionadas);

            if (!turmasParaAdicionar.isEmpty()) {
                try (PreparedStatement statementAdicionaTurma = conn.prepareStatement(adicionaTurmaAluno)) {
                    for (Long idTurma : turmasParaAdicionar) {
                        statementAdicionaTurma.setLong(1, idPessoa);
                        statementAdicionaTurma.setLong(2, idTurma);
                        statementAdicionaTurma.setInt(3, desconto);
                        statementAdicionaTurma.executeUpdate();
                    }
                }
            }

            if (!turmasParaRemover.isEmpty()) {
                try (PreparedStatement statementRemoveTurma = conn.prepareStatement(removeTurmaAluno)) {
                    for (Long idTurma : turmasParaRemover) {
                        statementRemoveTurma.setLong(1, idTurma);
                        statementRemoveTurma.executeUpdate();
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao gravar dados de Aluno para edicao.");
        }
    }

    public List<Long> buscaIdsTurmasFrequentes(Long idPessoa) {
        List<Long> idsTurmasFrequentes = new ArrayList<>();

        String sql = "SELECT id_turma FROM sistema.aluno WHERE id_pessoa = ? AND fim_vigencia IS NULL";

        try (Connection conn = ConexaoDatabase.getInstancia().getConexao();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setLong(1, idPessoa);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    idsTurmasFrequentes.add(resultSet.getLong("id_turma"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao buscar as turmas frequentes do aluno.");
        }

        return idsTurmasFrequentes;
    }

}
