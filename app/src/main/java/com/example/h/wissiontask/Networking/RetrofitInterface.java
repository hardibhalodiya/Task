package com.example.h.wissiontask.Networking;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitInterface {

    String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    @GET("playlistItems")
    Call<Object> getFeeds(@QueryMap LinkedHashMap<String, String> options);

    @GET("videos")
    Call<Object> getVideoCounts(@QueryMap LinkedHashMap<String, String> options);

}
