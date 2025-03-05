package org.mensalidades.View;

import com.toedter.calendar.JDateChooser;
import org.mensalidades.Controller.*;
import org.mensalidades.Model.*;
import org.mensalidades.tableModel.*;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TelaPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JPanel panelCadastros;
    private JButton cadastrarTurmaButton;
    private JButton cadastrarAlunoButton;
    private JButton cadastrarInstrutorButton;
    private JPanel panelTableAlunos;
    private JTable tableAlunos;
    private JPanel panelBusca;
    private JTextField textFieldBuscaAlunos;
    private JLabel labelBuscaALunos;
    private JScrollPane scrollPaneAlunos;
    private JButton buscarButton;
    private JButton editarButton;
    private JPanel panelTableInstrutores;
    private JTable tableInstrutores;
    private JTextField textFieldBuscaInstrutores;
    private JButton buttonBuscarInstrutor;
    private JButton buttonEditarInstrutor;
    private JScrollPane scrollPaneInstrutores;
    private JPanel panelBuscaInstrutores;
    private JLabel labelBuscaInstrutores;
    private JPanel panelTableTurmas;
    private JScrollPane scrollPaneTurmas;
    private JTable tableTurmas;
    private JPanel panelBuscaTurmas;
    private JTextField textFieldBuscaTurmas;
    private JLabel labelTurmas;
    private JButton buttonEditarTurmas;
    private JButton buttonBuscarTurmas;
    private JPanel jTabbedPaneMensalidades;
    private JPanel panelAcoes;
    private JButton buttonGerarMensalidades;
    private JTabbedPane jTabbedPaneGeral;
    private JPanel jTabbedPaneCadastros;
    private JPanel panelTableMensalidades;
    private JTable tableMensalidadesGeradas;
    private JScrollPane scrollPaneTableMensalidadesGeradas;
    private JPanel panelFiltroMensalidades;
    private JTextField textFieldBuscarAluno;
    private JCheckBox checkBoxPago;
    private JButton buttonBuscarMensalidades;
    private JLabel labelBuscar;
    private JTextField textFieldBuscarMes;
    private JCheckBox checkBoxAberto;
    private JPanel panelDate;
    private JButton buttonEfetuarPagamento;
    private JButton buttonCancelarPagamento;
    private JButton buttonGerarComissao;
    private JPanel panelTableComissoes;
    private JScrollPane scrollPaneTableComissao;
    private JTable tablecomissao;
    private JPanel panelFiltroComissao;
    private JTextField textFieldBuscarInstrutor;
    private JCheckBox checkBoxPagas;
    private JCheckBox checkBoxAbertas;
    private JButton buttonBuscarComissoes;
    private JButton buttonPagamentoComissao;
    private JButton buttonCancelarPagamentoComissao;
    private JLabel labelBuscarInstrutor;
    private JPanel panelDatas;
    private JDateChooser dateChooserInicio;
    private JDateChooser dateChooserFim;
    private JDateChooser dateChooserInicioCom;
    private JDateChooser dateChooserFimCom;

    private TurmaController turmaController = new TurmaController();
    private TurmaTableModel turmaTableModel = new TurmaTableModel(new ArrayList<>());
    private InstrutorController instrutorController = new InstrutorController();
    private InstrutorTableModel instrutorTableModel = new InstrutorTableModel(new ArrayList<>());
    private AlunoController alunoController = new AlunoController();
    private AlunoTableModel alunoTableModel = new AlunoTableModel(new ArrayList<>());
    private MensalidadeController mensalidadeController = new MensalidadeController();
    private MensalidadesGeradasTableModel mensalidadesGeradasTableModel = new MensalidadesGeradasTableModel(new ArrayList<>());
    private ComissaoController comissaoController = new ComissaoController();
    private ComissoesGeradasTableModel comissoesGeradasTableModel = new ComissoesGeradasTableModel(new ArrayList<>());

    private List<Aluno> alunosCadastrados;
    private List<Instrutor> instrutoresCadastrados;
    private List<Turma> turmasCadastradas;
    private List<Mensalidade> mensalidadesCadastradas;
    private List<Comissao> comissoesCadastradas;

    public TelaPrincipal() throws HeadlessException {
        setTitle("Controle de Mensalidades");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));

        add(panelPrincipal);

        revalidate();
        repaint();

        chamaTelaCadTurmas();
        chamaTelaCadInstrutor();
        chamaTelaCadAluno();
        chamaTelaGerarMensalidades();
        chamaTelaGerarComissao();
        populaTabelaAlunos();
        populaTabelaInstrutores();
        populaTabelaTurmas();
        populaTabelaMensalidadesGeradas();
        populaTableComissoes();
        filtraAlunos();
        filtraInstrutores();
        filtraTurmas();
        filtrarMensalidades();
        filtrarComissoes();
        editarCadastroAluno();
        editarCadastroInstrutor();
        editarCadastroTurma();
        criaPanelDateMensalidades();
        criaPanelDateComissoes();
        pagarMensalidade();
        pagarComissao();
        cancelarPagamentoMensalidade();
        cancelarPagamentoComissao();
    }

    private void chamaTelaCadTurmas() {
        cadastrarTurmaButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new TelaCadastroTurma(turmaController, this, null);
            });
        });
    }

    private void chamaTelaCadInstrutor() {
        cadastrarInstrutorButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new TelaCadastroInstrutor(instrutorController, this, null);
            });
        });
    }

    private void chamaTelaCadAluno() {
        cadastrarAlunoButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new TelaCadastroAluno(alunoController, this, null);
            });
        });
    }

    private void chamaTelaEditarAluno(Aluno aluno) {
        SwingUtilities.invokeLater(() -> {
            new TelaCadastroAluno(alunoController, this, aluno);
        });
    }

    private void chamaTelaEditarInstrutor(Instrutor instrutor) {
        SwingUtilities.invokeLater(() -> {
            new TelaCadastroInstrutor(instrutorController, this, instrutor);
        });
    }

    private void chamaTelaEditarTurma(Turma turma) {
        SwingUtilities.invokeLater(() -> {
            new TelaCadastroTurma(turmaController, this, turma);
        });
    }

    private void chamaTelaGerarMensalidades() {
        buttonGerarMensalidades.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new TelaGeracaoMensalidades(this);
            });
        });
    }

    private void chamaTelaGerarComissao() {
        buttonGerarComissao.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new TelaGeracaoComissao(comissaoController, this);
            });
        });
    }

    public void populaTabelaAlunos() {
        tableAlunos.setModel(alunoTableModel);

        panelTableAlunos.revalidate();
        panelTableAlunos.repaint();

        alunosCadastrados = alunoController.buscaAlunos();
        alunoTableModel.setAlunos(alunosCadastrados);
    }

    private void filtraAlunos() {
        buscarButton.addActionListener(e -> {
            filtroDeAlunosPorNome();
        });
    }

    private void filtroDeAlunosPorNome() {
        String busca = textFieldBuscaAlunos.getText().trim().toLowerCase();

        List<Aluno> alunosFiltrados = alunosCadastrados
                .stream()
                .filter(aluno -> aluno.getPessoa().getNome().toLowerCase().contains(busca))
                .collect(Collectors.toList());

        alunoTableModel.setAlunos(alunosFiltrados);
    }

    private void editarCadastroAluno() {
        editarButton.addActionListener(e -> {
            int linha = tableAlunos.getSelectedRow();

            if (linha != -1) {
                Aluno alunoSelecionado = alunoTableModel.getAluno(linha);
                chamaTelaEditarAluno(alunoSelecionado);

            } else {
                JOptionPane.showMessageDialog(null, "Selecione um Aluno.");
            }
        });
    }

    public void populaTabelaInstrutores() {
        tableInstrutores.setModel(instrutorTableModel);

        panelTableInstrutores.revalidate();
        panelTableInstrutores.repaint();

        instrutoresCadastrados = instrutorController.buscarInstrutores();
        instrutorTableModel.setInstrutores(instrutoresCadastrados);
    }

    private void filtraInstrutores() {
        buttonBuscarInstrutor.addActionListener(e -> {
            filtroDeInstrutoresPorNome();
        });
    }

    private void filtroDeInstrutoresPorNome() {
        String busca = textFieldBuscaInstrutores.getText().trim().toLowerCase();

        List<Instrutor> instrutoresFiltrados = instrutoresCadastrados
                .stream()
                .filter(instrutor -> instrutor.getPessoa().getNome().toLowerCase().contains(busca))
                .collect(Collectors.toList());

        instrutorTableModel.setInstrutores(instrutoresFiltrados);
    }

    private void editarCadastroInstrutor() {
        buttonEditarInstrutor.addActionListener(e -> {
            int linha = tableInstrutores.getSelectedRow();

            if (linha != -1) {
                Instrutor instrutorSelecionado = instrutorTableModel.getInstrutor(linha);
                chamaTelaEditarInstrutor(instrutorSelecionado);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um Instrutor.");
            }
        });
    }

    public void populaTabelaTurmas() {
        tableTurmas.setModel(turmaTableModel);

        panelTableTurmas.revalidate();
        panelTableTurmas.repaint();

        turmasCadastradas = turmaController.buscaTurmas();
        turmaTableModel.setTurmas(turmasCadastradas);
    }

    private void filtraTurmas() {
        buttonBuscarTurmas.addActionListener(e -> {
            filtroDeTurmasPorNome();
        });
    }

    private void filtroDeTurmasPorNome() {
        String busca = textFieldBuscaTurmas.getText().trim().toLowerCase();

        List<Turma> turmasFiltradas = turmasCadastradas
                .stream()
                .filter(turma -> turma.getModalidade().getNomeModalidade().toLowerCase().contains(busca))
                .collect(Collectors.toList());

        turmaTableModel.setTurmas(turmasFiltradas);
    }

    private void editarCadastroTurma() {
        buttonEditarTurmas.addActionListener(e -> {
            int linha = tableTurmas.getSelectedRow();

            if (linha != -1) {
                Turma turmaSelecionada = turmaTableModel.getTurma(linha);
                chamaTelaEditarTurma(turmaSelecionada);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma Turma.");
            }
        });
    }

    public void populaTabelaMensalidadesGeradas() {
        tableMensalidadesGeradas.setRowSelectionAllowed(true);
        tableMensalidadesGeradas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tableMensalidadesGeradas.setModel(mensalidadesGeradasTableModel);

        scrollPaneTableMensalidadesGeradas.revalidate();
        scrollPaneTableMensalidadesGeradas.repaint();

        mensalidadesCadastradas = mensalidadeController.buscaMensalidades();
        mensalidadesGeradasTableModel.setMensalidades(mensalidadesCadastradas);
    }

    public void filtrarMensalidades() {
        buttonBuscarMensalidades.addActionListener(e -> {
            filtraMensalidades();
        });
    }

    public void filtraMensalidades() {
        String buscaPorAluno = textFieldBuscarAluno.getText().trim().toLowerCase();
        boolean buscaPorAberto = checkBoxAberto.isSelected();
        boolean buscaPorPago = checkBoxPago.isSelected();
        Date dataInicio = dateChooserInicio.getDate();
        Date dataFim = dateChooserFim.getDate();

        List<Mensalidade> mensalidadesFiltradas = mensalidadesCadastradas
                .stream()
                .filter(
                        mensalidade ->
                                (buscaPorAluno.isEmpty() || mensalidade.getAluno().getPessoa().getNome().trim().toLowerCase().contains(buscaPorAluno.trim().toLowerCase())) &&
                                        (dataInicio == null || !mensalidade.getVencimento().before(dataInicio)) && (dataFim == null || !mensalidade.getVencimento().after(dataFim)) &&
                                        ((!buscaPorAberto && !buscaPorPago || (buscaPorAberto || mensalidade.isPago()) && (buscaPorPago || !mensalidade.isPago()))))
                .collect(Collectors.toList());

        mensalidadesGeradasTableModel.setMensalidades(mensalidadesFiltradas);
    }

    private void criaPanelDateMensalidades() {
        JLabel labelInicio = new JLabel("Inicio: ");
        dateChooserInicio = new JDateChooser();
        dateChooserInicio.setDateFormatString("dd/MM/yyyy");
        dateChooserInicio.setPreferredSize(new Dimension(150, 25));

        JLabel labelFim = new JLabel("Fim: ");
        dateChooserFim = new JDateChooser();
        dateChooserFim.setDateFormatString("dd/MM/yyyy");
        dateChooserFim.setPreferredSize(new Dimension(150, 25));

        panelDate.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelDate.add(labelInicio);
        panelDate.add(dateChooserInicio);
        panelDate.add(labelFim);
        panelDate.add(dateChooserFim);
        panelDate.revalidate();
        panelDate.repaint();

        dateChooserFim.addPropertyChangeListener("date", e -> {
            if (dateChooserInicio.getDate() != null && dateChooserFim.getDate() != null) {
                if (dateChooserInicio.getDate().after(dateChooserFim.getDate())) {
                    JOptionPane.showMessageDialog(null, "Data Inicio deve ser menor que a Data Fim.");
                    dateChooserInicio.setCalendar(null);
                }
            }
        });
    }

    private void pagarMensalidade() {
        buttonEfetuarPagamento.addActionListener(e -> {
            int[] linhasSelecionadas = tableMensalidadesGeradas.getSelectedRows();

            List<Long> listaIds = new ArrayList<>();
            for (int i = 0; i < linhasSelecionadas.length; i++) {
                int linha = linhasSelecionadas[i];
                Long id = (Long) tableMensalidadesGeradas.getValueAt(linha, 0);
                listaIds.add(id);
            }

            if (listaIds.size() > -1) {
                mensalidadeController.pagarMensalidade(listaIds);
                JOptionPane.showMessageDialog(null, "Mensalidade(s) paga(s) com sucesso.");
                populaTabelaMensalidadesGeradas();
                tableMensalidadesGeradas.clearSelection();
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma Mensalidade para pagamento.");
            }
        });
    }

    private void cancelarPagamentoMensalidade() {
        buttonCancelarPagamento.addActionListener(e -> {
            int[] linhasSelecionadas = tableMensalidadesGeradas.getSelectedRows();

            List<Long> listaIds = new ArrayList<>();
            for (int i = 0; i < linhasSelecionadas.length; i++) {
                int linha = linhasSelecionadas[i];
                Long id = (Long) tableMensalidadesGeradas.getValueAt(linha, 0);
                listaIds.add(id);
            }

            if (listaIds.size() > 0) {
                var selection = JOptionPane.showOptionDialog(null, "Deseja cancelar o pagamento da(s) mensalidade(s) selecionada(s)?", "Cancelar Pagamento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (selection == JOptionPane.YES_OPTION) {
                    mensalidadeController.cancelarPagamentoMensalidade(listaIds);
                    JOptionPane.showMessageDialog(null, "Mensalidade(s) cancelada(s) com sucesso.");
                    populaTabelaMensalidadesGeradas();
                    tableMensalidadesGeradas.clearSelection();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma Mensalidade para cancelamento.");
            }
        });
    }

    public void populaTableComissoes() {
        tablecomissao.setRowSelectionAllowed(true);
        tablecomissao.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tablecomissao.setModel(comissoesGeradasTableModel);

        scrollPaneTableComissao.revalidate();
        scrollPaneTableComissao.repaint();

        comissoesCadastradas = comissaoController.buscaComissoes();
        comissoesGeradasTableModel.setComissoes(comissoesCadastradas);
    }

    public void filtrarComissoes() {
        buttonBuscarComissoes.addActionListener(e -> {
            filtraComissoes();
        });
    }

    private void filtraComissoes() {
        String buscarPorInstrutor = textFieldBuscarInstrutor.getText().trim().toLowerCase();
        boolean buscaPorAberto = checkBoxAbertas.isSelected();
        boolean buscaPorPago = checkBoxPagas.isSelected();
        Date dataInicioSelecionada = dateChooserInicioCom.getDate();
        Date dataFimSelecionada = dateChooserFimCom.getDate();

        final int mesInicio = (dataInicioSelecionada != null)
                ? dataInicioSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue()
                : 1;

        final int mesFim = (dataFimSelecionada != null)
                ? dataFimSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue()
                : 12;

        List<Comissao> comissoesFiltradas = comissoesCadastradas
                .stream()
                .filter(
                        comissao ->
                                (buscarPorInstrutor.isEmpty() || comissao.getInstrutor().getPessoa().getNome().trim().toLowerCase().contains(buscarPorInstrutor.trim().toLowerCase())) &&
                                        (comissao.getMesReferente() >= mesInicio && comissao.getMesReferente() <= mesFim) &&
                                        ((!buscaPorAberto && !buscaPorPago || (buscaPorAberto || comissao.isPago()) && (buscaPorPago || !comissao.isPago()))))
                .collect(Collectors.toList());

        comissoesGeradasTableModel.setComissoes(comissoesFiltradas);
    }

    private void criaPanelDateComissoes() {
        JLabel labelInicio = new JLabel("Inicio: ");
        dateChooserInicioCom = new JDateChooser();
        dateChooserInicioCom.setDateFormatString("dd/MM/yyyy");
        dateChooserInicioCom.setPreferredSize(new Dimension(150, 25));

        JLabel labelFim = new JLabel("Fim: ");
        dateChooserFimCom = new JDateChooser();
        dateChooserFimCom.setDateFormatString("dd/MM/yyyy");
        dateChooserFimCom.setPreferredSize(new Dimension(150, 25));

        panelDatas.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelDatas.add(labelInicio);
        panelDatas.add(dateChooserInicioCom);
        panelDatas.add(labelFim);
        panelDatas.add(dateChooserFimCom);
        panelDatas.revalidate();
        panelDatas.repaint();

        dateChooserFimCom.addPropertyChangeListener("date", e -> {
            if (dateChooserInicioCom.getDate() != null && dateChooserFimCom.getDate() != null) {
                if (dateChooserInicioCom.getDate().after(dateChooserFimCom.getDate())) {
                    JOptionPane.showMessageDialog(null, "Data Inicio deve ser menor que a Data Fim.");
                    dateChooserInicioCom.setCalendar(null);
                }
            }
        });
    }

    private void pagarComissao() {
        buttonPagamentoComissao.addActionListener(e -> {
            int[] linhasSelecionadas = tablecomissao.getSelectedRows();

            List<Long> listaIds = new ArrayList<>();

            for (int i = 0; i < linhasSelecionadas.length; i++) {
                int linha = linhasSelecionadas[i];
                Long id = (Long) tablecomissao.getValueAt(linha, 0);
                listaIds.add(id);
            }

            if (listaIds.size() > -1) {
                comissaoController.pagarComissao(listaIds);
                JOptionPane.showMessageDialog(null, "Comissao(oes) paga(s) com sucesso.");
                populaTableComissoes();
                tablecomissao.clearSelection();
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma Comissao para pagamento.");
            }
        });
    }

    private void cancelarPagamentoComissao() {
        buttonCancelarPagamentoComissao.addActionListener(e -> {
            int[] linhasSelecionadas = tablecomissao.getSelectedRows();

            List<Long> listaIds = new ArrayList<>();

            for (int i = 0; i < linhasSelecionadas.length; i++) {
                int linha = linhasSelecionadas[i];
                Long id = (Long) tablecomissao.getValueAt(linha, 0);
                listaIds.add(id);
            }

            if (listaIds.size() > -1) {
                var selection = JOptionPane.showOptionDialog(null, "Deseja cancelar o pagamento da(s) comissao(oes) selecionada(s)?", "Cancelar Pagamento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (selection == JOptionPane.YES_OPTION) {
                    comissaoController.cancelarPagamentoComissao(listaIds);
                    JOptionPane.showMessageDialog(null, "Comissao(oes) cancelada(s) com sucesso.");
                    populaTableComissoes();
                    tablecomissao.clearSelection();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma Comissao para cancelamento.");
            }
        });
    }
}