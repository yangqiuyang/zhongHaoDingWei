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
import android.widget.ToggleButton;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.zhonghao.gps.R;
import org.zhonghao.gps.application.MyActivity;
import org.zhonghao.gps.customView.ToggleText;
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
        setSupportActionBar(toolBar);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ActionBar的文字
    }
    //设置listView
    ToggleText toggle;
    private void initList() {
        View headView =inflater.inflate(R.layout.head_list_warn_infromation, null);
        toggle = (ToggleText) headView.findViewById(R.id.toggle_head_warn_information);
        list.addHeaderView(headView);
        list.setAdapter(null);
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
        map.put("time","2017-03-07 00:00:00");
        map.put("userinfo","admin");
        JSONObject jsonObject=new JSONObject(map);
        Log.i("json111",jsonObject.toString());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.WARNING_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("re", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        }
        );
        queue.add(request);
    }



}
