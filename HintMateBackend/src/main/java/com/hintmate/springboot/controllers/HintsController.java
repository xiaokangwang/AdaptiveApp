package com.hintmate.springboot.controllers;

import com.hintmate.springboot.model.HintsRequestBody;
import com.hintmate.springboot.service.WordMeaningsService;
import com.hintmate.springboot.service.impl.WordMeaningsServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HintsController {

    private WordMeaningsService meaningsService = new WordMeaningsServiceImpl();

    @PostMapping("/getHintForWord")
    public String getWordMeanings(@RequestBody HintsRequestBody request) {
        String payload = request.getPayload().toLowerCase();
        String meaning = meaningsService.fetchMeanings(payload);
        return meaning;
    }
}
