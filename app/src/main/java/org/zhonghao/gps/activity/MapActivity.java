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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.BaiduMapSDKException;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import org.zhonghao.gps.R;
import org.zhonghao.gps.adapter.ListViewAdapter;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.biz.MapBiz;
import org.zhonghao.gps.biz.ServelBiz;
import org.zhonghao.gps.biz.Tools;
import org.zhonghao.gps.biz.UpdateBiz;
import org.zhonghao.gps.entity.DevicesInfo;
import org.zhonghao.gps.entity.RequestDevices;
import org.zhonghao.gps.entity.ResponseDevicesMove;
import org.zhonghao.gps.entity.UpdateInfo;
import org.zhonghao.gps.netUtils.MyJsonResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.badgeview.BGABadgeImageView;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    MapView mapView = null;
    BaiduMap mBaiduMap = null;
    RelativeLayout rlt = null;
    DevicesInfo.LoginResponse loginResponse = null;
    ListView listView;
    TextView username, introduce;
    RequestDevices requestDevices = null;
    ArrayList<DevicesInfo> devicesInfos = null;
    LocationClient locationClient = null;
    BitmapDescriptor bitmap;
    ImageButton location;
    ResponseDevicesMove responseDevicesMove = null;
    MyApplication myApplication;
    DrawerLayout drawer;
    BGABadgeImageView message;
    boolean mIsShow = true;
    public static ProgressBar mapLogin;
    int myposition = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("serve", "进入了handler");
            try {
                int msgId = msg.what;
                Bundle bundle = msg.getData();
                switch (msgId) {

                    case 1:
                        devicesInfos = (ArrayList<DevicesInfo>) bundle.get("list");
//                         Toast.makeText(MapActivity.this, "id===" + devicesInfos.get(0).getSendTime(), Toast.LENGTH_SHORT).show();
                        Log.d("serve", "进入了handler" + devicesInfos.get(0).getDeviceID());
                        MapBiz.startMap(devicesInfos.get(0), mapView, mBaiduMap, MapActivity.this, bitmap,myposition);
                        mapLogin.setVisibility(View.GONE);
                        break;
                    case 2:
                        mapLogin.setVisibility(View.VISIBLE);

                        responseDevicesMove = (ResponseDevicesMove) bundle.get("response");
                        Log.d("serve", "进入了handler" + responseDevicesMove.getContainerIds());
                        try {
                            MapBiz.moveLocusService(responseDevicesMove, mapView, mBaiduMap, MapActivity.this,handler);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mapLogin.setVisibility(View.GONE);
                            Log.d("serve", "进入了case2" + e.toString());
                        }

                        mapLogin.setVisibility(View.GONE);
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
                        Log.i("update", serverApkVersion);
                        Log.i("logcal", currntVersion);
           //                            if (!(serverApkVersion.equals(currntVersion))) {
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
                        mapLogin.setVisibility(View.GONE);
                        Toast.makeText(MapActivity.this,"网络状况不佳，休息一下再试吧",Toast.LENGTH_SHORT).show();
                    case 6:
                        mapLogin.setVisibility(View.VISIBLE);
                        break;
                    case 7:
                        Toast.makeText(MapActivity.this,"该设备还没有信息哦",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x11:
                        MapBiz.startMap(MyApplication.devicesInfo, mapView, mBaiduMap, MapActivity.this, bitmap,myposition);
                        drawer.closeDrawer(GravityCompat.END);
                        break;

                }

            } catch (Exception e) {
                Log.d("serve", "进入了handler" + e.toString());
                mapLogin.setVisibility(View.GONE);
                Toast.makeText(MapActivity.this,"出错了，稍后再试吧",Toast.LENGTH_SHORT).show();

            }
        }
    };

    //警铃通知
   /* @BindView(R.id.alarm_bell_map)
    ImageView alarmBell;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        mapLogin = (ProgressBar) findViewById(R.id.map_progress);
        //jieshou登陆页面传入数据
        Intent intent = this.getIntent();
        loginResponse = (DevicesInfo.LoginResponse) intent.getSerializableExtra("loginResponse");
        listView = (ListView) findViewById(R.id.lv_device_list);
        listView.setAdapter(new ListViewAdapter(this, loginResponse));
        myApplication = (MyApplication) this.getApplicationContext();
        MyOnItemOnClickListener listener = new MyOnItemOnClickListener();
        myApplication.setMyHandler(handler);
        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.map_mark);
        listView.setOnItemClickListener(listener);
        listView.setEmptyView(findViewById(R.id.tv_nomore));

        rlt = (RelativeLayout) findViewById(R.id.rl_message);
        mapView = (MapView) findViewById(R.id.bmapView);
        mapView.showZoomControls(false);
        mapView.showScaleControl(false);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMaxAndMinZoomLevel(19, 4);
        LatLng point = new LatLng(39.963175, 116.400244);
        MapStatus mapStatus = new MapStatus.Builder().zoom(7).target(point).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        Toolbar toolbarDevices = (Toolbar) findViewById(R.id.user_home_toolbar);
        toolbarDevices.setTitle("我的设备");

        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        location = (ImageButton) findViewById(R.id.ib_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng point = MyApplication.point;
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(point);
                mBaiduMap.animateMapStatus(mMapStatusUpdate);
            }
        });
       // initListener();
    }

    //控件监听事件
  /*  private void initListener() {
        alarmBell.setOnClickListener(this);
    }*/

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
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);

        return true;
    }

    //最右侧，中浩科技，加载出我的设备
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_more){
        drawer.openDrawer(GravityCompat.END);}
        if(item.getItemId()==R.id.action_warning){
            Intent intent=new Intent(this,WarnInformationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

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
            intent.putExtra("position",0);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        Toast.makeText(this, "onRescum", Toast.LENGTH_SHORT).show();
        mapView.onResume();
    }
    long l ;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            //2.点击的时间差如果大于2000，则提示用户点击两次退出
            if (System.currentTimeMillis() - l > 2000) {
                // 3.保存当前时间
                l = System.currentTimeMillis();
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


    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
//        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        mapView.onPause();
    }



    private class MyOnItemOnClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Toast.makeText(MapActivity.this,"d=点击了"+loginResponse.getDevices().get(position),Toast.LENGTH_SHORT).show();
            requestDevices = new RequestDevices();
            ArrayList<String> strings = new ArrayList<>();
            ArrayList<DevicesInfo> strings1 = new ArrayList<>();
            strings.add(MyApplication.nameDates.getUsername());
            strings1.add(loginResponse.getDevices().get(position));
            myposition = position;
            requestDevices.setUserinfo(strings);
            requestDevices.setDeviceID(strings1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ServelBiz.getDvices(requestDevices, MapActivity.this, handler);
                }
            }).start();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.END);
            mapLogin.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View v) {
    }
}
