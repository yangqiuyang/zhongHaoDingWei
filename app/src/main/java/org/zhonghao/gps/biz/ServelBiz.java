package org.zhonghao.gps.biz;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.entity.DevicesLocateInfo;
import org.zhonghao.gps.entity.DevicesSelfLocation;
import org.zhonghao.gps.entity.LoginResponse;
import org.zhonghao.gps.entity.LoginResponseDevice;
import org.zhonghao.gps.entity.NameDates;
import org.zhonghao.gps.entity.RequestQueryDevicesMoveInfo;
import org.zhonghao.gps.entity.ResponseDevicesMoveResult;
import org.zhonghao.gps.entity.ResponseUserinfo;
import org.zhonghao.gps.utils.MyJsonResponse;
import org.zhonghao.gps.utils.ProgressUtils;
import org.zhonghao.gps.utils.Urls;

/**
 * Created by lenovo on 2016/11/24.
 */

public class ServelBiz {

    //登录信息
    public static LoginResponse LoginBiz(ResponseUserinfo responseUserinfo, Context context) {
        URL url;
        HttpURLConnection connection;
        Gson gson = new Gson();
        try {
            url = new URL(Urls.BASE_URL+Urls.LOGIN_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            //将bean数据解析成json类型
            String jsonDate = gson.toJson(responseUserinfo);
            connection.setDoOutput(true);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(2000);
            //上传用户信息
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(jsonDate);
            Log.d("serve", "loginBiz:得到的conn" + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                MyApplication .responseState = true;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                }
                String response = sb.toString();
                if(TextUtils.isEmpty(response)){
                    Toast.makeText(context,"请求数据为空，请稍后再试",Toast.LENGTH_SHORT).show();
                    return null;
                }else {
                    LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONArray deviceArr = jsonObject1.getJSONArray("devices");
                    ArrayList<LoginResponseDevice> deviceList = new ArrayList();
                    for (int i = 0; i < deviceArr.length(); i++) {
                        JSONObject jsonObject = (JSONObject) deviceArr.get(i);
                        LoginResponseDevice device = new LoginResponseDevice();
                        device.setDeviceID(jsonObject.getString("deviceid"));
                        device.setDeviceName(jsonObject.getString("devicename"));
                        deviceList.add(device);
                    }
                    loginResponse.setDevices(deviceList);
                    MyApplication.myUserInnfo = loginResponse.getUserinfo();//用户信息
                    MyApplication.loginResponse = loginResponse;//设备信息
                    return loginResponse;
                }
            } else {
                MyApplication.responseState = false;
            }
        } catch (Exception e) {
            MyApplication.responseState = false;
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public static void getDvicesMoveLocus(RequestQueryDevicesMoveInfo requestDevicesMoveInfo, final Context context, final Handler handler, final int devicePosition) {
        ProgressUtils.showProgress(MyApplication.context);//显示进度条
        RequestQueue queue= Volley.newRequestQueue(context);
        String jsonDate = MyApplication.gson.toJson(requestDevicesMoveInfo);
        MyJsonResponse res=new MyJsonResponse(Urls.BASE_URL+Urls.DEVICE_ROUTE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<ResponseDevicesMoveResult> list = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        list.add(MyApplication.gson.fromJson(obj.toString(),ResponseDevicesMoveResult.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //  Log.d("serve", "loginBiz:得到的jason" + response);
                //请求过来是一个JsonArray格式的
                Log.i("list",list.get(0).toString());
                if (list.get(0) != null) {
                    MyApplication.responseDevicesMove=list.get(0);
                    Message msg = handler.obtainMessage();
                    msg.what = 2;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("response", list.get(0));
                    bundle.putSerializable("position",devicePosition);//传送过来的是一个deviceid号
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
                else {
                    Toast.makeText(context,"请求数据为空，请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.sendEmptyMessage(5);
            }
        },jsonDate){

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }
        };
        queue.add(res);
    }

    //设备定位信息请求,获取设备信息
    public static void getSelfLocation(final Context context, final int position, LoginResponseDevice loginDevice) {
        final Handler handler=((MyApplication)context.getApplicationContext()).getMyHandler();
        final List<DevicesLocateInfo> deMsg=new ArrayList<>();
        DevicesSelfLocation devices= new DevicesSelfLocation();//上传的设备信息和用户名
        NameDates nameDates =MyApplication.nameDates;//用户名信息等
        List<String> deviceID=new ArrayList<>();
        List<String> userinfo=new ArrayList<>();
        deviceID.add(loginDevice.getDeviceID());//设备编号
        userinfo.add(nameDates.getUsername());//用户名
        //请求数据需上传的json数据
        devices.setDeviceID(deviceID);
        devices.setUserinfo(userinfo);
        String json = new Gson().toJson(devices);
        RequestQueue queue= Volley.newRequestQueue(context);
        ProgressUtils.showProgress(MyApplication.context);//加载轨迹数据进度条
        MyJsonResponse res=new MyJsonResponse(Urls.BASE_URL + Urls.SELF_DEVICE_LOCATION, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        deMsg.add(MyApplication.gson.fromJson(object.toString(),DevicesLocateInfo.class));//设备定位信息
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //获取设备信息
                if(deMsg!=null) {
                    MyApplication.devicesLocate = deMsg.get(0);
                    MyApplication.locatePosition = position;
                    Message msg = Message.obtain();
                    msg.obj = deMsg.get(0);
                    msg.arg1 = position;
                    msg.what = 0x11;
                    handler.sendMessage(msg);
                }else {
                    Toast.makeText(context,"请求数据为空，请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.sendEmptyMessage(5);
            }
        },json){

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
           queue.add(res);
    }
}
