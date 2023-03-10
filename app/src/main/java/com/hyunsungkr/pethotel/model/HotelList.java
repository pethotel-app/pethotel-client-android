package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.List;

public class HotelList implements Serializable {

    private String result;
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


}

