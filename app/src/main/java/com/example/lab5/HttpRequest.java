package com.example.lab5;

import static com.example.lab5.ApiService.BASE_URL;

import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private ApiService reApiService;

    public HttpRequest() {
        reApiService= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
}
    public ApiService callAPI() {
        return reApiService;
    }
}
