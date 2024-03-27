package com.example.lab5;

import java.util.List;

public class ApiResponse<T>{
    private int status;
    private String messenger;
    private List<Distributors>data;

    public ApiResponse(int status, String messenger, List<Distributors> data) {
        this.status = status;
        this.messenger = messenger;
        this.data = data;
    }

    public ApiResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public List<Distributors> getData() {
        return data;
    }

    public void setData(List<Distributors> data) {
        this.data = data;
    }
}
