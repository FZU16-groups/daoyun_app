package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.LinkagePicker;
import cn.addapp.pickers.util.DateUtils;
import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Utils.AlertDialogUtil;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import cn.edu.fzu.daoyun_app.adapter.SignInHistoryAdapter;
import cn.edu.fzu.daoyun_app.fragment.MemberFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignInRecoderActivity extends AppCompatActivity {

    public List<SignInHistory> hisList = new ArrayList<>();

    private LinearLayout linearLayout;
    private Button backBtn;
    private ListView listView;
    private TextView signInTV;
    public SignInHistoryAdapter signAdapter;
    public TextView signRateTv;
    public LocationClient locationClient;
    private boolean isCreate = true;
    public double longitude = 0.0;
    public double latitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_recoder);

        backBtn = findViewById(R.id.toolbar_left_btn);
        backBtn.setOnClickListener(v -> finish());
        isCreate = ClassTabActivity.enterType.equals("create");
        signInTV = findViewById(R.id.start_sign_Tv);

        signRateTv = findViewById(R.id.start_sign_rate_Tv);


        if (!isCreate) {
            signInTV.setText("????????????");
        } else {
            //???????????????????????????
            signRateTv.setVisibility(View.GONE);

        }
        linearLayout = findViewById(R.id.signin_layout);
        linearLayout.setOnClickListener(v -> {
            //??????GPS???????????????
            if (AlertDialogUtil.checkGPSIsOpen(SignInRecoderActivity.this)) {
                //  showPopupMenu(v);
                judgein(v);
            } else {
                AlertDialogUtil.openGPSSettings(SignInRecoderActivity.this);
            }
        });
        iniHis();
    }

    private void iniHis() {
        if (isCreate)
            iniTeaHis();
        else
            iniStuHis();
    }

    private void iniStuHis() {
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        json.put("cNumber", ClassTabActivity.classId);
        json.put("peId", MainActivity.peid);
        OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.STUDENT_SIGNIN_INFO), json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("???????????????", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String responseBodyStr = response.body().string();
                    Log.v("signrecord", responseBodyStr.toString());
                    parseStuHisList(responseBodyStr);
                    afterStuAction();

                } catch (Exception e) {
                    AlertDialogUtil.showToastText(e.getMessage(), SignInRecoderActivity.this);
                }
            }


