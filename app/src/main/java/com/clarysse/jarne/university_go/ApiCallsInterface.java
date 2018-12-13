package com.clarysse.jarne.university_go;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiCallsInterface {
    @GET("move")
    Call<List<Move>> getAllMoves();

    @GET("event")
    Call<List<Event>> getAllEvents();

    @GET("ownunimon/{userid}")
    Call<List<Unimon>> getOwnUnimons(@Path("userid") String userid);

    @POST("updateunimon")
    Call<String> updateunimon(@Body String unimons);

    //@POST("/login")

    //@POST("/google_login")

    @POST("user")
    Call<String> register(@Body JSONObject userInfo);

    @POST("login")
    Call<String> login(@Body JSONObject userInfo);

    @POST("getsalt")
    Call<String> getsalt(@Body JSONObject email);
}
