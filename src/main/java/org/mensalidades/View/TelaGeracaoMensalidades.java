package org.mensalidades.View;

import com.toedter.calendar.JDateChooser;
import org.mensalidades.Controller.AlunoController;
import org.mensalidades.Controller.MensalidadeController;
import org.mensalidades.Model.Aluno;
import org.mensalidades.Model.Mensalidade;
import org.mensalidades.Model.Turma;
import org.mensalidades.generics.ConversorData;
import org.mensalidades.tableModel.GeracaoMensalidadeTableModel;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TelaGeracaoMensalidades extends JFrame{
    private JPanel panelMensalidades;
    private JButton buttonGerar;
    private JPanel panelBotoes;
    private JComboBox comboBoxMes;
    private JPanel panelDadosMensalidade;
    private JTable tableGeracaoMensalidades;
    private JScrollPane scrollPaneMensalidades;
    private JDateChooser dateChooserVencimento;

    private MensalidadeController mensalidadeController = new MensalidadeController();
    private GeracaoMensalidadeTableModel geracaoMensalidadeTableModel = new GeracaoMensalidadeTableModel(new ArrayList<>());
    private AlunoController alunoController = new AlunoController();

    private List<Mensalidade> mensalidadesGeradas;
    private List<Aluno> alunosCadastrados;
    private List<Mensalidade> mensalidades = new ArrayList<>();

    private TelaPrincipal telaPrincipal;

    public TelaGeracaoMensalidades(TelaPrincipal telaPrincipal) {
        this.telaPrincipal = telaPrincipal;

        setTitle("Geração de Mensalidades");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        add(panelMensalidades);

        revalidate();
        repaint();

        populaTabelaGeracaoMensalidades();
        salvarMensalidades();
        criaDateChooser();
    }

    public void populaTabelaGeracaoMensalidades() {
        tableGeracaoMensalidades.setModel(geracaoMensalidadeTableModel);

        scrollPaneMensalidades.revalidate();
        scrollPaneMensalidades.repaint();

        alunosCadastrados = alunoController.buscaAlunos();

        List<Long> idAlunos = new ArrayList<>();

        for(Aluno aluno : alunosCadastrados) {
            idAlunos.add(aluno.getId());
        }

        mensalidadesGeradas = mensalidadeController.gerarTabelaMensalidade(idAlunos);
        geracaoMensalidadeTableModel.setMensalidades(mensalidadesGeradas);
        geracaoMensalidadeTableModel.fireTableDataChanged();
    }

    public List<Mensalidade> gerarMensalidades() {
        Date dataVencimento = ConversorData.converteData(dateChooserVencimento.getDate());
        int numeroMes = dateChooserVencimento.getDate().getMonth();

        for (int i = 0; i < tableGeracaoMensalidades.getRowCount(); i++) {
            Mensalidade mensalidade = new Mensalidade();
            mensalidade.setMesReferente(numeroMes);
            mensalidade.setVencimento(dataVencimento);

            Aluno aluno = new Aluno();
            aluno.setId((Long) tableGeracaoMensalidades.getValueAt(i, 0));
            mensalidade.setAluno(aluno);

            Turma turma = new Turma();
            turma.setId((Long) tableGeracaoMensalidades.getValueAt(i, 2));
            mensalidade.setTurma(turma);

            mensalidade.setValor((BigDecimal) tableGeracaoMensalidades.getValueAt(i, 4));

            mensalidades.add(mensalidade);
        }

        return mensalidades;
    }

    private void salvarMensalidades() {
        buttonGerar.addActionListener(e -> {
            gerarMensalidades();

            if(!mensalidades.isEmpty()) {
                mensalidadeController.gerarMensalidades(mensalidades);
                JOptionPane.showMessageDialog(null, "Mensalidades geradas com sucesso!");
                this.dispose();
                telaPrincipal.populaTabelaMensalidadesGeradas();
            } else {
                JOptionPane.showMessageDialog(null, "Nenhuma mensalidade foi gerada!");
            }
            populaTabelaGeracaoMensalidades();
            revalidate();
            repaint();
        });
    }

    private void criaDateChooser() {
        JLabel labelDataVencimento = new JLabel("Data de vencimento: ");
        labelDataVencimento.setFont(new Font("Arial", Font.BOLD, 12));
        dateChooserVencimento = new JDateChooser();
        dateChooserVencimento.setDateFormatString("dd/MM/yyyy");
        dateChooserVencimento.setPreferredSize(new Dimension(150, 25));

        panelDadosMensalidade.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelDadosMensalidade.add(labelDataVencimento);
        panelDadosMensalidade.add(dateChooserVencimento);
        panelDadosMensalidade.revalidate();
        panelDadosMensalidade.repaint();
    }
}
