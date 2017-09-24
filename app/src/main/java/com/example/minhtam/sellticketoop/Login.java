package com.example.minhtam.sellticketoop;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtInfo = (TextView) findViewById(R.id.txt);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String password = edtPassword.getText().toString();
                        String email = edtEmail.getText().toString();
                        Toast.makeText(Login.this, "Đang xử lý", Toast.LENGTH_SHORT).show();
                        new requestPostURL().execute("https://tickett.herokuapp.com/api/v1/customers/sign_in", email, password);
                    }
                });
            }
        });
    }

    private class requestPostURL extends AsyncTask<String,Integer,String>{
        //API web dang nhap
        OkHttpClient okHttpClient = new OkHttpClient();
        @Override
        protected String doInBackground(String... params) {
            RequestBody requestBody = new MultipartBody.Builder()       // gan header
                    .addFormDataPart("password",params[2])              //cac bien json de gui du lieu len
                    .addFormDataPart("email",params[1])
                    .setType(MultipartBody.FORM)
                    .build();
            Request request = new Request.Builder()                     //request len web
                    .url(params[0])
                    .post(requestBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string(); //chuoi tra lai s o ham onPostExecute
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
                    String token = body.getString("token");
                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, LocationActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                } else Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
