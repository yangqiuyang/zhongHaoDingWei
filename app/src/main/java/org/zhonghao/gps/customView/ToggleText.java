package org.zhonghao.gps.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.icu.text.DisplayContext;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.zhonghao.gps.R;
import org.zhonghao.gps.utils.WarnIPopupWindow;

/**
 * Created by Administrator on 2017/huoche/15.
 */

public class ToggleText extends CheckBox {
    public WarnIPopupWindow pop;
    public ToggleText(Context context) {
        this(context,null);
    }
    public ToggleText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    String unSelectedText;
    String selectedText;
    public ToggleText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        pop=new WarnIPopupWindow(context);
        TypedArray array= context.obtainStyledAttributes(attrs,R.styleable.ToggleText);
         selectedText=array.getString(R.styleable.ToggleText_selectedText);
         unSelectedText=array.getString(R.styleable.ToggleText_unSelectedText);
         this.setText(unSelectedText);
        // isSelected=array.getBoolean(R.styleable.ToggleText_isSelected,true);
         array.recycle();
         initListener();
    }

    private void initListener() {
        this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                if(!isChecked){
                    setText(unSelectedText);
                    pop.dismiss();
                }
                else {
                    setText(selectedText);
                    pop.setAnimationStyle(R.style.pop_show);
                    pop.showAtLocation(v, Gravity.BOTTOM,0,0);
                }
            }
        });
    }

    public WarnIPopupWindow getPop() {
        return pop;
    }

    public void setPop(WarnIPopupWindow pop) {
        this.pop = pop;
    }
}
