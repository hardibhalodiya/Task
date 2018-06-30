package com.example.h.wissiontask;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h.wissiontask.Adapters.FeedAdapter;
import com.example.h.wissiontask.Database.SqlController;
import com.example.h.wissiontask.Models.Feed;
import com.example.h.wissiontask.Networking.ResponseCallback;
import com.example.h.wissiontask.Networking.RetrofitController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class FeedData extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private TextView mTextMessage;
    private String TAG = FeedData.class.getName();
    private int totalVideoCount = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    populateRecyclerView();
                    return true;
                case R.id.navigation_profile:
                    Intent i = new Intent(FeedData.this, Profile_Page.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_data);
        setUpRecyclerView();
        populateRecyclerView();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setUpRecyclerView() {
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void populateRecyclerView() {

        showProgressDialog();
        if (new SqlController(FeedData.this).noOfRows("feeds") > 0) {
            getDataFromDBAndShowInRecyclerView();
        } else {
            generateDummyVideoList(new ResponseCallback() {
                @Override
                public void data(JSONObject data, String status) {

                    if (status.equalsIgnoreCase("success")) {

                        Log.i(TAG, "successfully entered...");
                        getDataFromDBAndShowInRecyclerView();

                    } else {
                        Toast.makeText(FeedData.this, "Check Your Internet Connection...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    private void generateDummyVideoList(final ResponseCallback responseCallback) {

        final RetrofitController retrofitController = new RetrofitController();
        retrofitController.getVideos(new ResponseCallback() {
            @Override
            public void data(JSONObject jsonObject, String status) {
                try {
                    if (status.equalsIgnoreCase("success")) {
                        if (jsonObject.has("nextPageToken"))
                            MyApplication.getInstance().setNextPageToken(jsonObject.getString("nextPageToken"));

                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        totalVideoCount = jsonArray.length();
                        final int[] count = {0};
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("snippet");

                            ContentValues c = new ContentValues();
                            c.put("title", jsonObject1.getString("title"));
                            c.put("description", jsonObject1.getString("description"));
                            if (jsonObject1.has("thumbnails"))
                                c.put("thumbnail", jsonObject1.getJSONObject("thumbnails").getJSONObject("maxres").getString("url"));
                            c.put("channelTitle", jsonObject1.getString("channelTitle"));
                            c.put("videoId", jsonObject1.getJSONObject("resourceId").getString("videoId"));
                            new SqlController(FeedData.this).insert(c, "feeds");

                            retrofitController.getVideoCounts(jsonObject1.getJSONObject("resourceId").getString("videoId"), new ResponseCallback() {
                                @Override
                                public void data(JSONObject jsonObject2, String status) {
                                    if (status.equalsIgnoreCase("success")) {
                                        try {
                                            JSONArray jsonArray2 = jsonObject2.getJSONArray("items");
                                            for (int j = 0; j < jsonArray2.length(); j++) {
                                                String videoId1 = jsonArray2.getJSONObject(j).getString("id");
                                                JSONObject jsonObject12 = jsonArray2.getJSONObject(j).getJSONObject("statistics");
                                                ContentValues values = new ContentValues();
                                                values.put("videoId", videoId1);
                                                values.put("viewCount", jsonObject12.getString("viewCount"));
                                                values.put("likeCount", jsonObject12.getString("likeCount"));
                                                values.put("dislikeCount", jsonObject12.getString("dislikeCount"));
                                                values.put("favoriteCount", jsonObject12.getString("favoriteCount"));
                                                new SqlController(FeedData.this).insert(values, "videolikecounts");
                                            }
                                            count[0]++;
                                            if (count[0] == totalVideoCount) {
                                                responseCallback.data(new JSONObject().put("success", "done"), "success");
                                            }

                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(FeedData.this, "222..." + jsonObject2, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(FeedData.this, "Unable To Connect Server..." , Toast.LENGTH_SHORT).show();
                        responseCallback.data(new JSONObject().put("fail", jsonObject.getString("error")), "fail");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getDataFromDBAndShowInRecyclerView() {
        Cursor cursor = new SqlController(FeedData.this).getAll("feeds");
        List<Feed> feedList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Feed feed = new Feed();
            feed.setId(cursor.getString(0));
            feed.setTitle(cursor.getString(1));
            feed.setDescription(cursor.getString(2));
            feed.setThumbnail(cursor.getString(3));
            feed.setChannelTitle(cursor.getString(4));
            feed.setVideoId(cursor.getString(5));

            Cursor cursor2 = new SqlController(FeedData.this).get("videolikecounts", "videoId", cursor.getString(5));
            while (cursor2.moveToNext()) {
                feed.setViewCount(cursor2.getString(2));
                feed.setLikeCount(cursor2.getString(3));
                feed.setDislikeCount(cursor2.getString(4));
                feed.setFavoriteCount(cursor2.getString(5));
            }

            feedList.add(feed);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FeedData.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        FeedAdapter feedAdapter = new FeedAdapter(FeedData.this, feedList);
        recyclerView.setAdapter(feedAdapter);
        feedAdapter.notifyDataSetChanged();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onRefresh() {
        showProgressDialog();
        swipeRefreshLayout.setRefreshing(false);
        generateDummyVideoList(new ResponseCallback() {
            @Override
            public void data(JSONObject data, String status) {

                if (status.equalsIgnoreCase("success")) {

                    Log.i(TAG, "successfully entered...");
                    getDataFromDBAndShowInRecyclerView();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                } else {
                    Toast.makeText(FeedData.this, "Check Your Internet Connection..." , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }
}
