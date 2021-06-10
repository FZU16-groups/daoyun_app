package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    protected EditText userNameET;
    protected EditText verificationET;
    protected Button sendCodeBtn;
    protected EditText phoneET;
    protected EditText emailET;
    protected Button registerBtn;
    private int verificationCode;
    boolean a = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);
       // userNameET = findViewById(R.id.et_reg_username);
        verificationET = findViewById(R.id.et_reg_vericode);
        sendCodeBtn = findViewById(R.id.bt_veri_submit);
        phoneET = findViewById(R.id.et_reg_phone);
        //emailET = findViewById(R.id.et_reg_email);
        registerBtn = findViewById(R.id.bt_login_submit);
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toast.makeText(RegisterActivity.this, "稍后重试", Toast.LENGTH_SHORT).show();
                }
                sendCodeWithHttpURLConnection();

            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if(phoneET.getText().toString().equals("")) {
                     AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                             .setMessage("请输入手机号!")
                             .setPositiveButton("确定", null);
                     builder.show();

                 }else {

                     sendCodeAndPnone(verificationET.getText().toString());
                 }
            }
        });
    }

    private void sendCodeWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
                String requestBody = phoneET.getText().toString();
                Request request = new Request.Builder()
                        .url(" http://47.98.236.0:8080/daoyun_service/fastRegisterSendMessage.do")
                        .post(RequestBody.create(mediaType, requestBody))
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                                    Toast.makeText(RegisterActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);
                        Log.i("RegisterInfo", responseBodyStr);
                        Log.i("shoudao", messjsonObject.get("message").toString());
//                        if (messjsonObject.get("message").toString().equals("Ok"))
//                        {
//                            sendCodeBtn.setText("已发送");
//                            sendCodeBtn.setEnabled(false);
//                        }
                        //JSON字符串转换成JSON对象
//                        JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);
//                        System.out.println( messjsonObject.getString("message"));
                       // sendCodeAndPnone(messjsonObject.getJSONObject("data").getString("captcha"));
                    }
                });
            }
        }).start();
    }
    private void sendCodeAndPnone(String captcha)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                json.put("loginToken",phoneET.getText().toString() );
                json.put("captcha",captcha);
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .url("http://47.98.236.0:8080/daoyun_service/registerFastUser.do")
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Toast.makeText(RegisterActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = Objects.requireNonNull(response.body()).string();
                        JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);
                        Log.i("注册返回信息", responseBodyStr);
                        if (messjsonObject.get("message").toString().equals("Ok"))
                        {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else {
                            showAlertDialog("验证码错误");
                        }
                    }
                });
            }
        }).start();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    protected void showAlertDialog(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                        .setMessage(msg);
                if(msg.equals("注册成功！")){
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                    });
                }else{
                    builder.setPositiveButton("确定", null);
                }
                builder.show();
            }
        });
    }

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}