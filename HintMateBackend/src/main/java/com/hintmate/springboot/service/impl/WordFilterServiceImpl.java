package com.hintmate.springboot.service.impl;

import com.hintmate.springboot.model.UserKnowledgeModel;
import com.hintmate.springboot.model.WordListsBody;
import com.hintmate.springboot.service.DatabaseConnectionService;
import com.hintmate.springboot.service.WordsFilterService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WordFilterServiceImpl implements WordsFilterService {

    @Override
    public List<String> getUnknownWords(WordListsBody wordListsBody) {
        List<String> allWords = new ArrayList<>();
        if(wordListsBody.getPayload()!=null){
            allWords = wordListsBody.getPayload();
            allWords.replaceAll(String::toLowerCase);
            try{
                DatabaseConnectionService dbs = new DatabaseConnectionServiceImpl();
                Connection dbConnection = dbs.getDBConnectionToUKnowledge();

                String wordQuery = allWords.stream()
                        .map(s -> StringUtils.wrap(s, "\""))
                                .collect(Collectors.joining(", "));
                System.out.println(wordQuery);
                String sqlQuery = "SELECT * FROM Known_Words WHERE word IN ("+wordQuery+");";
                System.out.println(sqlQuery);
                Statement stmt=dbConnection.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);
                System.out.println(rs);
                List<String> fetchedWords = new ArrayList<>();

                while (rs.next()) {
                    String fetchedWord = rs.getString("word");
                    fetchedWords.add(fetchedWord);
                }
                allWords.removeAll(fetchedWords);
                System.out.println(allWords);
                return allWords;
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        return allWords;
    }

}