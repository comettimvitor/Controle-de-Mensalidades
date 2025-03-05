package org.mensalidades.tableModel;

import org.mensalidades.Model.Comissao;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Objects;

public class ComissoesGeradasTableModel extends AbstractTableModel {
    private final String[] colunas = {"ID Comissão", "Instrutor", "Mes Referente", "Valor", "Pago?"};
    private final List<Comissao> comissoes;
    private Boolean[] pago;

    public ComissoesGeradasTableModel(List<Comissao> comissao) {
        this.comissoes = comissao;
    }

    @Override
    public int getRowCount() {
        return comissoes.size();
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
        Comissao comissao = comissoes.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> comissao.getId();
            case 1 -> comissao.getInstrutor().getPessoa().getNome();
            case 2 -> comissao.getMes_referente();
            case 3 -> comissao.getValor();
            case 4 -> pago[rowIndex];
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == 4) {
            pago[rowIndex] = (Boolean) aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return (columnIndex == 4) ? Boolean.class : Objects.class;
    }

    public void setComissoes(List<Comissao> comissoes) {
        this.comissoes.clear();
        this.comissoes.addAll(comissoes);

        pago = new Boolean[comissoes.size()];

        for(Comissao comissao : comissoes) {
            if(comissao.isPago()) {
                pago[comissoes.indexOf(comissao)] = true;
            } else {
                pago[comissoes.indexOf(comissao)] = false;
            }
        }

        fireTableDataChanged();
    }
}
