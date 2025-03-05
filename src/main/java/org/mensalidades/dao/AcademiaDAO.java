package org.mensalidades.dao;

import org.mensalidades.connection.ConexaoDatabase;
import org.mensalidades.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademiaDAO {

    public boolean existeAcademia() {
        String confereAcademia = "SELECT COUNT(1) FROM sistema.academia WHERE fim_vigencia IS NULL;";

        try(Connection connection = ConexaoDatabase.getInstancia().getConexao();
            PreparedStatement statement = connection.prepareStatement(confereAcademia);
            ResultSet resultSet = statement.executeQuery()) {

            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao verificar existencia de Academia.");
        }
    }

    public void cadastraAcademia(String rua, String bairro, String numero, String complemento, String cep, String uf, String nome) {
        String criaEndereco = "INSERT into sistema.endereco(rua,bairro,numero,complemento,cep,inicio_vigencia,fim_vigencia,uf) " +
                "VALUES(?,?,?,?,?, CURRENT_DATE, NULL, ?) RETURNING id;";

        int idGerado = 0;

        String criaAcademia = "INSERT INTO sistema.academia(id_endereco, nome, inicio_vigencia) VALUES(?,?,CURRENT_DATE);";

        try(Connection conn = ConexaoDatabase.getInstancia().getConexao();
            PreparedStatement statementEndereco = conn.prepareStatement(criaEndereco)) {

            statementEndereco.setString(1, rua);
            statementEndereco.setString(2, bairro);
            statementEndereco.setString(3, numero);
            statementEndereco.setString(4, complemento);
            statementEndereco.setString(5, cep);
            statementEndereco.setString(6, uf);

            try(ResultSet resultSet = statementEndereco.executeQuery()){
                if(resultSet.next()) {
                    idGerado = resultSet.getInt("id");
                } else {
                    throw new DatabaseException("Erro ao obter ID do endereco.");
                }
            }

            try(PreparedStatement statementAcademia = conn.prepareStatement(criaAcademia)) {
                statementAcademia.setInt(1, idGerado);
                statementAcademia.setString(2, nome);
                statementAcademia.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Erro ao cadastrar academia.");
        }
    }
}
