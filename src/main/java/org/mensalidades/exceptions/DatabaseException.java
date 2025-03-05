package org.mensalidades.exceptions;

import javax.swing.*;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String mensagem) {
        super(mensagem);
        janelaErro(mensagem);
    }

    public void janelaErro(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
