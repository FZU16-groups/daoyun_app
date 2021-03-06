package cn.edu.fzu.daoyun_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 该自定义Dialog应用在：弹出框居中显示图片，点击其他区域自动关闭Dialog
 *
 */
public class CustomPopDialog2 extends Dialog {

    public CustomPopDialog2(Context context) {
        super(context);
    }

    public CustomPopDialog2(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private Bitmap image;
        private String cNumber;
        private Button exitButton;

        public Builder(Context context) {
            this.context = context;
        }

        public Bitmap getImage() {
            return image;
        }
        public String getcNumber(){return cNumber;}
        public void setcNumber(String cNumber){this.cNumber=cNumber;}

        public void setImage(Bitmap image) {
            this.image = image;
        }

        public CustomPopDialog2 create() {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomPopDialog2 dialog = new CustomPopDialog2(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog2, null);
            dialog.addContentView(layout, new LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            ImageView img = (ImageView)layout.findViewById(R.id.img_qrcode);
            img.setImageBitmap(getImage());
            exitButton = (Button)layout.findViewById(R.id.bt_dialog_submit);
            exitButton.setOnClickListener(v -> dialog.dismiss());
            TextView txt=(TextView) layout.findViewById(R.id.dialog_cNumber);
            txt.setText(getcNumber());
            return dialog;
        }
    }
}

