package org.zhonghao.gps.entity;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/13.
 */
//device移动路线
public class ResponseDevicesMoveResult implements Serializable {
    private String myCentersLat;
    private String containerTypes;
    private String trainIds;
    private ArrayList<MyLocation> location;
    private String endAddresssLon;
    private String ids;
    private String endAddresssLat;
    private String containerIds;
    private String myCentersLon;
    private String routeTypes;

    public void setMycenterslat(String mycenterslat) {
        this.myCentersLat = mycenterslat;
    }

    public String getMycenterslat() {
        return myCentersLat;
    }

    public void setContainerTypes(String containerTypes) {
        this.containerTypes = containerTypes;
    }

    public String getContainerTypes() {
        return containerTypes;
    }

    public void setTrainIds(String trainIds) {
        this.trainIds = trainIds;
    }

    public String getTrainIds() {
        return trainIds;
    }

    public void setLocation(ArrayList<MyLocation> location) {
        this.location = location;
    }

    public ArrayList<MyLocation> getLocation() {
        return location;
    }

    public void setEndAddresssLon(String endAddresssLon) {
        this.endAddresssLon = endAddresssLon;
    }

    public String getEndAddresssLon() {
        return endAddresssLon;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getIds() {
        return ids;
    }

    public void setEndAddresssLat(String endAddresssLat) {
        this.endAddresssLat = endAddresssLat;
    }

    public String getEndAddresssLat() {
        return endAddresssLat;
    }

    public void setContainerIds(String containerIds) {
        this.containerIds = containerIds;
    }

    public String getContainerIds() {
        return containerIds;
    }

    public void setMyCentersLon(String myCentersLon) {
        this.myCentersLon = myCentersLon;
    }

    public String getMyCentersLon() {
        return myCentersLon;
    }

    public void setRouteTypes(String routeTypes) {
        this.routeTypes = routeTypes;
    }

    public String getRouteTypes() {
        return routeTypes;
    }

    @Override
    public String toString() {
        return "ResponseDevicesMoveResult{" +
                "myCentersLat='" + myCentersLat + '\'' +
                ", containerTypes='" + containerTypes + '\'' +
                ", trainIds='" + trainIds + '\'' +
                ", location=" + location +
                ", endAddresssLon='" + endAddresssLon + '\'' +
                ", ids='" + ids + '\'' +
                ", endAddresssLat='" + endAddresssLat + '\'' +
                ", containerIds='" + containerIds + '\'' +
                ", myCentersLon='" + myCentersLon + '\'' +
                ", routeTypes='" + routeTypes + '\'' +
                '}';
    }
}
