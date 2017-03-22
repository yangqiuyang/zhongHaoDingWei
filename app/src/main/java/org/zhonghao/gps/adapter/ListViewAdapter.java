package org.zhonghao.gps.adapter;

import android.app.Application;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import org.zhonghao.gps.R;
import org.zhonghao.gps.activity.MapActivity;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.biz.ServelBiz;
import org.zhonghao.gps.entity.DevicesInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2016/10/18.
 */

public class ListViewAdapter extends BaseAdapter implements View.OnClickListener {

    //得到一个LayoutInfalter对象用来导入布局
    private LayoutInflater inflater;
    private DevicesInfo.LoginResponse loginResponse;
    private ArrayList<DevicesInfo> devicesInfos;
    private int type;
    private Context context;
    DrawerLayout drawer;

    /**
     * 构造函数
     */
    public ListViewAdapter(Context context, DevicesInfo.LoginResponse loginResponse,DrawerLayout drawer) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.loginResponse = loginResponse;
        this.drawer=drawer;
    }


    @Override
    public int getCount() {
            return MyApplication.myDevice.size();
    }

    @Override
    public Object getItem(int position) {

            return MyApplication.myDevice.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder mHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null) {
            convertView = inflater.inflate(R.layout.list_layout, parent,false);
            holder = new ViewHolder();
            ButterKnife.bind(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.devicesMumber.setText(MyApplication.myDevice.get(position).getDeviceID());
        holder.devicesMumber.setOnClickListener(this);
        holder.devicesMumber.setTag(position);
        mHolder=holder;
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_devicesnum:
                int position = (int) mHolder.devicesMumber.getTag();
                ServelBiz.getSelfLocation(context,position);
                drawer.closeDrawer(GravityCompat.END);
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.tv_devicesnum)
        TextView devicesMumber;
    }

}
