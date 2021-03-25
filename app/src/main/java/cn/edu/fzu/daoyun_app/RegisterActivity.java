package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    protected Button sendCodeBtn;
    private int verificationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sendCodeBtn = findViewById(R.id.bt_veri_submit);
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                verificationCode = r.nextInt(899999) + 100000;
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("验证码")
                        .setMessage("验证码为：" + verificationCode)
                        .setPositiveButton("确定", null);
                builder.show();
                sendCodeBtn.setText("已发送");
                sendCodeBtn.setEnabled(false);
            }
        });
    }
}