package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.os.Handler;
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
import com.witiot.cloudbox.model.NewsListRes;

import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */

public class NewsListAdapter extends BaseRecyclerAdapter<NewsListAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<NewsListRes.DatBean.NewsBean> newsBeanList;
    private String serverPath = UrlManage.IMG_BASE_URL;

    public NewsListAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<NewsListRes.DatBean.NewsBean> newsBeanList){
        this.newsBeanList = newsBeanList;
        notifyDataSetChanged();
    }

    @Override
    public NewsListAdapter.ViewHolder getViewHolder(View view) {
        return new NewsListAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsListAdapter.ViewHolder holder, int position, boolean isItem) {
        holder.iv.setImageURI( serverPath + newsBeanList.get(position).getPictureUrl());
        holder.title.setText(newsBeanList.get(position).getTitle());
        holder.desc.setText(newsBeanList.get(position).getArticleDesc());
        holder.time.setText(newsBeanList.get(position).getCreateTime()+"\t\t"
            + newsBeanList.get(position).getSource());
        holder.num.setText("浏览"+newsBeanList.get(position).getVisitCount()+"次");
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
        private TextView time;
        private TextView num;
        private TextView desc;
        private SimpleDraweeView iv;
        private RelativeLayout layout;
        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;
            iv = (SimpleDraweeView) itemView.findViewById(R.id.item_news_iv);
            title = (TextView) itemView.findViewById(R.id.item_news_title);
            time = (TextView) itemView.findViewById(R.id.item_news_time);
            num = (TextView) itemView.findViewById(R.id.item_news_num);
            desc = (TextView) itemView.findViewById(R.id.item_news_desc);

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
