package org.zhonghao.gps.biz;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
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

import org.zhonghao.gps.activity.MoveLocusActivity;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.entity.DevicesInfo;
import org.zhonghao.gps.entity.DevicesLocationMsg;
import org.zhonghao.gps.entity.DevicesSelfLocation;
import org.zhonghao.gps.entity.NameDates;
import org.zhonghao.gps.entity.RequestDevices;
import org.zhonghao.gps.entity.RequestQueryDevicesMoveInfo;
import org.zhonghao.gps.entity.ResponseDevicesMove;
import org.zhonghao.gps.entity.ResponseUserinfo;
import org.zhonghao.gps.utils.Constants;
import org.zhonghao.gps.utils.MyJsonResponse;
import org.zhonghao.gps.utils.ProgressUtils;
import org.zhonghao.gps.utils.Urls;

/**
 * Created by lenovo on 2016/11/24.
 */

public class ServelBiz {

    public static DevicesInfo.LoginResponse LoginBiz(ResponseUserinfo responseUserinfo, Context context) {
       // Log.d("serve", "LoginBiz: 进入了");
        URL url;
        HttpURLConnection connection;
        Gson gson = new Gson();
        try {
            url = new URL(Constants.URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            //将bean数据解析成json类型
            String jsonDate = gson.toJson(responseUserinfo);
            Log.d("serve", "loginBiz:发出的json" + jsonDate);
            connection.setDoOutput(true);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(2000);
            //上传用户信息
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(jsonDate);
           // Log.d("serve", "loginBiz:得到的conn" + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                MyApplication .responseState = true;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                }
                String response = sb.toString();
               // Log.d("serve", "123loginBiz:得到的jason" + response);
                DevicesInfo.LoginResponse loginResponse = gson.fromJson(response, DevicesInfo.LoginResponse.class);

                JSONObject jsonObject1 = new JSONObject(response);
                JSONArray deviceArr = jsonObject1.getJSONArray("devices");
             //   Log.d("device", deviceArr.toString());
                ArrayList<DevicesInfo> deviceList = new ArrayList();
                for (int i = 0; i < deviceArr.length(); i++){
                    JSONObject jsonObject = (JSONObject) deviceArr.get(i);
                    DevicesInfo device = new DevicesInfo();
                    device.setDeviceID(jsonObject.getString("deviceid"));
                    device.setDeviceName(jsonObject.getString("devicename"));
                    deviceList.add(device);
                }
                loginResponse.setDevices(deviceList);

              //  Log.d("serve", "得到的User Info是=====" + gson.toJson(loginResponse.getUserinfo()));

                MyApplication.myUserInnfo = loginResponse.getUserinfo();
                return loginResponse;
            } else {
                MyApplication.responseState = false;
            }
        } catch (Exception e) {
          //  Log.d("serve", "j进入catch" + e.toString());
            MyApplication.responseState = false;
            e.printStackTrace();
            return null;
        }finally {
        }
        MyApplication.responseState = false;
        return null;
    }

    public static void getDvices(RequestDevices requestDevices, Context context, final Handler handler) {
       /* HttpURLConnection connection;
        Gson gson = new Gson();
        URL url;
        ArrayList<DevicesInfo> list = new ArrayList<DevicesInfo>();
        try {
            url=new URL(Urls.DEVICE_DETAIL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            String jsonDate = gson.toJson(requestDevices);
         //   Log.d("serve", "123456loginBiz:发出的" + jsonDate);
            connection.setDoOutput(true);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(jsonDate);
            if (connection.getResponseCode() == 200) {
             //   Log.d("serve", "456loginBiz:得到的conn" + connection.getResponseCode());
                BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                }
                String response = sb.toString();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    DevicesInfo deviceInfo = gson.fromJson(obj.toString(), DevicesInfo.class);
                    list.add(deviceInfo);
                }
                Message msg = handler.obtainMessage();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putSerializable("list",list);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(5);
        }finally {
            return list;

        }*/
        final ArrayList<DevicesInfo> list = new ArrayList<DevicesInfo>();
        Gson gson = new Gson();
        String jsonDate = gson.toJson(requestDevices);
        RequestQueue queue= Volley.newRequestQueue(context);
        MyJsonResponse res=new MyJsonResponse(Urls.DEVICE_DETAIL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        DevicesInfo deviceInfo = new Gson().fromJson(object.toString(),DevicesInfo.class);
                        list.add(deviceInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = handler.obtainMessage();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putSerializable("list",list);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.sendEmptyMessage(5);
            }
        },jsonDate);
        queue.add(res);
    }
    public static void getDvicesMoveLocus(RequestQueryDevicesMoveInfo requestDevicesMove, Context context, final Handler handler) {
        /*URL url;
        HttpURLConnection connection;
        Gson gson = new Gson();
        ArrayList<ResponseDevicesMove> list = new ArrayList<>();
        ResponseDevicesMove responseDevicesMove = new ResponseDevicesMove();
        try {
            url = new URL("http://gps.zhonghaokeji.cn/NewGPSTrace2.0/app/appDeviceRoute.do");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            String jsonDate = gson.toJson(requestDevicesMove);
            Log.d("serve", "123456loginBiz:发出的" + jsonDate);
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(jsonDate);
          //  Log.d("serve", "456loginBiz:得到的conn" + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                }
                String response = sb.toString();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    list.add(gson.fromJson(obj.toString(),ResponseDevicesMove.class));
                }
                responseDevicesMove = list.get(0);
              //  Log.d("serve", "loginBiz:得到的jason" + response);
                if (requestDevicesMove != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = 2;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("response", responseDevicesMove);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    return list.get(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(5);
        }
        return null;*/
        final Gson gson = new Gson();
        final ArrayList<ResponseDevicesMove> list = new ArrayList<>();
        RequestQueue queue= Volley.newRequestQueue(context);
        String jsonDate = gson.toJson(requestDevicesMove);
        MyJsonResponse res=new MyJsonResponse(Urls.DEVICE_ROUTE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        list.add(gson.fromJson(obj.toString(),ResponseDevicesMove.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //  Log.d("serve", "loginBiz:得到的jason" + response);
                if (list.get(0) != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = 2;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("response", list.get(0));
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.sendEmptyMessage(5);
            }
        },jsonDate);
        queue.add(res);
    }

    //设备定位信息请求
    public static void getSelfLocation(Context context,int position) {
        final Handler handler=((MyApplication)context.getApplicationContext()).getMyHandler();
        final List<DevicesInfo> deMsg=new ArrayList<>();
        DevicesSelfLocation devices= MyApplication.devicesSelfLocation;
        ArrayList<DevicesInfo> myDevice = MyApplication.myDevice;
        NameDates nameDates =MyApplication.nameDates;
        List<String> deviceID=new ArrayList<>();
        List<String> userinfo=new ArrayList<>();
        deviceID.add(myDevice.get(position).getDeviceID());
        userinfo.add(nameDates.getUsername());
        devices.setDeviceID(deviceID);
        devices.setUserinfo(userinfo);
        String json = new Gson().toJson(devices);
        RequestQueue queue= Volley.newRequestQueue(context);
        ProgressUtils.showProgress(context);
        MyJsonResponse res=new MyJsonResponse(Urls.BASE_URL + Urls.SELF_DEVICE_LOCATION, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        deMsg.add(new Gson().fromJson(object.toString(),DevicesInfo.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                MyApplication.devicesInfo=deMsg.get(0);
                handler.sendEmptyMessage(0x11);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.sendEmptyMessage(5);
            }
        },json);
           queue.add(res);
    }
}
