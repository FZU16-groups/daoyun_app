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

import cn.edu.fzu.daoyun_app.QRCodeDialog;

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

    /**
     * 弹出二维码
     *
     * @param qrcodecontent 二维码内容
     * @param activity      需要弹出的activity
     */
    public static void alertQRCode(String qrcodecontent, Activity activity) {
        activity.runOnUiThread(
                () -> {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapUtils.create2DCode(qrcodecontent);//根据内容生成二维码

                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        // 取得想要缩放的matrix参数.
                        Matrix matrix = new Matrix();
                        //设置xy 放大比例
                        matrix.postScale(2, 2);
                        // 得到新的图片.
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        Bitmap newbitmap = Bitmap.createBitmap(bitmap.getWidth(),
                                bitmap.getHeight(), bitmap.getConfig());
                        Canvas canvas = new Canvas(newbitmap);
                        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
                        canvas.drawBitmap(bitmap, 0, 0, paint);

                        QRCodeDialog.Builder dialogBuild = new QRCodeDialog.Builder(activity);
                        dialogBuild.setImage(newbitmap);
                      QRCodeDialog dialog = dialogBuild.create();
                        dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

}
