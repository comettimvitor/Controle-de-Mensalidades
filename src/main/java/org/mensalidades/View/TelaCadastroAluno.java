package org.mensalidades.View;

import com.toedter.calendar.JDateChooser;
import org.mensalidades.Controller.AlunoController;
import org.mensalidades.Model.Aluno;
import org.mensalidades.Model.Turma;
import org.mensalidades.generics.ConversorData;
import org.mensalidades.generics.CriaComboBoxEstados;
import org.mensalidades.generics.Mascaras;
import org.mensalidades.generics.enums.Estados;
import org.mensalidades.tableModel.AlunoTurmaTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaCadastroAluno extends JFrame {

    private JPanel panelAlunos;
    private JTextField textFieldNome;
    private JFormattedTextField formattedTextFieldDataNasc;
    private JFormattedTextField formattedTextFieldTelefone;
    private JTextField textFieldRua;
    private JTextField textFieldBairro;
    private JTextField textFieldNumero;
    private JTextField textFieldComplemento;
    private JFormattedTextField formattedTextFieldCep;
    private JComboBox comboBoxUf;
    private JLabel labelNome;
    private JLabel labelTelefone;
    private JLabel labelRua;
    private JLabel labelBairro;
    private JLabel labelNumero;
    private JLabel labelComplemento;
    private JLabel labelCep;
    private JLabel labelUf;
    private JButton salvarButton;
    private JFormattedTextField formattedTextFieldCpf;
    private JLabel labelCpf;
    private JComboBox comboBoxTurma;
    private JLabel labelTurma;
    private JTable tableTurmas;
    private JScrollPane scrollPaneTableTurmas;
    private JFormattedTextField formattedTextFieldDesconto;
    private JLabel labelDesconto;
    private JPanel panelLabelData;
    private JPanel panelData;
    private JDateChooser dateChooserNascimento;

    private TelaPrincipal telaPrincipal;
    private AlunoController alunoController;
    private Aluno editarAluno;
    private AlunoTurmaTableModel turmaTableModel = new AlunoTurmaTableModel(new ArrayList<>());

    private List<Turma> turmasCadastradas;
    private List<Long> idsTurma;

    public TelaCadastroAluno(AlunoController alunoController, TelaPrincipal telaPrincipal, Aluno editarAluno) {
        this.telaPrincipal = telaPrincipal;
        this.alunoController = alunoController;
        this.editarAluno = editarAluno;

        Mascaras.mascaraCep(formattedTextFieldCep);
        Mascaras.mascaraCpf(formattedTextFieldCpf);
        Mascaras.mascaraTelefone(formattedTextFieldTelefone);

        setTitle("Cadastro de Alunos");
        add(panelAlunos);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(550, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        CriaComboBoxEstados.criaComboBoxUfs(comboBoxUf);
        criaDateChooser();
        salvarAluno();
        preencherCamposEdicao();
        populaTabelaTurmas();
    }

    private void salvarAluno() {
        salvarButton.addActionListener(e -> {
            if (!textFieldNome.getText().isEmpty() && dateChooserNascimento.getDate() != null && !formattedTextFieldTelefone.getText().isEmpty() && !textFieldRua.getText().isEmpty() && !textFieldBairro.getText().isEmpty() && !textFieldNumero.getText().isEmpty() && !formattedTextFieldCep.getText().isEmpty() && !formattedTextFieldCpf.getText().isEmpty()) {

                if (!formattedTextFieldDesconto.getText().trim().matches("\\d*")) {
                    JOptionPane.showMessageDialog(null, "Apenas numeros no campo descanso!");
                    formattedTextFieldDesconto.setText("");
                }

                if (editarAluno == null) {
                    alunoController.cadastraAluno(textFieldRua.getText(), textFieldBairro.getText(), textFieldNumero.getText(), textFieldComplemento.getText(), formattedTextFieldCep.getText(), comboBoxUf.getSelectedItem().toString(), textFieldNome.getText(), formattedTextFieldTelefone.getText().toString(), ConversorData.converteData(dateChooserNascimento.getDate()), retornaTurmasSelecionadas(), formattedTextFieldCpf.getText(), Integer.parseInt(formattedTextFieldDesconto.getText()));
                    telaPrincipal.populaTabelaAlunos();
                } else {
                    alunoController.editarAluno(editarAluno.getId(), textFieldNome.getText(), ConversorData.converteData(dateChooserNascimento.getDate()), formattedTextFieldTelefone.getText().toString(), formattedTextFieldCpf.getText(), textFieldRua.getText(), textFieldBairro.getText(), textFieldNumero.getText(), textFieldComplemento.getText(), formattedTextFieldCep.getText(), comboBoxUf.getSelectedItem().toString(), retornaTurmasSelecionadas(), Integer.parseInt(formattedTextFieldDesconto.getText()));
                    telaPrincipal.populaTabelaAlunos();
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios!");
            }
        });
    }

    private void preencherCamposEdicao() {
        if (editarAluno != null) {
            textFieldNome.setText(editarAluno.getPessoa().getNome());
            dateChooserNascimento.setDate(editarAluno.getPessoa().getDataNascimento());
            formattedTextFieldTelefone.setValue(editarAluno.getPessoa().getTelefone());
            formattedTextFieldCpf.setValue(editarAluno.getPessoa().getCpf());
            textFieldRua.setText(editarAluno.getPessoa().getEndereco().getRua());
            textFieldBairro.setText(editarAluno.getPessoa().getEndereco().getBairro());
            textFieldNumero.setText(editarAluno.getPessoa().getEndereco().getNumero());
            textFieldComplemento.setText(editarAluno.getPessoa().getEndereco().getComplemento());
            formattedTextFieldCep.setValue(editarAluno.getPessoa().getEndereco().getCep());
            comboBoxUf.setSelectedItem(Estados.valueOf(editarAluno.getPessoa().getEndereco().getUf()));
            formattedTextFieldDesconto.setText(String.valueOf(editarAluno.getDesconto()));
        }
    }

    public void populaTabelaTurmas() {
        tableTurmas.setModel(turmaTableModel);

        panelAlunos.revalidate();
        panelAlunos.repaint();

        turmasCadastradas = alunoController.buscaTurmas();
        turmaTableModel.setTurmas(turmasCadastradas);

        Long idPessoa = -1L;

        if (editarAluno != null) {
            idPessoa = editarAluno.getPessoa().getId();
        }

        idsTurma = alunoController.buscaIdsTurmasFrequentes(idPessoa);

        limpaCheckboxes();

        for (Long idTurma : idsTurma) {
            for (int i = 0; i < tableTurmas.getRowCount(); i++) {
                if (idTurma.equals(tableTurmas.getValueAt(i, 0))) {
                    tableTurmas.setValueAt(true, i, 5);
                }
            }
        }
    }

    public void limpaCheckboxes() {
        for (int i = 0; i < tableTurmas.getRowCount(); i++) {
            tableTurmas.setValueAt(false, i, 0); // Coluna 0 é o checkbox
        }
    }


    public List<Long> retornaTurmasSelecionadas() {
        List<Long> turmasSelecionadas = new ArrayList<>();

        for (Turma turma : turmaTableModel.getTurmasSelecionadas()) {
            turmasSelecionadas.add(turma.getId());
        }

        return turmasSelecionadas;
    }

    private void criaDateChooser() {
        JLabel labelDataNascimento = new JLabel("Data de nascimento: ");
        labelDataNascimento.setFont(new Font("Arial", Font.BOLD, 12));
        dateChooserNascimento = new JDateChooser();
        dateChooserNascimento.setDateFormatString("dd/MM/yyyy");
        dateChooserNascimento.setPreferredSize(new Dimension(150, 25));

        panelData.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelLabelData.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelLabelData.add(labelDataNascimento);
        panelData.add(dateChooserNascimento);
        panelData.revalidate();
        panelData.repaint();
    }
}
