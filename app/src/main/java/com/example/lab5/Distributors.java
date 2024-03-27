package com.example.lab5;

import com.google.gson.annotations.SerializedName;

public class Distributors {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;


    public Distributors() {
    }

    public Distributors(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Distributors(String name) {
        this.name = name;
    }

    public Distributors(String id, String name, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
