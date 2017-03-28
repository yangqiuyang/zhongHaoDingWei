package org.zhonghao.gps.utils;

import android.view.View;

/**
 * Created by Administrator on 2017/3/27.
 */

public class MyOnClickListener implements View.OnClickListener {
    public int mPosition;
    public MyOnClickListener(int position){
        this.mPosition=position;
    }
    @Override
    public void onClick(View v) {

    }
}
