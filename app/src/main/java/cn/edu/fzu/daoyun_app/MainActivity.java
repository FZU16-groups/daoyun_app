package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.edu.fzu.daoyun_app.fragment.MainFragment;
import cn.edu.fzu.daoyun_app.fragment.MeFragment;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    protected LinearLayout mMenuMain;
    protected LinearLayout mMenuFind;
    protected LinearLayout mMenuMe;
    protected ImageView mainImageView;
    protected ImageView findImageView;
    protected ImageView meImageView;
    protected TextView mainTV;
    protected TextView findTV;
    protected TextView meTV;
    protected MainFragment mMainFragment=new MainFragment();//首页
   // protected FindFragment mFindFragment=new FindFragment();//发现
    protected MeFragment mMeFragment=new MeFragment();//我的
    public static String userName;
    public static String icon;
    public static String loginType;
    public static String name = null;
    public static String phoneNumber="";
    public int BUFFER_SIZE = 8192;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.finishAll();
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        initView();
        //获取管理类
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_content,mMainFragment)
                .add(R.id.container_content,mMeFragment)
                .hide(mMeFragment)
                //事物添加  默认：显示首页  其他页面：隐藏
                //提交
                .commit();
    }

    /**
     * 初始化视图
     */
    public void initView(){
        mMenuMain= this.findViewById(R.id.menu_main);
        //mMenuFind= this.findViewById(R.id.menu_find);
        mMenuMe= this.findViewById(R.id.menu_me);
        mainImageView = this.findViewById(R.id.Iv_main);
       // findImageView = this.findViewById(R.id.Iv_find);
        meImageView = this.findViewById(R.id.Iv_me);
        mainTV = this.findViewById(R.id.main_Tv);
       // findTV = this.findViewById(R.id.find_Tv);
        meTV = this.findViewById(R.id.me_Tv);

        mMenuMain.setOnClickListener(this);
       // mMenuFind.setOnClickListener(this);
        mMenuMe.setOnClickListener(this);

        mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_main_click));
        mainTV.setTextColor(Color.parseColor("#008CC9"));
    }


    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.menu_main://首页
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMainFragment)
                        .hide(mMeFragment)
                        .commit();
                mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_main_click));
                meImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_me_normal));
                mainTV.setTextColor(Color.parseColor("#008CC9"));
                meTV.setTextColor(Color.parseColor("#000000"));
                break;
          
            case  R.id.menu_me://我的
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainFragment)
                        .show(mMeFragment)
                        .commit();
                mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_main_normal));
               // findImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_find_normal));
                meImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.nav_me_click));
                mainTV.setTextColor(Color.parseColor("#000000"));
              //  findTV.setTextColor(Color.parseColor("#000000"));
                meTV.setTextColor(Color.parseColor("#008CC9"));
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}