package org.zhonghao.gps.utils;

import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.zhonghao.gps.R;

/**
 * Created by Administrator on 2017/3/20.
 */

public class ProgressUtils {
    private static Dialog mProgressDialog=null;
    public static void showProgress(Context context){
        mProgressDialog=new Dialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_layout, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.progress_linear);// 加载布局
        mProgressDialog.setContentView(layout,new ViewGroup.LayoutParams(350,350));//自定义布局,
    }
    public static void hideProgress(){
        mProgressDialog.cancel();
    }

}
