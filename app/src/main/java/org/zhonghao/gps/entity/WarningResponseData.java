package org.zhonghao.gps.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public class WarningResponseData {

    /**
     * receiver : glb
     * alarmList : [{"alarmID":1,"alarmType":2,"baseStation1":"","imei":"352544072577704","latitude":34.73922,"longitude":113.79494499999998,"readState":"0","ringState":"3         ","time":"2017-03-22 16:20:23"}]
     */

        private String receiver;
        /**
         * alarmID : 1
         * alarmType : 2
         * baseStation1 :
         * imei : 352544072577704
         * latitude : 34.73922
         * longitude : 113.79494499999998
         * readState : 0
         * ringState : 3
         * time : 2017-03-22 16:20:23
         */

        private List<AlarmListBean> alarmList;

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public List<AlarmListBean> getAlarmList() {
            return alarmList;
        }

        public void setAlarmList(List<AlarmListBean> alarmList) {
            this.alarmList = alarmList;
        }

        public static class AlarmListBean {
            private int alarmID;
            private int alarmType;
            private String baseStation1;
            private String imei;
            private double latitude;
            private double longitude;
            private String readState;
            private String ringState;
            private String time;

            public int getAlarmID() {
                return alarmID;
            }

            public void setAlarmID(int alarmID) {
                this.alarmID = alarmID;
            }

            public int getAlarmType() {
                return alarmType;
            }

            public void setAlarmType(int alarmType) {
                this.alarmType = alarmType;
            }

            public String getBaseStation1() {
                return baseStation1;
            }

            public void setBaseStation1(String baseStation1) {
                this.baseStation1 = baseStation1;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public String getReadState() {
                return readState;
            }

            public void setReadState(String readState) {
                this.readState = readState;
            }

            public String getRingState() {
                return ringState;
            }

            public void setRingState(String ringState) {
                this.ringState = ringState;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

}
