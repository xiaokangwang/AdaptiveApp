package com.hintmate.springboot.service;

public interface WordMeaningsService {
    String fetchMeanings(String word);
    void updateUserKnowledge(String word);
}
