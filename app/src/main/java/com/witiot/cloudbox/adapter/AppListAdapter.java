package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.AppInfo;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */
public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<AppInfo> appInfos;
    private int num = 4;
    Gson gson;


    public AppListAdapter(Context context, int num) {
        this.mContext = context;
        this.num = num;
        gson = new Gson();
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setData(List<AppInfo> appInfos){
        this.appInfos = appInfos;
        notifyDataSetChanged();
    }

    public void setOnItemClickLIstener(RcOnItemClickListener listener){
        this.itemClick = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageDrawable(appInfos.get(position).appIcon);
        holder.name.setText(appInfos.get(position).appName);
    }

    @Override
    public int getItemCount() {
        return appInfos!=null?appInfos.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RcOnItemClickListener onitemclick;
        private ImageView image;
        private TextView name;
        private LinearLayout layout;

        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;
            image = (ImageView) itemView.findViewById(R.id.app_iv);
            name = (TextView) itemView.findViewById(R.id.app_name);
            layout = (LinearLayout) itemView.findViewById(R.id.app_ll);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) layout.getLayoutParams();
            if(num == 4) {
                params.width = 1382 / num;
            }else {
                params.width = 1582 / num;
            }
            layout.setLayoutParams(params);
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) image.getLayoutParams();
            params1.gravity = Gravity.CENTER_HORIZONTAL;
            image.setLayoutParams(params1);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onitemclick != null){
                onitemclick.onClick(v,getAdapterPosition());
            }
        }
    }
}
