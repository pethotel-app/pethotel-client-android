package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

public class UserRes implements Serializable {

    private String result;
    private String access_token;
    private String email;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
