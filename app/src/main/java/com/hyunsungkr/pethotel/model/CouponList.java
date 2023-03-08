package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.List;

public class CouponList implements Serializable {

    private String result;
    private List<Coupon> items;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Coupon> getItems() {
        return items;
    }

    public void setItems(List<Coupon> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
