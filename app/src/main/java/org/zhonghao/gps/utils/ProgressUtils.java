package org.zhonghao.gps.utils;

import android.animation.PropertyValuesHolder;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.zhonghao.gps.R;

/**
 * Created by Administrator on 2017/3/20.
 */

public class ProgressUtils {
    private static ProgressUtils mProgressUtils=null;
    private static  ProgressDialog mProgressDialog=null;
    private ProgressUtils(Context context){
        mProgressDialog=new ProgressDialog(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.progress_layout, null);
       // mProgressDialog.setCancelable(true);//是否可以被取消
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//圆环风格
        mProgressDialog.setContentView(inflate);//自定义布局
        mProgressDialog.getWindow().getAttributes().gravity= Gravity.CENTER;
        mProgressDialog.getWindow().getAttributes().alpha=0.7f;
    }
    public static ProgressUtils getProgress(Context context){
        if(mProgressUtils==null){
            mProgressUtils=new ProgressUtils(context);
        }
        return mProgressUtils;
    }
    public static void showProgress(){
        mProgressDialog.show();
    }
    public static void hideProgress(){
        mProgressDialog.hide();
    }

}
