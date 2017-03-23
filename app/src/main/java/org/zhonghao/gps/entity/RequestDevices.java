package org.zhonghao.gps.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/6.
 */

public class RequestDevices implements Serializable {
    private ArrayList<String> userinfo;
    private ArrayList<DevicesLocateInfo> DeviceID;

    public ArrayList<String> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(ArrayList<String> userinfo) {
        this.userinfo = userinfo;
    }

    public ArrayList<DevicesLocateInfo> getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(ArrayList<DevicesLocateInfo> deviceID) {
        DeviceID = deviceID;
    }
}
