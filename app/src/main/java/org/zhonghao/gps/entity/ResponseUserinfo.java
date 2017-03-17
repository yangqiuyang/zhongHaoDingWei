package org.zhonghao.gps.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/11/29.
 */

public class ResponseUserinfo implements Serializable {
    private ArrayList<NameDates> userinfo;

    public ArrayList<NameDates> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(ArrayList<NameDates> userinfo) {
        this.userinfo = userinfo;
    }
}
