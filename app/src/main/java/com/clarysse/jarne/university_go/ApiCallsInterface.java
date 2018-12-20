package com.clarysse.jarne.university_go;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


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
    Call<JsonElement> getAllMoves();

    @GET("event")
    Call<JsonElement> getAllEvents();

    @GET("ownunimon/{userid}")
    Call<List<Unimon>> getOwnUnimons(@Path("userid") String userid);

    @POST("updateunimon")
    Call<String> updateunimon(@Body JsonArray unimons);

    @GET("getupdatemove/{latestupdate}")
    Call<JsonElement> getLatestMoves(@Path("latestupdate") String latestupdate);

    @GET("getupdateevent/{latestupdate}")
    Call<JsonElement> getLatestEvents(@Path("latestupdate") String latestupdate);



    //@POST("/login")

    //@POST("/google_login")

    @POST("user")
    Call<String> register(@Body JSONObject userInfo);

    @POST("login")
    Call<String> login(@Body JSONObject userInfo);

    @POST("getsalt")
    Call<String> getsalt(@Body JSONObject email);
}
