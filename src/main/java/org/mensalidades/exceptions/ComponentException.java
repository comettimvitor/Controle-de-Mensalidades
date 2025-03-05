package org.mensalidades.exceptions;

import javax.swing.*;

public class ComponentException extends RuntimeException {

    public ComponentException(String mensagem) {
        super(mensagem);
        janelaErro(mensagem);
    }

    public void janelaErro(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
