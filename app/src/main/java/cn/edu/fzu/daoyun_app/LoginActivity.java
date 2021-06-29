package cn.edu.fzu.daoyun_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
        //mTabHost.getTabWidget().setStripEnabled(false);
       // setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
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
        // 设置权限
        int checkStorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int checkInternetPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int checkLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int checkReadStorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int checkReadMountPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        int checkCameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int checkPhonePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (checkStorePermission != PackageManager.PERMISSION_GRANTED
                || checkInternetPermission != PackageManager.PERMISSION_GRANTED
                || checkLocationPermission != PackageManager.PERMISSION_GRANTED
                || checkFineLocationPermission != PackageManager.PERMISSION_GRANTED
                || checkReadStorePermission != PackageManager.PERMISSION_GRANTED
                || checkReadMountPermission != PackageManager.PERMISSION_GRANTED
                || checkCameraPermission != PackageManager.PERMISSION_GRANTED
                || checkPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE}, 0);
        }

    }


}