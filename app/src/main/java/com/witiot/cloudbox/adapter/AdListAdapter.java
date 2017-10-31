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
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.AdListRes;
import com.witiot.cloudbox.model.NewsListRes;

import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */

public class AdListAdapter extends BaseRecyclerAdapter<AdListAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<AdListRes.DatBean.AdBean> adBeanList;

    public AdListAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<AdListRes.DatBean.AdBean> adBeanList){
        this.adBeanList = adBeanList;
        notifyDataSetChanged();
    }

    @Override
    public AdListAdapter.ViewHolder getViewHolder(View view) {
        return new AdListAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public AdListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdListAdapter.ViewHolder holder, int position, boolean isItem) {

        holder.iv.setImageURI(UrlManage.IMG_BASE_URL+adBeanList.get(position).getVideoUrl());

    }

    @Override
    public int getAdapterItemCount() {
        return adBeanList !=null? adBeanList.size():0;
    }

    public void setOnItemClickLIstener(RcOnItemClickListener listener){
        this.itemClick = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RcOnItemClickListener onitemclick;
        private SimpleDraweeView iv;
        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;
            iv = (SimpleDraweeView) itemView.findViewById(R.id.item_ad_iv);
            if(iv != null){
                iv.setOnClickListener(this);
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
