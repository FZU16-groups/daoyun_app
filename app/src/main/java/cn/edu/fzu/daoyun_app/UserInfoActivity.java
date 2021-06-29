package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AlertDialog;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import okhttp3.*;

public class UserInfoActivity extends AppCompatActivity {

    protected TextView userNameTV;
    protected TextView userPhoneTV;
    protected Button backButton;
    protected EditText phoneET;
    protected EditText idET;
    protected EditText userNameET;
    protected EditText emailET;
    protected TextView createtimeTV;
    protected TextView idTV;
    protected Button saveBtn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Log.i("UserInfoInfo", "start");


        userNameTV = findViewById(R.id.user_name_Tv);
        userPhoneTV = findViewById(R.id.user_phone_Tv);

        saveBtn = findViewById(R.id.save_pas_btn);
        phoneET = findViewById(R.id.usersphone_Et);
        idTV = findViewById(R.id.userid_tv);
        userNameET = findViewById(R.id.name_Et);
        emailET = findViewById(R.id.info_email_et);
        createtimeTV=findViewById(R.id.create_time_tv);

        userPhoneTV.setText(MainActivity.phoneNumber);
        phoneET.setText(MainActivity.phoneNumber);
        userNameET.setText(MainActivity.userName);
        userNameTV.setText(MainActivity.userName);
        emailET.setText(MainActivity.email);
        createtimeTV.setText(MainActivity.createtime);
        idTV.setText(MainActivity.peNumber);


//        userNameTV.setText(MainActivity.userName);
//        if(MainActivity.userName.equals("admin")){
//            userPhoneTV.setText("15900000001");
//        }else if(MainActivity.userName.equals("teacher")){
//            userPhoneTV.setText("15900000002");
//        }else if(MainActivity.userName.equals("student1")){
//            userPhoneTV.setText("15900000003");
//        }else  if(MainActivity.userName.equals("student2")){
//            userPhoneTV.setText("15900000004");
//        }else{
//            userPhoneTV.setText(MainActivity.userName);
//        }
//        initUi();



        emailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    boolean flag = false;
                    try {
                        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                        Pattern regex = Pattern.compile(check);
                        Matcher matcher = regex.matcher(emailET.getText().toString());
                        flag = matcher.matches();
                    } catch (Exception e) {
                        flag = false;
                    }
                    if(!flag){
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this)
                                .setMessage("邮箱名不合法，请重新输入！")
                                .setPositiveButton("确定", null);
                        builder.show();
                        emailET.setText(null);
                    }
                }
            }
        });



        backButton = findViewById(R.id.toolbar_left_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        nameStr = nameET.getText().toString();
//                        idNum = idET.getText().toString();
//                        Log.i("returnLoginInfo",MainActivity.userName + " " + nameStr + ""
//                        + idNum + " " + selectedYear + " " + sex + " " + schoolStr + " " + role);
                        if(userPhoneTV.getText().toString().equals("")){
                            showAlertDialog("请输入手机号！");
                        }else if(userNameET.getText().toString().equals("")){
                            showAlertDialog("请输入用户名！");
                        }else if(emailET.getText().toString().equals("")){
                            showAlertDialog("请输入邮箱");
                        } else{
                            OkHttpClient okHttpClient = new OkHttpClient();


                            com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                            json.put("uName", MainActivity.userName);
                            json.put("phone", MainActivity.phoneNumber);
                            json.put("email", MainActivity.phoneNumber);
                            OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.UPDATE_USER), json, new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.i("错误的返回", e.getMessage());
                                }
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String responseBodyStr = response.body().string();
                                    Log.i("returnLoginInfo", responseBodyStr);
//                                    if(responseBodyStr.equals("save_success")){
//                                        Log.i("returnLoginInfo", "responseBodyStr");
//                                        showAlertDialog("您的个人信息保存成功！");
//                                        if(cropFile != null){
//                                            //MeFragment.iconFile = new File(cropFile.getAbsolutePath());
//                                        }
//                                        if(!nameStr.equals("")){
//                                            MainActivity.name = nameStr;
//                                        }
//                                    }else if(responseBodyStr.equals("userName_fail")){
//                                        showAlertDialog("用户名设置成功后不可更改！");
//                                      //  MeFragment.iconFile = cropFile;
//                                    }else if(responseBodyStr.equals("userName_exist")){
//                                        showAlertDialog("用户名已存在，请更换其他用户名！");
//                                    }else if(responseBodyStr.equals("email_exist")){
//                                        showAlertDialog("该邮箱已绑定其他用户，请更换您的其他邮箱！");
//                                    }else if(responseBodyStr.equals("IDNumber_exist")){
//                                        showAlertDialog("该学号/工号已存在，请重新输入！");
//                                    }
                                }
                            });
                        }

                    }
                }).start();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }




    private void showAlertDialog(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if((msg.equals("您的个人信息保存成功!") || msg.equals("用户名设置成功后不可更改！")) && !userNameET.getText().toString().equals(null)){
                    userNameET.setEnabled(false);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this)
                        .setMessage(msg)
                        .setPositiveButton("确定", null);
                builder.show();
            }
        });
    }

    private String catString(String str, String str1, String str2){
        str = str + "&" + str1 + "=" + str2;
        return str;
    }

}
