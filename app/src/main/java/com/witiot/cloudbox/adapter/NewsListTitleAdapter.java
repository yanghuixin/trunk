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
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.NewsListIndexRes;
import com.witiot.cloudbox.views.yjzx.NewsDetailActivity;

import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */

public class NewsListTitleAdapter extends BaseRecyclerAdapter<NewsListTitleAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<NewsListIndexRes.ItemBean.DataBean> newsBeanList;

    public NewsListTitleAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<NewsListIndexRes.ItemBean.DataBean> newsBeanList){
        this.newsBeanList = newsBeanList;
        notifyDataSetChanged();
    }

    @Override
    public NewsListTitleAdapter.ViewHolder getViewHolder(View view) {
        return new NewsListTitleAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public NewsListTitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_title,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsListTitleAdapter.ViewHolder holder, int position, boolean isItem) {

        holder.title.setText(newsBeanList.get(position).getTitle());
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
        private RelativeLayout layout;
        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;
            title = (TextView) itemView.findViewById(R.id.item_news_title);
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
