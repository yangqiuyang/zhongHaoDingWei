package org.zhonghao.gps.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.zhonghao.gps.R;
import org.zhonghao.gps.entity.WarningMark;
import org.zhonghao.gps.entity.WarningResponseData;
import org.zhonghao.gps.utils.MyOnCheckedChangeListener;
import org.zhonghao.gps.utils.MyOnClickListener;
import org.zhonghao.gps.utils.Urls;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/24.
 */

public class WarningMsgAdapter extends BaseAdapter {
    Context context;
    List<WarningResponseData> data;
    LayoutInflater inflate;
    boolean isChecked;
    public WarningMsgAdapter(Context context) {
        this.context=context;
        inflate=LayoutInflater.from(context);
    }

    public void addData(List<WarningResponseData> data) {
        this.data=data;
        notifyDataSetChanged();
    }
    //设置选中栏是否显示
    public void addCheck(boolean isChecked) {
        this.isChecked=isChecked;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.data!=null?this.data.size():0;
    }

    @Override
    public Object getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        WarningHolder holder=null;
        if(convertView==null){
            convertView = this.inflate.inflate(R.layout.waring_msg_list, parent, false);
            holder=new WarningHolder();
            ButterKnife.bind(holder,convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (WarningHolder) convertView.getTag();
        }
        WarningResponseData.AlarmListBean alarm = this.data.get(position).getAlarmList().get(0);
      //设备的IMEI号
        holder.deviceName.setText(String.valueOf(alarm.getImei()));
       //预警类型
        if(alarm.getAlarmType().equals(Urls.RAIL_TYPE)){
            holder.ringState.setText(Urls.ALARMTYPE_RAIL);
        }
        else if(alarm.getAlarmType().equals(Urls.Light_TYPE)){
            holder.ringState.setText(Urls.ALARMTYPE_Light);
        }
        else if(alarm.getAlarmType().equals(Urls.RAIL_LIGHT_TYPE)){
            holder.ringState.setText(Urls.ALARMTYPE_RAIL_Light);
        }
        //通知状态,是否已读状态
        if(alarm.getReadState().equals(Urls.UNREAD_TYPE)){
            holder.readState.setText(Urls.UNREAD);
        }
        else if(alarm.getReadState().equals(Urls.READ_TYPE)){
            holder.readState.setText(Urls.READ);
        }
        //消息的时间
        holder.time.setText(String.valueOf(alarm.getTime()));
        //是否阅读信息
        holder.warningContent.setOnClickListener(new MyOnClickListener(position){
            @Override
            public void onClick(View v) {
                data.get(mPosition).getAlarmList().get(0).setReadState(Urls.READ);
                notifyDataSetChanged();
            }
        });
        //设置选中框是否显示
        if(isChecked){
            holder.checkMsg.setVisibility(View.VISIBLE);
        }
        else{
            holder.checkMsg.setVisibility(View.GONE);
        }
        //设置标记是否已经选中
        if(alarm.getReadState().equals(Urls.READ_TYPE)){
            holder.checkMsg.setChecked(true);
        } else if (alarm.getReadState().equals(Urls.UNREAD_TYPE)) {
            holder.checkMsg.setChecked(false);
        }
        //更改是否已读状态
        holder.checkMsg.setOnCheckedChangeListener(new MyOnCheckedChangeListener(position){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WarningResponseData.AlarmListBean alarmBean = data.get(mPosition).getAlarmList().get(0);
                if(isChecked){
                  alarmBean.setReadState(Urls.READ_TYPE);
                }
                else{
                    alarmBean.setReadState(Urls.UNREAD_TYPE);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    static class WarningHolder{
        @BindView(R.id.check_msg_warning)
        CheckBox checkMsg;
        @BindView(R.id.device_name_warning)
        TextView deviceName;
        @BindView(R.id.ring_state_warning)
        TextView ringState;//
        @BindView(R.id.read_state_waring)
        TextView readState;//是否阅读
        @BindView(R.id.time_warning)
        TextView time;
        @BindView(R.id.back_warning)
        ImageView back;
        @BindView(R.id.warning_content)
        LinearLayout warningContent;//页面跳转
    }

}
