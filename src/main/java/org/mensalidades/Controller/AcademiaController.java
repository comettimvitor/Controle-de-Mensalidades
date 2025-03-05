package org.mensalidades.Controller;

import org.mensalidades.View.TelaCadastroAcademia;
import org.mensalidades.View.TelaPrincipal;
import org.mensalidades.services.AcademiaService;

import javax.swing.*;

public class AcademiaController {
    private AcademiaService academiaService;

    public AcademiaController() {
        this.academiaService = new AcademiaService();
    }

    public boolean existeAcademia() {
        return academiaService.existeAcademia();
    }

    public void cadastraAcademia(String rua, String bairro, String numero, String complemento, String cep, String uf, String nome) {
        academiaService.cadastraAcademia(rua, bairro, numero, complemento, cep, uf, nome);
    }
}
