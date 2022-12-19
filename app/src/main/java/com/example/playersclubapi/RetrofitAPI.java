package com.example.playersclubapi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface RetrofitAPI {
    @POST("Staffapis")
    Call<Staff> createPost(@Body Staff staff);

    @PUT("Staffapis")
    Call<Staff> updateData(@Query("id") int Id, @Body Staff staff);

    @DELETE("Staffapis")
    Call<Staff> deleteData(@Query("id") int Id);
}

