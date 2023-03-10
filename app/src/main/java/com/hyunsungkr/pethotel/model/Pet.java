package com.hyunsungkr.pethotel.model;

import java.io.Serializable;

public class Pet implements Serializable {

    private int id;
    private int userId;
    private String petImgUrl;
    private String name;
    private int classification;
    private String species;
    private int age;
    private int weight;
    private int gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPetImgUrl() {
        return petImgUrl;
    }

    public void setPetImgUrl(String petImgUrl) {
        this.petImgUrl = petImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    // 성별 int값에 따라 String으로 변환하여 반환하는 메소드
    public String getGenderString() {
        if (gender == 1) {
            return "수컷";
        } else if (gender == 2) {
            return "암컷";
        } else {
            return "기타";
        }
    }

    // 분류 int값에 따라 String으로 변환하여 반환하는 메소드
    public String getClassificationString() {
        if (classification == 1) {
            return "강아지";
        } else if (classification == 2) {
            return "고양이";
        } else {
            return "기타";
        }
    }

}
