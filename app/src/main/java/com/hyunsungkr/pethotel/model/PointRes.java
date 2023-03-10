package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

public class PointRes implements Serializable {

    private String result;
    private int totalPoint;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }
}
