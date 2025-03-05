package org.mensalidades.services;

import org.mensalidades.dao.AcademiaDAO;

public class AcademiaService {
    private final AcademiaDAO academiaDAO;

    public AcademiaService() {
        this.academiaDAO =  new AcademiaDAO();
    }

    public boolean existeAcademia() {
        return academiaDAO.existeAcademia();
    }

    public void cadastraAcademia(String rua, String bairro, String numero, String complemento, String cep, String uf, String nome) {
        academiaDAO.cadastraAcademia(rua, bairro, numero, complemento, cep, uf, nome);
    }
 }
