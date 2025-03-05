package org.mensalidades.generics;

import com.toedter.calendar.JDateChooser;
import org.mensalidades.Model.Aluno;
import org.mensalidades.Model.Pessoa;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConversorData {

    public static java.sql.Date converteData(Date dateChooser) {
        String formatoData = "dd/MM/yyyy";
        SimpleDateFormat data = new SimpleDateFormat(formatoData);
        data.setLenient(false);

        Date dataConvertida = dateChooser;
        return new java.sql.Date(dataConvertida.getTime());
    }

    public static String formataData(Pessoa pessoa) {
        if (pessoa.getDataNascimento() == null) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(pessoa.getDataNascimento());
    }
}
