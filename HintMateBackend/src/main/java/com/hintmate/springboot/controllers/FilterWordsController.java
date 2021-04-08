package com.hintmate.springboot.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterWordsController {

    @RequestMapping("/fetchFilteredWords")
    public List fetchFilteredWords(@RequestParam(value="payload")JSONObject payload) {
        List<String> filteredWords = new ArrayList<>();
        filteredWords.add("word1");
        filteredWords.add("2ndWord");
        return filteredWords;
    }
}
