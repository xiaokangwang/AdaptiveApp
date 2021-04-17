package com.hintmate.springboot.model;

import java.util.List;

public class WordListsBody {
    private List<String> payload;

    public List<String> getPayload() {
        return payload;
    }

    public void setPayload(List<String> payload) {
        this.payload = payload;
    }
}
