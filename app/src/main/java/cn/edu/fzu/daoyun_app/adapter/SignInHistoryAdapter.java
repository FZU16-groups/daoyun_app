package cn.edu.fzu.daoyun_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

import cn.edu.fzu.daoyun_app.ClassTabActivity;
import cn.edu.fzu.daoyun_app.R;
import cn.edu.fzu.daoyun_app.SignInHistory;
import cn.edu.fzu.daoyun_app.holder.SignInHistoryViewHolder;

public class SignInHistoryAdapter extends ArrayAdapter<SignInHistory> {
    //课程ID
    private int resourceId;

    public SignInHistoryAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<SignInHistory> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SignInHistory his = getItem(position);
        final View view;
        final SignInHistoryViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new SignInHistoryViewHolder();
            viewHolder.consistTv = view.findViewById(R.id.his_consist_Tv);
            viewHolder.datetypeTv = view.findViewById(R.id.his_datetype_Tv);
            viewHolder.isFinishTv = view.findViewById(R.id.his_isFinish_Tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (SignInHistoryViewHolder) view.getTag();
        }

        if (ClassTabActivity.enterType.equals("create")) {
            viewHolder.isFinishTv.setText(his.getSigninpeople());
            viewHolder.consistTv.setText(his.getConDate());
            viewHolder.datetypeTv.setText(his.getDateType());
            if (his.getEndTime().equals("-1"))
            {

                viewHolder.consistTv .setText(his.getStartminyte() + "-" + "未结束");
            }
            else {
                viewHolder.consistTv .setText(his.getStartminyte() + "-" + his.getEndTime());
            }     //学生历史签到记录需要添加缺勤之类
        } else {
            String f =  "已签到" ;
            viewHolder.isFinishTv.setText(f);
            viewHolder.consistTv.setText(his.getValue()+"经验");
            viewHolder.datetypeTv.setText(his.getDateType());
        }
        return view;
    }
}
