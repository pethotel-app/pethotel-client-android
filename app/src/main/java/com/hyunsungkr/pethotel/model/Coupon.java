package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

public class Coupon implements Serializable {

    private int id;
    private String userId;
    private String couponId;
    private String title;
    private String description;
    private int percent;
    private String dateOfUseStart;
    private String dateOfUseEnd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getDateOfUseStart() {
        return dateOfUseStart;
    }

    public void setDateOfUseStart(String dateOfUseStart) {
        this.dateOfUseStart = dateOfUseStart;
    }

    public String getDateOfUseEnd() {
        return dateOfUseEnd;
    }

    public void setDateOfUseEnd(String dateOfUseEnd) {
        this.dateOfUseEnd = dateOfUseEnd;
    }
}
