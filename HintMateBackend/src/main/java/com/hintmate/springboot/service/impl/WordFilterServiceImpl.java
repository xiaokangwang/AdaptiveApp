package com.hintmate.springboot.service.impl;

import com.hintmate.springboot.model.UserKnowledgeModel;
import com.hintmate.springboot.model.WordListsBody;
import com.hintmate.springboot.service.DatabaseConnectionService;
import com.hintmate.springboot.service.WordsFilterService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WordFilterServiceImpl implements WordsFilterService {

    private static final String STOP_WORDS_RESOURCE = "src/main/resources/stopWords.txt";
    private List<String> englishStopWords = new ArrayList<>();

    @Override
    public List<String> getUnknownWords(WordListsBody wordListsBody) {
        List<String> allWords = new ArrayList<>();
        if(wordListsBody.getPayload()!=null){
            allWords = wordListsBody.getPayload();
            allWords = cleanList(allWords);
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

    private List<String> cleanList(List<String> wordList){
        wordList.replaceAll(String::toLowerCase);

        // Removing duplicates
        System.out.println(wordList.size());
        Set<String> set = new HashSet<>(wordList);
        wordList.clear();
        wordList.addAll(set);
        System.out.println(wordList.size());

        // Removing stop-words
        if(englishStopWords.isEmpty()){
            try{
                File file = ResourceUtils.getFile(STOP_WORDS_RESOURCE);
                englishStopWords = Files.readAllLines(file.toPath());
                englishStopWords = englishStopWords.stream().map(line -> line.toLowerCase()).collect(Collectors.toList());
            }catch (Exception ex){
                ex.printStackTrace();
                System.out.println("Could not load stop-words because of the above exception !");
                return wordList;
            }
        }
        wordList.removeAll(englishStopWords);
        System.out.println(wordList);
        // Removing words containing numbers
        List<String> cleanWordsList = wordList.stream()
                .filter(word -> !containsNumbers(word))
                .collect(Collectors.toList());

        System.out.println(cleanWordsList);
        return cleanWordsList;
    }

    private boolean containsNumbers(String word){
        boolean containsNumbers = false;
        if (word != null && !word.isEmpty()) {
            for (char c : word.toCharArray()) {
                if (containsNumbers = Character.isDigit(c)) {
                    break;
                }
            }
        }
        return containsNumbers;
    }

}