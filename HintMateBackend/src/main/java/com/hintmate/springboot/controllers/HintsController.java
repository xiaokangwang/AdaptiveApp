package com.hintmate.springboot.controllers;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HintsController {
    @RequestMapping("/getMeaning")
    public String getWordMeanings(@RequestParam(value="payload")String payload) {
    	String meaning = "ABCDE";
    	String res = payload + " Means " + meaning; 
        return res;
    }
}
