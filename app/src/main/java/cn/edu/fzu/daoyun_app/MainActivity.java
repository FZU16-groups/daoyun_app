package cn.edu.fzu.daoyun_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Utils.AlertDialogUtil;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import cn.edu.fzu.daoyun_app.adapter.CourseAdapter;
import cn.edu.fzu.daoyun_app.fragment.MainFragment;
import cn.edu.fzu.daoyun_app.fragment.MeFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class
MainActivity extends AppCompatActivity  implements View.OnClickListener{

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
    public static String peid="";
    public static String role="";
    public int BUFFER_SIZE = 8192;
    private final static int REQ_CODE = 1028;
    private Context mContext;
    BasePopupView popupView;


    private ImageView mImage;
    private TextView mTvResult;
    private ImageView mImageCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.finishAll();
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        initView();
        mContext = this;
        //获取管理类
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_content,mMainFragment)
//                .add(R.id.container_content,mMeFragment)
 //               .hide(mMeFragment)
//                //事物添加  默认：显示首页  其他页面：隐藏
//                //提交
                .commit();
    }

    /**
     * 初始化视图
     */
    public void initView(){
        mMenuMain= this.findViewById(R.id.menu_main);
        mMenuFind= this.findViewById(R.id.menu_san);
        mMenuMe= this.findViewById(R.id.menu_me);
        mainImageView = this.findViewById(R.id.Iv_main);
        findImageView = this.findViewById(R.id.Iv_san);
        meImageView = this.findViewById(R.id.Iv_me);
        mainTV = this.findViewById(R.id.main_Tv);
        findTV = this.findViewById(R.id.san_Tv);
        meTV = this.findViewById(R.id.me_Tv);

        mMenuMain.setOnClickListener(this);
        mMenuFind.setOnClickListener(this);
        mMenuMe.setOnClickListener(this);

        mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_education));
        mainTV.setTextColor(Color.parseColor("#17c98b"));
    }


    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.menu_main://首页
//                this.getSupportFragmentManager()
//                        .beginTransaction()
//                        .show(mMainFragment)
//                        .hide(mMeFragment)
//                        .commit();
                mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_education));
                findImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_scan2));
                meImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_person2));
                mainTV.setTextColor(Color.parseColor("#17c98b"));
                findTV.setTextColor(Color.parseColor("#000000"));
                meTV.setTextColor(Color.parseColor("#000000"));
                break;

            case  R.id.menu_san://扫码
//                this.getSupportFragmentManager()
//                        .beginTransaction()
//                        .hide(mMainFragment)
//                        .hide(mMeFragment)
//                        .commit();
                Intent intent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(intent, REQ_CODE);
                mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_education2));
                findImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_scan));
                meImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_person2));
                mainTV.setTextColor(Color.parseColor("#000000"));
                findTV.setTextColor(Color.parseColor("#17c98b"));
                meTV.setTextColor(Color.parseColor("#000000"));
                break;
            case  R.id.menu_me://我的
//                this.getSupportFragmentManager()
//                        .beginTransaction()
//                        .hide(mMainFragment)
//                        .show(mMeFragment)
//                        .commit();
                mainImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_education2));
                findImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_scan2));
                meImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_person));
                mainTV.setTextColor(Color.parseColor("#000000"));
                findTV.setTextColor(Color.parseColor("#000000"));
                meTV.setTextColor(Color.parseColor("#17c98b"));
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            Bitmap bitmap = data.getParcelableExtra(CaptureActivity.SCAN_QRCODE_BITMAP);
            Log.i("扫码结果", result);
            joinClass(result);
        }

    }
     //二维码加入班级
    public void joinClass(String result)
    {


        popupView = new XPopup.Builder(mContext)
                .dismissOnBackPressed(true)
                .dismissOnTouchOutside(true)
                .isDestroyOnDismiss(true)

                .asConfirm("确定加入班级", result,
                        "取消", "确定",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                mMainFragment.joinClass(result);
                            }
                        }, null, false);
        popupView.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setTitle("确定加入班级："+result);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//               // String classStr = editText.getText().toString();
//                mMainFragment.joinClass(result);
//
//            }
//        });
//        builder.setNegativeButton("取消", null);
//        builder.show();

    }

}