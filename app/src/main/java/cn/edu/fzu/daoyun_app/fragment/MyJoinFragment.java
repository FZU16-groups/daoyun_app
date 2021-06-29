package cn.edu.fzu.daoyun_app.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fzu.daoyun_app.ClassTabActivity;
import cn.edu.fzu.daoyun_app.Course;
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


public class MyJoinFragment extends Fragment {

    public List<Course> courseList = new ArrayList<>();
    public CourseAdapter adapter;
    public ListView listView;
    private int myJoinNum = 0;
    public ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_join,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        initCourses();
        listView = getActivity().findViewById(R.id.list_view);
    }

    private void initCourses(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("peId",MainActivity.peid );
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

                Request request = new Request.Builder()
                        .url("http://47.98.151.20:8080/daoyun_service/addedCourse.do")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        Toast.makeText(getContext(), "Connection failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("initCourse", responseBodyStr);
                        com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
                        if (messjsonObject.get("message").toString().equals("用户未加入班课"))
                        {
                            progressDialog.dismiss();
                        }else {
                        courseList = parseJsonWithJsonObject(responseBodyStr);
                        afterAction();}
                    }
                });
            }
        }).start();


    }

    private void afterAction(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new CourseAdapter(getContext(), R.layout.course_item, courseList,1);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Course course = courseList.get(position);
                       // 进入课程后设置
                        Intent intent = new Intent(getContext(), ClassTabActivity.class);
                        intent.putExtra("courseName", course.getCourseName());
                        intent.putExtra("classId", course.getClassId());
                        intent.putExtra("enterType", "join");
                       //intent.putExtra("teacherPhone", course.getTeacherPhone());
                        startActivity(intent);
                    }
                });
                progressDialog.dismiss();
            }
        });
    }

    private List<Course> parseJsonWithJsonObject(String jsonData){
        try{
//            File classFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/"
//                    + MainActivity.phoneNumber + "_join.json");
//            if(classFile.exists()){
//                classFile.delete();
//            }
//            classFile.createNewFile();
//            byte[] bt = new byte[4096];
//            bt = jsonData.getBytes();
//            FileOutputStream out = new FileOutputStream(classFile);
//            out.write(bt, 0, bt.length);
//            out.close();
            JSONObject obj = new JSONObject(jsonData);
            JSONObject objLocation = obj.getJSONObject("data");
            JSONArray jsonArray = objLocation.getJSONArray("personCourseList");
           // JSONArray jsonArray = new JSONArray(jsonData);
            List<Course> cList = new ArrayList<Course>();
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String classId = jsonObject.getString("cNumber");
                final String term = jsonObject.getString("term");
                final String className = jsonObject.getString("cName");
                final String teacherName = jsonObject.getString("teacher");
                final String gradeClass = "gradeClass";
                final Course[] course = {null};
                File classIconFile = null;
                //if(classIcon.equals("")){
                    if(teacherName == null){
                        course[0] = new Course(R.drawable.course_img_1, className, "", gradeClass, classId,term);
                    }else{
                        course[0] = new Course(R.drawable.course_img_1, className, teacherName, gradeClass, classId,term);
                    }
                cList.add(course[0]);
            }
            Log.i("LoginInfo", cList.size()+"");
            return cList;
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }


}
