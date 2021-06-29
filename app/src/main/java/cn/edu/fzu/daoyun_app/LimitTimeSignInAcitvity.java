package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

public class LimitTimeSignInAcitvity extends AppCompatActivity {

    private Button backBtn;
    private static double EARTH_RADIUS = 6378.137;
    public double longitude = 0.0;
    public double latitude = 0.0;
    private double teacherLongitude = 0.0;
    private double teacherLatitude = 0.0;
    private double distanceLimit = 0;
    private Button oneButton;
    public LocationClient locationClient;
    public ProgressDialog progressDialog;
    public String signinId;
    public TextView signinNumTV;
    public TextView signinRateTV;
    public TextView signinTimeTV;
    public String value;
    public int second;
    // public List<SigninRecord> signinRecordList = new ArrayList<>();
    public ListView listView;
    // public SigninRecordAdapter signinRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit_time_sign_in_acitvity);
        initSigninRecord();

        backBtn = findViewById(R.id.toolbar_left_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signinNumTV = findViewById(R.id.limitedd_num_Tv);
        signinRateTV = findViewById(R.id.limited_rate_Tv);
        signinTimeTV =findViewById(R.id.limited_time_Tv);
//        double value = getDistance(112.37544503, 32.72238775, 112.3752777783, 32.7222221667);
//        Log.i("DistanceCalculate", String.valueOf(value));

        Intent intent = getIntent();
       // teacherLongitude = Double.valueOf(intent.getStringExtra("longitude"));
       // teacherLatitude = Double.valueOf(intent.getStringExtra("latitude"));
        //distanceLimit = Double.valueOf(intent.getStringExtra("limitDistance"));
        Log.i("studentsignininfo",teacherLongitude+teacherLatitude+"");
        signinId = intent.getStringExtra("signinId");
        //设置倒计时
        second=Integer.parseInt(getIntent().getStringExtra("second"));
        Log.i("limittimesecond", second+"");
        initCuntDown();
        oneButton = findViewById(R.id.one_btn);
        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AlertDialogUtil.checkGPSIsOpen(LimitTimeSignInAcitvity.this)){ progressDialog = new ProgressDialog(LimitTimeSignInAcitvity.this);
                progressDialog.setMessage("获取定位中...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                locationClient = new LocationClient(getApplicationContext());
                locationClient.registerLocationListener(new LimitTimeSignInAcitvity.MyLocationListener());
                locationClient.start();}
            }
        });
    }

    public void initSigninRecord(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("classId", ClassTabActivity.classId)
                        .add("phoneNumber", MainActivity.phoneNumber)
                        .build();
                Request request = new Request.Builder()
                        .url("http://47.98.236.0:8080/querysigninrecord")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        String responseBodyStr = response.body().string();
//                        try {
//                            int rank = 1;
//                            int signinNum = 0;
//                            int signinedNum = 0;
//                            JSONArray jsonArray = new JSONArray(responseBodyStr);
//                            for(int i = 0; i < jsonArray.length(); i++){
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                if(jsonObject.getString("signinNum").equals("")
//                                        && jsonObject.getString("signinedNum").equals("")){
//                                    SigninRecord signinRecord = new SigninRecord(rank, jsonObject.getString("time"));
//                                    signinRecordList.add(signinRecord);
//                                    rank++;
//                                }else{
//                                    signinedNum = Integer.valueOf(jsonObject.getString("signinedNum"));
//                                    signinNum = Integer.valueOf(jsonObject.getString("signinNum"));
//                                }
//                            }
//                            final int finalSigninedNum = signinedNum;
//                            final int finalSigninNum = signinNum;
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    signinNumTV.setText("签到" + finalSigninedNum + "次");
//                                    int rate = (int) (finalSigninedNum*1.0 / finalSigninNum * 100);
//                                    signinRateTV.setText("签到率" + rate + "%");
//                                    signinRecordAdapter = new SigninRecordAdapter(OneBtnSignInActivity.this, R.layout.signin_record_item, signinRecordList);
//                                    listView = findViewById(R.id.signin_record_list_view);
//                                    listView.setAdapter(signinRecordAdapter);
//                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                            SigninRecord signinRecord = signinRecordList.get(position);
//                                            Toast.makeText(OneBtnSignInActivity.this, signinRecord.getTime(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            });
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        longitude = 0.0;
        latitude = 0.0;
        finish();
    }

    public static double getDistance(double lngLocation, double latLocation, double lngTarget, double latTarget) {
        double lat1 = rad(latLocation);
        double lat2 = rad(latTarget);
        double lat = lat1 - lat2;
        double lng = rad(lngLocation) - rad(lngTarget);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(lat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(lng / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000d) / 10000d;
        distance = distance * 1000;
        return distance;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    protected void showProgressDialog(final ProgressDialog progressDialog){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(LimitTimeSignInAcitvity.this)
                        .setMessage("一键签到成功！")
                        .setPositiveButton("确定", null);
                builder.show();
            }
        });
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();
            progressDialog.dismiss();
           // double distance = getDistance(longitude, latitude, teacherLongitude, teacherLatitude);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Log.i("studentsignininfo",longitude+":::"+latitude+"");
                    com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                    json.put("cNumber", ClassTabActivity.classId);
                    json.put("ssId", signinId);
                    json.put("peId", MainActivity.peid);
                    json.put("position_x", longitude);
                    json.put("position_y", latitude);

                    OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.STUDENT_SIGNIN), json, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i("错误的返回", e.getMessage());
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String responseBodyStr = response.body().string();
                            Log.i("longitudelatitude",  longitude+latitude+"");
                            Log.i("studentsignininfo",  responseBodyStr);
                            if(responseBodyStr.contains("请勿重复签到")) {
                                AlertDialogUtil.showToastText("请勿重复签到", LimitTimeSignInAcitvity.this);
                            }else if(responseBodyStr.contains("不在签到范围内")){
                                AlertDialogUtil.showToastText("已超出范围，不在签到范围内", LimitTimeSignInAcitvity.this);
                            }else{
                                try {
                                    JSONObject jsonObject = new JSONObject(responseBodyStr);
                                    //Log.i("studentsignininfo", jsonObject.toString());
                                    value = jsonObject.getJSONObject("data").getJSONObject("signIn").getString("value").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LimitTimeSignInAcitvity.this)
                                                .setMessage("限时签到成功！")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        AlertDialogUtil.showToastText("获得"+value+"经验值", LimitTimeSignInAcitvity.this);
                                                        Intent intent=new Intent();
                                                        setResult(1,intent);
                                                        finish();
                                                    }
                                                });
                                        builder.show();
                                    }
                                });
                            }
                        }
                    });
                }
            }).start();

        }
    }

    public void showAlertDialog(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LimitTimeSignInAcitvity.this)
                        .setMessage(msg)
                        .setPositiveButton("确定", null);
                builder.show();
            }
        });
    }

    public void initCuntDown()
    {
        new CountDownTimer(second*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                String shour;
                String sminute;
                String ssecond;
                // finishSignInBtn.setText(millisUntilFinished + "ms\n" + millisUntilFinished / 1000 + "s");
                long day = millisUntilFinished / (1000 * 24 * 60 * 60); //单位天
                long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60); //单位时
                long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60); //单位分
                long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;//单位秒
                if(hour<10) { shour="0"+hour; }else {shour=hour+"";}
                if(minute<10) { sminute="0"+minute; }else {sminute=minute+"";}
                if(second<10) { ssecond="0"+second; }else {ssecond=second+"";}
                signinTimeTV.setText(shour + ":" + sminute + ":" + ssecond + "");

            }
            public void onFinish() {
                longitude = 0.0;
                latitude = 0.0;

                AlertDialog.Builder builder = new AlertDialog.Builder(LimitTimeSignInAcitvity.this)
                        .setMessage("限时签到已停止")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.show();
                //   mTextField.setText("done!");
            }
        }.start();
    }

}
