package org.zhonghao.gps.entity;

/**
 * Created by Administrator on 2017/huoche/17.
 */

public class DevicesLocationMsg {

    /**
     * containerType : 0
     * routeType : 0
     * deviceID : 957
     * SendTime : 2017-03-16T16:35:00.000
     * userName : glb
     * devicename : ZS100-010200458
     * longitude : 9.915913
     * latitude : 53.542955
     * trainId : 2017-03-02
     * containerId : CICU9880095
     * bindingTime : 2017-03-02 09:48:08.643
     */

    private String containerType;
    private String routeType;
    private String deviceID;
    private String SendTime;
    private String userName;
    private String devicename;
    private String longitude;
    private String latitude;
    private String trainId;
    private String containerId;
    private String bindingTime;

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String SendTime) {
        this.SendTime = SendTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
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

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getBindingTime() {
        return bindingTime;
    }

    public void setBindingTime(String bindingTime) {
        this.bindingTime = bindingTime;
    }
}
