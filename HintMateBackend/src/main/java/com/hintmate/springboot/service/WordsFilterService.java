package com.hintmate.springboot.service;

import com.hintmate.springboot.model.WordListsBody;

import java.util.List;

public interface WordsFilterService {
    List<String> getUnknownWords(WordListsBody wordListsBody);
}