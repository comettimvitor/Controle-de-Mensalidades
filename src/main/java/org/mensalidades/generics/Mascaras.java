package org.mensalidades.generics;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class Mascaras {

    public static void mascaraCep(JFormattedTextField jFormattedTextField) {
        try {
            MaskFormatter mask = new MaskFormatter("##.###-###");
            mask.setPlaceholderCharacter('_');
            jFormattedTextField.setFormatterFactory(new DefaultFormatterFactory(mask));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mascaraData(JFormattedTextField jFormattedTextField) {
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            jFormattedTextField.setFormatterFactory(new DefaultFormatterFactory(mask));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mascaraTelefone(JFormattedTextField jFormattedTextField) {
        try {
            MaskFormatter mask = new MaskFormatter("(##)#####-####");
            mask.setPlaceholderCharacter('_');
            jFormattedTextField.setFormatterFactory(new DefaultFormatterFactory(mask));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mascaraCpf(JFormattedTextField jFormattedTextField) {
        try {
            MaskFormatter mask = new MaskFormatter("###.###.###-##");
            mask.setPlaceholderCharacter('_');
            jFormattedTextField.setFormatterFactory(new DefaultFormatterFactory(mask));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mascaraValor(JFormattedTextField jFormattedTextField) {
        try {
            MaskFormatter mask = new MaskFormatter("###.##");
            mask.setPlaceholderCharacter('_');
            jFormattedTextField.setFormatterFactory(new DefaultFormatterFactory(mask));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mascaraHorario(JFormattedTextField horaInicio, JFormattedTextField horaFim) {
        try {
            MaskFormatter mask = new MaskFormatter("##:##");
            mask.setPlaceholderCharacter('_');
            horaInicio.setFormatterFactory(new DefaultFormatterFactory(mask));
            horaFim.setFormatterFactory(new DefaultFormatterFactory(mask));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
