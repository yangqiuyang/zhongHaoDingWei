package org.zhonghao.gps.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/12/13.
 */
public class MyLocation implements Serializable{
    private String Time;
    private String latD;
    private String lonD;

    public String getTime() {
        return Time;
    }

    public String getLatD() {
        return latD;
    }

    public String getLonD() {
        return lonD;
    }
}
