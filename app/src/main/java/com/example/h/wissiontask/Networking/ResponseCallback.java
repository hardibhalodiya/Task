package com.example.h.wissiontask.Networking;

import org.json.JSONObject;

/**
 *  Created by hardi on 30/06/18.
 */

public interface ResponseCallback {

    public void data(JSONObject data, String status);

}
