package cn.edu.fzu.daoyun_app.Utils;

import android.app.Activity;
import android.widget.Toast;

public class AlertDialogUtil {
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
