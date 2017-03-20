package org.zhonghao.gps.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.daasuu.bl.BubbleLayout;

import org.zhonghao.gps.R;
import org.zhonghao.gps.activity.MapActivity;
import org.zhonghao.gps.activity.MoveLocusActivity;

import static org.zhonghao.gps.application.MyApplication.devicesInfo;

/**
 * Created by Administrator on 2017/3/20.
 */

public class LocationMsgShow {
    public static void getLocationMsg(final Context context, Marker marker, final BaiduMap mBaiduMap, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        BubbleLayout bubbleLayout = (BubbleLayout) inflater.inflate(R.layout.bubble, null);
        TextView textView = (TextView) bubbleLayout.findViewById(R.id.tv_user_name);
        TextView textView1 = (TextView) bubbleLayout.findViewById(R.id.tv_container_type);
        TextView textView2 = (TextView) bubbleLayout.findViewById(R.id.tv_route_type);
        TextView textView3 = (TextView) bubbleLayout.findViewById(R.id.tv_devicesnum);
        TextView textView4 = (TextView) bubbleLayout.findViewById(R.id.tv_send_time);
        TextView textView5 = (TextView) bubbleLayout.findViewById(R.id.tv_container_id);
        TextView textView6 = (TextView) bubbleLayout.findViewById(R.id.tv_train_id);
        TextView textView7 = (TextView) bubbleLayout.findViewById(R.id.tv_Latitude_id);
        TextView textView8 = (TextView) bubbleLayout.findViewById(R.id.tv_Longtitude_id);
        Button query = (Button) bubbleLayout.findViewById(R.id.btn_query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoveLocusActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        textView.setText("用户名：" + devicesInfo.getUserName());
        if (devicesInfo.getContainerType() == "0") {
            textView1.setText("集装箱类型：普通车厢");
        } else {
            textView1.setText("集装箱类型：冷藏车厢");
        }
        if (devicesInfo.getRouteType() == "0") {
            textView2.setText("线路类型：去程");
        } else {
            textView2.setText("线路类型：回程");
        }
        textView3.setText("设备号：" + devicesInfo.getDeviceID());
        textView4.setText(devicesInfo.getSendTime());
        textView5.setText("集装箱号：" + devicesInfo.getContainerId());
        textView6.setText("班列号：" + devicesInfo.getTrainId());
        textView7.setText("经度：" + devicesInfo.getLatitude());
        textView8.setText("纬度：" + devicesInfo.getLongitude());
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(bubbleLayout, marker.getPosition(), -70);
        //显示InfoWindow
         mBaiduMap.showInfoWindow(mInfoWindow);
        //隐藏
         mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
             @Override
             public void onMapClick(LatLng latLng) {
                 mBaiduMap.hideInfoWindow();
             }

             @Override
             public boolean onMapPoiClick(MapPoi mapPoi) {
                 return false;
             }
         });
    }

    public static void getRouteMsg(final Context context, int position, final Marker marker, final BaiduMap mBaiduMap, final double latitude, double longitude) {
        final LayoutInflater inflater=LayoutInflater.from(context);
        final LatLng latLng=new LatLng(latitude,longitude);
        //poi
        GeoCoder geoCoder=GeoCoder.newInstance();
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        final Boolean[] flags = {false};
        final BubbleLayout bubbleLayout = (BubbleLayout) inflater.inflate(R.layout.bubble_city_location, null);
        final TextView city= (TextView) bubbleLayout.findViewById(R.id.city);
        TextView country= (TextView) bubbleLayout.findViewById(R.id.country);
        final TextView latitudeTxt= (TextView) bubbleLayout.findViewById(R.id.latitude);
        final TextView longtitudeTxt= (TextView) bubbleLayout.findViewById(R.id.longtitude);
        final TextView province= (TextView) bubbleLayout.findViewById(R.id.province);
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if(result==null||result.error!= SearchResult.ERRORNO.NO_ERROR){
                    Toast.makeText(context, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                else{
                    ReverseGeoCodeResult.AddressComponent addressDetail = result.getAddressDetail();
                    InfoWindow mInfoWindow = new InfoWindow(bubbleLayout, marker.getPosition(), -70);
                        city.setText(addressDetail.city);
                        province.setText(addressDetail.province);
                        latitudeTxt.setText(DecimalUtils.getDouble(latLng.latitude));
                        longtitudeTxt.setText(DecimalUtils.getDouble(latLng.longitude));
                        mBaiduMap.showInfoWindow(mInfoWindow);

                        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                mBaiduMap.hideInfoWindow();
                            }

                            @Override
                            public boolean onMapPoiClick(MapPoi mapPoi) {
                                return false;
                            }
                        });

                    }
                }
        });

    }
}
