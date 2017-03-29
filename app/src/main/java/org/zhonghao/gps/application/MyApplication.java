package org.zhonghao.gps.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.zhonghao.gps.entity.DevicesLocateInfo;
import org.zhonghao.gps.entity.DevicesLocationMsg;
import org.zhonghao.gps.entity.DevicesSelfLocation;
import org.zhonghao.gps.entity.LoginResponse;
import org.zhonghao.gps.entity.NameDates;
import org.zhonghao.gps.entity.RequestQueryDevicesMoveInfo;
import org.zhonghao.gps.entity.ResponseDevicesMoveResult;
import org.zhonghao.gps.entity.UserInfo;
import org.zhonghao.gps.entity.WarningResponseData;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lenovo on 2016/11/28.
 */

public class MyApplication extends Application {
    public static int locatePosition;
    public static DevicesLocateInfo devicesLocate=new DevicesLocateInfo();//设备定位信息
    public static Context context;
    public static NameDates nameDates = new NameDates();
    public static Boolean responseState = false;
    public static Boolean loginState=false;//登录状态
    public static LoginResponse loginResponse;//登录后的响应信息，在第一次启动时就有
    public static UserInfo myUserInnfo = new UserInfo();//用户信息
    public static ArrayList<DevicesLocateInfo> myDevice = new ArrayList<>();//设备信息
    public static DevicesSelfLocation devicesSelfLocation=new DevicesSelfLocation();
    public static DevicesLocationMsg devicesLocationMsg=new DevicesLocationMsg();
    public static DevicesLocateInfo devicesInfo=new DevicesLocateInfo();
    public static ResponseDevicesMoveResult responseDevicesMove=new ResponseDevicesMoveResult();//请求的轨迹数据
    public static RequestQueryDevicesMoveInfo DevicesMoveInfo=new RequestQueryDevicesMoveInfo();//集装箱路线信息类
    public static LatLng point = new LatLng(34.73412, 113.79439);//定位郑州
    public static List<WarningResponseData> warnResponseData=new ArrayList<>();//请求的预警信息
    public Handler myHandler = null;
    public Handler getMyHandler() {
        return myHandler;
    }
    public static Gson gson = new Gson();
    public void setMyHandler(Handler myHandler) {
        this.myHandler = myHandler;
    }

    SharedPreferences mPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        //百度地图
        SDKInitializer.initialize(this);
        //xutils
        x.Ext.init(this);
        //极光推送
       // JPushInterface.setDebugMode(true);
        mPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean jpushAllow = mPreferences.getBoolean("jpushAllow", false);
        JPushInterface.init(this);
        JPushInterface.setLatestNotificationNumber(this,5);//设置最多显示5条未读
        //设置是否接收极光推送消息
        if(jpushAllow){
            JPushInterface.resumePush(this);
        }
        else {
            JPushInterface.stopPush(this);
        }

    }

}
