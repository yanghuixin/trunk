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
 * Created by liuyp on 2017/9/13.
 */

public class BoxSpecificAdapter extends RecyclerView.Adapter<BoxSpecificAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<FlowSpecifiRes.DatBean.RowsBean> rowsBeanList;
    public BoxSpecificAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<FlowSpecifiRes.DatBean.RowsBean> rowsBeanList) {
        this.rowsBeanList = rowsBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_box_specific,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.box_spec_list.setBackground(ContextCompat.getDrawable(holder.box_spec_list.getContext(), R.drawable.bg_get_code));
        holder.txt_serviceName.setText(rowsBeanList.get(position).getServiceName()+"");
        holder.txt_useDesc.setText(rowsBeanList.get(position).getUseDesc());

        // holder.img_device.setImageResource( R.mipmap.jingshuiqi);
        if(rowsBeanList.get(position).getServiceName().indexOf("密码")>=0){
            holder.img_device.setImageResource( R.mipmap.dv_mm);
        }
        else if(rowsBeanList.get(position).getServiceName().indexOf("空气")>=0){
            holder.img_device.setImageResource( R.mipmap.dv_kq);
        }
        else if(rowsBeanList.get(position).getServiceName().indexOf("消毒")>=0){
            holder.img_device.setImageResource( R.mipmap.dv_xd);
        }
        else if(rowsBeanList.get(position).getServiceName().indexOf("净水")>=0){
            holder.img_device.setImageResource( R.mipmap.dv_js);
        }
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

        LinearLayout box_spec_list;
        private ImageView img_device;
        private TextView txt_serviceName;
        private TextView txt_useDesc;

        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;

            box_spec_list = (LinearLayout) itemView.findViewById(R.id.box_spec_list);
            img_device = (ImageView) itemView.findViewById(R.id.img_device);
            txt_serviceName = (TextView) itemView.findViewById(R.id.txt_serviceName);
            txt_useDesc = (TextView) itemView.findViewById(R.id.txt_useDesc);

            if(box_spec_list != null){
                box_spec_list.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if(onitemclick != null){
                onitemclick.onClick(v, getAdapterPosition());
            }
        }
    }
}
