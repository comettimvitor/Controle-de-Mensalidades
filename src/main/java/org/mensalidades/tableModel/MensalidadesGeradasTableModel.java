package org.mensalidades.tableModel;

import org.mensalidades.Model.Mensalidade;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MensalidadesGeradasTableModel extends AbstractTableModel {
    private final String[] colunas = {"ID", "Aluno", "Vencimento", "Valor", "Pago?"};
    private final List<Mensalidade> mensalidades;
    private Boolean[] pago;

    public MensalidadesGeradasTableModel(List<Mensalidade> mensalidades) {
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
            case 0 -> mensalidade.getId();
            case 1 -> mensalidade.getAluno().getPessoa().getNome();
            case 2 -> mensalidade.getVencimento();
            case 3 -> mensalidade.getValor();
            case 4 -> pago[rowIndex];
            default -> null;
        };
    }

    public void setMensalidades(List<Mensalidade> mensalidades) {
        this.mensalidades.clear();
        this.mensalidades.addAll(mensalidades);

        pago = new Boolean[mensalidades.size()];

        for(Mensalidade mensalidade : mensalidades){
            if(mensalidade.isPago()){
                pago[mensalidades.indexOf(mensalidade)] = true;
            }else{
                pago[mensalidades.indexOf(mensalidade)] = false;
            }
        }

        fireTableDataChanged();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 4) {
            pago[rowIndex] = (Boolean) aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return (columnIndex == 4) ? Boolean.class : Object.class;
    }
}
