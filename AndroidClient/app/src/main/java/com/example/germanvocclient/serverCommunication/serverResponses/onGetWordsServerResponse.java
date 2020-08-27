package com.example.germanvocclient.serverCommunication.serverResponses;

import com.example.germanvocclient.models.Word;

import java.util.List;

public interface onGetWordsServerResponse {
    void onWordsResponse(List<Word> quiz);
}

