package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

public class Point implements Serializable {

    private int id;
    private int userId;
    private String content;
    private int addPoint;
    private int totalPoint;
    private String createdAt;
    private int isEarn;

    public int getIsEarn() {
        return isEarn;
    }

    public void setIsEarn(int isEarn) {
        this.isEarn = isEarn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAddPoint() {
        return addPoint;
    }

    public void setAddPoint(int addPoint) {
        this.addPoint = addPoint;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Point(String content, int addPoint) {
        this.content = content;
        this.addPoint = addPoint;
    }
}
