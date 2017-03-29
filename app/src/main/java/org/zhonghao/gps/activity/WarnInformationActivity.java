package org.zhonghao.gps.activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zhonghao.gps.R;
import org.zhonghao.gps.adapter.WarningMsgAdapter;
import org.zhonghao.gps.application.MyActivity;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.customView.ToggleText;
import org.zhonghao.gps.entity.WarningMark;
import org.zhonghao.gps.entity.WarningMarkList;
import org.zhonghao.gps.entity.WarningResponseData;
import org.zhonghao.gps.jpush.JpushReceiver;
import org.zhonghao.gps.utils.MyJsonResponse;
import org.zhonghao.gps.utils.ProgressUtils;
import org.zhonghao.gps.utils.Urls;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class WarnInformationActivity extends MyActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.toolbar_warn_information)
    Toolbar toolBar;
    @BindView(R.id.list_warn_information)
    ListView list;
    @BindView(R.id.activity_warn_information)
    LinearLayout main;
   @BindView(R.id.empty_list)
    TextView emptyList;
    MyApplication application;
    @BindView(R.id.head_warn)
    LinearLayout headWarning;//警告头布局
    RequestQueue queue;
    @BindView(R.id.showWarning)
    CheckBox showWarning;//是否显示极光推送的通知在通知栏上
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_warn_information);
        ButterKnife.bind(this);
        application = (MyApplication) getApplicationContext();
        queue=Volley.newRequestQueue(this);
        initActionBar();
        initList();
        initWarning();
        initListener();
    }
    List<WarningResponseData>  warnData;
    private void initWarning() {
            ProgressUtils.showProgress(this);
            RequestQueue queue= Volley.newRequestQueue(this);
            HashMap<String,String> map=new HashMap<>();
            map.put("userinfo",MyApplication.nameDates.getUsername());
            String data = MyApplication.gson.toJson(map);
            MyJsonResponse request=new MyJsonResponse(Urls.BASE_URL + Urls.WARNING_URL, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    warnData=new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json=response.optJSONObject(i);
                        warnData.add(MyApplication.gson.fromJson(json.toString(),WarningResponseData.class));
                    }
                    ProgressUtils.hideProgress();
                    adapter.addData(warnData);
                    if(response.length()!=0) {
                        headWarning.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    application.getMyHandler().sendEmptyMessage(5);
                    ProgressUtils.hideProgress();
                }
            },data);
            queue.add(request);
        list.setEmptyView(emptyList);
    }

    //ToolBar代替ActionBar
    private void initActionBar() {
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    //设置listView
    ToggleText toggle;
    WarningMsgAdapter adapter;
    private void initList() {
        toggle = (ToggleText) findViewById(R.id.toggle_head_warn_information);
        adapter=new WarningMsgAdapter(this);
        toggle.setAdapter(adapter);
        list.setAdapter(adapter);
    }
    //监听事件

    SharedPreferences pref;
    SharedPreferences.Editor mEditor;
    private void initListener() {
        //弹出窗的监听
        toggle.getPop().getMarkRead().setOnClickListener(this);
        toggle.getPop().getAllMark().setOnClickListener(this);
        //获取sharedPreference中的内容，判断是否设置极光推送
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        mEditor=pref.edit();
        boolean jpushAllow = pref.getBoolean("jpushAllow", false);
        if(jpushAllow){
            showWarning.setChecked(true);
        }
        else {
            showWarning.setChecked(false);
        }
        //设置是否接受推送
        showWarning.setOnCheckedChangeListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mark_read:
                initRead();
                break;
            case R.id.all_mark:
                initAllMark();
                break;
        }
    }

    private void initAllMark() {
        WarningMarkList warnList=new WarningMarkList();
        List<WarningMark> markList=new ArrayList<>();
        for (int i = 0; i < warnData.size(); i++) {
            WarningResponseData.AlarmListBean alarmBean= warnData.get(i).getAlarmList().get(0);
            alarmBean.setReadState(Urls.READ_TYPE);
            WarningMark mark=new WarningMark();
            mark.setAlarmID(alarmBean.getAlarmID());
            mark.setReadState(alarmBean.getReadState());
            markList.add(mark);
            warnList.setMarkList(markList);
        }
        adapter.notifyDataSetChanged();
        updateMark(warnList);
    }

    private void initRead() {
        WarningMarkList warnMarkList=new WarningMarkList();
        List<WarningMark> markList=new ArrayList<>();
        for (int i = 0; i < warnData.size(); i++) {
            WarningResponseData.AlarmListBean alarm = warnData.get(i).getAlarmList().get(0);
            if (alarm.getReadState().equals(Urls.READ_TYPE)) {
                WarningMark mark=new WarningMark();
                mark.setAlarmID(alarm.getAlarmID());
                mark.setReadState(alarm.getReadState());
                markList.add(mark);
                warnMarkList.setMarkList(markList);
            }
        }
        updateMark(warnMarkList);
    }

    private void updateMark(WarningMarkList warnMarkList) {
        try {
            JSONObject jsn=new JSONObject(MyApplication.gson.toJson(warnMarkList));
            Log.i("jsn", String.valueOf(jsn));
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.WARING_READ, jsn, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    resetList();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    application.getMyHandler().sendEmptyMessage(5);
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        toggle.getPop().hide();
    }
    //移除已标记的
    private void resetList() {
        for (int i = 0; i < warnData.size(); i++) {
            WarningResponseData.AlarmListBean alarmListBean = warnData.get(i).getAlarmList().get(0);
            if (alarmListBean.getReadState().equals(Urls.READ_TYPE)) {
                warnData.remove(i);//移除已经标记过的
                adapter.addData(warnData);
            }
        }
        list.setEmptyView(emptyList);//设置为空时的显示
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.showWarning:
               mEditor.putBoolean("jpushAllow", isChecked);
               mEditor.commit();
                //设置是否接收极光推送
                if(isChecked){
                    JPushInterface.resumePush(this);
                }
                else{
                    JPushInterface.stopPush(this);

                }
                JPushInterface.clearAllNotifications(this);//清空以前的推送
                break;
        }
    }
}
