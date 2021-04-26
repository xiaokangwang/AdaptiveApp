package com.hintmate.springboot.controllers;

import com.hintmate.springboot.model.HintResponseBody;
import com.hintmate.springboot.model.WordListsBody;
import com.hintmate.springboot.service.WordsFilterService;
import com.hintmate.springboot.service.impl.WordFilterServiceImpl;
import com.hintmate.springboot.utilities.Utility;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FilterWordsController {

    @PostMapping("/getWordToHint")
    public HintResponseBody fetchFilteredWords(@RequestBody WordListsBody request) {
        List<String> unknownWords = new ArrayList<>();
        Utility.checkUserKnowledge();
        if(!Utility.userKnowledgeExists){
            Utility.createEmptyUserKnowledge();
            request.setPayload(unknownWords);
        }
        WordsFilterService filterService = new WordFilterServiceImpl();
        unknownWords = filterService.getUnknownWords(request);
        return new HintResponseBody(unknownWords);
    }


}