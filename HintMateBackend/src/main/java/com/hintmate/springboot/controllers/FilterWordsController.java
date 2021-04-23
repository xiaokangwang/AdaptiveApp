package com.hintmate.springboot.controllers;

import com.hintmate.springboot.model.HintResponseBody;
import com.hintmate.springboot.model.WordListsBody;
import com.hintmate.springboot.service.WordsFilterService;
import com.hintmate.springboot.service.impl.WordFilterServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilterWordsController {

    @PostMapping("/getWordToHint")
    public HintResponseBody fetchFilteredWords(@RequestBody WordListsBody request) {
        List words = request.getPayload();
        List<String> filteredWords = new ArrayList<>();

        WordsFilterService filterService = new WordFilterServiceImpl();
        filterService.getUnknownWords(request);
        filteredWords.add("word1");
        filteredWords.add("2ndWord");
        //In real version, be sure to just respond the word that should be shown hint for
        return new HintResponseBody(words);
    }
}