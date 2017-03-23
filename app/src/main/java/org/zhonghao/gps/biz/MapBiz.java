package org.zhonghao.gps.biz;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import org.zhonghao.gps.R;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.entity.DevicesLocateInfo;
import org.zhonghao.gps.entity.MyLocation;
import org.zhonghao.gps.entity.ResponseDevicesMoveResult;
import org.zhonghao.gps.utils.ProgressUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/12/7.
 */

public class MapBiz {
    public static void startMap(MapView mapView, BaiduMap mBaiduMap, final Context context, BitmapDescriptor bitmap, int position, DevicesLocateInfo deviceInfo)throws Exception {
        mapView.getMap().clear();
        LatLng point1 = (new LatLng(Double.parseDouble(deviceInfo.getLatitude()), Double.parseDouble(deviceInfo.getLongitude())));
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(point1);
        LatLng point = converter.convert();
        MyApplication.point = point;
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .zIndex(9);
       //在地图上添加Marker，并显示
        Marker marker = (Marker) mBaiduMap.addOverlay(option);
        Bundle bundle=new Bundle();
        bundle.putInt("markerType",0x12);
        bundle.putInt("position",position);
        marker.setExtraInfo(bundle);
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.animateMapStatus(mMapStatusUpdate);
        ProgressUtils.hideProgress();//隐藏进度条
    }
    public static void moveLocusService(final ResponseDevicesMoveResult responseDevicesMove, final MapView mapView, final BaiduMap mBaiduMap, final Context context, Handler handler, int devicePosition) {
       //清除原来的覆盖物
        mapView.getMap().clear();
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_st);
        BitmapDescriptor bitmap2 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_en);
        ArrayList<MyLocation> devicesLocation = responseDevicesMove.getLocation();
        final List<LatLng> point = new ArrayList<LatLng>();
        if(devicesLocation.size() < 1){
            handler.sendEmptyMessage(5);//数据没有请求到
            return;
        }
        for (int i = 0; i < devicesLocation.size() - 1; i++) {

            LatLng latLng1 = new LatLng(Double.parseDouble(devicesLocation.get(i).getLatD()), Double.parseDouble(devicesLocation.get(i).getLonD()));
            if (latLng1 != null) {
                //转换成百度坐标
                CoordinateConverter converter = new CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                converter.coord(latLng1);
                LatLng latLng = converter.convert();
                point.add(latLng);
            }
        }
        if (point.size() > 0) {
            mBaiduMap.clear();
        }
        OverlayOptions option=null;
        Bundle bundle=null;
        for (int i = 0; i < point.size(); i++) {
            if(i==0){
               option = new MarkerOptions()
                        .position(point.get(0))
                        .icon(bitmap1)
                        .zIndex(9);
                    //添加覆盖物
                bundle=new Bundle();
                bundle.putInt("markerType",0x13);
                bundle.putInt("position",0);//点击的Marker位置
                bundle.putInt("devicePosition",devicePosition);//diviced号
                addMarker(mBaiduMap,option,bundle);
            }
            else if(i==point.size()-1){
                option = new MarkerOptions()
                        .position(point.get(point.size() - 1))
                        .icon(bitmap2)
                        .zIndex(9);
                bundle=new Bundle();
                bundle.putInt("markerType",0x14);
                bundle.putInt("position",point.size()-1);//点击的覆盖物位置
                bundle.putInt("devicePosition",devicePosition);//dividce号
                addMarker(mBaiduMap,option,bundle);
            }

        }
        // 构造折线点坐标
       //构建分段颜色索引数组
        List<Integer> colors = new ArrayList<>();
        colors.add(Integer.valueOf(Color.BLUE));
        colors.add(Integer.valueOf(Color.RED));
        colors.add(Integer.valueOf(Color.YELLOW));
        colors.add(Integer.valueOf(Color.GREEN));
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(Integer.valueOf(Color.RED)).points(point);
        mBaiduMap.addOverlay(ooPolyline);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.baidupano_floortab_line);
        OverlayOptions Polyline = new PolylineOptions().width(10).color(Color.RED).points(point).customTexture(bitmapDescriptor);
        mBaiduMap.addOverlay(Polyline);
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(point.get(0));
        mBaiduMap.animateMapStatus(mMapStatusUpdate);
        ProgressUtils.hideProgress();//隐藏进度条
    }

    private static void addMarker(BaiduMap mBaiduMap, OverlayOptions option, Bundle bundle) {
        //根据经纬度返回地理位置
        Marker overlay = (Marker) mBaiduMap.addOverlay(option);
        overlay.setExtraInfo(bundle);
    }
}
