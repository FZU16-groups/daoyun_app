package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Utils.AlertDialogUtil;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import cn.edu.fzu.daoyun_app.adapter.SignIngMemberAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignStudentRecoderActivity extends AppCompatActivity {

    private Button backBtn;
    //未签到列表
    private ListView usListView;
    private List<Member> usMemberList = new ArrayList<>();
    private SignIngMemberAdapter usSignIngMemberAdapter;
    //已签到列表
    private ListView sListView;
    private List<Member> sMemberList = new ArrayList<>();
    private SignIngMemberAdapter signIngMemberAdapter;

    private String signID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_student_recoder);
        backBtn = findViewById(R.id.toolbar_left_btn);
        //返回
        backBtn.setOnClickListener(v -> {
            finish();
        });
        usListView = findViewById(R.id.unsignedIn_listview);
        sListView = findViewById(R.id.signedIn_listview);
        signID = getIntent().getStringExtra("signID");
        //初始化member
        initMember();
    }

    public void initMember() {

        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        json.put("ssId", signID);
        OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.TEACHER_CHECK_SIGNIN), json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("错误的返回", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String responseBodyStr = response.body().string();
                    praseJsonToList(responseBodyStr);
                    afterAction();

                } catch (Exception e) {
                    AlertDialogUtil.showToastText(e.getMessage(), SignStudentRecoderActivity.this);
                }
            }
        });
    }

    private void praseJsonToList(String jsonData) {
        JSONArray noArray = JSONObject.parseObject(jsonData).getJSONObject("data").getJSONArray("NotSignList");
        for (int i = 0; i < noArray.size(); i++) {
            //TODO 发起签到之前先检查下是否签到
            JSONObject jsonObject = noArray.getJSONObject(i);
            final String studentID = jsonObject.getString("peNumber");
            final String name = jsonObject.getString("peName");
            final String experienceScore = "2";
            Member member;
            member = new Member(String.valueOf(i + 1), "", name, studentID, experienceScore);
            usMemberList.add(member);
        }
        JSONArray sArray = JSONObject.parseObject(jsonData).getJSONObject("data").getJSONArray("signedList");
        for (int i = 0; i < sArray.size(); i++) {
            //TODO 发起签到之前先检查下是否签到
            JSONObject jsonObject = sArray.getJSONObject(i);
            final String studentID = jsonObject.getString("peNumber");
            final String name = jsonObject.getString("peName");
            final String experienceScore = "2";
            Member member;
            member = new Member(String.valueOf(i + 1), "", name, studentID, experienceScore);
            sMemberList.add(member);
        }
    }


    private void afterAction() {
        runOnUiThread(
                () -> {
                    signIngMemberAdapter = new SignIngMemberAdapter(SignStudentRecoderActivity.this, R.layout.member_item, sMemberList);
                    sListView.setAdapter(signIngMemberAdapter);
                    signIngMemberAdapter.notifyDataSetChanged();
                    sListView.setOnItemClickListener((parent, view, position, id) -> {
                        //TODO 设置
                        Member m = sMemberList.get(position);
                    });
                    usSignIngMemberAdapter = new SignIngMemberAdapter(SignStudentRecoderActivity.this, R.layout.member_item, usMemberList);
                    usListView.setAdapter(usSignIngMemberAdapter);
                    usSignIngMemberAdapter.notifyDataSetChanged();
                    usListView.setOnItemClickListener((parent, view, position, id) -> {
                        //TODO 设置
                        Member m = usMemberList.get(position);
                    });
                }
        );
    }

    private void showPopUp() {

    }
}