//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) {
//
//                try {
//                    String responseBodyStr = response.body().string();
//                    parseTeaHisList(responseBodyStr);
//                    afterTeaAction();
//
//                } catch (Exception e) {
//                    //??????????????????????????????????????? ??????????????????
//                    AlertDialogUtil.showToastText(e.getMessage(), StartSignInActivity.this);
//                }
//            }
        });
    }

    private void judgein(View v) {
        if (ClassTabActivity.enterType.equals("create")) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                    json.put("cNumber", ClassTabActivity.classId);
                    json.put("peId", MainActivity.peid);
                    OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.JUDGE_SIGNIN), json, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i("???????????????", e.getMessage());
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseBodyStr = response.body().string();
                            com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
                            //if (messjsonObject.get("message").toString().equals("Ok")) {
                            Log.i("judgesinginfo", responseBodyStr);
                            if (messjsonObject.get("message").toString().equals("Ok")) {
                                // setDialog();
                                showPopupMenu(v);
                            } else if (responseBodyStr.contains("?????????????????????")) {
                                try {
                                    JSONObject jsonObject = JSONObject.parseObject(responseBodyStr);
                                    if (responseBodyStr.contains("OneButton")) {
                                        JSONObject jsonObject2 = jsonObject.getJSONObject("data").getJSONObject("OneButton");
                                        String signinId = jsonObject2.getString("ssId");
                                        String signinType = jsonObject2.getString("type");
                                        //??????????????????
                                        Intent intent = new Intent(SignInRecoderActivity.this, FinishSignInActivity.class);
                                        intent.putExtra("signin_mode", "1");
                                        intent.putExtra("signinId", signinId);
                                        intent.putExtra("second", "0");
                                        startActivity(intent);
                                    } else {


                                        JSONObject jsonObject2 = jsonObject.getJSONObject("data").getJSONObject("LimitTime");
                                        String signinId = jsonObject2.getString("ssId");
                                        String signinType = jsonObject2.getString("type");
                                        String reminTime=jsonObject2.getString("reminTime");
                                        //??????????????????
                                        Intent intent = new Intent(SignInRecoderActivity.this, FinishSignInActivity.class);
                                        intent.putExtra("signin_mode", "2");
                                        intent.putExtra("signinId", signinId);
                                        intent.putExtra("second", reminTime);
                                        startActivity(intent);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                }

            }).start();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                    json.put("cNumber", ClassTabActivity.classId);
                    OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.STUDENT_JUDGE_SIGNIN), json, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i("???????????????", e.getMessage());
                        }


                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseBodyStr = response.body().string();
                            if (responseBodyStr.contains("????????????????????????")) {
                                AlertDialogUtil.showToastText("?????????????????????????????????", SignInRecoderActivity.this);
                            } else if (responseBodyStr.contains("???????????????")) {
                                AlertDialogUtil.showToastText("???????????????", SignInRecoderActivity.this);
                            } else {
                                try {
                                    Log.i("judgesigninInfo", responseBodyStr);
                                    JSONObject jsonObject = JSONObject.parseObject(responseBodyStr);
                                    JSONObject jsonObject2 = jsonObject.getJSONObject("data").getJSONObject("sendSignIn");

                                    String signinType = jsonObject2.getString("type");
                                    String signinId = jsonObject2.getString("ssId");
                                    if (signinType.equals("2")) {
                                        String reminTime = jsonObject2.getString("reminTime");
//                                                GraphicLockView.mPassword = jsonObject.getString("gesturePassword");
                                        Log.i("reminTimeInfo", reminTime);
                                        Intent intent = new Intent(SignInRecoderActivity.this, LimitTimeSignInAcitvity.class);
                                        intent.putExtra("longitude", jsonObject2.getString("position_x"));
                                        intent.putExtra("latitude", jsonObject2.getString("position_y"));
                                        intent.putExtra("limitDistance", jsonObject2.getString("limitdis"));
                                        intent.putExtra("second", reminTime);
                                        intent.putExtra("signinId", signinId);
                                        SignInRecoderActivity.this.startActivityForResult(intent, ClassTabActivity.ADD_EXPER);
                                    } else {
                                        Intent intent = new Intent(SignInRecoderActivity.this, OneBtnSignInActivity.class);
                                        intent.putExtra("longitude", jsonObject2.getString("position_x"));
                                        intent.putExtra("latitude", jsonObject2.getString("position_y"));
                                        intent.putExtra("limitDistance", jsonObject2.getString("limitdis"));
                                        intent.putExtra("signinId", signinId);
                                        SignInRecoderActivity.this.startActivityForResult(intent, ClassTabActivity.ADD_EXPER);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                }
            }).start();
        }
    }

    private void showPopupMenu(View view) {
//        Looper.prepare();
        new XPopup.Builder(SignInRecoderActivity.this)
                .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                .asBottomList("", new String[]{"????????????", "????????????"},
                        //  null, 2,//???????????????
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (text) {
                                    case "????????????":
                                        //bottomDialog.dismiss();
                                        //????????????????????????
                                        startActivity(new Intent(SignInRecoderActivity.this, OneBtnSignInSettingActivity.class));
                                        break;
//
                                    case "????????????":
                                        //  bottomDialog.dismiss();
                                        //limitSignin(11)
                                        onTimePicker(view);
                                        //????????????????????????
                                        //  startActivity(new Intent(getContext(), GestureSettingActivity.class));
                                        break;
                                }
                                // toast("click " + text);
                            }
                        })
                .show();
