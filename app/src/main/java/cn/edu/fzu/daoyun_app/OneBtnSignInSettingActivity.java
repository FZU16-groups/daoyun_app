package cn.edu.fzu.daoyun_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.edu.fzu.daoyun_app.Config.GConfig;
import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Utils.AlertDialogUtil;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OneBtnSignInSettingActivity extends AppCompatActivity {

    private EditText distanceLimitET;
    private Button startOneBtn;
    private Button backBtn;
    private TextView experienceSettingTV;
    private LinearLayout latitudeLayout;
    private LinearLayout longitudeLayout;
    private TextView latitudeTV;
    private TextView longitudeTV;
    private String selectExperience;
    private double latitude;
    private double longitude;
    public static boolean startOrNot = false;
    public static int distanceLimit = 0;
    public LocationClient locationClient;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharedPreferences preferences = getSharedPreferences("sigin", MODE_PRIVATE);
        setContentView(R.layout.content_one_btn_sign_in_setting);
        backBtn = findViewById(R.id.toolbar_left_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());

        experienceSettingTV = findViewById(R.id.signin_experience_Tv);
        final String[] experience = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        experienceSettingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OneBtnSignInSettingActivity.this)
                        .setTitle("???????????????")
                        .setSingleChoiceItems(experience, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectExperience = experience[which];
                            }
                        });
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        experienceSettingTV.setText(selectExperience);
                    }
                });
                builder.show();
            }
        });
        latitudeLayout = findViewById(R.id.latitude_layout);
        latitudeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AlertDialogUtil.checkGPSIsOpen(OneBtnSignInSettingActivity.this)){
                getLongitudeLatitude();}
            }
        });
        longitudeLayout = findViewById(R.id.longitude_layout);
        longitudeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AlertDialogUtil.checkGPSIsOpen(OneBtnSignInSettingActivity.this)){
                getLongitudeLatitude();}
            }
        });
        latitudeTV = findViewById(R.id.latitude_Tv);
        longitudeTV = findViewById(R.id.longitude_Tv);
        distanceLimitET = findViewById(R.id.distance_limit_Et);
        startOneBtn=findViewById(R.id.start_one_btn_ok);
        startOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(distanceLimitET.getText().toString().equals("")){
                    showAlertDialog("??????????????????????????????");
                }else if(experienceSettingTV.getText().toString().equals("")){
                    showAlertDialog("???????????????????????????");
                }else {
                    if(longitudeTV.getText().toString().equals("") && latitudeTV.getText().toString().equals("")){
                        getLongitudeLatitude();
                    }
                            com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                            json.put("cNumber", ClassTabActivity.classId);
                            json.put("peId", MainActivity.peid);
                            json.put("type", "1");
                            json.put("limitdis",distanceLimitET.getText().toString());
                            json.put("value", experienceSettingTV.getText().toString());
                            json.put("position_y", latitudeTV.getText().toString());
                            json.put("position_x", longitudeTV.getText().toString());


                            OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.SEND_SIGNIN), json, new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    Log.i("???????????????", e.getMessage());
                                }
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    final String responseBodyStr = response.body().string();
                                    Log.i("hhhsendsingininfo", responseBodyStr);
                                    //String signinId;
                                    com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);

                                                                String signinId=messjsonObject.getJSONObject("data").getJSONObject("sendSignIn").getString("ssId").toString();

                                    Intent intent = new Intent(OneBtnSignInSettingActivity.this, FinishSignInActivity.class);
                                                               intent.putExtra("signin_mode", "1");
                                                               intent.putExtra("signinId", signinId);
                                                               intent.putExtra("second", "0");
                                                               startActivity(intent);

//                                            AlertDialog.Builder builder = new AlertDialog.Builder(OneBtnSignInSettingActivity.this)
//                                                    .setMessage("???????????????????????????")
//                                                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            try {
//                                                                JSONObject jsonObject = new JSONObject(responseBodyStr);
//                                                                Log.i("LoginInfoInfo", jsonObject.toString());
//                                                                String signinId=jsonObject.getJSONObject("data").getJSONObject("sendSignIn").getString("ssId").toString();
////                                                                startActivityForResult(new Intent(OneBtnSignInSettingActivity.this, FinishSignInActivity.class)
////                                                                        .putExtra("signin_mode","1")
////                                                                        .putExtra("signinId", signinId), 1);
//                                                                //??????????????????
//                                                                Intent intent = new Intent(OneBtnSignInSettingActivity.this, FinishSignInActivity.class);
//                                                                intent.putExtra("signin_mode", "1");
//                                                                intent.putExtra("signinId", signinId);
//                                                                startActivity(intent);
//                                                            } catch (Exception e) {
//                                                                e.printStackTrace();
//                                                            }
//
//
//                                                        }
//                                                    });
//                                            builder.show();
//                                        }
//                                    });
                                }
                            });
                        }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            finish();
        }
    }

    public void getLongitudeLatitude(){
        progressDialog = new ProgressDialog(OneBtnSignInSettingActivity.this);
        progressDialog.setMessage("?????????????????????...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        locationClient.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    public void showAlertDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(OneBtnSignInSettingActivity.this)
                .setMessage(msg)
                .setPositiveButton("??????",null);
        builder.show();
    }

    protected void showProgressDialog(final ProgressDialog progressDialog){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
//                startActivity(new Intent(OneBtnSignInSettingActivity.this, FinishSignInActivity.class)
//                        .putExtra("signin_mode", "one_btn_mode"));
            }
        });
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            longitude = bdLocation.getLongitude();
            latitude = bdLocation.getLatitude();
            Log.i("latitude",String.valueOf(latitude)+String.valueOf(longitude));

            longitudeTV.setText(String.valueOf(longitude));
            latitudeTV.setText(String.valueOf(latitude));
            progressDialog.dismiss();
        }
    }

}