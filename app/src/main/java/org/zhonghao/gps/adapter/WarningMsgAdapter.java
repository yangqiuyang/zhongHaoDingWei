package org.zhonghao.gps.adapter;

import android.content.Context;
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
import org.zhonghao.gps.utils.MyOnClickListener;
import org.zhonghao.gps.utils.Urls;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/24.
 */

public class WarningMsgAdapter extends BaseAdapter {
    Context context;
    WarningResponseData data;
    LayoutInflater inflate;
    Map<Integer,Boolean> checkedMap;
    WarningMark mark;
    public WarningMsgAdapter(Context context) {
        this.context=context;
        inflate=LayoutInflater.from(context);
    }

    public void addData(WarningResponseData data, Map<Integer, Boolean> checkedMap, WarningMark mark) {
        this.data=data;
        this.checkedMap=checkedMap;
        this.mark=mark;
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
        final WarningResponseData.AlarmListBean alarm = data.getAlarmList().get(position);
        holder.deviceName.setText(String.valueOf(alarm.getImei()));
       //预警类型
        if(alarm.getAlarmType().equals("0")){
            holder.ringState.setText(Urls.ALARMTYPE_RAIL);
        }
        else if(alarm.getAlarmType().equals("1")){
            holder.ringState.setText(Urls.ALARMTYPE_Light);
        }
        else if(alarm.getAlarmType().equals("2")){
            holder.ringState.setText(Urls.ALARMTYPE_RAIL_Light);
        }
        //通知状态
        if(alarm.getReadState().equals("0")){
            holder.readState.setText(Urls.UNREAD);
        }
        else if(alarm.getReadState().equals("1")){
            holder.readState.setText(Urls.READ);
        }
        holder.time.setText(String.valueOf(alarm.getTime()));
        //是否阅读信息
        holder.warningContent.setOnClickListener(new MyOnClickListener(position){
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"你好"+position,Toast.LENGTH_LONG).show();
                data.getAlarmList().get(this.mPosition).setReadState(Urls.READ);
                notifyDataSetChanged();
            }
        });
        holder.checkMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkedMap.put(position,Urls.CHECKED);
                    mark.setAlarmID(alarm.getAlarmID());
                    mark.setReadState(String.valueOf(Urls.READ_TYPE));
                }
                else{
                    checkedMap.put(position,Urls.UNCHECKED);
                }
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
