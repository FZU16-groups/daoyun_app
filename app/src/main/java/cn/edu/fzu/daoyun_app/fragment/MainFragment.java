package cn.edu.fzu.daoyun_app.fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitmapUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Course;
import cn.edu.fzu.daoyun_app.CreateClassActivity;
import cn.edu.fzu.daoyun_app.CustomPopDialog2;
import cn.edu.fzu.daoyun_app.MainActivity;
import cn.edu.fzu.daoyun_app.R;
import cn.edu.fzu.daoyun_app.Utils.AlertDialogUtil;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import cn.edu.fzu.daoyun_app.adapter.CourseAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainFragment extends Fragment {
    //    protected Button add_btn;
    protected TextView addTV;
    protected TextView myCreateTV;
    protected TextView myJoinTV;
    protected View myCreateView;
    protected View myJoinView;
    protected MyCreateFragment myCreateFragment = new MyCreateFragment();
    protected MyJoinFragment myJoinFragment = new MyJoinFragment();
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        addTV = view.findViewById(R.id.toolbar_right_tv);
        // Inflate the layout for this fragment
        return view;
        // return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        this.mContext = getActivity();
        myCreateView = activity.findViewById(R.id.view_mycreate);
        myJoinView = activity.findViewById(R.id.view_myjoin);
        myCreateTV = activity.findViewById(R.id.myCreateTv);
        myJoinTV = activity.findViewById(R.id.joinedClassTv);
        addTV = activity.findViewById(R.id.toolbar_right_tv);
        myJoinTV.setTextColor(Color.parseColor("#17c98b"));
        myCreateView.setVisibility(View.INVISIBLE);
        Log.i("MainFragmentInfo", "test");
//        myJoinTV.setTextColor(Color.parseColor("#80000000"));

        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_content_layout, myCreateFragment)
                .add(R.id.container_content_layout, myJoinFragment)
                .hide(myCreateFragment)
                .commit();
        addTV.setEnabled(true);
        addTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainFragmentInfo", "add textview");
