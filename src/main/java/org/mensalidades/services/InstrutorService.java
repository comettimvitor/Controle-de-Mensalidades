package org.mensalidades.services;

import org.mensalidades.Model.Instrutor;
import org.mensalidades.dao.InstrutorDAO;

import java.sql.Date;
import java.util.List;

public class InstrutorService {
    private final InstrutorDAO instrutorDAO;

    public InstrutorService() {
        this.instrutorDAO = new InstrutorDAO();
    }

    public void cadastraInstrutor(String rua, String bairro, String numero, String complemento, String cep, String uf, String nome, String telefone, Date dataNasc, String cpf, int comissao) {
        instrutorDAO.cadastraInstrutor(rua, bairro, numero, complemento, cep, uf, nome, telefone, dataNasc, cpf, comissao);
    }

    public List<Instrutor> buscarInstrutores() {
        return instrutorDAO.buscarInstrutores();
    }

    public void editaInstrutor(Long idInstrutor, String rua, String bairro, String numero, String complemento, String cep, String uf, String nome, String telefone, Date dataNasc, String cpf, int comissao) {
        instrutorDAO.editarInstrutor(idInstrutor, rua, bairro, numero, complemento, cep, uf, nome, telefone, dataNasc, cpf, comissao);
    }
}
