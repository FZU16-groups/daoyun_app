package cn.edu.fzu.daoyun_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitmapUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.fzu.daoyun_app.Config.GConfig;
import cn.edu.fzu.daoyun_app.Config.UrlConfig;
import cn.edu.fzu.daoyun_app.Utils.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateClassActivity extends AppCompatActivity {

    private ImageView classIconIV;
    private LinearLayout termLayout;
    private LinearLayout collegeLayout;
    private TextView termTV;
    private TextView collegeTV;
    private AutoCompleteTextView classNameET;
    private EditText classNumberET;

    private EditText classIntroductionET;
    private Button createClassBtn;
    private Button backBtn;
    private final int IMAGE_SELECT = 1;
    private final int IMAGE_CUT = 2;
    private File cropFile = null;
    private String selectedTerm;
    private String selectedCollege;
    //获取选择院校
    public static final int SELECT_FACULTY = 11002;
    Bitmap bitmap = null;
    ImageView imageView=null;
    private Context mContext;
    private String school;
    private String academy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

       // Log.i("传递的值",MainActivity.peid);

        termTV = findViewById(R.id.term_Tv);
        collegeTV=findViewById(R.id.college_Tv);
        classNameET = findViewById(R.id.class_name_Et);
        classNumberET = findViewById(R.id.class_number_Et);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, GConfig.CLASSNAMES);
        classNameET.setAdapter(adapter);
        Log.i("classNameET",GConfig.CLASSNAMES.toString());
        classNameET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                classNameET.showDropDown();
        });

        classNameET.setOnTouchListener((v, event) -> {
            classNameET.showDropDown();
            return false;
        });



        classIntroductionET = findViewById(R.id.class_introduction_Et);
        createClassBtn = findViewById(R.id.create_class_btn);
        backBtn = findViewById(R.id.toolbar_left_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mContext=this;
        imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);

        classIconIV = findViewById(R.id.class_icon_Iv);
        classIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, IMAGE_SELECT);
            }
        });
        initTermSelect();
        final String[] college = new String[]{"\n" +
                "电气工程与自动化学院", "数学与计算机科学学院", "环境与资源学院", "外国语学院",
                "建筑与城乡规划学院", "厦门工艺美术学院", "法学院", "海洋学院", "马克思主义学院", "经济与管理学院",
                "化学学院", "机械工程及自动化学院", "土木工程学院", "生物科学与工程学院",};
//        final String[] term = new String[]{"2016-2017-1", "2016-2017-2", "2017-2018-1", "2017-2018-2",
//                "2018-2019-1", "2018-2019-2", "2019-2020-1", "2019-2020-2", "2020-2021-1", "2020-2021-2",
//                "2021-2022-1", "2021-2022-2", "2022-2023-1", "2022-2023-2", "2023-2024-1", "2023-2024-2",
//                "2024-2025-1", "2024-2025-2", "2025-2026-1", "2025-2026-2", "2026-2027-1", "2026-2027-2",};
        collegeLayout=findViewById(R.id.college_layout);
        collegeLayout.setOnClickListener(v -> {

            Intent intent = new Intent(CreateClassActivity.this,SelectFacultyActivity.class);
            startActivityForResult(intent,CreateClassActivity.SELECT_FACULTY);

        });
