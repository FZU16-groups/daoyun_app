package cn.edu.fzu.daoyun_app.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.edu.fzu.daoyun_app.ClassTabActivity;
import cn.edu.fzu.daoyun_app.Config.GConfig;
import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.MainActivity;
import cn.edu.fzu.daoyun_app.PasLoginActivity;
import cn.edu.fzu.daoyun_app.R;
import cn.edu.fzu.daoyun_app.Utils.AlertDialogUtil;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DetailFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private Button backBtn;
    private Button exitDismissBtn;
    private ImageView classIconIV;
    private TextView gradeClassTV;
    private TextView classNameTV;
    private TextView teacherNameTv;
    private TextView termTV;
    private TextView schoolDepartmentTV;
    private TextView classIntructionTV;
    protected CheckBox permitaddClassCB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);
        backBtn = view.findViewById(R.id.exit_dismiss_btn);
        classIconIV = view.findViewById(R.id.more_fragment_Iv);
        gradeClassTV = view.findViewById(R.id.more_class_Tv);
        classNameTV = view.findViewById(R.id.more_course_Tv);
        teacherNameTv = view.findViewById(R.id.more_teacher_Tv);
        termTV = view.findViewById(R.id.more_term_Tv);
        schoolDepartmentTV = view.findViewById(R.id.cloud_school_Tv);
        classIntructionTV = view.findViewById(R.id.class_intruction_Tv);
        exitDismissBtn = view.findViewById(R.id.exit_dismiss_btn);
        permitaddClassCB = view.findViewById(R.id.cb_permit_addclass);

        classNameTV.setText(ClassTabActivity.courseName);
        termTV.setText(ClassTabActivity.term);
        teacherNameTv.setText(ClassTabActivity.classId);
        permitaddClassCB.setOnCheckedChangeListener( this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishclass();
            }
        });
        return view;
    }


    public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
        // TODO Auto-generated method stub
        switch (checkBox.getId()) {
            case R.id.cb_permit_addclass:
                if (checked) {// 选中吃
                    ischecked();
                    AlertDialogUtil.showToastText("选中", getActivity());
                } else {
                    dischecked();
                    //like.remove("eat");
                    AlertDialogUtil.showToastText("未选中", getActivity());
                }
                break;
            default:
                break;
        }
    }

    private void ischecked()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("cNumber",ClassTabActivity.classId );
                json.put("can_join","1");

                OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.PERMIT_JOIN_CLASS),json, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("LoginInfo", responseBodyStr);
                        com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
                        System.out.println( messjsonObject.get("message").toString());
                        if (messjsonObject.get("message").toString().equals("Ok"))
                        {
                            AlertDialogUtil.showToastText("允许加入班课", getActivity());
                        }else
                            {
                                AlertDialogUtil.showToastText("允许加入班课失败", getActivity());
                            }
                    }
                });
            }
        }).start();
    }

    private void dischecked()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("cNumber",ClassTabActivity.classId );
                json.put("can_join","0");

                OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.PERMIT_JOIN_CLASS),json, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("LoginInfo", responseBodyStr);
                        com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
                        System.out.println( messjsonObject.get("message").toString());
                        if (messjsonObject.get("message").toString().equals("Ok"))
                        {
                            AlertDialogUtil.showToastText("已禁止加入班课", getActivity());
                        }else
                        {
                            AlertDialogUtil.showToastText("禁止加入班课失败", getActivity());
                        }
                    }
                });
            }
        }).start();
    }
    private void finishclass()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("cNumber",ClassTabActivity.classId );
                json.put("state","0");

                OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.PERMIT_JOIN_CLASS),json, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("LoginInfo", responseBodyStr);
                        com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
                        System.out.println( messjsonObject.get("message").toString());
                        if (messjsonObject.get("message").toString().equals("Ok"))
                        {
                            AlertDialogUtil.showToastText("已结束班课", getActivity());
                        }else
                        {
                            AlertDialogUtil.showToastText("结束班课失败", getActivity());
                        }
                    }
                });
            }
        }).start();
    }

}