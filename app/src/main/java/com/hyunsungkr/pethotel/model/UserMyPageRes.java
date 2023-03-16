package com.hyunsungkr.pethotel.model;

import java.io.Serializable;
import java.util.List;

public class UserMyPageRes implements Serializable {

    private String result;

    private List<UserMyPage> items;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<UserMyPage> getItems() {
        return items;
    }

    public void setItems(List<UserMyPage> items) {
        this.items = items;
    }
}
