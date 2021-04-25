package com.hintmate.springboot.utilities;

import com.hintmate.springboot.controllers.FileToDBHandler;

import java.io.File;

public class Utility {
    private static final String USER_INPUT_DIR = "upload-dir";
    private static final String KNOWN_WORDS_RESOURCE = "pyFileDir" + System.getProperty("file.separator") + "known_words.txt";
    private static final String UNKNOWN_WORDS_RESOURCE = "pyFileDir" + System.getProperty("file.separator") + "unknown_words.txt";
    private static String KNOWLEDGE_DB = "src" + System.getProperty("file.separator") + "main" + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "user-knowledge.db";
    public static Boolean userKnowledgeExists = null;

    public static void checkUserKnowledge() {
        File userInput = new File(USER_INPUT_DIR);
        File knownWordsFile = new File(KNOWN_WORDS_RESOURCE);
        File unknownWordsFile = new File(UNKNOWN_WORDS_RESOURCE);
        File userKnowledgeDB = new File(KNOWLEDGE_DB);
        if (userKnowledgeDB.exists() && userInput.exists() && knownWordsFile.exists() && unknownWordsFile.exists()) {
            userKnowledgeExists = Boolean.TRUE;
        } else {
            userKnowledgeExists = Boolean.FALSE;
        }
    }

    public static void createEmptyUserKnowledge(){
        if (userKnowledgeExists == null) {
            checkUserKnowledge();
        }
        if (!userKnowledgeExists) {
            try {
                FileToDBHandler.createDatabase();
                File knownWordsEmptyFile = new File(KNOWN_WORDS_RESOURCE);
                File unknownWordsEmptyFile = new File(UNKNOWN_WORDS_RESOURCE);
                knownWordsEmptyFile.createNewFile();
                unknownWordsEmptyFile.createNewFile();
                FileToDBHandler.loadKnownWords();
                FileToDBHandler.loadUnKnownWords();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
