package com.example.nguyen.cinema.Data.Remote;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://nam-cinema.herokuapp.com/";

    public static APIService getAPIService(String token) {

        return RetrofitClient.getClient(BASE_URL,token).create(APIService.class);
    }
}
