package com.example.germanvocclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.germanvocclient.serverCommunication.CallServerService;

public class MainActivity extends AppCompatActivity {
    private CallServerService callServerService;
    private Button loginButton;
    private EditText userNameEditText;
    private EditText userPasswordEditText;
    private TextView createUserText;

    public String name;
    public String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callServerService = CallServerService.getInstance();
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.loginButton);
        userNameEditText = findViewById(R.id.editTextLogin);
        userPasswordEditText = findViewById(R.id.editTextPass);
        createUserText = findViewById(R.id.createUserText);

        createUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userNameEditText.getText().toString();
                pass = userPasswordEditText.getText().toString();
                Log.i("INFO", "create: got string values, sending...");
                callServerService.callServerCreateUser(name, pass, v);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userNameEditText.getText().toString();
                pass = userPasswordEditText.getText().toString();
                Log.i("INFO", "login: got string values, sending...");
                callServerService.callServerLogin(name, pass, v);
            }
        });
    }
}
