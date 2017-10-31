package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.DeviceRecordRes;
import com.witiot.cloudbox.model.NewsListRes;

import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */

public class DeviceRecordAdapter extends BaseRecyclerAdapter<DeviceRecordAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<DeviceRecordRes.DatBean.RecordBean> recordBeanList;

    public DeviceRecordAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<DeviceRecordRes.DatBean.RecordBean> recordBeanList){
        this.recordBeanList = recordBeanList;
        notifyDataSetChanged();
    }

    @Override
    public DeviceRecordAdapter.ViewHolder getViewHolder(View view) {
        return new DeviceRecordAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public DeviceRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceRecordAdapter.ViewHolder holder, int position, boolean isItem) {
        holder.reportContent.setText( recordBeanList.get(position).getReportContent());
        holder.reportMan.setText(recordBeanList.get(position).getReportMan());
        holder.reportMobile.setText(recordBeanList.get(position).getReportMobile());
        holder.reportTime.setText(recordBeanList.get(position).getReportTime());
    }

    @Override
    public int getAdapterItemCount() {
        return recordBeanList !=null? recordBeanList.size():0;
    }

    public void setOnItemClickLIstener(RcOnItemClickListener listener){
        this.itemClick = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RcOnItemClickListener onitemclick;
        private TextView reportContent;
        private TextView reportMan;
        private TextView reportMobile;
        private TextView reportTime;

        private RelativeLayout layout;

        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;

            reportContent = (TextView) itemView.findViewById(R.id.item_reportContent);
            reportMan = (TextView) itemView.findViewById(R.id.item_reportMan);
            reportMobile = (TextView) itemView.findViewById(R.id.item_reportMobile);
            reportTime = (TextView) itemView.findViewById(R.id.item_reportTime);

            layout = (RelativeLayout) itemView.findViewById(R.id.item_layout);
            if(layout != null){
                layout.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if(onitemclick != null){
                onitemclick.onClick(v,getAdapterPosition());
            }
        }
    }

}
