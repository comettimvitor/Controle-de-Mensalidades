package org.mensalidades.View;

import com.toedter.calendar.JDateChooser;
import org.mensalidades.Controller.ComissaoController;
import org.mensalidades.connection.ConexaoDatabase;
import org.mensalidades.exceptions.DatabaseException;

import javax.swing.*;
import java.awt.*;
import java.lang.module.ResolutionException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class TelaGeracaoComissao extends JFrame {
    private JPanel panelGeracaoComissao;
    private JPanel panelDataGeracao;
    private JPanel panelButtons;
    private JButton buttonGeracao;
    private JTable tableGeracaoComissao;
    private JScrollPane scrollPaneComissao;
    private JDateChooser dateChooserInicio;
    private JDateChooser dateChooserFim;

    private ComissaoController comissaoController;

    private TelaPrincipal telaPrincipal;

    public TelaGeracaoComissao(ComissaoController comissaoController, TelaPrincipal telaPrincipal) {
        this.comissaoController = comissaoController;
        this.telaPrincipal = telaPrincipal;

        setTitle("Geração de Comissões");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        setSize(new Dimension(600, 150));
        setLocationRelativeTo(null);

        add(panelGeracaoComissao);

        revalidate();
        repaint();

        criaDateChooser();
        gerarComissao();
    }

    private void criaDateChooser() {
        JLabel labelDataInicio = new JLabel("Inicio período: ");
        labelDataInicio.setFont(new Font("Arial", Font.BOLD, 12));
        dateChooserInicio = new JDateChooser();
        dateChooserInicio.setDateFormatString("dd/MM/yyyy");
        dateChooserInicio.setPreferredSize(new Dimension(150, 25));

        JLabel labelDataFim = new JLabel("Fim período: ");
        labelDataFim.setFont(new Font("Arial", Font.BOLD, 12));
        dateChooserFim = new JDateChooser();
        dateChooserFim.setDateFormatString("dd/MM/yyyy");
        dateChooserFim.setPreferredSize(new Dimension(150, 25));

        panelDataGeracao.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelDataGeracao.add(labelDataInicio);
        panelDataGeracao.add(dateChooserInicio);
        panelDataGeracao.add(labelDataFim);
        panelDataGeracao.add(dateChooserFim);
        panelDataGeracao.revalidate();
        panelDataGeracao.repaint();
    }

    private void gerarComissao() {
        buttonGeracao.addActionListener(e -> {
            Date dataInicio = new java.sql.Date(dateChooserInicio.getDate().getTime());
            Date dataFim = new java.sql.Date(dateChooserFim.getDate().getTime());

            comissaoController.gerarTabelaComissao(dataInicio, dataFim);

            JOptionPane.showMessageDialog(null, "Comissões geradas com sucesso!");
            this.dispose();

            telaPrincipal.populaTableComissoes();
        });
    }
}
