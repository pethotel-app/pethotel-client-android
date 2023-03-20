package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.List;

public class PointList implements Serializable {

    private String result;
    private List<Point> items;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Point> getItems() {
        return items;
    }

    public void setItems(List<Point> items) {
        this.items = items;
    }
}
