package cn.edu.fzu.daoyun_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity  extends AppCompatActivity {
    protected Handler mHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
//               startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        },2000);
    }
}
