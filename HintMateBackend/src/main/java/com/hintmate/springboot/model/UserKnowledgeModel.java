package com.hintmate.springboot.model;

public class UserKnowledgeModel {
    private String word;
    private int hit_counter;


    public UserKnowledgeModel(String word, int hit_counter) {
        this.word = word;
        this.hit_counter = hit_counter;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getHit_counter() {
        return hit_counter;
    }

    public void setHit_counter(int hit_counter) {
        this.hit_counter = hit_counter;
    }
}
