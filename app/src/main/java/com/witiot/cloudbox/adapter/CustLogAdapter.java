package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.CustLogRequest;
import com.witiot.cloudbox.model.CustLogResponse;
import com.witiot.cloudbox.model.DeviceRecordRes;
import com.witiot.cloudbox.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by liuyp on 2017/9/16.
 */

public class CustLogAdapter extends BaseRecyclerAdapter<CustLogAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<CustLogResponse.DatBean.CustLogBean> recordBeanList;

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public CustLogAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List< CustLogResponse.DatBean.CustLogBean> recordBeanList){
        this.recordBeanList = recordBeanList;
        notifyDataSetChanged();
    }

    @Override
    public CustLogAdapter.ViewHolder getViewHolder(View view) {
        return new CustLogAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public CustLogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custlog, parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustLogAdapter.ViewHolder holder, int position, boolean isItem) {
        String tm = dateFormat.format( TimeUtils.string2Date(recordBeanList.get(position).getOperTime()) );
        holder.operTime.setText(tm);
        holder.operContent.setText(recordBeanList.get(position).getOperContent());
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

        private TextView operTime;
        private TextView operContent;

        private RelativeLayout layout;

        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;

            operContent = (TextView) itemView.findViewById(R.id.item_operContent);
            operTime = (TextView) itemView.findViewById(R.id.item_operTime);

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
