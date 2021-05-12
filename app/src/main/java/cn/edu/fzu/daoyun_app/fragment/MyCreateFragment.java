package cn.edu.fzu.daoyun_app.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

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


public class MyCreateFragment extends Fragment {

    public  static List<Course> courseList = new ArrayList<>();
    private int myJoinNum = 0;
    public CourseAdapter adapter;
    public ListView listView;
    public ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_create, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        initCourses();
        adapter = new CourseAdapter(getContext(), R.layout.course_item, courseList, 2);
        listView = getActivity().findViewById(R.id.list_view1);
        listView.setAdapter(adapter);
        //进入课程设置
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = courseList.get(position);
//
//                Intent intent = new Intent(getContext(), ClassTabActivity.class);
//                intent.putExtra("courseName", course.getCourseName());
//                intent.putExtra("classId", course.getClassId());
//                intent.putExtra("enterType", "create");
//                startActivity(intent);
            }
        });
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
//                Request request = new Request.Builder()
//                        .header("Content-Type", "application/json")
//                        .url("http://47.98.236.0:8080/daoyun_service/loginUser.do")
//                        .post(requestBody)
//                        .build();
//                OkHttpClient okHttpClient = new OkHttpClient();
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("phone", MainActivity.phoneNumber)
//                        .build();
                Request request = new Request.Builder()
                        .url("http://47.98.151.20:8080/daoyun_service/createdCourse.do")
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
                        Log.i("responseBodyStr", responseBodyStr);
                        final List<Course> temp_courseList = parseJsonWithJsonObject(responseBodyStr);
                       //Log.i("myCreateFragInfo", courseList.get(0).getCourseName());
                        afterAction(temp_courseList);

                    }
                });
            }
        }).start();

    }

    private void afterAction(final List<Course> temp_courseList){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                courseList = temp_courseList;
                adapter.notifyDataSetChanged();
                adapter = new CourseAdapter(getContext(), R.layout.course_item, courseList, 2);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
            }
        });
    }


    private List<Course> parseJsonWithJsonObject(String jsonData) throws IOException {
//        File classFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/"
//                + MainActivity.phoneNumber + ".json");
//        if(classFile.exists()){
//            classFile.delete();
//        }
//        classFile.createNewFile();
//        byte[] bt = new byte[4096];
//        bt = jsonData.getBytes();
//
//        FileOutputStream out = new FileOutputStream(classFile);
//        out.write(bt, 0, bt.length);
//        out.close();
        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            Log.i("myCreateFragInfo", jsonData);
            final List<Course> cList = new ArrayList<Course>();
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String classId = jsonObject.getString("pcId");
                final String term=jsonObject.getString("term");
                final String className = jsonObject.getString("cName");

//                final String gradeClass = jsonObject.getString("gradeClass");
//                final String path = jsonObject.getString("classIcon");
                final Course course;
          //      if(path.equals("")){
//                    if(MainActivity.name == null){
//                        course = new Course(R.drawable.course_img_1, className, "", gradeClass, classId);
//                    }else{
//                        course = new Course(R.drawable.course_img_1, className, MainActivity.name, gradeClass, classId);
//                    }
                course = new Course(R.drawable.course_img_1,term, className, classId);
                cList.add(course);
//                }else{
//                    final File imgFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/" + path);
//                    if(!imgFile.exists()){
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                OkHttpClient okHttpClient = new OkHttpClient();
//                                RequestBody requestBody = new FormBody.Builder()
//                                        .add("icon", path)
//                                        .add("type", "classicon")
//                                        .build();
//                                Request request = new Request.Builder()
//                                        .url("http://47.98.236.0:8080/downloadicon")
//                                        .post(requestBody)
//                                        .build();
//                                okHttpClient.newCall(request).enqueue(new Callback() {
//                                    @Override
//                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                                    }
//
//                                    @Override
//                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                                        File iconFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/" + path);
//                                        FileOutputStream os = new FileOutputStream(iconFile);
//                                        byte[] BytesArray = response.body().bytes();
//                                        os.write(BytesArray);
//                                        os.flush();
//                                        os.close();
//                                        Course course1;
//                                        if(MainActivity.name == null){
//                                            course1 = new Course(iconFile.getAbsolutePath(), className, "", gradeClass, classId);
//                                        }else{
//                                            course1 = new Course(iconFile.getAbsolutePath(), className, MainActivity.name, gradeClass, classId);
//                                        }
//                                        cList.add(course1);
//                                    }
//                                });
//                            }
//                        }).start();
//                    }else{
//                        if(MainActivity.name == null){
//                            course = new Course(imgFile.getAbsolutePath(), className, "", gradeClass, classId);
//                        }else{
//                            course = new Course(imgFile.getAbsolutePath(), className, MainActivity.name, gradeClass, classId);
//                        }
//                        cList.add(course);
//                    }
//                }

            }
            Log.i("LoginInfo", cList.size()+"");
            return cList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}