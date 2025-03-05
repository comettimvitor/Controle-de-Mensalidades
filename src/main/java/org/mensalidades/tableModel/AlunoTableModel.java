package org.mensalidades.tableModel;

import org.mensalidades.Model.Aluno;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AlunoTableModel extends AbstractTableModel {
    private final String[] colunas = {"ID", "Nome", "Turma", "Horario", "Mensalidade", "Desconto"};
    private final List<Aluno> alunos;

    public AlunoTableModel(List<Aluno> alunos) {
        this.alunos = new ArrayList<>(alunos);
    }

    @Override
    public int getRowCount() {
        return alunos.size();
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
        Aluno aluno = alunos.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> aluno.getId();
            case 1 -> aluno.getPessoa() != null ? aluno.getPessoa().getNome() : "N/A";
            case 2 -> aluno.getTurma() != null && aluno.getTurma().getModalidade() != null
                    ? aluno.getTurma().getModalidade().getNomeModalidade() : "N/A";
            case 3 -> aluno.getTurma() != null && aluno.getTurma().getHorario() != null
                    ? aluno.getTurma().getHorario().getHorarioInicio() : "N/A";
            case 4 -> aluno.getTurma() != null ? aluno.getTurma().getModalidade().getValorMensalidade() : "N/A";
            case 5 -> aluno.getDesconto();
            default -> null;
        };
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos.clear();
        this.alunos.addAll(alunos);
        fireTableDataChanged();
    }

    public Aluno getAluno(int rowIndex) {
        Aluno aluno = alunos.get(rowIndex);
        return aluno;
    }
}
