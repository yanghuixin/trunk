package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.witiot.cloudbox.R;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.FlowSpecifiRes;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by lixin on 2017/3/15.
 */

public class FlowSpecificAdapter extends RecyclerView.Adapter<FlowSpecificAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<FlowSpecifiRes.DatBean.RowsBean> rowsBeanList;
    public FlowSpecificAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<FlowSpecifiRes.DatBean.RowsBean> rowsBeanList) {
        this.rowsBeanList = rowsBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_specific,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(rowsBeanList.get(position).isSelect()){
            holder.ll.setBackground(ContextCompat.getDrawable(holder.ll.getContext(), R.drawable.bg_get_code));
        }else{
            holder.ll.setBackground(ContextCompat.getDrawable(holder.ll.getContext(), R.mipmap.flow_unselect));
        }
        if(rowsBeanList.get(position).getServiceDiscount() < 1){
            //holder.discount.setText("（"+rowsBeanList.get(position).getServiceDiscount()*10+"折）");
            holder.discount.setText("（"+ new DecimalFormat("#").format( rowsBeanList.get(position).getServiceDiscount()*10)+"折）");
        }else {
            holder.discount.setText("");
        }

        holder.price.setText(rowsBeanList.get(position).getServicePrice()+"");
        holder.specifi.setText(rowsBeanList.get(position).getServiceName());
    }

    @Override
    public int getItemCount() {
        return rowsBeanList!=null?rowsBeanList.size():0;
    }

    public void setOnItemClickLIstener(RcOnItemClickListener listener){
        this.itemClick = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RcOnItemClickListener onitemclick;
        LinearLayout ll;
        private TextView specifi;
        private TextView price;
        private TextView discount;
        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;
            ll = (LinearLayout) itemView.findViewById(R.id.flow_ll);
            specifi = (TextView) itemView.findViewById(R.id.flow_specifi);
            price = (TextView) itemView.findViewById(R.id.flow_price);
            discount = (TextView) itemView.findViewById(R.id.flow_discount);
            price = (TextView) itemView.findViewById(R.id.flow_price);
            if(ll != null){
                ll.setOnClickListener(this);
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
