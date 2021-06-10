package cn.edu.fzu.daoyun_app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fzu.daoyun_app.ClassTabActivity;
import cn.edu.fzu.daoyun_app.MainActivity;
import cn.edu.fzu.daoyun_app.Member;
import cn.edu.fzu.daoyun_app.OneBtnSignInSettingActivity;
import cn.edu.fzu.daoyun_app.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MemberFragment extends Fragment {
    public List<Member> memberList = new ArrayList<>();
    private LinearLayout linearLayout;
    private LinearLayout affectionCardLayout;
    private LinearLayout groupPlanLayout;
    private TextView signInTV;
    private TextView memberSumTV;
    private Button backBtn;
    private ListView listView;
   // public MemberAdapter memberAdapter;
    private List<Integer> experienceList = new ArrayList<>();
    private List<Integer> indexList = new ArrayList<>();
    private int userMark;
    private Dialog bottomDialog;
    private String userExperienceScore;
    private TextView rankTV;
    public TextView experTV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Member> tempMemberList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member,container,false);
        backBtn = view.findViewById(R.id.toolbar_left_btn);
        memberSumTV = view.findViewById(R.id.member_sum_Tv);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMember();

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        rankTV = getActivity().findViewById(R.id.ranking_Tv);
        experTV = getActivity().findViewById(R.id.experience_temp_Tv);
        if(ClassTabActivity.enterType.equals("create")){
            rankTV.setVisibility(View.GONE);
            experTV.setVisibility(View.GONE);
        }

        signInTV = getActivity().findViewById(R.id.signin_Tv);
        if(ClassTabActivity.enterType.equals("create")){
            signInTV.setText("发起签到");
        }

        linearLayout = getActivity().findViewById(R.id.signin_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ClassTabActivity.enterType.equals("create")){
                    setDialog();
//
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            OkHttpClient okHttpClient = new OkHttpClient();
//                            RequestBody requestBody = new FormBody.Builder()
//                                    .add("classId", ClassTabActivity.classId)
//                                    .build();
//                            Request request = new Request.Builder()
//                                    .url("http://47.98.236.0:8080/signinpermit")
//                                    .post(requestBody)
//                                    .build();
//                            okHttpClient.newCall(request).enqueue(new Callback() {
//                                @Override
//                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                                }
//
//                                @Override
//                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                                    String responseBodyStr = response.body().string();
//                                    if(responseBodyStr.equals("permit_create")){
//                                        setDialog();
//                                    }else{
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(responseBodyStr);
//                                            String signinId = jsonObject.getString("signinId");
//                                            String signinType = jsonObject.getString("signinType");
//                                            //进入签到页面
////                                            Intent intent = new Intent(getContext(), FinishSignInActivity.class);
////                                            intent.putExtra("signin_mode", signinType.equals("gesture") ? "gesture_signin_mode" : "one_btn_mode");
////                                            intent.putExtra("signinId", signinId);
////                                            startActivity(intent);
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                }
//                            });
//                        }
//                    }).start();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("classId", ClassTabActivity.classId)
                                    .add("studentPhone", MainActivity.phoneNumber)
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://47.98.236.0:8080/querysignin")
                                    .post(requestBody)
                                    .build();
                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String responseBodyStr = response.body().string();
                                    if(responseBodyStr.equals("have_signin")){
                                        showAlertDialog("你已签到成功，不可重复签到！");
                                    }else if(responseBodyStr.equals("no_signin")){
                                        showAlertDialog("当前老师并未发起签到！");
                                    }else{
                                        //注释掉了
//                                        try {
//                                            Log.i("signinInfo", responseBodyStr);
//                                            JSONObject jsonObject = new JSONObject(responseBodyStr);
//                                            String signinType = jsonObject.getString("signinType");
//                                            String signinId = jsonObject.getString("signinId");
//                                            if(signinType.equals("gesture")){
//                                                GraphicLockView.mPassword = jsonObject.getString("gesturePassword");
//                                                Log.i("memFrgInfo", signinId);
//                                                startActivity(new Intent(getContext(), GestureUnlockActivity.class)
//                                                        .putExtra("signinId", signinId));
//                                            }else{
//                                                Intent intent = new Intent(getContext(), OneBtnSignInActivity.class);
//                                                intent.putExtra("longitude", jsonObject.getString("longitude"));
//                                                intent.putExtra("latitude", jsonObject.getString("latitude"));
//                                                intent.putExtra("limitDistance", jsonObject.getString("limitDistance"));
//                                                intent.putExtra("signinId", signinId);
//                                                startActivity(intent);
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }

                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        affectionCardLayout = getActivity().findViewById(R.id.affection_card_layout);
        affectionCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("暂不支持\"心意卡片\"功能");
            }
        });

        groupPlanLayout = getActivity().findViewById(R.id.group_plan_layout);
        groupPlanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("暂不支持\"小组方案\"功能");
            }
        });

    }

    public void refresh(){
        initMember();
//        memberAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Looper.prepare();
//        bottomDialog.dismiss();
//        Looper.loop();
//        initMember();
    }

    public void setDialog(){
//        Looper.prepare();
        bottomDialog = new Dialog(getContext(), R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.signin_bottom_dialog, null);
        root.findViewById(R.id.btn_gesture_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                //进入手势签到设置
              //  startActivity(new Intent(getContext(), GestureSettingActivity.class));
            }
        });
        root.findViewById(R.id.btn_one_btn_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                //进入一键签到设置
               startActivity(new Intent(getContext(), OneBtnSignInSettingActivity.class));
            }
        });
        bottomDialog.setContentView(root);
        Window dialogWindow = bottomDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        bottomDialog.show();
      //  Looper.loop();
    }

    public void showAlertDialog(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setMessage(msg)
                        .setPositiveButton("确定", null);
                builder.show();
            }
        });
    }

    private void initMember(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                memberList.clear();
                indexList.clear();
                tempMemberList.clear();
                experienceList.clear();
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("classId", ClassTabActivity.classId)
                        .build();
                Request request = new Request.Builder()
                        .url("http://47.98.236.0:8080/queryclassmember")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("TaskReInfo", responseBodyStr);
                        if(responseBodyStr.equals("nobody_join")){

                        }else{
                            try {
                                parseJoinedList(responseBodyStr);
                                afterAction();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public void parseJoinedList(String JsonArrayData) throws JSONException {
        JSONArray jsonArray = new JSONArray(JsonArrayData);
        int rank = 0;
        for(int i = 0 ; i < jsonArray.length() ; i++){
            rank++;
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            final String phoneNumber = jsonObject.getString("phoneNumber");
            final String name = jsonObject.getString("name");
            final String IDNumber = jsonObject.getString("IDNumber");
            final String experienceScore = jsonObject.getString("experienceScore");
            final String icon = jsonObject.getString("icon");
            if(phoneNumber.equals(MainActivity.phoneNumber)){
                userMark = rank - 1;
                userExperienceScore = experienceScore;
            }
            experienceList.add(Integer.valueOf(experienceScore));
            indexList.add(rank-1);
            if(icon.equals("")){
                Member member;
                if(name.equals("")){
                    member = new Member(String.valueOf(rank), R.drawable.course_img_1, phoneNumber, IDNumber, experienceScore+"经验值");
                }else{
                    member = new Member(String.valueOf(rank), R.drawable.course_img_1, name, IDNumber, experienceScore+"经验值");
                }
                memberList.add(member);
                tempMemberList.add(member);
//                memberAdapter.notifyDataSetChanged();
            }else{
                final File userIconFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/" +icon);
                if(!userIconFile.exists()){
                    final int finalRank = rank;
                    final int finalRank1 = rank;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("icon", icon)
                                    .add("type", "usericon")
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://47.98.236.0:8080/downloadicon")
                                    .post(requestBody)
                                    .build();
                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    FileOutputStream os = new FileOutputStream(userIconFile);
                                    byte[] BytesArray = response.body().bytes();
                                    os.write(BytesArray);
                                    os.flush();
                                    os.close();
//                                    afterAction(finalRank, userIconFile.getAbsolutePath(), name, IDNumber, experienceScore, phoneNumber);
                                    Member member;
                                    if(name.equals("")){
                                        member = new Member(String.valueOf(finalRank1), userIconFile.getAbsolutePath(), phoneNumber, IDNumber, experienceScore+"经验值");
                                    }else{
                                        member = new Member(String.valueOf(finalRank1), userIconFile.getAbsolutePath(), name, IDNumber, experienceScore+"经验值");
                                    }
                                    memberList.add(member);
                                    tempMemberList.add(member);
//                                    memberAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                }else{
                    Member member;
                    if(name.equals("")){
                        member = new Member(String.valueOf(rank), userIconFile.getAbsolutePath(), phoneNumber, IDNumber, experienceScore+"经验值");
                    }else{
                        member = new Member(String.valueOf(rank), userIconFile.getAbsolutePath(), name, IDNumber, experienceScore+"经验值");
                    }
                    memberList.add(member);
                    tempMemberList.add(member);
//                    memberAdapter.notifyDataSetChanged();
                }

            }
        }
        final int finalRank2 = rank;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                memberSumTV.setText(finalRank2 + "人");
            }
        });
    }

   public void afterAction(){
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i = experienceList.size() - 1 ; i >= 0; i--){
//                    for(int m = 0 ; m < i ; m++){
//                        if(experienceList.get(m) < experienceList.get(m+1)){
//                            int temp = experienceList.get(m);
//                            experienceList.set(m, experienceList.get(m+1));
//                            experienceList.set(m+1, temp);
//                            temp = indexList.get(m);
//                            indexList.set(m, indexList.get(m+1));
//                            indexList.set(m+1, temp);
//                        }
//                    }
//                }
//                int flag = -1;
//                Log.i("MemberFragInfo","tempSize:"+memberList.size());
//                try{
//                    for(int i = 0 ; i < experienceList.size() ; i++){
//                        tempMemberList.set(i, memberList.get(indexList.get(i)));
//                        if(indexList.get(i) == userMark){
//                            userMark = i;
//                        }
//                        flag = i;
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Log.i("MemberFragInfo",flag+"");
//                }
//
////                memberList = tempMemberList;
//                for(int i = 0 ; i < experienceList.size() ; i++){
//                    memberList.set(i, tempMemberList.get(i));
//                }
//                int index = 1;
//                for(int i = 0 ; i < experienceList.size() ; i++){
//                    if(i == 0){
//                        indexList.set(i, 1);
//                    }else if(experienceList.get(i) == experienceList.get(i-1)){
//                        indexList.set(i, index);
//                    }else if(experienceList.get(i) != experienceList.get(i-1)){
//                        index++;
//                        indexList.set(i, index);
//                    }
//                    memberList.get(i).setRanking(String.valueOf(indexList.get(i)));
//                    if(userMark == i && !ClassTabActivity.enterType.equals("create")){
//                        rankTV.setText("第" + index + "名");
//                        experTV.setText("当前获得" + userExperienceScore + "经验值");
//                    }
//                }
//                memberAdapter = new MemberAdapter(getContext(), R.layout.member_item, memberList);
////                View view = getLayoutInflater().inflate(R.layout.fragment_member, null);
//                listView = getActivity().findViewById(R.id.member_list_view);
//                listView.setAdapter(memberAdapter);
//                Utility.setListViewHeightBasedOnChildren(listView);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Member member = memberList.get(position);
//                        Toast.makeText(getContext(), member.getMemberName(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        }

}