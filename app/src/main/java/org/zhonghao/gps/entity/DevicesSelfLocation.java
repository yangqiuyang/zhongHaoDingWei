package org.zhonghao.gps.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/huoche/17.
 */

public class DevicesSelfLocation {
    private List<String> userinfo;
    private List<String> DeviceID;

    public List<String> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<String> userinfo) {
        this.userinfo = userinfo;
    }

    public List<String> getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(List<String> deviceID) {
        DeviceID = deviceID;
    }
}
