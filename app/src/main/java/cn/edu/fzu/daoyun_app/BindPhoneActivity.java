package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BindPhoneActivity extends AppCompatActivity {
    protected EditText phoneET;
    protected EditText verificationET;
    protected Button sendCodeBtn;
    protected Button bindBtn;
    private String openID;
    boolean a = true;
    private static final Pattern CHINA_PATTERN = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);


        Intent intent = getIntent();
        openID = intent.getStringExtra("openID");
       Log.i("传过来的openID", openID);
        verificationET = findViewById(R.id.et_bind_vericode);
        sendCodeBtn = findViewById(R.id.bt_bind_sendcode);
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isChinaPhoneLegal(phoneET.getText().toString()))
                {

                   // Toast.makeText(getApplicationContext(), "手机号绑定成功", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                else if (a)
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
                    sendCodeWithHttpURLConnection();
                }else {
                    Toast.makeText(BindPhoneActivity.this, "稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
        phoneET = findViewById(R.id.et_bind_phone);
        bindBtn=findViewById(R.id.bt_bind_submit);
        bindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(!verificationET.getText().toString().equals(verificationCode+"")){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
//                            .setMessage("验证码错误！")
//                            .setPositiveButton("确定", null);
//                    builder.show();
//                    sendCodeBtn.setText("发送验证码");
//                    sendCodeBtn.setEnabled(true);
//                 if(userNameET.getText().toString().equals("")){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
//                            .setMessage("请输入用户名！")
//                            .setPositiveButton("确定", null);
//                    builder.show();
                if(phoneET.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BindPhoneActivity.this)
                            .setMessage("请输入手机号!")
                            .setPositiveButton("确定", null);
                    builder.show();
                }else {
                    sendCodeAndopenId(verificationET.getText().toString());
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
                        .url("http://47.98.151.20:8080/daoyun_service/sendTheAuthMessage.do")
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

    private void sendCodeAndopenId(String captcha)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("openId", openID);
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                json.put("openId",openID);
                json.put("phone",phoneET.getText().toString() );
                json.put("captcha",captcha);
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .url("http://47.98.151.20:8080/daoyun_service/bindPhone.do")
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                       // Toast.makeText(RegisterActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
                        Log.i("三方登陆返回信息错误", e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = Objects.requireNonNull(response.body()).string();
                        JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);
                        Log.i("三方登陆返回信息", responseBodyStr);
                        if (messjsonObject.get("message").toString().equals("Ok"))
                        {
                            Intent intent = new Intent(BindPhoneActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "手机号绑定成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }else {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
            }
        }).start();


    }
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        Matcher m = CHINA_PATTERN.matcher(str);
        return m.matches();
    }
}