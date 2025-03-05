package org.mensalidades.generics;

import org.mensalidades.generics.enums.Estados;

import javax.swing.*;

public class CriaComboBoxEstados {

    public static void criaComboBoxUfs(JComboBox comboBox) {
        comboBox.removeAllItems();

        for (Estados estado : Estados.values()) {
            comboBox.addItem(estado);
        }
    }
}
