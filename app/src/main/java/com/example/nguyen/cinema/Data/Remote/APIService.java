package com.example.nguyen.cinema.Data.Remote;

import com.example.nguyen.cinema.Data.Model.Film;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;

public interface APIService {

//    @FormUrlEncoded
//    @POST("/api/v1/movies/")
//    Call<Film> send();

    @Multipart
    @POST("/api/v1/movies/")
    Call<ResponseBody> uploafFileWithPartMap(
            @PartMap
    )
}