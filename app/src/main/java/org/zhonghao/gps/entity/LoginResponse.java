package org.zhonghao.gps.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/23.
 */

public class LoginResponse implements Serializable{
        private String message;
        private Boolean state;
        private UserInfo userinfo;
        private ArrayList<LoginResponseDevice> devices;
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


        public UserInfo getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserInfo userinfo) {
            this.userinfo = userinfo;
        }

    public ArrayList<LoginResponseDevice> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<LoginResponseDevice> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", state=" + state +
                ", userinfo=" + userinfo +
                ", devices=" + devices +
                '}';
    }
}
