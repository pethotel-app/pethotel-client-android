package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.List;

public class HotelReviewList implements Serializable {

    private String result;
    private List<HotelReview> items;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<HotelReview> getItems() {
        return items;
    }

    public void setItems(List<HotelReview> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
