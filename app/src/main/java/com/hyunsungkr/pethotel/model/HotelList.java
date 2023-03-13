package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

import java.util.HashMap;

import java.util.Dictionary;

import java.util.List;

public class HotelList implements Serializable {

    private String result;

    private List<Hotel> items;
    private int count;

    private Hotel hotel;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public List<Hotel> getItems() {
        return items;
    }

    public void setItems(List<Hotel> hotel) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}

