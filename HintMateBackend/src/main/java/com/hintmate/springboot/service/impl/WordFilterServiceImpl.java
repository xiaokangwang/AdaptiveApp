package com.hintmate.springboot.service.impl;

import com.hintmate.springboot.model.UserKnowledgeModel;
import com.hintmate.springboot.model.WordListsBody;
import com.hintmate.springboot.service.DatabaseConnectionService;
import com.hintmate.springboot.service.WordsFilterService;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordFilterServiceImpl implements WordsFilterService {

    @Override
    public List<String> getUnknownWords(WordListsBody wordListsBody) {
        createDatabase();
        loadKnownWords();
        loadUnKnownWords();

        return null;
    }



    public void loadKnownWords(){
        DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
        Connection dbConnection = dbs.getDBConnectionToUKnowledge();
        try {
            String query = "CREATE TABLE IF NOT EXISTS Known_Words (word PRIMARYKEY NOT NULL);";
            Statement s  = dbConnection.createStatement();
            s.execute(query);
            String filePath = "src/main/resources/known_words.txt";
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bReader = new BufferedReader(isr);
            ArrayList<UserKnowledgeModel> listResult = new ArrayList<>();

            String line = null;

            String[] strWord = null;

            while(true) {
                line = bReader.readLine();
                if(line == null) {
                    break;
                } else {
                    strWord = line.split(",");
                    listResult.add(new UserKnowledgeModel(strWord[0], Integer.parseInt(strWord[1])));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadUnKnownWords(){
        DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
        Connection dbConnection = dbs.getDBConnectionToUKnowledge();
        try {
            String query = "CREATE TABLE IF NOT EXISTS Unknown_Words (word PRIMARYKEY NOT NULL);";
            Statement s  = dbConnection.createStatement();
            s.execute(query);
            String filePath = "src/main/resources/unknown_words.txt";
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bReader = new BufferedReader(isr);
            ArrayList<UserKnowledgeModel> listResult = new ArrayList<>();

            String line = null;

            String[] strWord = null;

            while(true) {
                line = bReader.readLine();
                if(line == null) {
                    break;
                } else {
                    strWord = line.split(",");
                    listResult.add(new UserKnowledgeModel(strWord[0], 0));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createDatabase(){
        String KNOWLEDGE_DB = "src/main/resources/user-knowledge.db";
        try (Connection conn = DriverManager.getConnection(KNOWLEDGE_DB)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                meta.getDriverName();
                System.out.println("DB created");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
