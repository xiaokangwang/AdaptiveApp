package com.hintmate.springboot.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hintmate.springboot.service.DatabaseConnectionService;
import com.hintmate.springboot.service.WordMeaningsService;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.Collection;

public class WordMeaningsServiceImpl implements WordMeaningsService {

    private Multimap<String, String> map = null;
    private String KNOWN_WORDS_TABLE = "Known_Words";
    private String UNKNOWN_WORDS_TABLE = "Unknown_Words";

    @Override
    public String fetchMeanings(String word) {
        DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
        Connection dbConnection = dbs.getDBConnectionToMeanings();
        ResultSet resultData = fetchAllData(dbConnection);
        if (map == null) {
            loadDictionary(resultData, dbConnection);
        }
        return processQuery(word);
    }

    @Override
    public void updateUserKnowledge(String word){
        Integer knownWordCounter = getWordHitCount(word, KNOWN_WORDS_TABLE);
        if(knownWordCounter !=null){
            System.out.println("Word is known to the user");
            int updatedCounter = knownWordCounter + 1;
            if(updatedCounter == 5 ){
                System.out.println("Update condition met, moving the word to unknown_words table");
                removeWordFromTable(word, KNOWN_WORDS_TABLE);
                addWordToTable(word, UNKNOWN_WORDS_TABLE);
            }else{
                System.out.println("Updating the hit-counter");
                updateHitCounter(word, KNOWN_WORDS_TABLE, updatedCounter);
            }
        }else {
            Integer unknownWordCounter = getWordHitCount(word, UNKNOWN_WORDS_TABLE);
            if(unknownWordCounter !=null) {
                System.out.println("Word is unknown to the user");
                int updatedCounter = unknownWordCounter + 1;
                if(updatedCounter == 5){
                    System.out.println("Update condition met, moving the word to known_words table");
                    removeWordFromTable(word, UNKNOWN_WORDS_TABLE);
                    addWordToTable(word, KNOWN_WORDS_TABLE);
                }else{
                    System.out.println("Updating the hit-counter");
                    updateHitCounter(word, UNKNOWN_WORDS_TABLE, updatedCounter);
                }

            }else{
                System.out.println("Word not found in both tables, adding it to unknown_words table");
                addWordToTable(word, UNKNOWN_WORDS_TABLE);
            }
        }

    }

    private void removeWordFromTable(String word, String tableName){
        DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
        try(Connection dbConnection = dbs.getDBConnectionToUKnowledge();) {
            word = "\""+word+"\"";
            String sqlQuery = "DELETE FROM " +tableName+" WHERE word = "+word+";";
            System.out.println(sqlQuery);
            Statement stmt=dbConnection.createStatement();
            stmt.execute(sqlQuery);
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addWordToTable(String word, String tableName){
        DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
        try(Connection dbConnection = dbs.getDBConnectionToUKnowledge();) {
            String sqlQuery = "INSERT INTO "+tableName+"(word,counter) VALUES(?,?)";
            System.out.println(sqlQuery);
            PreparedStatement pstmt = dbConnection.prepareStatement(sqlQuery);

            pstmt.setString(1, word);
            pstmt.setDouble(2, 0);

            boolean status = pstmt.execute();
            System.out.println(status);
            } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void updateHitCounter(String word, String tableName, int counter){
        DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
        try(Connection dbConnection = dbs.getDBConnectionToUKnowledge();) {
            word = "\""+word+"\"";
            String sqlQuery = "UPDATE "+tableName+" SET counter="+counter+" WHERE word = "+word+";";
            System.out.println(sqlQuery);
            Statement stmt=dbConnection.createStatement();
            stmt.execute(sqlQuery);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private Integer getWordHitCount(String word, String tableName){
        DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
        try(Connection dbConnection = dbs.getDBConnectionToUKnowledge();) {
            word = "\""+word+"\"";
            String sqlQuery = "SELECT * FROM "+tableName+" WHERE word = "+word+";";
            System.out.println(sqlQuery);
            Statement stmt=dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.isBeforeFirst() || rs.getRow() != 0) {
                if (rs.next()) {
                    int counter = rs.getInt("counter");
                    System.out.println(counter);
                    return counter;
                }
            }
            return null;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private Collection<String> lookupWord(String key) {
        return map.get(key);
    }

    private boolean isInputValid(String keyword) {
        if (keyword.matches("[^\\x00-\\x7F]+")) {
            System.out.println("\n The query must contain only english letters.");
            return false;
        }
        return true;
    }

    public String processQuery(String keyword) {

        if (isInputValid(keyword) == true) {
            if (lookupWord(keyword).isEmpty()) {
                System.out.println(" Meaning not found for " + "\"" + keyword + "\"" + ".");
            } else {
                return lookupWord(keyword).toString();
            }
        }
        return "Meaning not found !";
    }

    private ResultSet fetchAllData(Connection connection) {

        ResultSet resultSet = null;
        String sql = " SELECT `lemma`, `definition`" + " FROM `words` NATURAL JOIN"
                + " `senses` NATURAL JOIN `synsets`";

        try {
            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery(sql);

        } catch (SQLException s) {
            s.printStackTrace();
        }
        return resultSet;
    }

    private void loadDictionary(ResultSet resultSet, Connection connection) {

        map = ArrayListMultimap.create();

        try {
            while (resultSet.next()) {

                String word = resultSet.getString("lemma");
                String definition = resultSet.getString("definition");

                map.put(word, definition);

            }
        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException s) {
                s.printStackTrace();
            }
        }
    }
}
