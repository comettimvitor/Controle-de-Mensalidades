package org.mensalidades.services;

import org.mensalidades.Model.Aluno;
import org.mensalidades.Model.Turma;
import org.mensalidades.dao.AlunoDAO;

import java.sql.Date;
import java.util.List;

public class AlunoService {

    private final AlunoDAO alunoDAO;

    public AlunoService() {
        this.alunoDAO = new AlunoDAO();
    }

    public List<Turma> buscaTurmas() {
        return alunoDAO.buscaTurmas();
    }

    public void cadastraAluno(String rua, String bairro, String numero, String complemento, String cep, String uf, String nome, String telefone, Date dataNasc, List<Long> turmasSelecionadas, String cpf, int desconto){
        alunoDAO.cadastraAluno(rua, bairro, numero, complemento, cep, uf, nome, telefone, dataNasc, turmasSelecionadas, cpf, desconto);
    }

    public List<Aluno> buscaAlunos() {
        return alunoDAO.buscarAlunos();
    }

    public void editarAluno(Long idAluno, String nome, Date dataNasc, String telefone, String cpf, String rua, String bairro, String numero, String complemento, String cep, String uf, List<Long> turmasSelecionadas, int desconto) {
        alunoDAO.editarAluno(idAluno, nome, dataNasc, telefone, cpf, rua, bairro, numero, complemento, cep, uf, turmasSelecionadas, desconto);
    }

    public List<Long> buscaIdsTurmasFrequentes(Long idPessoa) {
        return alunoDAO.buscaIdsTurmasFrequentes(idPessoa);
    }
}
