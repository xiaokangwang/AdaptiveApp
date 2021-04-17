package com.hintmate.springboot.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hintmate.springboot.service.DatabaseConnectionService;
import com.hintmate.springboot.service.WordMeaningsService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class WordMeaningsServiceImpl implements WordMeaningsService {

    private Multimap<String, String> map = null;

    @Override
    public String fetchMeanings(String word) {
        DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
        Connection dbConnection = dbs.getDBConnection();
        ResultSet resultData = fetchAllData(dbConnection);
        if (map == null) {
            loadDictionary(resultData, dbConnection);
        }
        return processQuery(word);
    }

    private Collection<String> lookupWord(String key) {
        return map.get(key);
    }

//    private String searchSimilarResults(String keyword) {
//        String similarResults = "";
//        for (String key : map.keySet()) {
//            if (key.startsWith(keyword)) {
//                similarResults += key + ", ";
//            }
//        }
//        return similarResults;
//    }

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
                System.out.println(" No exact matches for " + "\"" + keyword + "\"" + ".");
//                if (!searchSimilarResults(keyword).isEmpty()) {
//                    String r = searchSimilarResults(keyword);
//                    return r;
//                } else {
//                    return "No match found !";
//                }
            } else {
                return lookupWord(keyword).toString();
            }
        }
        return "No match found !";
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
