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
import com.witiot.cloudbox.model.NewsListRes;

import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */

public class NewsListHotAdapter extends BaseRecyclerAdapter<NewsListHotAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<NewsListRes.DatBean.NewsBean> newsBeanList;

    public NewsListHotAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<NewsListRes.DatBean.NewsBean> newsBeanList){
        this.newsBeanList = newsBeanList;
        notifyDataSetChanged();
    }

    @Override
    public NewsListHotAdapter.ViewHolder getViewHolder(View view) {
        return new NewsListHotAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public NewsListHotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_index,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsListHotAdapter.ViewHolder holder, int position, boolean isItem) {

        holder.title.setText(newsBeanList.get(position).getTitle());
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
        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;
            iv = (SimpleDraweeView) itemView.findViewById(R.id.item_news_iv);
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