//        Looper.loop();

    }

    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();
        }
    }

    public void onTimePicker(View view) {
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {

            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @Override
            public List<String> provideFirstData() {
                ArrayList<String> firstList = new ArrayList<>();
                for (int i = 0; i <= 23; i++) {
                    String str = DateUtils.fillZero(i);
                    firstList.add(str);
                }
                return firstList;
            }

            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();
                for (int i = 0; i <= 59; i++) {
                    String str = DateUtils.fillZero(i);

                    secondList.add(str);
                }
                return secondList;
            }

            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }

        };
        LinkagePicker picker = new LinkagePicker(SignInRecoderActivity.this, provider);
        picker.setCanLoop(false);
        picker.setGravity(Gravity.BOTTOM);
        picker.setLabel("????????????", "");
        picker.setLineVisible(true);
        picker.setHeight(700);
        picker.setSelectedIndex(0, 8);
        picker.setAnimationStyle(R.style.Animation_CustomPopup);
        //int i = Integer.parseInt( first );
        //picker.setSelectedItem("12", "9");
        picker.setOnMoreItemPickListener(new OnMoreItemPickListener<String>() {

            @Override
            public void onItemPicked(String first, String second, String third) {
                int i = Integer.parseInt(first);
                int j = Integer.parseInt(second);
                int s = i * 60 + j;
                limitSignin(s);
                //showAlertDialog(first + "-" + second);
            }
        });
        picker.show();
    }

    private void limitSignin(int minute) {
        locationClient = new LocationClient(SignInRecoderActivity.this);
        locationClient.registerLocationListener(new MyLocationListener());
        locationClient.start();
//        double latitude = bdLocation.getLatitude();
//        double longitude = bdLocation.getLongitude();
        if (latitude == 0 && longitude == 0) {
            AlertDialogUtil.showConfirmClickAlertDialog("GPS??????????????????????????????????????????", SignInRecoderActivity.this);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                    json.put("cNumber", ClassTabActivity.classId);
                    json.put("peId", MainActivity.peid);
                    json.put("type", "2");
                    json.put("limitdis", "200");
                    json.put("value", "2");
                    json.put("position_x", longitude);
                    json.put("position_y", latitude);
                    json.put("limitime", minute);


                    OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.SEND_SIGNIN), json, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i("???????????????", e.getMessage());
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String responseBodyStr = response.body().string();
                            Log.i("sendsingininfo", responseBodyStr);
                            //String signinId;
                            com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity())
//                                        .setMessage("???????????????????????????")
//                                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
                            try {
                                org.json.JSONObject jsonObject = new org.json.JSONObject(responseBodyStr);
                                Log.i("limitsignininfo", jsonObject.toString());
                                Log.i("secondinfo", String.valueOf(minute * 60));
                                String signinId = jsonObject.getJSONObject("data").getJSONObject("sendSignIn").getString("ssId").toString();
                                startActivity(new Intent(SignInRecoderActivity.this, FinishSignInActivity.class)
                                        .putExtra("signin_mode", "2")
                                        .putExtra("second", String.valueOf(minute * 60))
                                        .putExtra("signinId", signinId));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//
//                                            }
//                                        });
//                                builder.show();
//                            }
//                        });
                        }
                    });
                }
            }).start();
        }

    }

    //?????????????????????
    private void iniTeaHis() {

        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        json.put("cNumber", ClassTabActivity.classId);
        json.put("peId", MainActivity.peid);
        OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.TEACHER_CHECK_RECORD), json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("???????????????", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String responseBodyStr = response.body().string();
                    Log.v("signrecord", responseBodyStr.toString());
                    parseTeaHisList(responseBodyStr);
                    afterTeaAction();

                } catch (Exception e) {
                    AlertDialogUtil.showToastText(e.getMessage(), SignInRecoderActivity.this);
                }
            }


