package org.mensalidades.View;

import org.mensalidades.Controller.TurmaController;
import org.mensalidades.Model.Instrutor;
import org.mensalidades.Model.Turma;
import org.mensalidades.generics.Mascaras;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class TelaCadastroTurma extends JFrame {
    private JPanel panelCadTurmas;
    private JPanel panelHorario;
    private JFormattedTextField textFieldInicio;
    private JFormattedTextField textFieldFim;
    private JLabel labelHorInicio;
    private JLabel labelHorFim;
    private JPanel panelModalidade;
    private JTextField textFieldModalidade;
    private JTextField textFieldDescricao;
    private JLabel labelModalidade;
    private JLabel labelDescricao;
    private JLabel labelValor;
    private JPanel panelCheckBoxsDias;
    private JFormattedTextField textFieldValor;
    private JButton salvarButton;
    private JCheckBox segundaFeiracheckBox;
    private JCheckBox quartaFeiraCheckBox;
    private JCheckBox tercaFeiraCheckBox;
    private JCheckBox quintaFeiraCheckBox;
    private JCheckBox sextaFeiraCheckBox;
    private JCheckBox sabadoCheckBox;
    private JCheckBox domingoCheckBox;
    private JLabel labelInstrutor;
    private JComboBox comboBoxInstrutores;
    private JCheckBox checkBoxDias;
    private List<JCheckBox> checkBoxes;
    private DefaultListModel<String> modelo;
    private JList<String> listaInstrutores;
    private JScrollPane jScrollPane;

    private TurmaController turmaController;
    private TelaPrincipal telaPrincipal;
    private Turma editarTurma;

    public TelaCadastroTurma(TurmaController turmaController, TelaPrincipal telaPrincipal, Turma turma) {
        this.turmaController = turmaController;
        this.telaPrincipal = telaPrincipal;
        this.editarTurma = turma;

        setTitle("Cadastro de Turma");
        add(panelCadTurmas);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(550, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        Mascaras.mascaraHorario(textFieldInicio, textFieldFim);
        Mascaras.mascaraValor(textFieldValor);
        iniciaCheckboxes();
        preencheHorarioFim();
        buscaInstrutores();
        salvarTurma();
        preencherCamposEdicao();
    }

    private void iniciaCheckboxes() {
        checkBoxes = new ArrayList<>();
        checkBoxes.add(segundaFeiracheckBox);
        checkBoxes.add(tercaFeiraCheckBox);
        checkBoxes.add(quartaFeiraCheckBox);
        checkBoxes.add(quintaFeiraCheckBox);
        checkBoxes.add(sextaFeiraCheckBox);
        checkBoxes.add(sabadoCheckBox);
        checkBoxes.add(domingoCheckBox);
    }

    private void preencheHorarioFim() {
        textFieldInicio.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String valorHora = textFieldInicio.getText().trim();

                if (valorHora.matches("\\d{2}.*")) {
                    textFieldFim.setText(valorHora.substring(0, 2) + ":59");
                } else {
                    textFieldFim.setText("");
                }

            }
        });
    }

    private String diasSelecionados() {
        List<String> diasSelecionados = new ArrayList<>();

        for (JCheckBox dia : checkBoxes) {
            if (dia.isSelected()) {
                diasSelecionados.add(dia.getText());
            }
        }

        return String.join(", ", diasSelecionados);
    }

    private void salvarTurma() {
        salvarButton.addActionListener(e -> {
            if (!textFieldInicio.getText().isEmpty() && !textFieldFim.getText().isEmpty() && !textFieldModalidade.getText().isEmpty() && !textFieldValor.getText().isEmpty()) {
                if(editarTurma == null){
                    turmaController.CadastraTurma(textFieldInicio.getText(), textFieldFim.getText(), textFieldModalidade.getText(), textFieldDescricao.getText(), new BigDecimal(textFieldValor.getText()).setScale(2, RoundingMode.HALF_EVEN), diasSelecionados(), retornaIdInstrutor());
                    telaPrincipal.populaTabelaTurmas();
                } else {
                    turmaController.editarTurma(editarTurma.getId(), editarTurma.getHorario().getId(), editarTurma.getModalidade().getId(), textFieldInicio.getText(), textFieldFim.getText(), textFieldModalidade.getText(), textFieldDescricao.getText(), new BigDecimal(textFieldValor.getText()).setScale(2, RoundingMode.HALF_EVEN), diasSelecionados(), retornaIdInstrutor());
                    telaPrincipal.populaTabelaTurmas();
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios!");
            }
        });
    }

    private void buscaInstrutores() {
        comboBoxInstrutores.removeAllItems();

        List<Instrutor> instrutores = turmaController.buscaInstrutor();

        for (Instrutor instrutor : instrutores) {
            comboBoxInstrutores.addItem(instrutor);
        }
    }

    private Long retornaIdInstrutor() {
        Instrutor instrutorSelecionado = (Instrutor) comboBoxInstrutores.getSelectedItem();

        if (instrutorSelecionado != null) {
            return instrutorSelecionado.getId();
        }

        return null;
    }

    private void preencherCamposEdicao() {
        if (editarTurma != null) {
            textFieldInicio.setText(editarTurma.getHorario().getHorarioInicio().toString());
            textFieldFim.setText(editarTurma.getHorario().getHorarioFim().toString());
            textFieldModalidade.setText(editarTurma.getModalidade().getNomeModalidade());
            textFieldDescricao.setText(editarTurma.getModalidade().getDescricao());
            setSelectedItemComboBox(comboBoxInstrutores);
            textFieldValor.setValue(editarTurma.getModalidade().getValorMensalidade());
            setSelectedCheckBoxes(checkBoxes);
        }
    }

    private void setSelectedItemComboBox(JComboBox comboBox) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Instrutor instrutor = (Instrutor) comboBox.getItemAt(i);

            if (editarTurma.getInstrutor().getPessoa().getNome().equals(instrutor.getPessoa().getNome())) {
                comboBox.setSelectedItem(instrutor);
                break;
            }
        }
    }

    private void setSelectedCheckBoxes(List<JCheckBox> diasSemana) {
        List<String> diasSelecionados = editarTurma.getModalidade().getDiaSemana();

        for(JCheckBox diaSemana : diasSemana) {
            for(String diaSelecionado : diasSelecionados) {
                if(diaSemana.getText().trim().toLowerCase().equals(diaSelecionado.trim().toLowerCase())) {
                    diaSemana.setSelected(true);
                }
            }
        }
    }
}
