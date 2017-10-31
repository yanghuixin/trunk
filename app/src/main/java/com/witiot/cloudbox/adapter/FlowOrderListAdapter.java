package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
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
import com.witiot.cloudbox.model.NewsListIndexRes;
import com.witiot.cloudbox.model.OrderListRes;
import com.witiot.cloudbox.utils.CommonUtils;

import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */

public class FlowOrderListAdapter extends BaseRecyclerAdapter<FlowOrderListAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<OrderListRes.DatBean.OrderBean> orderBeanList;
    private Handler payHandler;
    public FlowOrderListAdapter(Context context,Handler payHandler){
        this.mContext = context;
        this.payHandler =payHandler;
    }

    public void setData(List<OrderListRes.DatBean.OrderBean> orderBeanList){
        this.orderBeanList = orderBeanList;
        notifyDataSetChanged();
    }

    @Override
    public FlowOrderListAdapter.ViewHolder getViewHolder(View view) {
        return new FlowOrderListAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public FlowOrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_order,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FlowOrderListAdapter.ViewHolder holder, int position, boolean isItem) {

        if(orderBeanList.get(position).getDiscount() == ""||orderBeanList.get(position).getDiscount() == "1")  {
            holder.discount.setText("");
        }
        else {
            Double discount = Double.parseDouble(orderBeanList.get(position).getDiscount()) * 10;
            if (discount == 10f) {
                holder.discount.setText("");
            } else {
                holder.discount.setText("（" + discount.toString() + "折）");
            }
        }

        holder.no.setText( orderBeanList.get(position).getOrderId().toUpperCase());
        holder.oldPrice.setText("¥"+orderBeanList.get(position).getOrginalAmount());
        holder.truePrice.setText("¥"+orderBeanList.get(position).getAmount());
        holder.orderTime.setText(orderBeanList.get(position).getOrderTime());
        holder.special.setText( CommonUtils.formatMBOrGB(orderBeanList.get(position).getBuyCount() ));
        holder.item_topay.setVisibility(View.INVISIBLE);
        if(orderBeanList.get(position).getStatus() == 0){
            holder.item_topay.setVisibility(View.VISIBLE);
            holder.item_topay.setOnClickListener(new payListener(position));
            holder.payStatus.setText("未支付");
            holder.payStatus.setTextColor(ContextCompat.getColor(mContext,R.color.red_button));
        }else if(orderBeanList.get(position).getStatus() == 2){
            holder.payStatus.setText("已支付");
            holder.payStatus.setTextColor(ContextCompat.getColor(mContext,R.color.green_bt));
        }
    }

    private class payListener implements View.OnClickListener{
        int position;

        public payListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            payHandler.sendEmptyMessage(position);
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
        private TextView oldPrice;
        private TextView discount;
        private TextView payStatus;
        private RelativeLayout layout;
        private TextView item_topay;
        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;
            item_topay = (TextView) itemView.findViewById(R.id.item_topay);
            no = (TextView) itemView.findViewById(R.id.item_no);
            orderTime = (TextView) itemView.findViewById(R.id.item_time);
            special = (TextView) itemView.findViewById(R.id.item_special);
            truePrice = (TextView) itemView.findViewById(R.id.item_true_price);
            oldPrice = (TextView) itemView.findViewById(R.id.item_old_price);
            discount = (TextView) itemView.findViewById(R.id.item_discount);
            payStatus = (TextView) itemView.findViewById(R.id.item_pay_status);
            layout = (RelativeLayout) itemView.findViewById(R.id.item_news_layout);
            oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
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
