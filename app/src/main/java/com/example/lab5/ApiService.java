package com.example.lab5;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String BASE_URL = "http://192.168.1.10:3000/";

    @GET("api/get-distributors")
    Call<List<Distributors>> getData();

    @PUT("/api/update-istributors-by-id/{id}")
    Call<Distributors> updateDink(@Path("id") String id, @Body Distributors dinkModel);
    @DELETE("api/delete-distributors-by-id/{id}") // Annotation @DELETE để xác định phương thức DELETE
    Call<Distributors> deleteDistributorsById(@Path("id") String id);
    @POST("/api/add-distributors")
    Call<List<Distributors>> addDink(@Body Distributors dinkModel);

    @GET("api/searchDistributor")
    Call<ApiResponse> searchDink(@Query("key") String key);
}
