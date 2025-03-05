package org.mensalidades.tableModel;

import org.mensalidades.Model.Instrutor;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class InstrutorTableModel extends AbstractTableModel {
    private final String[] colunas = {"ID", "Nome", "Comissão"};
    private final List<Instrutor> instrutores;

    public InstrutorTableModel(List<Instrutor> instrutores) {
        this.instrutores = instrutores;
    }

    @Override
    public int getRowCount() {
        return instrutores.size();
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
        Instrutor instrutor = instrutores.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> instrutor.getId();
            case 1 -> instrutor.getPessoa().getNome();
            case 2 -> instrutor.getComissao();
            default -> null;
        };
    }

    public void setInstrutores(List<Instrutor> instrutores) {
        this.instrutores.clear();
        this.instrutores.addAll(instrutores);
        fireTableDataChanged();
    }

    public Instrutor getInstrutor(int rowIndex) {
        Instrutor instrutor = instrutores.get(rowIndex);
        return instrutor;
    }
}