//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) {
//
//                try {
//                    String responseBodyStr = response.body().string();
//                    parseTeaHisList(responseBodyStr);
//                    afterTeaAction();
//
//                } catch (Exception e) {
//                    //??????????????????????????????????????? ??????????????????
//                    AlertDialogUtil.showToastText(e.getMessage(), StartSignInActivity.this);
//                }
//            }
        });
    }

    public void parseTeaHisList(String JsonArrayData) {
        hisList = new ArrayList<>();
        JSONArray jsonArray = com.alibaba.fastjson.JSONObject.parseObject(JsonArrayData).getJSONObject("data").getJSONArray("sendSignInDTOList");
        try {


            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String signId = jsonObject.getString("ssId");
                String type = jsonObject.getString("type");
                String lng = jsonObject.getString("position_x");
                String lat = jsonObject.getString("position_y");
                String startTime = jsonObject.getString("date");
                String studentCount = jsonObject.getString("studentCount");
                String signedCount = jsonObject.getString("signedCount");
                String starminute = jsonObject.getString("startTime");
                String endTime = jsonObject.getString("endTime");
                boolean isFinish = Boolean.valueOf(jsonObject.getString("state"));
                String Signinpeople = signedCount + "???/" + studentCount + "???";
//                if (!isFinish)
//                    continue;
                SignInHistory signInHistory = new SignInHistory(signId, startTime, endTime, lng, lat, isFinish, Signinpeople,starminute);
//                String d = startTime.substring(0, startTime.length() - 9);
                if (type.contains("1")) {
                    signInHistory.setDateType(startTime + "\t????????????");
                    signInHistory.setConDate(startTime);
                    Log.v("typeinfo",type.length()+"");

                } else {
                    signInHistory.setDateType(startTime + "\t????????????");
                    signInHistory.setConDate(startTime + jsonObject.getString("limitime") + "??????");

//                    signInHistory.setConDate(startTime.substring(startTime.length() - 7, startTime.length() - 3) + "-" + endTime.substring(endTime.length() - 7, endTime.length() - 3) + " ??????" +
//                            TimeUtil.figMinute(TimeUtil.strConvertDate(startTime), TimeUtil.strConvertDate(endTime)) + " ??????");
                }
                hisList.add(signInHistory);
            }
        } catch (Exception e) {
            AlertDialogUtil.showToastText(e.getMessage(), SignInRecoderActivity.this);
        }

        Collections.reverse(hisList);
    }

    public void afterTeaAction() {
        runOnUiThread(() -> {
            signAdapter = new SignInHistoryAdapter(SignInRecoderActivity.this, R.layout.item_signinhistory, hisList);
            ListView listView = findViewById(R.id.his_list_view);
            listView.setAdapter(signAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
//                //TODO ??????
                SignInHistory s = hisList.get(position);
                Intent intent = new Intent(SignInRecoderActivity.this, SignStudentRecoderActivity.class).
                        putExtra("signID"
                                , s.getSignID());
                startActivity(intent);
            });

        });


    }

    public void parseStuHisList(String JsonArrayData) {
        hisList = new ArrayList<>();
        JSONArray jsonArray = com.alibaba.fastjson.JSONObject.parseObject(JsonArrayData).getJSONObject("data").getJSONArray("signInDTOList");
        try {


            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String signId = jsonObject.getString("ssId");
                String type = jsonObject.getString("type");
                String lng = jsonObject.getString("position_x");
                String lat = jsonObject.getString("position_y");
                String value = jsonObject.getString("value");
                String startTime = jsonObject.getString("date");
                String endTime = startTime;
                boolean isFinish = Boolean.valueOf(jsonObject.getString("state"));
//                if (!isFinish)
//                    continue;
                SignInHistory signInHistory = new SignInHistory(signId, startTime, endTime, lng, lat, isFinish,value);
//                String d = startTime.substring(0, startTime.length() - 9);
//
                if (type.equals("1")) {
                    signInHistory.setDateType(startTime + "\t????????????");
                    signInHistory.setConDate(startTime);
                } else {
                    signInHistory.setDateType(startTime + "\t????????????");
                    signInHistory.setConDate(startTime + jsonObject.getString("limitime") + "??????");

//                    signInHistory.setConDate(startTime.substring(startTime.length() - 7, startTime.length() - 3) + "-" + endTime.substring(endTime.length() - 7, endTime.length() - 3) + " ??????" +
//                            TimeUtil.figMinute(TimeUtil.strConvertDate(startTime), TimeUtil.strConvertDate(endTime)) + " ??????");
                }
                hisList.add(signInHistory);
            }
        } catch (Exception e) {
            AlertDialogUtil.showToastText(e.getMessage(), SignInRecoderActivity.this);
        }

        Collections.reverse(hisList);
    }

    public void afterStuAction() {
        runOnUiThread(() -> {
            signAdapter = new SignInHistoryAdapter(SignInRecoderActivity.this, R.layout.item_signinhistory, hisList);
            ListView listView = findViewById(R.id.his_list_view);
            listView.setAdapter(signAdapter);
//            listView.setOnItemClickListener((parent, view, position, id) -> {
////                //TODO ??????
//                SignInHistory s = hisList.get(position);
//                AlertDialogUtil.showToastText(s.getSignID(), SignInRecoderActivity.this);
//
//                Intent intent = new Intent(SignInRecoderActivity.this, SignStudentRecoderActivity.class).
//                        putExtra("signID"
//                                , s.getSignID());
//                startActivity(intent);
//            });

        });


    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    @Override

    public void onPause() {

        super.onPause();
        finish();

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}