package cn.edu.fzu.daoyun_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.TabHost;
import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity extends TabActivity {

    //选项卡
    private TabHost mTabHost;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置全屏去除标题栏
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //直接获取到TabHost   getTabHost()相对于findViewById
        mTabHost = getTabHost();
        //初始化
        mTabHost.setup();

        TabHost.TabSpec tabSpec1 = mTabHost.newTabSpec("tab1").setIndicator("密码登陆").setContent(new Intent(this,PasLoginActivity.class));
        TabHost.TabSpec tabSpec2 = mTabHost.newTabSpec("tab2").setIndicator("验证码登陆").setContent(new Intent(this,VerLoginActivity.class));


        mTabHost.addTab(tabSpec1);
        mTabHost.addTab(tabSpec2);

        for (int i =0; i <mTabHost.getTabWidget().getChildCount(); i++) {
//	         //修改Tabhost高度和宽度
//			mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 30;
//			mTabHost.getTabWidget().getChildAt(i).getLayoutParams().width = 65;
            //修改显示字体大小
            TextView tv = (TextView)mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(18);
            //修改显示字体颜色
            tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
        }


    }


}