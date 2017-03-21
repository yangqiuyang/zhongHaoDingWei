package org.zhonghao.gps.biz;

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
import org.zhonghao.gps.utils.DecimalUtils;
import org.zhonghao.gps.utils.ProgressUtils;

import static org.zhonghao.gps.application.MyApplication.devicesInfo;

/**
 * Created by Administrator on 2017/3/20.
 */

public class LocationMsgShow {
    public static void getLocationMsg(final Context context, Marker marker, final BaiduMap mBaiduMap, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        BubbleLayout bubbleLayout = (BubbleLayout) inflater.inflate(R.layout.bubble, null);
        TextView user_name = (TextView) bubbleLayout.findViewById(R.id.tv_user_name);
        TextView container_type = (TextView) bubbleLayout.findViewById(R.id.tv_container_type);
        TextView route_type= (TextView) bubbleLayout.findViewById(R.id.tv_route_type);
        TextView devicesnum = (TextView) bubbleLayout.findViewById(R.id.tv_devicesnum);
        TextView send_time = (TextView) bubbleLayout.findViewById(R.id.tv_send_time);
        TextView container_id = (TextView) bubbleLayout.findViewById(R.id.tv_container_id);
        TextView train_id = (TextView) bubbleLayout.findViewById(R.id.tv_train_id);
        TextView lattitude = (TextView) bubbleLayout.findViewById(R.id.tv_Latitude_id);
        TextView longtitude = (TextView) bubbleLayout.findViewById(R.id.tv_Longtitude_id);
        final TextView address = (TextView) bubbleLayout.findViewById(R.id.tv_address_id);
        Button query = (Button) bubbleLayout.findViewById(R.id.btn_query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoveLocusActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        user_name.setText("用户名：" + devicesInfo.getUserName());
        if (devicesInfo.getContainerType() == "0") {
            container_type.setText("集装箱类型：普通车厢");
        } else {
            container_type.setText("集装箱类型：冷藏车厢");
        }
        if (devicesInfo.getRouteType() == "0") {
            route_type.setText("线路类型：去程");
        } else {
            route_type.setText("线路类型：回程");
        }
        devicesnum.setText("设备号：" + devicesInfo.getDeviceID());
        send_time.setText("发车时间: "+devicesInfo.getSendTime());
        container_id.setText("集装箱号：" + devicesInfo.getContainerId());
        train_id.setText("班列号：" + devicesInfo.getTrainId());
        lattitude.setText("经度：" + devicesInfo.getLatitude().substring(0,5)+"°");
        longtitude.setText("纬度：" + devicesInfo.getLongitude().substring(0,5)+"°");
      //
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(bubbleLayout, marker.getPosition(), -70);
        getAddress(context,address,mInfoWindow,mBaiduMap);
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

    private static void getAddress(final Context context, final TextView address, final InfoWindow mInfoWindow, final BaiduMap mBaiduMap) {
        //查询集装箱所在的城市
        GeoCoder geoCoder=GeoCoder.newInstance();
        LatLng latLng = new LatLng(Double.valueOf(devicesInfo.getLatitude()),Double.valueOf( devicesInfo.getLongitude()));
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                ProgressUtils.hideProgress();
                if(result==null||result.error!= SearchResult.ERRORNO.NO_ERROR){
                    Toast.makeText(context, "抱歉，未能找到地址信息", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                else{
                    ReverseGeoCodeResult.AddressComponent addressDetail = result.getAddressDetail();
                    address.setText("地址："+addressDetail.countryName+"-"+addressDetail.province+"-"+addressDetail.city);
                }
                mBaiduMap.showInfoWindow(mInfoWindow);
            }
        });
    }

    public static void getRouteMsg(final Context context, int position, final Marker marker, final BaiduMap mBaiduMap, final double latitude, double longitude) {
        final LayoutInflater inflater=LayoutInflater.from(context);
        final LatLng latLng=new LatLng(latitude,longitude);
        //poi
        GeoCoder geoCoder=GeoCoder.newInstance();
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        final BubbleLayout bubbleLayout = (BubbleLayout) inflater.inflate(R.layout.bubble_city_location, null);
        final TextView city= (TextView) bubbleLayout.findViewById(R.id.city);
        final TextView country= (TextView) bubbleLayout.findViewById(R.id.country);
        final TextView latitudeTxt= (TextView) bubbleLayout.findViewById(R.id.latitude);
        final TextView longtitudeTxt= (TextView) bubbleLayout.findViewById(R.id.longtitude);
        final TextView province= (TextView) bubbleLayout.findViewById(R.id.province);
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                ProgressUtils.hideProgress();
                if(result==null||result.error!= SearchResult.ERRORNO.NO_ERROR){
                    Toast.makeText(context, "抱歉，未能找到地址信息", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                else{
                    ReverseGeoCodeResult.AddressComponent addressDetail = result.getAddressDetail();
                    InfoWindow mInfoWindow = new InfoWindow(bubbleLayout, marker.getPosition(), -70);
                        city.setText(addressDetail.city+addressDetail.district+addressDetail.street+addressDetail.streetNumber);
                        province.setText(addressDetail.province);
                        country.setText(addressDetail.countryName);
                        latitudeTxt.setText(DecimalUtils.getDouble(latLng.latitude));
                        longtitudeTxt.setText(DecimalUtils.getDouble(latLng.longitude));
                        mBaiduMap.showInfoWindow(mInfoWindow);
                        ProgressUtils.hideProgress();
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
