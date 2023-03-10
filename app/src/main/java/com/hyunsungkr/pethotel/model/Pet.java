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

    public Pet() {
    }

    public Pet(int id, int userId, String petImgUrl, String name, int classification, String species, int age, int weight, int gender) {
        this.id = id;
        this.userId = userId;
        this.petImgUrl = petImgUrl;
        this.name = name;
        this.classification = classification;
        this.species = species;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
    }

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
}
