package org.mensalidades.View;

import org.mensalidades.Controller.AcademiaController;
import org.mensalidades.generics.CriaComboBoxEstados;
import org.mensalidades.generics.Mascaras;
import org.mensalidades.generics.enums.Estados;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class TelaCadastroAcademia extends JFrame{
    private JTextField textFieldAcademia;
    private JTextField textFieldRua;
    private JTextField textFieldBairro;
    private JTextField textFieldNumero;
    private JTextField textFieldComplemento;
    private JComboBox comboBoxUf;
    private JFormattedTextField formattedTextFieldCep;
    private JPanel panelCadAcademia;
    private JLabel labelAcademia;
    private JLabel labelRua;
    private JLabel labelBairro;
    private JLabel labelNumero;
    private JLabel labelComplemento;
    private JLabel labelcep;
    private JLabel labelUf;
    private JButton buttonSalvar;

    private AcademiaController academiaController;

    public TelaCadastroAcademia(AcademiaController academiaController) {
        this.academiaController = academiaController;

        setTitle("Cadastro de Academia");
        add(panelCadAcademia);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        Mascaras.mascaraCep(formattedTextFieldCep);
        salvarAcademia();
        CriaComboBoxEstados.criaComboBoxUfs(comboBoxUf);
    }

    private void salvarAcademia() {
        buttonSalvar.addActionListener(e -> {
            if(!textFieldAcademia.getText().isEmpty() && !textFieldRua.getText().isEmpty() && !textFieldBairro.getText().isEmpty() && !textFieldNumero.getText().isEmpty() && !textFieldComplemento.getText().isEmpty() && !formattedTextFieldCep.getText().isEmpty()) {
                academiaController.cadastraAcademia(textFieldRua.getText(), textFieldBairro.getText(), textFieldNumero.getText(), textFieldComplemento.getText(), formattedTextFieldCep.getText(), comboBoxUf.getSelectedItem().toString(), textFieldAcademia.getText());
                this.dispose();
                SwingUtilities.invokeLater(TelaPrincipal::new);
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            }
        });
    }
}
