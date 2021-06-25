package cn.edu.fzu.daoyun_app.Utils;

import android.app.Activity;
import android.widget.Toast;


import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.zxing.common.BitmapUtils;

/**
 * 常见的弹框工具类
 */
public class AlertDialogUtil {

    /**
     * 添加确认框
     *
     * @param msg      提示消息
     * @param activity 创建提示的框
     */
    public static void showConfirmClickAlertDialog(final String msg, Activity activity) {
        activity.runOnUiThread(
                () -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                            .setMessage(msg)
                            .setPositiveButton("确定", null);
                    builder.show();
                });
    }

    /**
     * 添加确认框带有监听事件
     *
     * @param msg      提示消息
     * @param activity 创建提示的框
     */
    public static void showConfirmClickAlertDialogWithLister(final String msg, Activity activity, DialogInterface.OnClickListener listener) {
        activity.runOnUiThread(
                () -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                            .setMessage(msg)
                            .setPositiveButton("确定", listener);
                    builder.show();
                });
    }

    /**
     * 添加确认框带有监听事件
     *
     * @param msg      提示消息
     * @param activity 创建提示的框
     */
    public static void showConfirmClickAlertDialogTwoButtonWithLister(final String msg, Activity activity, DialogInterface.OnClickListener listener) {
        activity.runOnUiThread(
                () -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                            .setMessage(msg)
                            .setNegativeButton("确定", listener)
                            .setPositiveButton("取消", null);
                    builder.show();
                });
        //.setNeutralButton("Neutral", listener)("取消", null); 中间按钮
    }


    /**
     * 展示广播文本框
     *
     * @param msg      提示消息
     * @param activity 创建提示的框的act
     */
    public static void showToastText(final String msg, Activity activity) {

        activity.runOnUiThread(
                () -> {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                });
    }

}
