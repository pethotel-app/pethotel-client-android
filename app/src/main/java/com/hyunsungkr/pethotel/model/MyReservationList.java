package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.List;

public class MyReservationList implements Serializable {

    private String result;
    private List<MyReservation> hotel;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<MyReservation> getHotel() {
        return hotel;
    }

    public void setHotel(List<MyReservation> hotel) {
        this.hotel = hotel;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
