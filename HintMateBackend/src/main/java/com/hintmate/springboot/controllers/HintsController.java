package com.hintmate.springboot.controllers;

import com.hintmate.springboot.model.HintsRequestBody;
import com.hintmate.springboot.service.DatabaseConnectionService;
import com.hintmate.springboot.service.WordMeaningsService;
import com.hintmate.springboot.service.impl.DatabaseConnectionServiceImpl;
import com.hintmate.springboot.service.impl.WordMeaningsServiceImpl;
import com.hintmate.springboot.utilities.Utility;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HintsController {

    private WordMeaningsService meaningsService = new WordMeaningsServiceImpl();
    private DatabaseConnectionService dbService = new DatabaseConnectionServiceImpl();
    @PostMapping("/getHintForWord")
    public String getWordMeanings(@RequestBody HintsRequestBody request) {
        String payload = request.getPayload().toLowerCase();
        Utility.checkUserKnowledge();
        if(!Utility.userKnowledgeExists){
            Utility.createEmptyUserKnowledge();
        }
        String meaning = meaningsService.fetchMeanings(payload);
        meaningsService.updateUserKnowledge(payload);
        return meaning;
    }
}
