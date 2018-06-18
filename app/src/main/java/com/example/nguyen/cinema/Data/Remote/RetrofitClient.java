package com.example.nguyen.cinema.Data.Remote;
import android.content.SharedPreferences;

import com.example.nguyen.cinema.Activity.SignInActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl,final String token) {


        if (retrofit==null) {
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request original = chain.request();
//
//                    Request request = original.newBuilder()
//                            .header("x-access-token",
//                                    token)
//                            .header("Content-Type", "application/json")
//                            .method(original.method(), original.body())
//                            .build();
//
//                    return chain.proceed(request);
//                }
//            });
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
//                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}