package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VerLoginActivity extends AppCompatActivity {

    protected EditText phoneET;
    protected EditText verificationET;
    protected Button sendCodeBtn;
    protected Button loginBtn;
    boolean a = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_login);


        phoneET = findViewById(R.id.et_verlogin_username);
        verificationET = findViewById(R.id.et_verlogin_vericode);
        sendCodeBtn = findViewById(R.id.bt_verlogin_submitcode);
        loginBtn = findViewById(R.id.bt_verlogin_submit);
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=phoneET.getText().toString();
                if (phone.equals(""))
                {
                    showAlertDialog("请输入手机号!");
                }else {
                    if (a)
                    {
                        ValueAnimator animator = ValueAnimator.ofInt(60,0);
                        //设置时间
                        animator.setDuration(60000);
                        //均匀显示
                        animator.setInterpolator(new LinearInterpolator());
                        //监听
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (Integer) animation.getAnimatedValue();
                                sendCodeBtn.setText(value+"s重新发送");
                                sendCodeBtn.setTextColor(Color.GRAY);
                                if(value==0){
                                    sendCodeBtn.setText("重新发送");
                                    sendCodeBtn.setTextColor(Color.BLUE);
                                    a = true;
                                }
                            }
                        });
                        //开启线程
                        animator.start();
                        a=false;
                    }else {
                        Toast.makeText(VerLoginActivity.this, "稍后重试", Toast.LENGTH_SHORT).show();
                    }
                  sendCodeWithHttpURLConnection();}


            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=phoneET.getText().toString();
                String code=verificationET.getText().toString();
                if (phone.equals(""))
                {
                    showAlertDialog("请输入手机号!");
                }else if(code.equals(""))
                {
                    showAlertDialog("请输入验证码");
                }
                else {
                    sendCodeAndPnone();
                }

            }
        });
    }
//发送验证码

    private void sendCodeWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
                String requestBody = phoneET.getText().toString();
                Request request = new Request.Builder()
                        .url("http://47.98.236.0:8080/daoyun_service/sendMessage.do")
                        .post(RequestBody.create(mediaType, requestBody))
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("RegisterInfoerror", e.getMessage());
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("RegisterInfo", responseBodyStr);
                        JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);

                    }
                });
            }
        }).start();
    }

    private void sendCodeAndPnone()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String phone=phoneET.getText().toString();
                String captcha=verificationET.getText().toString();
                Log.i("captcha", captcha);
                Log.i("phone", phone);
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                json.put("loginToken",phone );
                json.put("captcha",captcha);
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .url("http://47.98.236.0:8080/daoyun_service/loginByMessage.do")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Toast.makeText(VerLoginActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("验证码登陆", responseBodyStr);
                        JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);
//                        if (messjsonObject.get("message").toString().equals("Ok"))
//                        {
//                            Intent intent = new Intent(VerLoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                        }
                        if (messjsonObject.get("message").toString().equals("Ok")) {

                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(responseBodyStr);
                                Log.i("LoginInfoInfo", jsonObject.toString());
//                                MainActivity.loginType = jsonObject.getString("loginType");
                                //MainActivity.icon = jsonObject.getString("icon");
//                                MainActivity.name = jsonObject.getString("uName");
//                                MainActivity.phoneNumber = jsonObject.getString("phone");
                                MainActivity.peid =jsonObject.getJSONObject("data").getJSONObject("person").getString("peId");
                                //Log.i("LoginInfoInfo", MainActivity.phoneNumber);
                                Log.i("peIdInfoInfo", MainActivity.peid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(VerLoginActivity.this, MainActivity.class);
                            intent.putExtra("username", "username");
                            startActivity(intent);

                        }else
                            {
                                showAlertDialog("验证码错误");
                            }
                    }
                });
            }
        }).start();
    }

    protected void showAlertDialog(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerLoginActivity.this)
                        .setMessage(msg)
                        .setPositiveButton("确定", null);
                builder.show();
            }
        });
    }
}