package org.mensalidades.Controller;

import org.mensalidades.Model.Instrutor;
import org.mensalidades.services.InstrutorService;

import java.sql.Date;
import java.util.List;

public class InstrutorController {
    private InstrutorService instrutorService;

    public InstrutorController() {
        this.instrutorService = new InstrutorService();
    }

    public void cadastraInstrutor(String rua, String bairro, String numero, String complemento, String cep, String uf, String nome, String telefone, Date dataNasc, String cpf, int comissao) {
        instrutorService.cadastraInstrutor(rua, bairro, numero, complemento, cep, uf, nome, telefone, dataNasc, cpf, comissao);
    }

    public List<Instrutor> buscarInstrutores() {
        return instrutorService.buscarInstrutores();
    }

    public void editaInstrutor(Long idInstrutor, String rua, String bairro, String numero, String complemento, String cep, String uf, String nome, String telefone, Date dataNasc, String cpf, int comissao) {
        instrutorService.editaInstrutor(idInstrutor, rua, bairro, numero, complemento, cep, uf, nome, telefone, dataNasc, cpf, comissao);
    }
}
