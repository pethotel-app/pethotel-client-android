package com.hyunsungkr.pethotel.model;


public class ChatDTO {
    private String userId;
    private String editText;
    private String userName;
    private String adminName;
    private String sentTime;

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public ChatDTO(String userId, String editText, String userName, String adminName, String sentTime) {
        this.userId = userId;
        this.editText = editText;
        this.userName = userName;
        this.adminName = adminName;
        this.sentTime = sentTime;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public ChatDTO() {}
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ChatDTO(String userId, String editText, String userName,String sentTime) {
        this.userId = userId;
        this.editText = editText;
        this.userName = userName;
        this.sentTime = sentTime;
    }

    public String getUserId() {
        return userId;
    }



    public String getEditText() {
        return editText;
    }

    public void setEditText(String editText) {
        this.editText = editText;
    }
}