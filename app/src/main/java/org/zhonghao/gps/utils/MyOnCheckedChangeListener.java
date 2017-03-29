package org.zhonghao.gps.utils;

import android.widget.CompoundButton;

/**
 * Created by Administrator on 2017/3/29.
 */

public class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
    public int mPosition;
   public MyOnCheckedChangeListener(int position){
        this.mPosition=position;
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
