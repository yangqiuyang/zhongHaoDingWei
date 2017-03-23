package org.zhonghao.gps.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.zhonghao.gps.R;

/**
 * Created by lenovo on 2016/12/13.
 */

public class MyDialog extends Dialog implements View.OnClickListener{
    public static String  myTime;
    private Context context;
    private LeaveMyDialogListener listener;
    private TextView confirm,cancel;
    private DatePicker datePicker;



    public static String getMyTime() {
        return myTime;
    }

    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }


    public interface LeaveMyDialogListener{
        public void onClick(View view);
    }
    public MyDialog(Context context,LeaveMyDialogListener listener) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listener = listener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_selector_layout);
        confirm = (TextView) findViewById(R.id.btn_dialog_cancel);
        cancel = (TextView) findViewById(R.id.btn_dialog_confirm);
        datePicker = (DatePicker) findViewById(R.id.date_picker);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
        myTime = datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth();
    }
}
