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

import cn.edu.fzu.daoyun_app.ClassTabActivity;
import cn.edu.fzu.daoyun_app.Config.GConfig;
import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Course;
import cn.edu.fzu.daoyun_app.MainActivity;
import cn.edu.fzu.daoyun_app.R;
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
                Intent intent = new Intent(getContext(), ClassTabActivity.class);
                intent.putExtra("courseName", course.getCourseName());
                intent.putExtra("classId", course.getClassId());
                intent.putExtra("enterType", "create");
                intent.putExtra("term", course.getCourseTerm());
                startActivity(intent);
            }
        });
    }

    private void initCourses(){
        new Thread(new Runnable() {
            @Override

            public void run() {

                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("peId",MainActivity.peid );
                OkHttpUtil.getInstance().PostWithJsonWithToken(UrlConfig.getUrl(UrlConfig.UrlType.CREATE_CLASS),json, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        String responseBodyStr = response.body().string();
                     //   com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
                        Log.i("responsecreatedCourse", responseBodyStr);
                        if (responseBodyStr.contains("用户未创建班课"))
                        {
                            progressDialog.dismiss();

                        }else {
                        final List<Course> temp_courseList = parseJsonWithJsonObject(responseBodyStr);
                        afterAction(temp_courseList);
                        Log.i("courselistinfo", GConfig.CLASSNAMES.toString());
                        }

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
        try{
            JSONObject obj = new JSONObject(jsonData);
            JSONObject objLocation = obj.getJSONObject("data");
            JSONArray jsonArray = objLocation.getJSONArray("personCourseList");
            //            JSONObject objLocation = obj.getJSONObject("data");
            //JSONArray jsonArray =  new JSONArray(jsonData);
            Log.i("myCreateFragInfo", jsonData);
            final List<Course> cList = new ArrayList<Course>();
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String classId = jsonObject.getString("cNumber");
                final String term=jsonObject.getString("term");
                final String className = jsonObject.getString("cName");
                GConfig.CLASSNAMES.add(className);
//                final String gradeClass = jsonObject.getString("gradeClass");
//                final String path = jsonObject.getString("classIcon");
                final Course course;
          //      if(path.equals("")){
//                    if(MainActivity.name == null){
//                        course = new Course(R.drawable.course_img_1, className, "", gradeClass, classId);
//                    }else{
//                        course = new Course(R.drawable.course_img_1, className, MainActivity.name, gradeClass, classId);
//                    }
                course = new Course(R.drawable.course_img_3,term, className, classId);
                cList.add(course);


            }
            Log.i("LoginInfo", cList.size()+"");
            return cList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}