package org.zhonghao.gps.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.zhonghao.gps.R;

/**
 * Created by Administrator on 2017/huoche/14.
 */

public class WarnIPopupWindow extends PopupWindow {
    public TextView markRead;
    public TextView allMark;

    public TextView getAllMark() {
        return allMark;
    }

    public void setAllMark(TextView allMark) {
        this.allMark = allMark;
    }

    public TextView getMarkRead() {
        return markRead;
    }

    public void setMarkRead(TextView markRead) {
        this.markRead = markRead;
    }

    public WarnIPopupWindow(Context context) {
        this(context,null);
    }
    public WarnIPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    LayoutInflater inflater;
    public WarnIPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        View view = inflater.inflate(R.layout.read_status_mark, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        /*setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.GRAY));//设置窗体背景色*/
        markRead= (TextView) view.findViewById(R.id.mark_read);
        allMark= (TextView) view.findViewById(R.id.all_mark);
    }
    public void show(View view){
         setAnimationStyle(R.style.pop_show);
         showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
    public void hide(){
        dismiss();

    }
}
