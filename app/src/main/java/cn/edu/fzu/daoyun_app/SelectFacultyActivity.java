package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;



import java.util.ArrayList;
import java.util.List;

import cn.edu.fzu.daoyun_app.adapter.TreeAdapter;
import cn.edu.fzu.daoyun_app.bean.TreeBean;

public class SelectFacultyActivity extends AppCompatActivity {
    RecyclerView treeRecycleView;
    TreeAdapter treeAdapter;
    List<TreeBean> list;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_faculty);
        //获取树的内容


        backBtn = findViewById(R.id.toolbar_left_btn);
        backBtn.setOnClickListener(v -> finish());

        treeRecycleView = (RecyclerView) findViewById(R.id.tree_recycle);
        //设置层级管理
        treeRecycleView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        initData();
        treeAdapter = new TreeAdapter(this, list);
        treeRecycleView.setAdapter(treeAdapter);
    }

    private void initData() {
        List<String> schoolList = TreeAdapter.getShcoolList();
        //TODO 后续使用院系接口
        for (int i = 0; i < schoolList.size(); i++) {
            TreeBean bean = new TreeBean();
            bean.setTrunk1(schoolList.get(i));
            list.add(bean);
        }
    }
}