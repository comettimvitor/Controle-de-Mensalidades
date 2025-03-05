package org.mensalidades.tableModel;

import org.mensalidades.Model.Turma;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TurmaTableModel extends AbstractTableModel {
    private final String[] colunas = {"ID", "Modalidade", "Mensalidade"};
    private final List<Turma> turmas;

    public TurmaTableModel(List<Turma> turmas) {
        this.turmas = turmas;
    }

    @Override
    public int getRowCount() {
        return turmas.size();
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
        Turma turma = turmas.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> turma.getId();
            case 1 -> turma.getModalidade().getNomeModalidade();
            case 2 -> turma.getModalidade().getValorMensalidade();
            default -> null;
        };
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas.clear();
        this.turmas.addAll(turmas);
        fireTableDataChanged();
    }

    public Turma getTurma(int rowIndex) {
        Turma turma = turmas.get(rowIndex);
        return turma;
    }
}
