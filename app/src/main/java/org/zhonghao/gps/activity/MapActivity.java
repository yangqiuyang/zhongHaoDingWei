package org.zhonghao.gps.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONObject;
import org.zhonghao.gps.R;
import org.zhonghao.gps.adapter.RecyclerDeviceAdapter;
import org.zhonghao.gps.application.MyActivity;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.biz.MapBiz;
import org.zhonghao.gps.biz.Tools;
import org.zhonghao.gps.biz.UpdateBiz;
import org.zhonghao.gps.entity.DevicesLocateInfo;
import org.zhonghao.gps.entity.RequestDevices;
import org.zhonghao.gps.entity.ResponseDevicesMoveResult;
import org.zhonghao.gps.entity.UpdateInfo;
import org.zhonghao.gps.biz.LocationMsgShow;
import org.zhonghao.gps.entity.WarningResponseData;
import org.zhonghao.gps.utils.ProgressUtils;
import org.zhonghao.gps.utils.Urls;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends MyActivity
        implements NavigationView.OnNavigationItemSelectedListener,  BaiduMap.OnMarkerClickListener {

    BaiduMap mBaiduMap = null;
    TextView username;
    ResponseDevicesMoveResult responseDevicesMove = null;
    MyApplication myApplication;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                int msgId = msg.what;
                Bundle bundle = msg.getData();
                switch (msgId) {
                    case 2:
                        responseDevicesMove = (ResponseDevicesMoveResult) bundle.get("response");
                        int  position = (int) bundle.get("position");
                          try {
                              MapBiz.moveLocusService(responseDevicesMove, mapView,  mBaiduMap, MapActivity.this,handler,position);
                          } catch (Exception e) {
                            e.printStackTrace();
                            ProgressUtils.hideProgress();
                        }
                        break;
                    case 3:
                        Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 4:
                        // 判断服务器上的版本是不是最新的
                        String currntVersion = Tools
                                .getCurrentVersion(getApplicationContext());
                        final UpdateInfo info = (UpdateInfo) bundle
                                .getSerializable("versionEntity");
                        String serverApkVersion = info.getVersion();
                        if (Double.parseDouble(serverApkVersion) > Double.parseDouble(currntVersion)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MapActivity.this);
                            dialog.setMessage(info.getDescription());
                            dialog.setNegativeButton("取消", null);
                            dialog.setPositiveButton("升级", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("升级10000", info.getUrl());
                                    UpdateBiz updateBiz = new UpdateBiz();
                                    updateBiz.downloadApk(info.getUrl(), handler, MapActivity.this);

                                }
                            });
                            dialog.show();
                        } else {
                            Toast.makeText(getApplicationContext(), "当前就是最新的,服务器版本" ,
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 5:
                        Toast.makeText(MapActivity.this,"该设备暂无数据信息",Toast.LENGTH_SHORT).show();
                        ProgressUtils.hideProgress();
                        break;
                    case 7:
                        Toast.makeText(MapActivity.this,"该设备还没有信息哦",Toast.LENGTH_SHORT).show();
                        ProgressUtils.hideProgress();
                        break;
                    case 0x11:
                        int myPosition=msg.arg1;//请求的是第几个deviceid的
                        DevicesLocateInfo deviceInfo = (DevicesLocateInfo) msg.obj;
                        MapBiz.startMap(mapView, mBaiduMap, MapActivity.this, bitmap,myPosition,deviceInfo);
                        drawer.closeDrawer(GravityCompat.END);
                        break;
                }

            } catch (Exception e) {
                Toast.makeText(MapActivity.this,"出错了，稍后再试吧",Toast.LENGTH_SHORT).show();
                ProgressUtils.hideProgress();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        initToolBar();
        initWarning();
        initDrawer();
        initMap();
        initListView();
        //jieshou登陆页面传入数据
        myApplication = (MyApplication) this.getApplicationContext();
        myApplication.setMyHandler(handler);
        MyApplication.context=MapActivity.this;
    }


    //侧滑菜单
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer ;
    private void initDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_map);
        username = (TextView) headerView.findViewById(R.id.tv_username);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);
        username.setText(MyApplication.nameDates.getUsername());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, UserHomeActivity.class);
                startActivity(intent);

            }
        });

    }

    @BindView(R.id.lv_device_list)
    RecyclerView recycler;
    @BindView(R.id.tv_nomore)
    TextView deviceNomore;
    RecyclerDeviceAdapter adapter;
    private void initListView() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);
        adapter=new RecyclerDeviceAdapter(this);
        recycler.setAdapter(adapter);
        if(MyApplication.loginResponse==null||MyApplication.loginResponse.getDevices()==null){
            deviceNomore.setVisibility(View.VISIBLE);
        }
        adapter.addData(MyApplication.loginResponse);

    }

    //地图初始化
    @BindView(R.id.bmapView)
    MapView mapView ;
    @BindView(R.id.ib_location)
    ImageView location;
    BitmapDescriptor bitmap;
    private void initMap() {
        mapView.showZoomControls(true);
        mapView.showScaleControl(false);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setZoomGesturesEnabled(true);           //启用缩放手势
        mUiSettings.setScrollGesturesEnabled(true);         //启用平移手势
        mUiSettings.setRotateGesturesEnabled(false);        //关闭旋转手势
        mUiSettings.setOverlookingGesturesEnabled(false);   //关闭俯视手势
        mUiSettings.setCompassEnabled(false);               //关闭指南针图层
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMaxAndMinZoomLevel(19, 4 );
        LatLng point = new LatLng(39.963175, 116.400244);
        MapStatus mapStatus = new MapStatus.Builder().zoom(8).target(point).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.map_mark);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng point = MyApplication.point;
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(point);
                mBaiduMap.animateMapStatus(mMapStatusUpdate);
            }
        });
        //百度地图覆盖物点击事件
        mBaiduMap.setOnMarkerClickListener(this);
    }

    //设置toolBar
    @BindView(R.id.app_toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_home_toolbar)
    Toolbar toolbarDevices;
    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    //返回键功能
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);

        return true;
    }

    //最右侧，中浩科技，加载出我的设备
    WarningResponseData message;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_more){
        drawer.openDrawer(GravityCompat.END);}
        if(item.getItemId()==R.id.action_warning){
                Intent intent = new Intent(this, WarnInformationActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //drawlayout中导航条的事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.my_devices) {
            // Handle the camera action
            drawer.closeDrawer(GravityCompat.START);
            drawer.openDrawer(GravityCompat.END);

        } else if (id == R.id.nav_move_locus) {
            Intent intent = new Intent(this, MoveLocusActivity.class);
            intent.putExtra("position",0);//轨迹查询默认第一
            startActivity(intent);

        } else if (id == R.id.nav_user_home) {
            //TODO
            Intent intent = new Intent(this, UserHomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_update) {

            UpdateBiz updateBiz = new UpdateBiz();
            updateBiz.getNewVersion(handler, MapActivity.this);


        } else if (id == R.id.nav_business_server) {
            Intent intent = new Intent(this, BusinessServerActivity.class);
            startActivity(intent);

        }else if (id == R.id.about_us) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    long time;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            //jingling.点击的时间差如果大于2000，则提示用户点击两次退出
            if (System.currentTimeMillis() - time > 2000) {
                // huoche.保存当前时间
                time = System.currentTimeMillis();
                //4.提示
                Toast.makeText(MapActivity.this, "请再次点击退出程序", Toast.LENGTH_SHORT).show();
            } else {
                //5.点击的时间差小于2000，退出。
                finish();
                System.exit(0);
            }
        }
        return true;
    }

    //百度地图覆盖物监听事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle extraInfo = marker.getExtraInfo();
        int makerType = extraInfo.getInt("markerType");
        int markerPostion = extraInfo.getInt("position");//包含定位时是点击的list跳转的路线定位的位置,另一个是路线规划时的点击的Marker位置
        int devicePosition= extraInfo.getInt("devicePosition");//获取设备信息的设备devicedid
        switch (makerType){
            case 0x12://集装箱定位
                LocationMsgShow.getLocationMsg(MapActivity.this,marker,mBaiduMap,markerPostion);
                break;
            default://路线定位信息
             LocationMsgShow.getRouteMsg(MapActivity.this,marker,mBaiduMap,markerPostion,devicePosition);
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    //警告信息请求完后才能跳转，不然预警可能没数据
    public void initWarning() {
       /* RequestQueue queue= Volley.newRequestQueue(this);
        HashMap<String,String> map=new HashMap<>();
        map.put("userinfo",MyApplication.nameDates.getUsername());
        JSONObject data=new JSONObject(map);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.WARNING_URL, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                message = MyApplication.gson.fromJson(String.valueOf(response.optJSONObject("message")), WarningResponseData.class);
                MyApplication.warnResponseData=message;
                //EventBus.getDefault().post(new MyEvent(message));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressUtils.hideProgress();//隐藏进度条
            }
        });
        queue.add(request);*/
    }
}
