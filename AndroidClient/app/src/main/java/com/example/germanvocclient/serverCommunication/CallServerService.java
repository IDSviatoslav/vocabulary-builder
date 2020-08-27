package com.example.germanvocclient.serverCommunication;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.germanvocclient.QuizActivity;
import com.example.germanvocclient.models.User;
import com.example.germanvocclient.models.Word;
import com.example.germanvocclient.serverCommunication.serverResponses.onGetServerMessageResponse;
import com.example.germanvocclient.serverCommunication.serverResponses.onGetWordsServerResponse;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallServerService {
    private static CallServerService callServerService;

    private CallServerService(){}

    public static CallServerService getInstance(){
        if (callServerService == null){
            callServerService = new CallServerService();
        }
        return callServerService;
    }

    public void callServerCreateUser(String name,String pass,final View view){
        Retrofit retrofit = RetrofitInstance.getInstance();
        final interfaceAPI api = retrofit.create(interfaceAPI.class);
        JsonObject jsonlogin = new JsonObject();
        jsonlogin.addProperty("name", name);
        jsonlogin.addProperty("password", pass);
        Call<User> call = api.createAccount(jsonlogin);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User>  call, Response<User> response) {
                if (response.isSuccessful()) {
                    String serverMessage = response.body().getAccountInfo();
                    if (response.body() != null) {
                        if (serverMessage.equals("user created")) {
                            Log.i("LogI", "OK posted");
                            Log.i("LogI", response.body().toString());
                            //Log.i("LogI", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                        }
                    } else {
                        Log.i("LogI", "NO failed");
                        Log.i("LogI", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                    }
                    Toast.makeText(view.getContext(), serverMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("LogI : failed ", t.toString());
            }
        });
    }

   public void callServerLogin(String name, String pass, final View view){
        Retrofit retrofit = RetrofitInstance.getInstance();
        final interfaceAPI api = retrofit.create(interfaceAPI.class);
        JsonObject jsonlogin = new JsonObject();
        jsonlogin.addProperty("name", name);
        jsonlogin.addProperty("password", pass);
        Call<User> call = api.login(jsonlogin);
        call.enqueue(new Callback<User> () {
            @Override
            public void onResponse(Call<User>  call, Response<User>  response) {
                if(response.isSuccessful()){
                    if (response.body()!=null) {
                        Log.i("LogI", "OK posted");
                        Log.i("LogI", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                        Log.i("LogI", "handle = "+ response.body().getHandle());
                        if (response.body().getHandle()!=0 && response.body().getAccountInfo() == null){
                            Intent intent = new Intent(view.getContext(), QuizActivity.class);
                            intent.putExtra("userHandle", response.body().getHandle());
                            view.getContext().startActivity(intent);
                        }
                        else Toast.makeText(view.getContext(), response.body().getAccountInfo(), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Log.i("LogI","NO failed");
                    Log.i("LogI", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("LogI : failed ", t.toString());
            }
        });
    }

    public void callServerGetQuiz(int wordNum, int handle, final onGetWordsServerResponse onServerResponse){
        Retrofit retrofit = RetrofitInstance.getInstance();
        final interfaceAPI api = retrofit.create(interfaceAPI.class);
        Call<List<Word>> call = api.getQuizWords(handle, wordNum);
        call.enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>>  call, Response<List<Word>> response) {
                if(response.isSuccessful()){
                    if (response.body()!=null) {
                        Log.i("LogI", "OK posted");
                        Log.i("quizresponsebody", response.body().toString());
                        onServerResponse.onWordsResponse(response.body());
                    }
                }
                else{
                    Log.i("LogI","NO failed");
                    Log.i("LogI", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                Log.i("LogI : failed ", t.toString());
            }
        });
    }

    public void callServerSendAnswer(String answer, int handle,final onGetServerMessageResponse onServerResponse){
        Retrofit retrofit = RetrofitInstance.getInstance();
        final interfaceAPI api = retrofit.create(interfaceAPI.class);
        Call<JsonObject> call = api.checkAnswer(handle, answer);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject>  call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    if (response.body()!=null) {
                        Log.i("LogI", "OK posted");
                        Log.i("LogI", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                        onServerResponse.onMessageResponse(response.body());
                    }
                }
                else{
                    Log.i("LogI","NO failed");
                    Log.i("LogI", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("LogI : failed ", t.toString());
            }
        });
    }
}
