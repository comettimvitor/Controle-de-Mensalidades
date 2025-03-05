package org.mensalidades.Controller;

import org.mensalidades.Model.Aluno;
import org.mensalidades.Model.Turma;
import org.mensalidades.services.AlunoService;

import java.sql.Date;
import java.util.List;

public class AlunoController {

    private AlunoService alunoService;

    public AlunoController() {
        this.alunoService = new AlunoService();
    }

    public void cadastraAluno(String rua, String bairro, String numero, String complemento, String cep, String uf, String nome, String telefone, Date dataNasc, List<Long> turmasSelecionadas, String cpf, int desconto) {
        alunoService.cadastraAluno(rua, bairro, numero, complemento, cep, uf, nome, telefone, dataNasc, turmasSelecionadas, cpf, desconto);
    }

    public List<Turma> buscaTurmas() {
        return alunoService.buscaTurmas();
    }

    public List<Aluno> buscaAlunos() {
        return alunoService.buscaAlunos();
    }

    public void editarAluno(Long idAluno, String nome, Date dataNasc, String telefone, String cpf, String rua, String bairro, String numero, String complemento, String cep, String uf, List<Long> turmasSelecionadas, int desconto) {
        alunoService.editarAluno(idAluno, nome, dataNasc, telefone, cpf, rua, bairro, numero, complemento, cep, uf, turmasSelecionadas, desconto);
    }

    public List<Long> buscaIdsTurmasFrequentes(Long idPessoa) {
        return alunoService.buscaIdsTurmasFrequentes(idPessoa);
    }
}
