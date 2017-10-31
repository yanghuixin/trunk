package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.witiot.cloudbox.model.NewsListIndexRes;
import com.witiot.cloudbox.model.NewsListRes;
import com.witiot.cloudbox.views.yjzx.NewsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */

public class NewsListIndexAdapter extends BaseRecyclerAdapter<NewsListIndexAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<NewsListIndexRes.ItemBean.DataBean> newsBeanList;
    private SimpleDateFormat dtFormat;
    private String serverPath = UrlManage.IMG_BASE_URL;

    public NewsListIndexAdapter(Context context){
        this.mContext = context;
        dtFormat = new SimpleDateFormat("MM月dd日");
    }

    public void setData(List<NewsListIndexRes.ItemBean.DataBean> newsBeanList){
        this.newsBeanList = newsBeanList;
        notifyDataSetChanged();
    }

    @Override
    public NewsListIndexAdapter.ViewHolder getViewHolder(View view) {
        return new NewsListIndexAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public NewsListIndexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_index,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsListIndexAdapter.ViewHolder holder, int position, boolean isItem) {
        holder.iv.setImageURI( serverPath + newsBeanList.get(position).getPictureUrl());
        holder.title.setText(newsBeanList.get(position).getTitle());
        holder.time.setText(  newsBeanList.get(position).getCreateTime() +"\t\t"
                + newsBeanList.get(position).getSource());
        holder.num.setText("浏览"+newsBeanList.get(position).getVisitCount()+"次");

        holder.layout.setOnClickListener(new onItemClick(position));
    }

    class onItemClick implements View.OnClickListener{
       int position ;

        public onItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, NewsDetailActivity.class);
            intent.putExtra("newsId",newsBeanList.get(position).getId()+"");
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return newsBeanList !=null? newsBeanList.size():0;
    }

    public void setOnItemClickLIstener(RcOnItemClickListener listener){
        this.itemClick = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RcOnItemClickListener onitemclick;
        private TextView title;
        private SimpleDraweeView iv;
        private RelativeLayout layout;
        private TextView time;
        private TextView num;

        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;

            iv = (SimpleDraweeView) itemView.findViewById(R.id.item_news_iv);
            title = (TextView) itemView.findViewById(R.id.item_news_title);
            layout = (RelativeLayout) itemView.findViewById(R.id.item_news_layout);
            time = (TextView) itemView.findViewById(R.id.item_news_time);
            num = (TextView) itemView.findViewById(R.id.item_news_num);
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
