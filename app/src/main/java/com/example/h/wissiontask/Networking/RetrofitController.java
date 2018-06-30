package com.example.h.wissiontask.Networking;

import android.util.Log;

import com.example.h.wissiontask.MyApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class RetrofitController {

    public void getVideos(final ResponseCallback responseCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        Call<Object> call = api.getFeeds(getPlayListMap());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    responseCallback.data(new JSONObject(new ObjectMapper().writeValueAsString(response.body())), "success");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                try {
                    responseCallback.data(new JSONObject().put("error", t.getMessage()), "fail");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public LinkedHashMap getPlayListMap() {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        try {
            data.put("part", "snippet,contentDetails");
            data.put("maxResults", "10");
            data.put("playlistId", "PLaoF-xhnnrRWHtmb8ZGmu8N4Wl2Zr26V7");
            data.put("key", "AIzaSyCQNxPqv9OrvilI4hrw_Si-INwFLgr9VEg");
            Log.i(TAG, "nexToken="+ MyApplication.getInstance().getNextPageToken());
            if (!MyApplication.getInstance().getNextPageToken().equalsIgnoreCase(""))
                data.put("pageToken", MyApplication.getInstance().getNextPageToken());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return data;
    }

    public LinkedHashMap getVideoCountMap(String vid_id) {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        try {
            data.put("id", vid_id);
            data.put("key", "AIzaSyCQNxPqv9OrvilI4hrw_Si-INwFLgr9VEg");
            data.put("part", "snippet,contentDetails,statistics,status");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return data;
    }

    public void getVideoCounts(String vid_id, final ResponseCallback responseCallback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface api = retrofit.create(RetrofitInterface.class);

        Call<Object> call = api.getVideoCounts(getVideoCountMap(vid_id));
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    responseCallback.data(new JSONObject(new ObjectMapper().writeValueAsString(response.body())), "success");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                try {
                    responseCallback.data(new JSONObject().put("error", t.getMessage()), "fail");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
