package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.List;

public class MyReservationList implements Serializable {

    private String result;
    private List<MyReservation> items;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<MyReservation> getItems() {
        return items;
    }

    public void setItems(List<MyReservation> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
