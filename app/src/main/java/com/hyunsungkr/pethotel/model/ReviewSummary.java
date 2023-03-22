package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

public class ReviewSummary implements Serializable {

    private String result;
    private String summary;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
