package org.zhonghao.gps.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class LoginResponseDevice implements Serializable{
    private String deviceID;
    private String deviceName;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
