package com.example.minhtam.sellticketoop;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    Button login,singIn;
    TextView txt;
    String cookiePub = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.login);
        singIn = (Button) findViewById(R.id.singIn);
        txt = (TextView) findViewById(R.id.txt);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);

            }
        });

        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lay cookie tri-uyen tu man hinh login
                Bundle extras = getIntent().getExtras();
                cookiePub = extras.getString("cookie");
                txt.setText(cookiePub);
            }
        });
    }
}
