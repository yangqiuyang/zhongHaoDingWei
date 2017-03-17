package org.zhonghao.gps.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/5.
 */

public class DevicesInfo implements Serializable{
    private String containerType;
    private String routeType;
    private String deviceID;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private String deviceName;
    private String SendTime;
    private String userName;
    private String containerId;
    private String trainId;
    private String longitude;
    private String latitude;
    private String bindingTime;

    public String getBindingTime() {
        return bindingTime;
    }
    public void setBindingTime(String bindingTime) {
        this.bindingTime = bindingTime;
    }
    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }



    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getTime() {
        return SendTime;
    }

    public void setTime(String time) {
        this.SendTime = time;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    /**
     * Created by lenovo on 2016/11/24.
     */

    public static class LoginResponse implements Serializable {
        private String message;
        private Boolean state;
        private UserInfo userinfo;
        private ArrayList<DevicesInfo> devices;
        public void setState(Boolean state) {
            this.state = state;
        }

        public Boolean getState() {
            return state;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<DevicesInfo> getDevices() {
            return devices;
        }

        public void setDevices(ArrayList<DevicesInfo> devices) {
            this.devices = devices;
        }

        public UserInfo getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserInfo userinfo) {
            this.userinfo = userinfo;
        }
    }
}
