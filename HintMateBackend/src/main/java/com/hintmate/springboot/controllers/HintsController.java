package com.hintmate.springboot.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HintsController {
    @RequestMapping("/getMeaning")
    public String getWordMeanings() {
        return "This word means That";
    }
}
