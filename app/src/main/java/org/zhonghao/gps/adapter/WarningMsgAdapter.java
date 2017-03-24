package org.zhonghao.gps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.zhonghao.gps.R;
import org.zhonghao.gps.activity.WarnInformationActivity;
import org.zhonghao.gps.entity.WarningResponseData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/24.
 */

public class WarningMsgAdapter extends BaseAdapter{
    Context context;
    WarningResponseData data;
    LayoutInflater inflate;
    public WarningMsgAdapter(Context context) {
        this.context=context;
        inflate=LayoutInflater.from(context);
    }

    public void addData(WarningResponseData warningResponseData) {
        this.data=warningResponseData;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.data!=null&&this.data.getAlarmList()!=null?this.data.getAlarmList().size():0;
    }

    @Override
    public Object getItem(int position) {
        return this.data.getAlarmList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        WarningResponseData.AlarmListBean alarm = data.getAlarmList().get(position);
        holder.deviceName.setText(alarm.getAlarmID());
        holder.ringState.setText(alarm.getRingState());
        holder.readState.setText(alarm.getReadState());
        holder.time.setText(alarm.getTime());
        return convertView;
    }
     static class WarningHolder{
        @BindView(R.id.check_msg_warning)
        CheckBox checkMsg;
        @BindView(R.id.device_name_warning)
        TextView deviceName;
        @BindView(R.id.ring_state_warning)
        TextView ringState;//警铃类型
        @BindView(R.id.read_state_waring)
        TextView readState;//是否阅读
        @BindView(R.id.time_warning)
        TextView time;
        @BindView(R.id.back_warning)
        ImageView back;
    }

}
