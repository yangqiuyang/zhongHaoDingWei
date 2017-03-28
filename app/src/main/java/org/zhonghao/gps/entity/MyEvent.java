package org.zhonghao.gps.entity;

/**
 * Created by Administrator on 2017/3/27.
 */

public  class  MyEvent {

    public WarningResponseData data;
    public MyEvent(WarningResponseData dat) {
        this.data = data;
    }

    public WarningResponseData getData() {
        return data;
    }
}
