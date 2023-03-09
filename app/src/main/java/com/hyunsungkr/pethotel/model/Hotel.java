package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

public class Hotel implements Serializable {

    private int id;
    private String title;
    private String addr;
    private double longtitude;
    private double latitude;
    private String tel;
    private String imgUrl;
    private String naverUrl;
    private String description;
    private int small;
    private int medium;
    private int large;
    private double avg;
    private int cnt;
    private int favorite;

    public Hotel() {
    }

    public Hotel(int id, String title, String addr, double longtitude, double latitude, String tel, String imgUrl, String naverUrl, String description, int small, int medium, int large, double avg, int cnt, int favorite) {
        this.id = id;
        this.title = title;
        this.addr = addr;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.tel = tel;
        this.imgUrl = imgUrl;
        this.naverUrl = naverUrl;
        this.description = description;
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.avg = avg;
        this.cnt = cnt;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNaverUrl() {
        return naverUrl;
    }

    public void setNaverUrl(String naverUrl) {
        this.naverUrl = naverUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSmall() {
        return small;
    }

    public void setSmall(int small) {
        this.small = small;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getLarge() {
        return large;
    }

    public void setLarge(int large) {
        this.large = large;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
