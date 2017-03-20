package org.zhonghao.gps.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/3/20.
 */
public class DecimalUtils {
    public static String getTxt(String txt) {
        return txt.substring(0, 4);
    }

    public static String getDouble(double latitude) {
        DecimalFormat dical=new DecimalFormat("0.0");
        return dical.format(latitude)+"°";
    }
}
