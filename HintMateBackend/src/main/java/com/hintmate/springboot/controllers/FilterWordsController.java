package com.hintmate.springboot.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilterWordsController {

    @RequestMapping("/fetchFilteredWords")
    public List fetchFilteredWords() {
        List<String> filteredWords = new ArrayList<>();
        filteredWords.add("word1");
        filteredWords.add("2ndWord");
        return filteredWords;
    }
}
