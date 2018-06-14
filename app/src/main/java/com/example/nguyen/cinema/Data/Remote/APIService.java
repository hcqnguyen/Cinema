package com.example.nguyen.cinema.Data.Remote;

import com.example.nguyen.cinema.Data.Model.Film;
import com.example.nguyen.cinema.Data.Model.ResponeApi;

import java.util.ArrayList;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface APIService {

//    @FormUrlEncoded
//    @POST("/api/v1/movies/")
//    Call<Film> send();


    @Multipart
    @POST("/api/v1/movies/")
    Call<ResponseBody> uploadFileWithPartMap(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);

    @GET("/api/v1/movies/")
    Call<ResponeApi> getFilm();
}