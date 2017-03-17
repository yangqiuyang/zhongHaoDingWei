package org.zhonghao.gps.application;

import android.app.Application;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.model.LatLng;

import org.xutils.x;

import java.util.ArrayList;

import org.zhonghao.gps.entity.DevicesInfo;
import org.zhonghao.gps.entity.DevicesLocationMsg;
import org.zhonghao.gps.entity.DevicesSelfLocation;
import org.zhonghao.gps.entity.NameDates;
import org.zhonghao.gps.entity.UserInfo;
import org.zhonghao.gps.netUtils.BaseRetrofit;

/**
 * Created by lenovo on 2016/11/28.
 */

public class MyApplication extends Application {
    public static NameDates nameDates = new NameDates();
    public static Boolean responseState = false;
    public static UserInfo myUserInnfo = new UserInfo();
    public static ArrayList<DevicesInfo> myDevice = new ArrayList<>();
    public static DevicesSelfLocation devicesSelfLocation=new DevicesSelfLocation();
    public static DevicesLocationMsg devicesLocationMsg=new DevicesLocationMsg();
    public static DevicesInfo devicesInfo=new DevicesInfo();
    public static LatLng point = new LatLng(34.73412, 113.79439);
    public Handler myHandler = null;
    public Handler getMyHandler() {
        return myHandler;
    }

    public void setMyHandler(Handler myHandler) {
        this.myHandler = myHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //初始化Retrofit
        BaseRetrofit.init();
    }


}