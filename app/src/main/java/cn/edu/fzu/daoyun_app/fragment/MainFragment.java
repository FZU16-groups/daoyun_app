package cn.edu.fzu.daoyun_app.fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
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

import cn.edu.fzu.daoyun_app.Course;
import cn.edu.fzu.daoyun_app.CreateClassActivity;
import cn.edu.fzu.daoyun_app.MainActivity;
import cn.edu.fzu.daoyun_app.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,null);
        addTV = view.findViewById(R.id.toolbar_right_tv);
        // Inflate the layout for this fragment
        return view;
       // return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();

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
                .add(R.id.container_content_layout,myCreateFragment)
           //     .add(R.id.container_content_layout,myJoinFragment)
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

//        add_btn = (Button) activity.findViewById(R.id.toolbar_right_btn);
//        add_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupMenu(add_btn);
//            }
//        });

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
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MainFragInfo", requestCode + " " + resultCode + " " + "gradeClass");
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
//            BitmapDrawable bitmapDrawable = getImageDrawable(data.getStringExtra("classIcon"));
//            Course course = new Course(R.drawable.course_img_1, data.getStringExtra("className"),
//                    MainActivity.name, data.getStringExtra("gradeClass"));
//            MyCreateFragment.courseList.add(course);
            String classId = data.getStringExtra("classId");
            String classIcon = data.getStringExtra("classIcon");
            String className = data.getStringExtra("className");
            String gradeClass = data.getStringExtra("gradeClass");
            Log.i("MainFragInfo", classIcon + " " + className + " " + gradeClass);
            myCreateTV.setTextColor(Color.parseColor("#17c98b"));
            myJoinTV.setTextColor(Color.parseColor("#80000000"));
            myCreateView.setVisibility(View.VISIBLE);
            myJoinView.setVisibility(View.INVISIBLE);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .show(myCreateFragment)
                    .hide(myJoinFragment)
                    .commit();
            if(classIcon.equals("")){
                myCreateFragment.courseList.add(new Course(R.drawable.course_img_1, className, MainActivity.name, gradeClass, classId));
//                myCreateFragment.adapter.notifyDataSetChanged();
            }else{
                myCreateFragment.courseList.add(new Course(classIcon, className, MainActivity.name, gradeClass, classId));
//                myCreateFragment.adapter.notifyDataSetChanged();
            }
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
                                switch (text)
                                {
                                    case "输入班课号加入班课":
                                        new XPopup.Builder(getContext())
                                                .hasStatusBarShadow(false)
                                                //.dismissOnBackPressed(false)
                                                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                                .autoOpenSoftInput(true)
                                                // .isDarkTheme(true)
                                              //  .setPopupCallback(new DemoXPopupListener())
                                                //.moveUpToKeyboard(false)   //是否移动到软键盘上面，默认为true
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
    private void joinClass(final String classStr){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient okHttpClient = new OkHttpClient();
                String str = "phoneNumber=" + MainActivity.phoneNumber + "&classNumber=" + classStr;
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
                        str);
//                final RequestBody requestBody = new FormBody.Builder()
//                        .add("phoneNumber", MainActivity.phoneNumber)
//                        .add("classNumber", classStr)
//                        .build();
                final Request request = new Request.Builder()
                        .url("http://47.98.236.0:8080/joinclass")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        String responseBodyStr = response.body().string();
                        String responseBodyStr = new String(response.body().bytes(), "utf-8");
                        if (responseBodyStr.equals("class_not_exists")) {
                            showAlertDialog("加入班课失败！此班课不存在！");
                        }else if(responseBodyStr.equals("have_joined")){
                            showAlertDialog("你已经加入此班课，请勿重复加入！");
                        }else{
                            try {
                                JSONObject jsonObject = new JSONObject(responseBodyStr);
                                final String classId = jsonObject.getString("classId");
                                final String className = jsonObject.getString("className");
                                final String teacherName = jsonObject.getString("teacherName");
                                final String classIcon = jsonObject.getString("classIcon");
                                final String gradeClass = jsonObject.getString("gradeClass");

                                final String school = jsonObject.getString("schoolDepartment");
                                final String term = jsonObject.getString("term");
                                final String classIntruction = jsonObject.getString("classIntruction");

                                joinClassAlertDialog(classId, className, classIcon, gradeClass, teacherName, term, school, classIntruction);
                                Log.i("MainFragInfo1", responseBodyStr);

                                if(classIcon == null){
                                    Course course = new Course(R.drawable.course_img_1, className, teacherName, gradeClass, classId);
                                    myJoinFragment.courseList.add(course);
                                    myJoinFragment.adapter = new CourseAdapter(getContext(), R.layout.course_item, myJoinFragment.courseList);
                                    setAdapter();
                                }else{
                                    final File classIconFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/"
                                            + classIcon);
                                    if(!classIconFile.exists()){
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                OkHttpClient okHttpClient1 = new OkHttpClient();
                                                RequestBody requestBody1 = new FormBody.Builder()
                                                        .add("type", "classicon")
                                                        .add("icon", classIcon)
                                                        .build();
                                                Request request1 = new Request.Builder()
                                                        .url("http://47.98.236.0:8080/downloadicon")
                                                        .post(requestBody1)
                                                        .build();
                                                okHttpClient1.newCall(request1).enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                                    }

                                                    @Override
                                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                        FileOutputStream os = new FileOutputStream(classIconFile);
                                                        byte[] BytesArray = response.body().bytes();
                                                        os.write(BytesArray);
                                                        os.flush();
                                                        os.close();
                                                        Course course1;
                                                        if(teacherName == null){
                                                            course1 = new Course(classIconFile.getAbsolutePath(), className, "", gradeClass, classId);
                                                        }else{
                                                            course1 = new Course(classIconFile.getAbsolutePath(), className, teacherName, gradeClass, classId);
                                                        }
                                                        myJoinFragment.courseList.add(course1);
                                                        myJoinFragment.adapter = new CourseAdapter(getContext(), R.layout.course_item, myJoinFragment.courseList);
                                                        setAdapter();
                                                    }
                                                });
                                            }
                                        }).start();
                                    }else{
                                        Course course = new Course(classIconFile.getAbsolutePath(), className, teacherName, gradeClass, classId);
                                        myJoinFragment.courseList.add(course);
                                        myJoinFragment.adapter = new CourseAdapter(getContext(), R.layout.course_item, myJoinFragment.courseList);
                                        setAdapter();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public void joinClassAlertDialog(final String classId, final String className, final String classIcon, final String gradeClass, final String teacherName,
                                     final String term, final String school, final String classIntruction){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setMessage("成功加入" + className +"班课！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File classFile = new File(Environment.getExternalStorageDirectory()
                                        + "/daoyun/" + MainActivity.phoneNumber + "_join.json");
                                try {
                                    FileInputStream in = new FileInputStream(classFile);
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                                                            byte[] bt = new byte[4096];
                                    String classJsonStr = reader.readLine();
//                                                            Log.i("CreateClassInfo", classJsonStr+" "+"hello");
                                    JSONArray classJsonArray = new JSONArray(classJsonStr);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("classId", classId);
                                    jsonObject.put("className",className);
                                    jsonObject.put("classIcon", classIcon);
                                    jsonObject.put("gradeClass", gradeClass);
                                    jsonObject.put("teacherName", teacherName);
                                    jsonObject.put("school", school);
                                    jsonObject.put("term", term);
                                    jsonObject.put("classIntruction", classIntruction);
//                                                            Log.i("CreateClassInfo", jsonObject.toString());
                                    classJsonArray.put(jsonObject);
                                    if(classFile.exists()){
                                        classFile.delete();
                                    }
//                                                            Log.i("CreateClassInfo", classJsonArray.get(classJsonArray.length()-1).toString());
                                    FileOutputStream out = new FileOutputStream(classFile);
                                    out.write(classJsonArray.toString().getBytes("utf-8"));
                                    out.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                builder.show();
            }
        });
    }

    public void setAdapter(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myJoinFragment.listView.setAdapter(myJoinFragment.adapter);
            }
        });
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
}