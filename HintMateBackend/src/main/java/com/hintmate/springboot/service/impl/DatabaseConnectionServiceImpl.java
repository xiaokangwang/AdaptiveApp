package com.hintmate.springboot.service.impl;

import com.hintmate.springboot.service.DatabaseConnectionService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionServiceImpl implements DatabaseConnectionService {

    private static String MEANINGS_DB = "src/main/resources/sqlite-31.db";
    private static String KNOWLEDGE_DB = "src/main/resources/user-knowledge.db";

    @Override
    public Connection getDBConnectionToMeanings() {
        Connection connection = null;
        String DB_URL = "jdbc:sqlite:" + MEANINGS_DB;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return connection;
    }

    @Override
    public Connection getDBConnectionToUKnowledge() {
        Connection connection = null;
        String DB_URL = "jdbc:sqlite:" + KNOWLEDGE_DB;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return connection;
    }
}
