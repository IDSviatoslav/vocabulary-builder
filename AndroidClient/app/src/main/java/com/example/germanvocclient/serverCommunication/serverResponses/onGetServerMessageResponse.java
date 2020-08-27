package com.example.germanvocclient.serverCommunication.serverResponses;

import com.google.gson.JsonObject;

public interface onGetServerMessageResponse {
    void onMessageResponse(JsonObject answer);
}
