package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

public class Check implements Serializable {

    private String result;
    private String check;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
