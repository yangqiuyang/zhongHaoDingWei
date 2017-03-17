package org.zhonghao.gps.biz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.daasuu.bl.BubbleLayout;

import org.zhonghao.gps.R;
import org.zhonghao.gps.activity.MoveLocusActivity;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.entity.DevicesInfo;
import org.zhonghao.gps.entity.MyLocation;
import org.zhonghao.gps.entity.ResponseDevicesMove;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/12/7.
 */

public class MapBiz {
    public static void startMap(final DevicesInfo devicesInfo, MapView mapView, final BaiduMap mBaiduMap, final Context context, BitmapDescriptor bitmap, final int position)throws Exception {
        mapView.getMap().clear();
        LatLng point1 = (new LatLng(Double.parseDouble(devicesInfo.getLatitude()), Double.parseDouble(devicesInfo.getLongitude())));
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(point1);
        LatLng point = converter.convert();
        MyApplication.point = point;
        Log.d("serve", "进入了handler" + point.toString());
        Log.d("serve", "进入了handler" + devicesInfo.getLatitude());

//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .zIndex(9);

//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
//        MapStatus mapStatus = new MapStatus.Builder().zoom(7).target(point[0]).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.animateMapStatus(mMapStatusUpdate);
        final Boolean[] flags = {false};
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LayoutInflater inflater = LayoutInflater.from(context);
                BubbleLayout bubbleLayout = (BubbleLayout) inflater.inflate(R.layout.bubble, null);
                TextView textView = (TextView) bubbleLayout.findViewById(R.id.tv_user_name);
                TextView textView1 = (TextView) bubbleLayout.findViewById(R.id.tv_container_type);
                TextView textView2 = (TextView) bubbleLayout.findViewById(R.id.tv_route_type);
                TextView textView3 = (TextView) bubbleLayout.findViewById(R.id.tv_devicesnum);
                TextView textView4 = (TextView) bubbleLayout.findViewById(R.id.tv_send_time);
                TextView textView5 = (TextView) bubbleLayout.findViewById(R.id.tv_container_id);
                TextView textView6 = (TextView) bubbleLayout.findViewById(R.id.tv_train_id);
                TextView textView7= (TextView) bubbleLayout.findViewById(R.id.tv_Latitude_id);
                TextView textView8=(TextView) bubbleLayout.findViewById(R.id.tv_Longtitude_id);
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
                textView7.setText("经度: "+devicesInfo.getLatitude()+"°");
                textView8.setText("纬度: "+devicesInfo.getLongitude()+"°");
                //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                InfoWindow mInfoWindow = new InfoWindow(bubbleLayout, marker.getPosition(), -70);
                //显示InfoWindow
                if (!flags[0]) {
                    mBaiduMap.showInfoWindow(mInfoWindow);
                    flags[0] = true;
                } else {
                    mBaiduMap.hideInfoWindow();
                    flags[0] = false;
                }

                return true;
            }
        });

    }

    public static void moveLocusService(final ResponseDevicesMove responseDevicesMove, MapView mapView, final BaiduMap mBaiduMap, final Context context, Handler handler) {

        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_st);
        BitmapDescriptor bitmap2 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_en);

        ArrayList<MyLocation> devicesLocation = responseDevicesMove.getLocation();
        List<LatLng> point = new ArrayList<LatLng>();
        if(devicesLocation.size() < 1){
            handler.sendEmptyMessage(7);
        }
        Log.d("serve", "进入了MapBiz");
//        int j=0;
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
        Log.d("serve", point.toString());
        if (point.size() > 0) {
            mBaiduMap.clear();
        }
        OverlayOptions startOption = new MarkerOptions()
                .position(point.get(0))
                .icon(bitmap1)
                .zIndex(9);
        OverlayOptions endOption = new MarkerOptions()
                .position(point.get(point.size() - 1))
                .icon(bitmap2)
                .zIndex(9);
       //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(startOption);
        mBaiduMap.addOverlay(endOption);
        // 构造折线点坐标
       //构建分段颜色索引数组
        List<Integer> colors = new ArrayList<>();
        colors.add(Integer.valueOf(Color.BLUE));
        colors.add(Integer.valueOf(Color.RED));
        colors.add(Integer.valueOf(Color.YELLOW));
        colors.add(Integer.valueOf(Color.GREEN));
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(Integer.valueOf(Color.RED)).points(point);
        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.baidupano_floortab_line);
        OverlayOptions Polyline = new PolylineOptions().width(10).color(Color.RED).points(point).customTexture(bitmapDescriptor);
        mBaiduMap.addOverlay(Polyline);

//        MapStatus mapStatus = new MapStatus.Builder().zoom(7).target(point[0]).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(point.get(0));
        mBaiduMap.animateMapStatus(mMapStatusUpdate);
    }

}
