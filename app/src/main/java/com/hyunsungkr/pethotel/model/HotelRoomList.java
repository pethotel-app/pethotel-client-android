package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.List;

public class HotelRoomList implements Serializable {

    private String result;
    private List<HotelRoom> items;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<HotelRoom> getItems() {
        return items;
    }

    public void setItems(List<HotelRoom> items) {
        this.items = items;
    }
}
