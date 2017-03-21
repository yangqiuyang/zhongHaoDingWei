package org.zhonghao.gps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.zhonghao.gps.R;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.biz.ServelBiz;
import org.zhonghao.gps.entity.DevicesInfo;
import org.zhonghao.gps.entity.RequestQueryDevicesMoveInfo;
import org.zhonghao.gps.utils.ProgressUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MoveLocusActivity extends AppCompatActivity {
    private ArrayList<String> listDevices;
    public Boolean choose;
    public static String time;
    private String starttime, endtime, queryDeviceNum;
    private Date startDate, endDate;
    private Calendar myCalender;
    private Spinner date, num;
    private TextView start, end;
    private LinearLayout llstart, llend, selector, line1, line2, line3;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<DevicesInfo> devicesAdapter;
    private SimpleDateFormat sdf,sdf1;
    private DatePicker datePicker;
    private AlertDialog dialog;
    private Button confirm, cancel, query;
    private static final String[] name = {"最近一天", "最近一周", "最近一月", "自定义"};
    private ArrayList<DevicesInfo> device;
    private int myposition;
    private RequestQueryDevicesMoveInfo myQueryRequest = new RequestQueryDevicesMoveInfo();
    MyApplication myApplication;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_locus);
        setView();
        Intent intent = this.getIntent();
        myposition = intent.getIntExtra("position",0);
        myApplication = (MyApplication) this.getApplicationContext();
        handler = myApplication.getMyHandler();
        device = MyApplication.myDevice;
        queryDeviceNum = device.get(0).getDeviceID();
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String data = sdf1.format(new Date());
        endDate = new Date();
        endtime = sdf.format(endDate);
        myCalender = Calendar.getInstance();
        myCalender.setTime(endDate);
        myCalender.set(Calendar.DATE, myCalender.get(Calendar.DATE) - 1);
        try {
            startDate = sdf.parse(sdf.format(myCalender.getTime()));
            starttime = sdf.format(startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.sp_move_toolbar);
//        toolbar.addView(View.inflate(MapActivity.this,R.layout.list_layout,null),0);
        toolbar.setTitle("轨迹查询");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, name);
        devicesAdapter = new ArrayAdapter<DevicesInfo>(this, android.R.layout.simple_spinner_item, device);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devicesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date.setAdapter(adapter);
        num.setAdapter(devicesAdapter);
        num.setSelection(myposition);
        date.setOnItemSelectedListener(new MyItemClickListener());
        num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                queryDeviceNum = device.get(position).getDeviceID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        start.setText(data);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                choose = true;
            }
        });
        end.setText(data);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                choose = false;

            }
        });
        confirm.setOnClickListener(new MyDialogOnClickListener());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_query:
                        // 检查时间是否正确
                        if (startDate.before(endDate)) {
                            listDevices = new ArrayList<String>();
                            Log.d("serve", "" + queryDeviceNum);
                            listDevices.add(queryDeviceNum);
                            myQueryRequest.setUserinfo(MyApplication.nameDates.getUsername());
                            myQueryRequest.setDeviceID(listDevices);
                            myQueryRequest.setStartTime(starttime);
                            myQueryRequest.setEndTime(endtime);
                            ProgressUtils.showProgress(MoveLocusActivity.this);//加载轨迹数据进度条
                         /*   new Thread(new Runnable() {
                                @Override
                                public void run() {*/
                                    ServelBiz.getDvicesMoveLocus(myQueryRequest, MoveLocusActivity.this, handler);

                         /*       }
                            }).start();*/
                            finish();


                        } else {
                            Toast.makeText(MoveLocusActivity.this, "开始时间必须大于结束时间", Toast.LENGTH_SHORT).show();
                        }
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setView() {
        date = (Spinner) findViewById(R.id.sp_querydate);
        start = (TextView) findViewById(R.id.tv_query_start);
        end = (TextView) findViewById(R.id.tv_query_end);
        num = (Spinner) findViewById(R.id.sp_query_num);
        line1 = (LinearLayout) findViewById(R.id.line1);
        line2 = (LinearLayout) findViewById(R.id.line2);
        line3 = (LinearLayout) findViewById(R.id.line3);
        query = (Button) findViewById(R.id.btn_query);
        llstart = (LinearLayout) findViewById(R.id.ll_start);
        llend = (LinearLayout) findViewById(R.id.ll_end);
        selector = (LinearLayout) MoveLocusActivity.this.getLayoutInflater().inflate(R.layout.date_selector_layout, null);
        AlertDialog.Builder ad = new AlertDialog.Builder(MoveLocusActivity.this)
                .setView(selector);
        dialog = ad.create();
        confirm = (Button) selector.findViewById(R.id.btn_dialog_confirm);
        cancel = (Button) selector.findViewById(R.id.btn_dialog_cancel);
        datePicker = (DatePicker) selector.findViewById(R.id.date_picker);
    }


    class MyItemClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 3) {
                llstart.setVisibility(View.VISIBLE);
                llend.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                line3.setVisibility(View.VISIBLE);

            } else {
                llstart.setVisibility(View.GONE);
                llend.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                line3.setVisibility(View.GONE);
            }
            if (position == 0) {
                endDate = new Date();

                myCalender = Calendar.getInstance();
                myCalender.setTime(endDate);
                myCalender.set(Calendar.DATE, myCalender.get(Calendar.DATE) - 1);
                try {
                    startDate = sdf.parse(sdf.format(myCalender.getTime()));
                    endtime = sdf.format(endDate);
                    starttime = sdf.format(startDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (position == 1) {
                endDate = new Date();
                myCalender = Calendar.getInstance();
                myCalender.setTime(endDate);
                myCalender.set(Calendar.DATE, myCalender.get(Calendar.DATE) - 7);
                try {
                    startDate = sdf.parse(sdf.format(myCalender.getTime()));
                    endtime = sdf.format(endDate);
                    starttime = sdf.format(startDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (position == 2) {
                endDate = new Date();
                myCalender = Calendar.getInstance();
                myCalender.setTime(endDate);
                myCalender.set(Calendar.DATE, myCalender.get(Calendar.DATE) - 30);
                try {
                    startDate = sdf.parse(sdf.format(myCalender.getTime()));
                    endtime = sdf.format(endDate);
                    starttime = sdf.format(startDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class MyDialogOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            time = datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
            if (choose) {
                start.setText(time);
                try {
                    startDate = sdf.parse(time);
                    starttime = sdf.format(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                end.setText(time);
                try {
                    endDate = sdf.parse(time);
                    endtime = sdf.format(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dialog.dismiss();

        }

    }
}
