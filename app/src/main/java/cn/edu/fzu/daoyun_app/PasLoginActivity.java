package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.WriterException;
import com.google.zxing.common.BitmapUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PasLoginActivity extends AppCompatActivity {
    protected Button mBtnRegister;
    protected Button mBtnLogin;
    protected Button mBtnOtherLogin;
    protected EditText userNameET;
    protected EditText passwordET;
    protected CheckBox rememberUserCB;
    protected TextView forgetPasswordTV;
    protected TextView dialogcNameTV;
    protected View qqloginView;
    private static final String TAG = "PasLoginActivity";
    private static final String APP_ID = "101950248";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private Context mContext;
    private static final Pattern CHINA_PATTERN = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pas_login);


        ActivityCollector.finishAll();
        ActivityCollector.addActivity(this);

        mContext=this;
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, PasLoginActivity.this.getApplicationContext());

        userNameET = findViewById(R.id.et_paslogin_username);
        passwordET = findViewById(R.id.et_paslogin_pwd);
        rememberUserCB = findViewById(R.id.cb_remember_login);
        forgetPasswordTV = findViewById(R.id.tv_login_forget_pwd);
        dialogcNameTV=findViewById(R.id.dialog_cNumber);
        SharedPreferences preferences = getSharedPreferences("remember_user", MODE_PRIVATE);
        if (!preferences.getString("userName", "").equals("") && !preferences.getString("password", "").equals("")) {
            userNameET.setText(preferences.getString("userName", ""));
            passwordET.setText(preferences.getString("password", ""));
        }


        mBtnRegister = this.findViewById(R.id.bt_paslogin_register);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //二维码自定义Dialog测试
//                String content = "20180912131415";
//                Bitmap bitmap = null;
//                try {
//                  //  dialogcNameTV.setText(content);
//                    bitmap = BitmapUtils.create2DCode(content);
//                    CustomPopDialog2.Builder dialogBuild = new CustomPopDialog2.Builder(mContext);
//                    dialogBuild.setImage(bitmap);
//                    dialogBuild.setcNumber("课程号："+content);
//                    CustomPopDialog2 dialog = dialogBuild.create();
//                    dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
//                    dialog.show();
//                } catch (WriterException e) {
//                    e.printStackTrace();
//                }

   //             buttonLogin(view);//第三方登陆
                startActivity(new Intent(PasLoginActivity.this, RegisterActivity.class));  //注册页面
//                new XPopup.Builder(PasLoginActivity.this)   //弹窗测试
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

        qqloginView=findViewById(R.id.ll_login_QQ);

//        mBtnOtherLogin=findViewById(R.id.bt_other_login);
        qqloginView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buttonLogin(v);
            }

        });

        mBtnLogin = findViewById(R.id.bt_paslogin_submit);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNameET.getText().toString().equals("")){
                    showAlertDialog("请输入账号!");
                }else if(passwordET.getText().toString().equals("")){
                    showAlertDialog("请输入密码！");
                }else if(!isChinaPhoneLegal(userNameET.getText().toString())){
                    showAlertDialog("请输入正确的手机号！");
                }else{
                    sendRequestWithHttpURLConnection(userNameET.getText().toString(), passwordET.getText().toString());
                    //sendRequestWithHttpURLConnection("15900000004", "123456");
                }

              //  isChinaPhoneLegal(phoneET.getText().toString()
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
        if (checkStorePermission != PackageManager.PERMISSION_GRANTED
                || checkInternetPermission != PackageManager.PERMISSION_GRANTED
                || checkLocationPermission != PackageManager.PERMISSION_GRANTED
                || checkFineLocationPermission != PackageManager.PERMISSION_GRANTED
                || checkReadStorePermission != PackageManager.PERMISSION_GRANTED
                || checkReadMountPermission != PackageManager.PERMISSION_GRANTED
                || checkCameraPermission != PackageManager.PERMISSION_GRANTED
                || checkPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE}, 0);
        }

        Properties properties = PropertiesUtill.getProperties();
        PropertiesUtill.setProperties(PasLoginActivity.this, "gesturePassword", "");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    protected void showAlertDialog(final String msg) {
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

    private void sendRequestWithHttpURLConnection(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("loginToken",username );
                json.put("passwordToken",password);
                json.put("loginType",2);

                OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.PSD_LOGIN),json, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("LoginInfo", responseBodyStr);
                        com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
                        System.out.println( messjsonObject.get("message").toString());
                        if (messjsonObject.get("message").toString().equals("Ok")) {
                            SharedPreferences.Editor editor = getSharedPreferences("remember_user", MODE_PRIVATE).edit();
                            if (rememberUserCB.isChecked()) {
                                editor.putString("userName", username);
                                editor.putString("password", password);
                                editor.apply();
                            } else {
                                editor.putString("userName", "");
                                editor.putString("password", "");
                                editor.apply();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(responseBodyStr);
                                Log.i("LoginInfoInfo", jsonObject.toString());
                                MainActivity.peid = jsonObject.getJSONObject("data").getJSONObject("person").getString("peId").toString();
                                //Log.i("LoginInfoInfo", MainActivity.phoneNumber);
                                Log.i("peIdInfoInfo", MainActivity.peid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(PasLoginActivity.this, MainActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                    }
                });
            }
        }).start();
    }

    public void buttonLogin(View v) {
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        mTencent.login(PasLoginActivity.this, "all", mIUiListener);
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(PasLoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                Log.i("accessToken123", accessToken);
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                Log.i("qqtoken",qqToken.toString());
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        sendQQToken(openID,accessToken);
                        Log.e(TAG, "登录成功" + response.toString());
                    }
                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }
                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }
                    @Override
                    public void onWarning(int i) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void sendQQToken(final String openid, final String access_token) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i("openid", openid);
                    Log.i("accessToken", access_token);
                    OkHttpClient okHttpClient = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                    json.put("openId",openid );
                    json.put("accessToken",access_token);
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                    Request request = new Request.Builder()
                            .header("Content-Type", "application/json")
                            .url("http://47.98.151.20:8080/daoyun_service/loginByQQ.do")
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
                            com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
                            if (messjsonObject.get("message").toString().equals("绑定手机号"))
                            {
                                Intent intent = new Intent(PasLoginActivity.this, BindPhoneActivity.class);
                                intent.putExtra("openID",openid);
                                startActivity(intent);
                            }else if (messjsonObject.get("message").toString().equals("Ok"))
                            {
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }else {
                                showAlertDialog("授权失败");
                            }

                        }
                    });


                }
            }).start();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(PasLoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(PasLoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onWarning(int i) {

        }

    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        Matcher m = CHINA_PATTERN.matcher(str);
        return m.matches();
    }
}
