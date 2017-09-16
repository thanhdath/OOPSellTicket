package com.example.minhtam.sellticketoop;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.JarURLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.prefs.Preferences;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    EditText edtEmail,edtPassword;
    Button btnLogin,btnGetHtml;
    TextView txt;
    String cookiePub;               // luu tru gia tri cua cookie tai phien lam viec
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGetHtml = (Button) findViewById(R.id.getHtml);
        txt = (TextView) findViewById(R.id.txt);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String password = edtPassword.getText().toString();
                        String email = edtEmail.getText().toString();
                        Toast.makeText(Login.this,"dang chay",Toast.LENGTH_LONG).show();
                        //API web dang nhap
                        new requestPostURL().execute("https://tickett.herokuapp.com/api/v1/customers/sign_in",email,password);
                    }
                });
            }
        });
        btnGetHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new requestGetURL().execute("https://tickett.herokuapp.com/",cookiePub);
                    }
                });

            }
        });
    }
    private class requestPostURL extends AsyncTask<String,Integer,String>{
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
                String cookieRes = response.headers("Set-Cookie").toString();       //lay cookie
                String cookie = "";
                if(cookieRes.length()>5){
                    cookie = cookieRes.substring(1,cookieRes.length()-19);
                }

                String body = response.body().string();
                String requestCode = body.substring(8,9);

//                return response.body().string();
                return requestCode.concat(cookie);//chuoi tra lai s o ham onPostExecute
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            txt.setText(s);
            String cookie;
            cookie = s.substring(1);
            String requestCode = s.substring(0,1);
            cookiePub = cookie;
            if(requestCode.compareTo("0")==0){
                Toast.makeText(Login.this,"Đăng nhập không thành công",Toast.LENGTH_LONG).show();
            }
            else if (requestCode.compareTo("1")==0) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("cookie",cookie);               //chuyen cookie sang man hinh chinh
                startActivity(intent);                          //chuyen sang man hinh chinh
            }

        }
    }
    private class requestGetURL extends AsyncTask<String,Integer,String>{
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        @Override
        protected String doInBackground(String... params) {
            Request request;
            if(params[1].length()<5) {          //neu khong co cookie thi thoi
                request = new Request.Builder()
                        .url(params[0])
                        .build();
            }
            else                                // neu co cookie thi gui cookie len
            {
                request = new Request.Builder()
                        .url(params[0])
                        .addHeader("Cookie", params[1])
                        .build();
            }
            try {
                Response response = okHttpClient.newCall(request).execute();

                String cookieRes = response.headers("Set-Cookie").toString();       //cap nhat cookie moi
                String cookie = "";
                if(cookieRes.length()>5){
                    cookie = cookieRes.substring(1,cookieRes.length()-19);
                }

//                return response.body().string();
                return cookie;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            txt.setText(s);
            cookiePub = s;
            Toast.makeText(Login.this,s,Toast.LENGTH_LONG).show();
        }
    }
}
