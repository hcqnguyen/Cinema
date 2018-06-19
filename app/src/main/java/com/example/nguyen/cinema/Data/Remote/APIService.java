package com.example.nguyen.cinema.Data.Remote;

import com.example.nguyen.cinema.Data.Model.Film;
import com.example.nguyen.cinema.Data.Model.Login;
import com.example.nguyen.cinema.Data.Model.ResponeApi;

import java.util.ArrayList;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.PUT;
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

    @GET("/api/v1/users/:id/movies/")
    Call<ResponeApi> getUserListFilm();

    @GET("/api/v1/movies/")
    Call<ResponeApi> getFilm();

    @FormUrlEncoded
    @POST("/api/v1/auth/sign-up/")
    Call<ResponseBody> signUpAccount(
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password
            );

    @FormUrlEncoded
    @POST("/api/v1/auth/sign-in/")
    Call<Login> signIn(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/api/v1/auth/change-password/")
    Call<ResponseBody> changePassword(@Header("x-access-token") String access_token,
            @Field("oldPassword") String oldPassword,
            @Field("password") String newPassword
    );

    @GET("/api/v1/users/")
    Call<Login> getProfile(@Header("x-access-token") String access_token);

    @GET("/api/v1/users/:id/movies")
    Call<ResponeApi> getUsermovie(@Header("x-access-token") String access_token);

    @FormUrlEncoded
    @PUT("/api/v1/users/")
    Call<Login> changePhonenumber(
            @Header("x-access-token") String access_token,
                            @Field("phone") String phone

    );

    @FormUrlEncoded
    @PUT("/api/v1/users/")
    Call<Login> changeUsername(
            @Header("x-access-token") String access_token,
                                  @Field("username") String username

    );
    @Multipart
    @PUT("/api/v1/users/")
    Call<RequestBody> changeAvatar(
            @Header("x-access-token") String access_token,
            @Part MultipartBody.Part file

    );

    @FormUrlEncoded
    @POST("/api/v1/auth/request-reset-password")
    Call<ResponseBody> resetPassword(
                                      @Field("email") String newPassword
    );
}