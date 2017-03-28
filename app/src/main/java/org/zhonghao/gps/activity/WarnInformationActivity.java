package org.zhonghao.gps.activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.zhonghao.gps.R;
import org.zhonghao.gps.adapter.WarningMsgAdapter;
import org.zhonghao.gps.application.MyActivity;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.customView.ToggleText;
import org.zhonghao.gps.entity.WarningMark;
import org.zhonghao.gps.utils.Urls;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WarnInformationActivity extends MyActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_warn_information)
    Toolbar toolBar;
    @BindView(R.id.list_warn_information)
    ListView list;
    @BindView(R.id.activity_warn_information)
    LinearLayout main;
   @BindView(R.id.empty_list)
    TextView emptyList;
    MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_warn_information);
        ButterKnife.bind(this);
        application = (MyApplication) getApplicationContext();
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
        toggle = (ToggleText) findViewById(R.id.toggle_head_warn_information);
        adapter=new WarningMsgAdapter(this);
        list.setAdapter(adapter);
        list.setEmptyView(emptyList);
    }
    //监听事件
    private void initListener() {
        //弹出窗的监听
        toggle.getPop().getMarkRead().setOnClickListener(this);
        toggle.getPop().getAllMark().setOnClickListener(this);
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
    //设置标记阅读标记
    private Map<Integer,Boolean> checkedMap;
    WarningMark mark;
    private void initData() {
       /* Intent intent = getIntent();
       WarningResponseData message = (WarningResponseData) intent.getSerializableExtra("message");
        if(message!=null&&message.getAlarmList().size()!=0){
            adapter.addData(message);
        }*/
        checkedMap=new HashMap<>();
        mark=new WarningMark();
        adapter.addData(MyApplication.warnResponseData,checkedMap,mark);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mark_read:
              //  initRead();//标记已读
                initRead();
                break;
            case R.id.all_mark:
                break;
        }
    }

    private void initRead() {
        try {
            RequestQueue queue= Volley.newRequestQueue(this);
            JSONObject data = null;
            data = new JSONObject(MyApplication.gson.toJson(mark));
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.WARING_READ, data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /* private void initRead() {
         List<WarningResponseData.AlarmListBean> alarmList = MyApplication.warnResponseData.getAlarmList();
         for (int i = 0; i < checkedMap.size(); i++) {
             alarmList.get(i)
         }
     }
 */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
