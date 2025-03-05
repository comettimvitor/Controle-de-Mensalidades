package org.mensalidades.tableModel;

import org.mensalidades.Model.Turma;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AlunoTurmaTableModel extends AbstractTableModel {
    private final String[] colunas = {"ID", "Modalidade", "Instrutor", "Horario", "Mensalidade", " "};
    private final List<Turma> turmas;
    private Boolean[] selecionados;

    public AlunoTurmaTableModel(List<Turma> turmas) {
        this.turmas = new ArrayList<>(turmas);
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
            case 2 -> turma.getInstrutor().getPessoa().getNome();
            case 3 -> turma.getHorario().getHorarioInicio();
            case 4 -> turma.getModalidade().getValorMensalidade();
            case 5 -> selecionados[rowIndex];
            default -> null;
        };
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas.clear();
        this.turmas.addAll(turmas);

        selecionados = new Boolean[turmas.size()];
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 5;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 5) {
            selecionados[rowIndex] = (Boolean) aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return (columnIndex == 5) ? Boolean.class : Object.class;
    }

    public List<Turma> getTurmasSelecionadas() {
        List<Turma> turmasSelecionadas = new ArrayList<>();
        for (int i = 0; i < turmas.size(); i++) {
            if (Boolean.TRUE.equals(selecionados[i])) {
                turmasSelecionadas.add(turmas.get(i));
            }
        }
        return turmasSelecionadas;
    }
}
