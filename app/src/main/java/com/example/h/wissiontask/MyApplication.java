package com.example.h.wissiontask;

import android.app.Application;

/**
 * Created by hardi on 30/06/18.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    private String nextPageToken;
    private String DEVELOPER_KEY = "AIzaSyCQNxPqv9OrvilI4hrw_Si-INwFLgr9VEg";

    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getDeveloperKey() {
        return DEVELOPER_KEY;
    }
}
