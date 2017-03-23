package org.zhonghao.gps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zhonghao.gps.R;
import org.zhonghao.gps.activity.MapActivity;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.biz.ServelBiz;
import org.zhonghao.gps.entity.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/23.
 */

public class RecyclerDeviceAdapter extends RecyclerView.Adapter<RecyclerDeviceAdapter.DeviceHolder> {
    Context context;
    LoginResponse data;
    LayoutInflater inflater;
    public RecyclerDeviceAdapter(Context context) {
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    public void addData(LoginResponse loginResponse) {
        this.data=loginResponse;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return this.data!=null?this.data.getDevices().size():0;
    }


    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceHolder(inflater.inflate(R.layout.list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {
        holder.deviceNo.setText(this.data.getDevices().get(position).getDeviceID());
    }



    public class DeviceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.device_num_list)
        TextView deviceNo;
        public DeviceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            deviceNo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.device_num_list:
                    ServelBiz.getSelfLocation(context,getAdapterPosition(), MyApplication.loginResponse.getDevices().get(getAdapterPosition()));
                    break;
            }
        }
    }
}
