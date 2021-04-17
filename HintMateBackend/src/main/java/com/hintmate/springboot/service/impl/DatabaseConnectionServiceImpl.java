package com.hintmate.springboot.service.impl;

import com.hintmate.springboot.service.DatabaseConnectionService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionServiceImpl implements DatabaseConnectionService {

    private static String LOCAL_DB = "src/main/resources/sqlite-31.db";

    @Override
    public Connection getDBConnection() {
        Connection connection = null;
        String DB_URL = "jdbc:sqlite:" + LOCAL_DB;

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
