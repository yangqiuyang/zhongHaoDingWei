package org.zhonghao.gps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;

public class MoveLocusActivity extends AppCompatActivity {
    private ArrayList<String> listDevices;
    public Boolean choose;
    public  String time;
    private String starttime, endtime, queryDeviceNum;
    private Date startDate, endDate;
    private Calendar myCalender;
    @BindView(R.id.sp_querydate)
    Spinner date;
    @BindView(R.id.sp_query_num)
    AppCompatSpinner num;
    private TextView start, end;
    private LinearLayout llstart, llend, selector, line1, line2, line3;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> devicesAdapter;
    private SimpleDateFormat sdf,sdf1;
    private DatePicker datePicker;
    private AlertDialog dialog;
    private TextView confirm, cancel, query;
    private static final String[] name = {"最近一天", "最近一周", "最近一月", "自定义"};
    private ArrayList<DevicesInfo> device;
    private int myposition;
    private RequestQueryDevicesMoveInfo myQueryRequest = new RequestQueryDevicesMoveInfo();
    MyApplication myApplication;
    Handler handler;


    @BindView(R.id.sp_move_toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_move_locus);
        ButterKnife.bind(this);
        setView();
        initData();
        initAlertDialog();
        initTool();
        initDate();
        initAdapter();
        initListener();


    }

    private void initAlertDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(MoveLocusActivity.this)
                .setView(selector);
        dialog = ad.create();
        confirm = (TextView) selector.findViewById(R.id.btn_dialog_confirm);
        cancel = (TextView) selector.findViewById(R.id.btn_dialog_cancel);
        datePicker = (DatePicker) selector.findViewById(R.id.date_picker);
    }
     List<String> devices;
    private void initData() {
        Intent intent = this.getIntent();
        myposition = intent.getIntExtra("position",0);
        myApplication = (MyApplication) this.getApplicationContext();
        handler = myApplication.getMyHandler();
        devices=new ArrayList<>();
        device = MyApplication.myDevice;
        for (int i = 0; i < MyApplication.myDevice.size(); i++) {
            devices.add(MyApplication.myDevice.get(i).getDeviceID());
        }
        //设置默认设备号
        queryDeviceNum = device.get(0).getDeviceID();
    }

    private void initAdapter() {
        //日期选择Spinner
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date.setAdapter(adapter);
        //设备选择器Spinner
        devicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, devices);
        devicesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        num.setAdapter(devicesAdapter);
        num.setSelection(myposition);
    }

    private void initDate() {
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
        //设置默认时间
        start.setText(data);
        end.setText(data);
    }

    private void initTool() {
        toolbar.setTitle("轨迹查询");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    }

    private void initListener() {
        //日期选择
        date.setOnItemSelectedListener(new MyItemClickListener());
        //设备信息
        num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                queryDeviceNum = device.get(position).getDeviceID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //开始日期选择
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose = true;
                dialog.show();

            }
        });
        //结束日期选择
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose = false;
                dialog.show();


            }
        });

        //确认日期段
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                time = datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
                if (choose) {
                        start.setText(time);
                        startDate = sdf1.parse(time);
                        starttime = sdf.format(startDate);

                } else {
                        end.setText(time);
                        endDate = sdf1.parse(time);
                        endtime = sdf.format(endDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
              finally {
                    dialog.dismiss();
                }

            }
        });
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //按日期查询轨迹
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检查时间是否正确
                if (startDate.before(endDate)) {
                    listDevices = new ArrayList<String>();
                    listDevices.add(queryDeviceNum);
                    myQueryRequest.setUserinfo(MyApplication.nameDates.getUsername());
                    myQueryRequest.setDeviceID(listDevices);
                    myQueryRequest.setStartTime(starttime);
                    myQueryRequest.setEndTime(endtime);
                    Log.i("endtime",endtime);
                    Log.i("endtime",starttime);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServelBiz.getDvicesMoveLocus(myQueryRequest, MoveLocusActivity.this, handler);
                        }
                    }).start();
                    handler.sendEmptyMessage(6);
                    finish();


                } else {
                    Toast.makeText(MoveLocusActivity.this, "开始时间必须大于结束时间", Toast.LENGTH_SHORT).show();
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
        start = (TextView) findViewById(R.id.tv_query_start);
        end = (TextView) findViewById(R.id.tv_query_end);
        line1 = (LinearLayout) findViewById(R.id.line1);
        line2 = (LinearLayout) findViewById(R.id.line2);
        line3 = (LinearLayout) findViewById(R.id.line3);
        query = (TextView) findViewById(R.id.query_move_locus);
        llstart = (LinearLayout) findViewById(R.id.ll_start);
        llend = (LinearLayout) findViewById(R.id.ll_end);
        selector = (LinearLayout) MoveLocusActivity.this.getLayoutInflater().inflate(R.layout.date_selector_layout, null);
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
}
