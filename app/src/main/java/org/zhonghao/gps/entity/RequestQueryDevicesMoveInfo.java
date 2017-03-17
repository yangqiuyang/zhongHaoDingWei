package org.zhonghao.gps.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/13.
 */

public class RequestQueryDevicesMoveInfo implements Serializable{

        private String userinfo;
        private ArrayList<String> DeviceID;
        private String startTime;
        private String endTime;
        public void setUserinfo(String userinfo) {
            this.userinfo = userinfo;
        }
        public String getUserinfo() {
            return userinfo;
        }

        public void setDeviceID(ArrayList<String> deviceID) {
            this.DeviceID = deviceID;
        }
        public ArrayList<String> getDeviceID() {
            return DeviceID;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
        public String getStartTime() {
            return startTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
        public String getEndTime() {
            return endTime;
        }


}
