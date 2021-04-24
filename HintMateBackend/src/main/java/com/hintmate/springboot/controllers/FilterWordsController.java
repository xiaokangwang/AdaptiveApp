package com.hintmate.springboot.controllers;

import com.hintmate.springboot.model.HintResponseBody;
import com.hintmate.springboot.model.WordListsBody;
import com.hintmate.springboot.service.WordsFilterService;
import com.hintmate.springboot.service.impl.WordFilterServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class FilterWordsController {

    @PostMapping("/getWordToHint")
    public HintResponseBody fetchFilteredWords(@RequestBody WordListsBody request) {
        List words = request.getPayload();
        System.out.println(words.size());
        Set<String> set = new HashSet<>(words);
        words.clear();
        words.addAll(set);
        System.out.println(words.size());
        WordsFilterService filterService = new WordFilterServiceImpl();
        List<String> unknownWords = filterService.getUnknownWords(request);

        return new HintResponseBody(unknownWords);
    }
}