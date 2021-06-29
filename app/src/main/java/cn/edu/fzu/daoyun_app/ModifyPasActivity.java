package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.edu.fzu.daoyun_app.Config.GConfig;
import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ModifyPasActivity extends AppCompatActivity {
    protected Button backButton;
    protected TextView usernameTV;
    protected EditText pasET;
    protected Button modifyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pas);
        usernameTV = findViewById(R.id.pas_username_tv);
        pasET = findViewById(R.id.info_email_et);
        modifyButton = findViewById(R.id.save_pas_btn);
      //  backButton = findViewById(R.id.save_pas_btn);
        backButton = findViewById(R.id.toolbar_left_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        usernameTV.setText("账号： "+MainActivity.userName);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        nameStr = nameET.getText().toString();
//                        idNum = idET.getText().toString();
//                        Log.i("returnLoginInfo",MainActivity.userName + " " + nameStr + ""
//                        + idNum + " " + selectedYear + " " + sex + " " + schoolStr + " " + role);
                        if(pasET.getText().toString().equals("")){
                           // showAlertDialog("请输入密码！");
                        }else{
                            OkHttpClient okHttpClient = new OkHttpClient();


                            com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                            json.put("uId", GConfig.Uid);
                            json.put("passwordToken", pasET.getText().toString());

                            OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.UPDATE_USER), json, new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.i("错误的返回", e.getMessage());
                                }
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String responseBodyStr = response.body().string();
                                    Log.i("returnLoginInfo", responseBodyStr);
                                    finish();

                                }
                            });
                        }

                    }
                }).start();
            }
        });

    }
       // phoneET = findViewById(R.id.usersphone_Et);
    }

