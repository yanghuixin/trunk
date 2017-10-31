package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.OrderListRes;
import com.witiot.cloudbox.utils.SpanUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */

public class BoxOrderListAdapter extends BaseRecyclerAdapter<BoxOrderListAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<OrderListRes.DatBean.OrderBean> orderBeanList;

    public BoxOrderListAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<OrderListRes.DatBean.OrderBean> orderBeanList){
        this.orderBeanList = orderBeanList;
        notifyDataSetChanged();
    }

    @Override
    public BoxOrderListAdapter.ViewHolder getViewHolder(View view) {
        return new BoxOrderListAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public BoxOrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_box_order,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BoxOrderListAdapter.ViewHolder holder, int position, boolean isItem) {
        holder.no.setText( orderBeanList.get(position).getOrderId().toUpperCase());
        holder.truePrice.setText("¥"+orderBeanList.get(position).getAmount());
        holder.orderTime.setText(orderBeanList.get(position).getOrderTime());

        if(StringUtils.stringIsEmpty(  orderBeanList.get(position).getBeginTime() ) || StringUtils.stringIsEmpty(  orderBeanList.get(position).getEndTime() )){
            holder.special.setText( "<无效日期>");
        }
        else {
            Date beginTime = TimeUtils.string2Date(orderBeanList.get(position).getBeginTime());
            Date endTime = TimeUtils.string2Date(orderBeanList.get(position).getEndTime());
            holder.special.setText( new SimpleDateFormat("yyyy-MM-dd").format(beginTime)
                  + " ~ "  + new SimpleDateFormat("yyyy-MM-dd").format(endTime) );
        }

        if(orderBeanList.get(position).getStatus() == 0){
            holder.payStatus.setText("未支付");
            holder.payStatus.setTextColor(ContextCompat.getColor(mContext,R.color.red_button));
        }else if(orderBeanList.get(position).getStatus() == 2){
            holder.payStatus.setText("已支付");
            holder.payStatus.setTextColor(ContextCompat.getColor(mContext,R.color.green_bt));
        }
    }

    @Override
    public int getAdapterItemCount() {
        return orderBeanList !=null? orderBeanList.size():0;
    }

    public void setOnItemClickLIstener(RcOnItemClickListener listener){
        this.itemClick = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RcOnItemClickListener onitemclick;
        private TextView no;
        private TextView orderTime;
        private TextView special;
        private TextView truePrice;
        private TextView payStatus;
        private RelativeLayout layout;
        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;
            no = (TextView) itemView.findViewById(R.id.item_no);
            orderTime = (TextView) itemView.findViewById(R.id.item_time);
            special = (TextView) itemView.findViewById(R.id.item_special);
            truePrice = (TextView) itemView.findViewById(R.id.item_true_price);
            payStatus = (TextView) itemView.findViewById(R.id.item_pay_status);
            layout = (RelativeLayout) itemView.findViewById(R.id.item_news_layout);
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
