package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.List;

public class PetInfoList implements Serializable {

    private String result;
    private List<Pet> items;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Pet> getItems() {
        return items;
    }

    public void setItems(List<Pet> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