//                Toast.makeText(getContext(), "添加被按下", Toast.LENGTH_SHORT).show();

                showPopupMenu(addTV);
            }
        });


        myJoinTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myJoinTV.setTextColor(Color.parseColor("#17c98b"));
                myCreateTV.setTextColor(Color.parseColor("#80000000"));
                myJoinView.setVisibility(View.VISIBLE);
                myCreateView.setVisibility(View.INVISIBLE);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .show(myJoinFragment)
                        .hide(myCreateFragment)
                        .commit();
            }
        });

        myCreateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCreateTV.setTextColor(Color.parseColor("#17c98b"));
                myJoinTV.setTextColor(Color.parseColor("#80000000"));
                myCreateView.setVisibility(View.VISIBLE);
                myJoinView.setVisibility(View.INVISIBLE);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .show(myCreateFragment)
                        .hide(myJoinFragment)
                        .commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String creatclass = data.getStringExtra("creatclass");
        if (creatclass.equals("1")) {
            Bitmap bitmap = null;
            try {
                String classId = data.getStringExtra("classId");
                bitmap = BitmapUtils.create2DCode(classId);
                CustomPopDialog2.Builder dialogBuild = new CustomPopDialog2.Builder(this.getActivity());
                dialogBuild.setImage(bitmap);//显示二维码


                Log.i("kechenghao", classId);

                dialogBuild.setcNumber("创建成功!课程号：" + classId);//显示课程号
                CustomPopDialog2 dialog = dialogBuild.create();
                dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭

                dialog.show();
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MainFragInfo", requestCode + " " + resultCode + " " + "gradeClass");
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
//            BitmapDrawable bitmapDrawable = getImageDrawable(data.getStringExtra("classIcon"));
//            Course course = new Course(R.drawable.course_img_1, data.getStringExtra("className"),
//                    MainActivity.name, data.getStringExtra("gradeClass"));
//            MyCreateFragment.courseList.add(course);
            String term = data.getStringExtra("term");
            String classId = data.getStringExtra("classId");
            String classIcon = data.getStringExtra("classIcon");
            String className = data.getStringExtra("className");
            Log.i("MainFragInfo", classIcon + " " + className);
            myCreateTV.setTextColor(Color.parseColor("#17c98b"));
            myJoinTV.setTextColor(Color.parseColor("#80000000"));
            myCreateView.setVisibility(View.VISIBLE);
            myJoinView.setVisibility(View.INVISIBLE);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .show(myCreateFragment)
                    .hide(myJoinFragment)
                    .commit();
//            if(classIcon.equals("")){
//                myCreateFragment.courseList.add(new Course(R.drawable.course_img_1, className, MainActivity.name, gradeClass, classId));
////                myCreateFragment.adapter.notifyDataSetChanged();
//            }else{
//                myCreateFragment.courseList.add(new Course(classIcon, className, MainActivity.name, gradeClass, classId));
////                myCreateFragment.adapter.notifyDataSetChanged();
//            }
            myCreateFragment.courseList.add(new Course(R.drawable.course_img_1, term, className, classId));
            myCreateFragment.adapter.notifyDataSetChanged();
            myCreateFragment.adapter = new CourseAdapter(getContext(), R.layout.course_item, myCreateFragment.courseList, 2);
            myCreateFragment.listView.setAdapter(myCreateFragment.adapter);
        }
    }

    private void showPopupMenu(View view) {

        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asBottomList("", new String[]{"输入班课号加入班课", "创建班课"},
                        //  null, 2,//带选中效果
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (text) {
                                    case "输入班课号加入班课":
                                        new XPopup.Builder(getContext())
                                                .hasStatusBarShadow(false)
                                                //.dismissOnBackPressed(false)
                                                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                                .autoOpenSoftInput(true)
                                                .asInputConfirm("请输入七位班课号", null, null, "班课号",
                                                        new OnInputConfirmListener() {
                                                            @Override
                                                            public void onConfirm(String text) {
                                                                joinClass(text);

                                                                // Toast.makeText(getActivity().getApplicationContext(),GetTime(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                .show();
                                        break;
//
                                    case "创建班课":
                                        startActivityForResult(new Intent(getContext(), CreateClassActivity.class), 1);
                                        break;
                                }
                                // toast("click " + text);
                            }
                        })
                .show();

    }

    public static String GetTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

    public void joinClass(final String classStr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("peIdddddd", MainActivity.peid);
                final OkHttpClient okHttpClient = new OkHttpClient();
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("cNumber", classStr);
                json.put("peId", MainActivity.peid);
                OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.JOIN_CLASS), json, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("错误的返回", e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = new String(response.body().bytes(), "utf-8");
                        Log.i("CreateClassInfo", responseBodyStr);
                        if (responseBodyStr.contains("该班课不存在")) {
                            AlertDialogUtil.showToastText("加入班课失败！此班课不存在", getActivity());
                        } else if (responseBodyStr.contains("已加入该班课")) {
                            AlertDialogUtil.showToastText("你已经加入此班课，请勿重复加入！", getActivity());
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(responseBodyStr);
                                JSONObject jsonObject2 = jsonObject.getJSONObject("data").getJSONObject("course");
                                final String classId = jsonObject2.getString("cNumber");
                                final String term = jsonObject2.getString("term");
                                final String className = jsonObject2.getString("cName");
                                Course course;
                                if (jsonObject2.getString("peName").equals("")) {
                                    course = new Course(R.drawable.course_img_1, className, "", " gradeClass", classId, term);
                                } else {
                                    final String teacherName = jsonObject2.getString("peName");
                                    course = new Course(R.drawable.course_img_1, className, teacherName, " gradeClass", classId, term);
                                }
                                Log.i("MainFragInfo1", responseBodyStr);
                                myJoinFragment.courseList.add(course);
                                myJoinFragment.adapter = new CourseAdapter(getContext(), R.layout.course_item, myJoinFragment.courseList);
                                if (myJoinFragment.adapter != null)
                                    setAdapter();
                                AlertDialogUtil.showToastText("成功加入" + className + "班课！", getActivity());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }).start();
    }


    public void setAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myJoinFragment.listView.setAdapter(myJoinFragment.adapter);
            }
        });
    }

    public void showAlertDialog(final String msg) {
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


}