package org.zhonghao.gps.utils;

/**
 * Created by Administrator on 2017/huoche/15.
 */

public class Urls {
    //base_url 192.168.16.143:6666
    public static String BASE_URL="http://117.158.206.87:8743";
    //登录
    public static final String LOGIN_URL = "/NewGPSTrace2.0/app/applogin.do";
    //警铃url
    public static String WARNING_URL="/NewGPSTrace2.0/app/getwarning.do";
    public static String WARING_READ="/NewGPSTrace2.0/app/UpdateMessage.do";
    //集装箱设备定位:
    public static String SELF_DEVICE_LOCATION="/NewGPSTrace2.0/app/appDevicedetail.do";
    //设备路线定位
    public static String DEVICE_ROUTE="/NewGPSTrace2.0/app/appDeviceRoute.do";
    /////////预警页面对应的常量数据信息
    ////////ALARMTYPE：预警类型
    public static String ALARMTYPE_RAIL="围栏";
    public static String ALARMTYPE_Light="见光";
    public static String ALARMTYPE_RAIL_Light="围栏+见光";
    public static String RAIL_TYPE="0";
    public static String Light_TYPE="1";
    public static String RAIL_LIGHT_TYPE="2";

    ////////////预警消息阅读状态
    public static String UNREAD="未阅读";
    public static String READ="已通知";
    public static String UNREAD_TYPE="0";//未阅读
    public static String READ_TYPE="1";//已通知
    //设置选中，未选中状态
    public  static boolean CHECKED=true;
    public  static boolean UNCHECKED=false;

}
