package com.hintmate.springboot.model;

import java.util.List;

public class HintResponseBody {
    public HintResponseBody(List<String> payload) {
        this.payload = payload;
    }

    public List<String> getPayload() {
        return payload;
    }

    public void setPayload(List<String> payload) {
        this.payload = payload;
    }

    List<String> payload;
}
