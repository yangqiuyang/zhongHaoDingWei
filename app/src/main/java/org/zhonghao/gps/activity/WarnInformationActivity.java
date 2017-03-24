package org.zhonghao.gps.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zhonghao.gps.R;
import org.zhonghao.gps.adapter.WarningMsgAdapter;
import org.zhonghao.gps.application.MyActivity;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.customView.ToggleText;
import org.zhonghao.gps.entity.WarningResponseData;
import org.zhonghao.gps.utils.MyJsonResponse;
import org.zhonghao.gps.utils.Urls;
import org.zhonghao.gps.utils.WarnIPopupWindow;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WarnInformationActivity extends MyActivity {

    @BindView(R.id.toolbar_warn_information)
    Toolbar toolBar;
    @BindView(R.id.list_warn_information)
    ListView list;
    @BindView(R.id.activity_warn_information)
    LinearLayout main;
    LayoutInflater inflater;
    @BindView(R.id.empty_list)
    TextView emptyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_warn_information);
        ButterKnife.bind(this);
         inflater = LayoutInflater.from(this);
        initActionBar();
        initList();
        initData();
        initListener();
    }

    //ToolBar代替ActionBar
    private void initActionBar() {
        toolBar.setTitle("预警信息");
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolBar);
    }
    //设置listView
    ToggleText toggle;
    WarningMsgAdapter adapter;
    private void initList() {
        View headView =inflater.inflate(R.layout.head_list_warn_infromation, null);
        toggle = (ToggleText) headView.findViewById(R.id.toggle_head_warn_information);
        list.addHeaderView(headView);
        adapter=new WarningMsgAdapter(this);
        list.setAdapter(adapter);
        list.setEmptyView(emptyList);
    }
    //监听事件
    private void initListener() {
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
    /**
     *   网络请求警铃数据
     */
    private void initData() {
        RequestQueue queue= Volley.newRequestQueue(this);
        HashMap<String,String> map=new HashMap<>();
        map.put("time","2017-03-22 16:22:23");
        map.put("userinfo","admin");
        JSONObject data=new JSONObject(map);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.WARNING_URL, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                WarningResponseData warningResponseData = MyApplication.gson.fromJson(response.toString(), WarningResponseData.class);
                adapter.addData(warningResponseData);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }



}
