package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class HotelList implements Serializable {

    private String result;
    private List<Hotel> hotel;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Hotel> getHotel() {
        return hotel;
    }

    public void setHotel(List<Hotel> hotel) {
        this.hotel = hotel;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
