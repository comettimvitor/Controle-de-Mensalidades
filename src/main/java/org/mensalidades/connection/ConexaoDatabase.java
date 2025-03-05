package org.mensalidades.connection;

import org.mensalidades.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDatabase {
    private static ConexaoDatabase instancia;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/controle_mensalidades";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConexaoDatabase() throws SQLException{
        this.connection = criarNovaConexao();
    }

    private Connection criarNovaConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static synchronized ConexaoDatabase getInstancia() throws SQLException {
        if(instancia == null) {
            instancia = new ConexaoDatabase();
        }

        return instancia;
    }

    public Connection getConexao() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = criarNovaConexao();
        }

        return connection;
    }
}