//        termLayout = findViewById(R.id.term_layout);
//        termLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(CreateClassActivity.this)
//                        .setTitle("选择班课学期")
//                        .setSingleChoiceItems(term, 0, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                selectedTerm = term[which];
//                            }
//                        });
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        termTV.setText(selectedTerm);
//                    }
//                });
//                builder.setNegativeButton("取消", null);
//                builder.show();
//            }
//        });

        createClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (classNameET.getText().toString().equals("")) {
                    showAlertDialog("请输入班课名！");
                } else if (collegeTV.getText().toString().equals("")) {
                    showAlertDialog("请输入学校院系！");
                } else if (termTV.getText().toString().equals("班课学期未选择")) {
                    showAlertDialog("请选择班课学期！");
                } else if (classIntroductionET.getText().toString().equals("")) {
                    showAlertDialog("请输入班课简介！");
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String nowTime = GetTime();
                        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                        json.put("cNumber", nowTime);
                        json.put("cName", classNameET.getText().toString());
                        json.put("description", classIntroductionET.getText().toString());
                        json.put("term", termTV.getText().toString());
                        json.put("peId", MainActivity.peid);
                        json.put("classname", classNumberET.getText().toString());
                        OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.Create_Class), json, new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.i("错误的返回", e.getMessage());
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String responseBodyStr = response.body().string();
                                Log.i("CreateClassInfo", responseBodyStr);
                                GConfig.CLASSNAMES.add(classNameET.getText().toString());
                                Log.i("classnameinfo", GConfig.CLASSNAMES.toString());
                                com.alibaba.fastjson.JSONObject messjsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBodyStr);
                                Intent intent = new Intent();
                                intent.putExtra("term",termTV.getText().toString());
                                intent.putExtra("classId", nowTime);
                                intent.putExtra("className", classNameET.getText().toString());
                                intent.putExtra("creatclass","1");
                                setResult(RESULT_OK, intent);

                                finish();
                                //   }

                            }
                        });
                    }
                }).start();
            }

        });

    }
    private void initTermSelect() {

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        System.out.println("Calendar获取当前日期" + year + "年" + month + "月" + day + "日" + hour + ":" + minute + ":" + second);
//
//        final String[] term = new String[]{"2020-2021-1", "2020-2021-2",
//                "2021-2022-1", "2021-2022-2", "2022-2023-1", "2022-2023-2", "2023-2024-1", "2023-2024-2",
//                "2024-2025-1", "2024-2025-2", "2025-2026-1", "2025-2026-2", "2026-2027-1", "2026-2027-2",};
        int preYear, termID;
        //        if (month < 8 && month > 2) {
        if (month <= 6) {
            preYear = year - 1;
            termID = 2;
        } else {
            preYear = year;
            termID = 1;
        }
        //设置8个
        int sizeNum = termID == 2 ? 11 : 10;
        final String[] terms = new String[sizeNum];
        for (int i = 0; i < sizeNum; i++) {

            String term = String.format("%d-%d-%d", preYear, preYear + 1, termID);
            if (termID == 2) {
                preYear++;
                termID = 1;
            } else {
                termID++;
            }
            terms[i] = term;
        }
        termTV.setText(terms[0]);
        termLayout = findViewById(R.id.term_layout);
        termLayout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateClassActivity.this)
                    .setTitle("选择班课学期")
                    .setSingleChoiceItems(terms, 0, (dialog, which) -> selectedTerm = terms[which]);
            builder.setPositiveButton("确定", (dialog, which) -> termTV.setText(selectedTerm));
            builder.setNegativeButton("取消", null);
            builder.show();
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == IMAGE_CUT) {
                    Log.i("UserInfoInfo", Environment.getExternalStorageDirectory().toString());
//                    userIconIV.setImageURI(cropImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(cropFile.getAbsolutePath());
                    classIconIV.setImageBitmap(bitmap);
                    Log.i("UserInfoInfo", "cropFile.toString()");
                } else if (requestCode == IMAGE_SELECT) {
                    Uri iconUri = data.getData();
                    startCropImage(iconUri);
                }else if (requestCode == CreateClassActivity.SELECT_FACULTY) {//院系选择
                    school = data.getStringExtra("school");
                    academy = data.getStringExtra("academy");
                    collegeTV.setText(school + academy);
                }
                break;
            default:
                break;
        }
    }

    private void startCropImage(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            intent.setDataAndType(uri, "image/*");
            // 使图片处于可裁剪状态
            intent.putExtra("crop", "true");
            // 裁剪框的比例（根据需要显示的图片比例进行设置）
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // 让裁剪框支持缩放
            intent.putExtra("scale", true);
            // 裁剪后图片的大小（注意和上面的裁剪比例保持一致）
            intent.putExtra("outputX", 1000);
            intent.putExtra("outputY", 1000);
            // 传递原图路径
            cropFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/" + timeStamp + ".jpg");
//            cropFile = new File(path + File.separator + MainActivity.userName + ".jpg");
            if (cropFile.exists()) {
                cropFile.delete();
            }
            Uri cropImageUri = Uri.fromFile(cropFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
            // 设置裁剪区域的形状，默认为矩形，也可设置为原形
            //intent.putExtra("circleCrop", true);
            // 设置图片的输出格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // return-data=true传递的为缩略图，小米手机默认传递大图，所以会导致onActivityResult调用失败
            intent.putExtra("return-data", false);
            // 是否需要人脸识别
            intent.putExtra("noFaceDetection", true);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, IMAGE_CUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlertDialog(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateClassActivity.this)
                        .setMessage(msg)
                        .setPositiveButton("确定", null);
                builder.show();
            }
        });
    }
    public static String GetTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

}