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
    //??????????????????
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

       // Log.i("????????????",MainActivity.peid);

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
                "??????????????????????????????", "??????????????????????????????", "?????????????????????", "???????????????",
                "???????????????????????????", "????????????????????????", "?????????", "????????????", "?????????????????????", "?????????????????????",
                "????????????", "??????????????????????????????", "??????????????????", "???????????????????????????",};
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
//                        .setTitle("??????????????????")
//                        .setSingleChoiceItems(term, 0, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                selectedTerm = term[which];
//                            }
//                        });
//                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        termTV.setText(selectedTerm);
//                    }
//                });
//                builder.setNegativeButton("??????", null);
//                builder.show();
//            }
//        });

        createClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (classNameET.getText().toString().equals("")) {
                    showAlertDialog("?????????????????????");
                } else if (collegeTV.getText().toString().equals("")) {
                    showAlertDialog("????????????????????????");
                } else if (termTV.getText().toString().equals("?????????????????????")) {
                    showAlertDialog("????????????????????????");
                } else if (classIntroductionET.getText().toString().equals("")) {
                    showAlertDialog("????????????????????????");
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
                                Log.i("???????????????", e.getMessage());
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

        Time t = new Time(); // or Time t=new Time("GMT+8"); ??????Time Zone?????????
        t.setToNow(); // ?????????????????????
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        System.out.println("Calendar??????????????????" + year + "???" + month + "???" + day + "???" + hour + ":" + minute + ":" + second);
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
        //??????8???
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
                    .setTitle("??????????????????")
                    .setSingleChoiceItems(terms, 0, (dialog, which) -> selectedTerm = terms[which]);
            builder.setPositiveButton("??????", (dialog, which) -> termTV.setText(selectedTerm));
            builder.setNegativeButton("??????", null);
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
                }else if (requestCode == CreateClassActivity.SELECT_FACULTY) {//????????????
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
            // ??????????????????????????????
            intent.putExtra("crop", "true");
            // ?????????????????????????????????????????????????????????????????????
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // ????????????????????????
            intent.putExtra("scale", true);
            // ????????????????????????????????????????????????????????????????????????
            intent.putExtra("outputX", 1000);
            intent.putExtra("outputY", 1000);
            // ??????????????????
            cropFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/" + timeStamp + ".jpg");
//            cropFile = new File(path + File.separator + MainActivity.userName + ".jpg");
            if (cropFile.exists()) {
                cropFile.delete();
            }
            Uri cropImageUri = Uri.fromFile(cropFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
            // ?????????????????????????????????????????????????????????????????????
            //intent.putExtra("circleCrop", true);
            // ???????????????????????????
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // return-data=true????????????????????????????????????????????????????????????????????????onActivityResult????????????
            intent.putExtra("return-data", false);
            // ????????????????????????
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
                        .setPositiveButton("??????", null);
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