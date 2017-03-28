package org.zhonghao.gps.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
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
    public static WarningResponseData warnResponseData=new WarningResponseData();//请求的预警信息
    public Handler myHandler = null;
    public Handler getMyHandler() {
        return myHandler;
    }
    public static Gson gson = new Gson();
    public void setMyHandler(Handler myHandler) {
        this.myHandler = myHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        x.Ext.init(this);

    }

}
