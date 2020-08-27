package com.example.germanvocclient.serverCommunication;

import com.example.germanvocclient.models.User;
import com.example.germanvocclient.models.Word;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface interfaceAPI {
    @POST("/create_account")
    Call<User> createAccount(@Body JsonObject login);

    @POST("/login")
    Call<User> login(@Body JsonObject login);

    @GET("/show_all")
    Call<List<User>> getAll();

    @POST("/get_quiz")
    Call<List<Word>> getQuizWords(@Query("handle") Integer handle, @Query("wordNum") Integer wordNum);

    @POST("/check_answer")
    Call<JsonObject> checkAnswer(@Query("handle") Integer handle, @Query("userAnswer") String userAnswer);

}
