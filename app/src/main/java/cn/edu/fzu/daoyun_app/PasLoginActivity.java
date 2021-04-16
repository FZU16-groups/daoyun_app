package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PasLoginActivity extends AppCompatActivity {
    protected Button mBtnRegister;
    protected Button mBtnLogin;
    protected EditText userNameET;
    protected EditText passwordET;
    protected CheckBox rememberUserCB;
    protected TextView forgetPasswordTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pas_login);

        ActivityCollector.finishAll();
        ActivityCollector.addActivity(this);

        userNameET = findViewById(R.id.et_paslogin_username);
        passwordET = findViewById(R.id.et_paslogin_pwd);
        rememberUserCB = findViewById(R.id.cb_remember_login);
        forgetPasswordTV = findViewById(R.id.tv_login_forget_pwd);

        SharedPreferences preferences = getSharedPreferences("remember_user", MODE_PRIVATE);
        if(!preferences.getString("userName","").equals("") && !preferences.getString("password","").equals("")){
            userNameET.setText(preferences.getString("userName",""));
            passwordET.setText(preferences.getString("password",""));
        }


        mBtnRegister= this.findViewById(R.id.bt_paslogin_register);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(PasLoginActivity.this,RegisterActivity.class));
//                new XPopup.Builder(PasLoginActivity.this)
//                        .isDarkTheme(true)
//                        .hasShadowBg(true)
////                            .hasBlurBg(true)
////                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
//                                new OnSelectListener() {
//                                    @Override
//                                    public void onSelect(int position, String text) {
//                                        //TODO 这里接入转换接口
//                                        Toast.makeText(PasLoginActivity.this, text, Toast.LENGTH_SHORT).show();
//
//                                        if (position == 1) {
//                                            System.out.println("FUCK");
//                                        }
//                                    }
//                                }).show();
            }
        });



        mBtnLogin = findViewById(R.id.bt_paslogin_submit);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(userNameET.getText().toString().equals("")){
//                    showAlertDialog("请输入账号!");
//                }else if(passwordET.getText().toString().equals("")){
//                    showAlertDialog("请输入密码！");
//                }else{
//                    sendRequestWithHttpURLConnection(userNameET.getText().toString(), passwordET.getText().toString());
//                    //sendRequestWithHttpURLConnection("15900000004", "123456");
//                }


            }
        });
        int checkStorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int checkInternetPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int checkLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int checkReadStorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int checkReadMountPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        int checkCameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int checkPhonePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if(checkStorePermission != PackageManager.PERMISSION_GRANTED
                || checkInternetPermission != PackageManager.PERMISSION_GRANTED
                || checkLocationPermission != PackageManager.PERMISSION_GRANTED
                || checkFineLocationPermission != PackageManager.PERMISSION_GRANTED
                || checkReadStorePermission != PackageManager.PERMISSION_GRANTED
                || checkReadMountPermission != PackageManager.PERMISSION_GRANTED
                || checkCameraPermission != PackageManager.PERMISSION_GRANTED
                || checkPhonePermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE},0);
        }

        Properties properties = PropertiesUtill.getProperties();
        PropertiesUtill.setProperties(PasLoginActivity.this,"gesturePassword", "");

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
                AlertDialog.Builder builder = new AlertDialog.Builder(PasLoginActivity.this)
                        .setMessage(msg)
                        .setPositiveButton("确定", null);
                builder.show();
            }
        });
    }

    private void sendRequestWithHttpURLConnection(final String username, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {

//                HttpURLConnection connection = null;
//                try{
//                    URL url = new URL("http://www.baidu.com");
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("POST");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    connection.connect();
//                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes("userName=15900000003&password=123456");
//                    out.close();
//                    int responseCode = connection.getResponseCode();
//                    if(responseCode == 200){
//                        InputStream inputStream = connection.getInputStream();
//                        data = StreamUtils.inputSteam2String(inputStream);
//                        Log.i("LoginInfo", data);
//                        handler.obtainMessage(RESULT_OK, data).sendToTarget();
//                    }else {
//                        handler.obtainMessage(RESULT_CANCELED, responseCode).sendToTarget();
//                    }
//                }catch (Exception e){
//                    handler.obtainMessage(RESULT_CANCELED, e.getMessage()).sendToTarget();
//                    e.printStackTrace();
//                }finally {
//                    if(connection != null){
//                        connection.disconnect();
//                    }
//                }
                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("userName", username)
                        .add("password", password)
                        .build();
                Request request = new Request.Builder()
//                            .url("http://39.106.229.1:8080/project_training_daoyun/user/alogin")
                        .url("http://47.98.236.0:8080/login")
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

//                            Toast.makeText(LoginActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("LoginInfo", responseBodyStr);
                        if(responseBodyStr.equals("password_error")){
                            showAlertDialog("密码错误！");
                        }else if(responseBodyStr.equals("username_not_exist")){
                            showAlertDialog("用户名不存在！");
                        }else{
                            SharedPreferences.Editor editor = getSharedPreferences("remember_user", MODE_PRIVATE).edit();
                            if(rememberUserCB.isChecked()){
                                editor.putString("userName", username);
                                editor.putString("password", password);
                                editor.apply();
                            }else{
                                editor.putString("userName", "");
                                editor.putString("password", "");
                                editor.apply();
                            }

                            try {
                                JSONObject jsonObject = new JSONObject(responseBodyStr);
                                Log.i("LoginInfoInfo", jsonObject.toString());
                                MainActivity.loginType = jsonObject.getString("loginType");
                                MainActivity.icon = jsonObject.getString("icon");
                                MainActivity.name = jsonObject.getString("name");
                                MainActivity.phoneNumber = jsonObject.getString("phone");
                                Log.i("LoginInfoInfo", MainActivity.phoneNumber);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(PasLoginActivity.this, MainActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                    }
                });

//                if(username.equals("admin") || username.equals("teacher") || username.equals("student1")
//                        || username.equals("student2")){

//                }else {
//                    SharedPreferences preferences;
//                    if(!new File("/data/data/" + getPackageName().toString() + "/shared_prefs/",
//                            username + ".xml").exists()){
//                        showAlertDialog("用户名不存在！");
//                    }else{
//                        preferences = getSharedPreferences(username, MODE_PRIVATE);
//                        if(!preferences.getString("password", "").equals(password)){
//                            showAlertDialog("密码错误！");
//                        }else {
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.putExtra("username", username);
//                            startActivity(intent);
//                        }
//                    }
//
//                }

            }
        }).start();
    }


}