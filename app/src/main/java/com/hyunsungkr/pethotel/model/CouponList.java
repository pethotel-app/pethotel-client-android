package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.List;

public class CouponList implements Serializable {

    private String result;
    private List<Coupon> couponList;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
