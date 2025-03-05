package org.mensalidades.tableModel;

import org.mensalidades.Model.Mensalidade;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class GeracaoMensalidadeTableModel extends AbstractTableModel {
    private final String[] colunas = {"ID Aluno", "Nome", "ID Turma", "Turma", "Mensalidade"};
    private final List<Mensalidade> mensalidades;

    public GeracaoMensalidadeTableModel(List<Mensalidade> mensalidades) {
        this.mensalidades = mensalidades;
    }

    @Override
    public int getRowCount() {
        return mensalidades.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mensalidade mensalidade = mensalidades.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> mensalidade.getAluno().getId();
            case 1 -> mensalidade.getAluno().getPessoa().getNome();
            case 2 -> mensalidade.getTurma().getId();
            case 3 -> mensalidade.getTurma().getModalidade().getNomeModalidade();
            case 4 -> mensalidade.getValor();
            default -> null;
        };
    }

    public void setMensalidades(List<Mensalidade> mensalidades) {
        this.mensalidades.clear();
        this.mensalidades.addAll(mensalidades);
        fireTableDataChanged();
    }
}
