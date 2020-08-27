package com.example.germanvocclient.serverCommunication;

import com.example.germanvocclient.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static String BASE_URL = "http://192.168.1.192:8080/";
    private static Retrofit retrofit;
    private static Gson gson;
    private static OkHttpClient okHttpClient;

    public static Retrofit getInstance(){
        if (retrofit==null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            okHttpClient = new OkHttpClient().newBuilder().addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
            gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }
}
