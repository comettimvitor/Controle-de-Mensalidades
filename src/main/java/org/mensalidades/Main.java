package org.mensalidades;

import org.mensalidades.Controller.AcademiaController;
import org.mensalidades.View.TelaCadastroAcademia;
import org.mensalidades.View.TelaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AcademiaController academiaController = new AcademiaController();
        boolean existeAcademia = academiaController.existeAcademia();

        SwingUtilities.invokeLater(() -> {
            if(existeAcademia) {
                new TelaPrincipal();
            } else {
                new TelaCadastroAcademia(academiaController);
            }
        });

    }
}