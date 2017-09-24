package com.example.minhtam.sellticketoop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ThanhDat on 9/24/2017.
 */

public class LocationActivity extends AppCompatActivity {
    TextView txtList;
    String token; // token send in request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");

        txtList = (TextView) findViewById(R.id.list);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new requestGETLocations().execute("https://tickett.herokuapp.com/api/v1/customers/locations");
            }
        });
    }

    private class requestGETLocations extends AsyncTask<String,Integer,String> {
        //API get locations
        OkHttpClient okHttpClient = new OkHttpClient();
        @Override
        protected String doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(params[0])
                    .header("Authorization", token)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject body = new JSONObject(s);
                int code = body.getInt("code");
                if (code == 1) {
                    txtList.setText(s);
                    Toast.makeText(LocationActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(LocationActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
