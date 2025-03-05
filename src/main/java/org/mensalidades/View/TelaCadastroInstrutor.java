package org.mensalidades.View;

import com.toedter.calendar.JDateChooser;
import org.mensalidades.Controller.InstrutorController;
import org.mensalidades.Model.Instrutor;
import org.mensalidades.generics.ConversorData;
import org.mensalidades.generics.CriaComboBoxEstados;
import org.mensalidades.generics.Mascaras;
import org.mensalidades.generics.enums.Estados;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroInstrutor extends JFrame {
    private JPanel panelInstrutor;
    private JTextField textFieldNome;
    private JFormattedTextField formattedTextFieldDataNasc;
    private JFormattedTextField formattedTextFieldTelefone;
    private JTextField textFieldRua;
    private JTextField textFieldBairro;
    private JTextField textFieldNumero;
    private JTextField textFieldComplemento;
    private JFormattedTextField formattedTextFieldCep;
    private JComboBox comboBoxUf;
    private JButton salvarButton;
    private JLabel labelNome;
    private JLabel labelTelefone;
    private JLabel labelRua;
    private JLabel labelBairro;
    private JLabel labelNumero;
    private JLabel labelComplemento;
    private JLabel labelCep;
    private JLabel labelUf;
    private JFormattedTextField formattedTextFieldCpf;
    private JLabel labelCpf;
    private JTextField textFieldComissao;
    private JLabel labelComissao;
    private JPanel panelDateChooser;
    private JPanel panelLabelDate;
    private JDateChooser dateChooserNascimento;

    private InstrutorController instrutorController;
    private TelaPrincipal telaPrincipal;
    private Instrutor editarInstrutor;

    public TelaCadastroInstrutor(InstrutorController instrutorController, TelaPrincipal telaPrincipal, Instrutor editarInstrutor) {
        this.instrutorController = instrutorController;
        this.telaPrincipal = telaPrincipal;
        this.editarInstrutor = editarInstrutor;

        Mascaras.mascaraCep(formattedTextFieldCep);
        Mascaras.mascaraCpf(formattedTextFieldCpf);
        Mascaras.mascaraTelefone(formattedTextFieldTelefone);

        setTitle("Cadastro de Instrutores");
        add(panelInstrutor);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(550, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        CriaComboBoxEstados.criaComboBoxUfs(comboBoxUf);
        salvarInstrutor();
        preencherCamposEdicao();
        criaDateChooser();
    }

    private void salvarInstrutor() {
        salvarButton.addActionListener(e -> {
            if (!textFieldNome.getText().isEmpty() && dateChooserNascimento.getDate() != null && !formattedTextFieldTelefone.getText().isEmpty() && !textFieldRua.getText().isEmpty() && !textFieldBairro.getText().isEmpty() && !textFieldNumero.getText().isEmpty() && !formattedTextFieldCep.getText().isEmpty() && !formattedTextFieldCpf.getText().isEmpty()) {
                if(editarInstrutor == null){
                    instrutorController.cadastraInstrutor(textFieldRua.getText(), textFieldBairro.getText(), textFieldNumero.getText(), textFieldComplemento.getText(), formattedTextFieldCep.getText(), comboBoxUf.getSelectedItem().toString(), textFieldNome.getText(), formattedTextFieldTelefone.getText().toString(), ConversorData.converteData(dateChooserNascimento.getDate()), formattedTextFieldCpf.getText(), Integer.parseInt(textFieldComissao.getText()));
                    telaPrincipal.populaTabelaInstrutores();
                } else {
                    instrutorController.editaInstrutor(editarInstrutor.getId(), textFieldRua.getText(), textFieldBairro.getText(), textFieldNumero.getText(), textFieldComplemento.getText(), formattedTextFieldCep.getText(), comboBoxUf.getSelectedItem().toString(), textFieldNome.getText(), formattedTextFieldTelefone.getText().toString(), ConversorData.converteData(dateChooserNascimento.getDate()), formattedTextFieldCpf.getText(), Integer.parseInt(textFieldComissao.getText()));
                    telaPrincipal.populaTabelaInstrutores();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios!");
            }

            this.dispose();
        });
    }

    private void preencherCamposEdicao() {
        if (editarInstrutor != null) {
            textFieldNome.setText(editarInstrutor.getPessoa().getNome());
            dateChooserNascimento.setDate(editarInstrutor.getPessoa().getDataNascimento());
            formattedTextFieldTelefone.setValue(editarInstrutor.getPessoa().getTelefone());
            formattedTextFieldCpf.setValue(editarInstrutor.getPessoa().getCpf());
            textFieldRua.setText(editarInstrutor.getPessoa().getEndereco().getRua());
            textFieldBairro.setText(editarInstrutor.getPessoa().getEndereco().getBairro());
            textFieldNumero.setText(editarInstrutor.getPessoa().getEndereco().getNumero());
            textFieldComplemento.setText(editarInstrutor.getPessoa().getEndereco().getComplemento());
            formattedTextFieldCep.setValue(editarInstrutor.getPessoa().getEndereco().getCep());
            comboBoxUf.setSelectedItem(Estados.valueOf(editarInstrutor.getPessoa().getEndereco().getUf()));
            textFieldComissao.setText(String.valueOf(editarInstrutor.getComissao()));
        }
    }

    private void criaDateChooser() {
        JLabel labelDataNascimento = new JLabel("Data de nascimento: ");
        labelDataNascimento.setFont(new Font("Arial", Font.BOLD, 12));
        dateChooserNascimento = new JDateChooser();
        dateChooserNascimento.setDateFormatString("dd/MM/yyyy");
        dateChooserNascimento.setPreferredSize(new Dimension(150, 25));

        panelDateChooser.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelLabelDate.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelLabelDate.add(labelDataNascimento);
        panelDateChooser.add(dateChooserNascimento);
        panelDateChooser.revalidate();
        panelDateChooser.repaint();
    }
}
