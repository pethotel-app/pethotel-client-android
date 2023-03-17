package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

public class UserMyPage implements Serializable {

//            "id": 6,
//            "name": "곽두팔",
//            "userImgUrl": "https://img4.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202212/02/bemypet/20221202152203492rhcy.jpg",
//            "totalPoint": 1562,
//            "cntCoupon": 1

    private int id;
    private String name;
    private String userImgUrl;
    private int totalPoint;
    private int cntCoupon;

    public UserMyPage(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public int getCntCoupon() {
        return cntCoupon;
    }

    public void setCntCoupon(int cntCoupon) {
        this.cntCoupon = cntCoupon;
    }
}
