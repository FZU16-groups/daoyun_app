package cn.edu.fzu.daoyun_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.edu.fzu.daoyun_app.fragment.DetailFragment;
import cn.edu.fzu.daoyun_app.fragment.MemberFragment;

public class ClassTabActivity extends AppCompatActivity implements View.OnClickListener {

    protected LinearLayout mMenuRes;
    protected LinearLayout mMenuMember;
    protected LinearLayout mMenuActivity;
    protected LinearLayout mMenuMessage;
    protected LinearLayout mMenuMore;
    protected ImageView resImageView;
    protected ImageView memberImageView;
    protected ImageView activityImageView;
    protected ImageView messageImageView;
    protected ImageView moreImageView;
    // protected ResFragment mResFragment = new ResFragment();
    protected MemberFragment mMemberFragment = new MemberFragment();
    //    protected ActivityFragment mActivityFragment = new ActivityFragment();
//    protected MessageFragment mMessageFragment = new MessageFragment();
    protected DetailFragment mdetailFragment = new DetailFragment();

    public static String courseName;
    public static String classId;
    public static String enterType;
    public static String teacherPhone;
    public static String term;

    public static int ADD_EXPER = 10511;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_tab);
        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        classId = intent.getStringExtra("classId");
        enterType = intent.getStringExtra("enterType");
        //teacherPhone = intent.getStringExtra("teacherPhone");
        term = intent.getStringExtra("term");
//        Log.v("classinfo",term);
        initView();
        this.getSupportFragmentManager()
                .beginTransaction()
                //  .add(R.id.container_class_fragment,mResFragment)
                .add(R.id.container_class_fragment, mMemberFragment)
//                .add(R.id.container_class_fragment,mActivityFragment)
//                .add(R.id.container_class_fragment,mMessageFragment)
                .add(R.id.container_class_fragment, mdetailFragment)
//                .hide(mResFragment)
                .hide(mMemberFragment)
//                .hide(mMessageFragment)
                .hide(mdetailFragment)
                //事物添加  默认：显示首页  其他页面：隐藏
                //提交
                .commit();
    }

    public void initView() {
        mMenuRes = findViewById(R.id.menu_res);
        mMenuMember = findViewById(R.id.menu_member);
        mMenuActivity = findViewById(R.id.menu_activity);
        mMenuMessage = findViewById(R.id.menu_message);
        mMenuMore = findViewById(R.id.menu_more);

        resImageView = findViewById(R.id.Iv_res);
        memberImageView = findViewById(R.id.Iv_member);
        activityImageView = findViewById(R.id.Iv_activity);
        messageImageView = findViewById(R.id.Iv_message);
        moreImageView = findViewById(R.id.Iv_more);

        mMenuRes.setOnClickListener(this);
        mMenuMember.setOnClickListener(this);
        mMenuActivity.setOnClickListener(this);
        mMenuMessage.setOnClickListener(this);
        mMenuMore.setOnClickListener(this);

        activityImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.mainn));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_res:
                this.getSupportFragmentManager()
                        .beginTransaction()
//                        .show(mResFragment)
                        .hide(mMemberFragment)
//                        .hide(mActivityFragment)
//                        .hide(mMessageFragment)
                        .hide(mdetailFragment)
                        .commit();
                resImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.folder2));
                memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.person));
                activityImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.mainn2));
                messageImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.emaill));
                moreImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.more));
                break;
            case R.id.menu_member:
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMemberFragment)
//                        .hide(mResFragment)
//                        .hide(mActivityFragment)
//                        .hide(mMessageFragment)
                        .hide(mdetailFragment)
                        .commit();
                resImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.folder));
                memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.person2));
                activityImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.mainn2));
                messageImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.emaill));
                moreImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.more));
                break;
            case R.id.menu_activity:
                this.getSupportFragmentManager()
                        .beginTransaction()
//                        .show(mActivityFragment)
//                        .hide(mResFragment)
                        .hide(mMemberFragment)
//                        .hide(mMessageFragment)
                        .hide(mdetailFragment)
                        .commit();
                resImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.folder));
                memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.person));
                activityImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.mainn));
                messageImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.emaill));
                moreImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.more));
                break;
            case R.id.menu_message:
                this.getSupportFragmentManager()
                        .beginTransaction()
//                        .show(mMessageFragment)
//                        .hide(mResFragment)
//                        .hide(mActivityFragment)
                        .hide(mMemberFragment)
                        .hide(mdetailFragment)
                        .commit();
                resImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.folder));
                memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.person));
                activityImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.mainn2));
                messageImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.emaill2));
                moreImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.more));
                break;
            case R.id.menu_more:
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mdetailFragment)
//                        .hide(mResFragment)
//                        .hide(mActivityFragment)
//                        .hide(mMessageFragment)
                        .hide(mMemberFragment)
                        .commit();
                resImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.folder));
                memberImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.person));
                activityImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.mainn2));
                messageImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.emaill));
                moreImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.more2));
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("requertinfo",requestCode+"");
        if (requestCode == ClassTabActivity.ADD_EXPER) {
            mMemberFragment.refresh();
        }
    }

}