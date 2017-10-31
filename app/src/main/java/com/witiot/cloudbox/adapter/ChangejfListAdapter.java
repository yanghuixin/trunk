package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.ChangeJfListRes;
import com.witiot.cloudbox.model.ChangeJfListRqs;
import com.witiot.cloudbox.model.DelJfRecordRqs;
import com.witiot.cloudbox.model.OrderListRes;
import com.witiot.cloudbox.model.OrderListRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.SpanUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.widget.BackgroundDarkPopupWindow;
import com.witiot.cloudbox.widget.TipDialog;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lixin on 2017/3/15.
 */

public class ChangejfListAdapter extends BaseRecyclerAdapter<ChangejfListAdapter.ViewHolder> {
    private RcOnItemClickListener itemClick;
    private Context mContext;
    private List<ChangeJfListRes.DatBean.RowsBean> orderBeanList;

    public ChangejfListAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<ChangeJfListRes.DatBean.RowsBean> orderBeanList){
        this.orderBeanList = orderBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ChangejfListAdapter.ViewHolder getViewHolder(View view) {
        return new ChangejfListAdapter.ViewHolder(view,itemClick);
    }

    @Override
    public ChangejfListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_changejf,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,itemClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChangejfListAdapter.ViewHolder holder, int position, boolean isItem) {
        //holder.item_id.setText(orderBeanList.get(position).getId());  // id
        holder.img.setImageURI(UrlManage.IMG_BASE_URL+orderBeanList.get(position).getUploadPath());
        if(StringUtils.stringIsNotEmpty(orderBeanList.get(position).getTotalFee())){
            holder.item_price.setText( "¥ " +orderBeanList.get(position).getTotalFee());
        }else {
            holder.item_price.setText( "¥ 0" );
        }
        holder.item_score.setText(""+orderBeanList.get(position).getScore());
        holder.patientName.setText(orderBeanList.get(position).getPatientName());
        holder.item_delete.setVisibility(View.INVISIBLE);
        holder.failreason.setVisibility(View.INVISIBLE);
        if(orderBeanList.get(position).getStatus() == 0){//状态：0上传等待审核，1开始审核，2审核成功，3审核失败
            holder.item_delete.setVisibility(View.VISIBLE);
            holder.status.setText("待审核");
            holder.status.setTextColor(ContextCompat.getColor(mContext,R.color.black_text));
        }else if(orderBeanList.get(position).getStatus() == 1){
            holder.status.setText("开始审核");
            holder.status.setTextColor(ContextCompat.getColor(mContext,R.color.green_bt));
        }else if(orderBeanList.get(position).getStatus() == 2){
            holder.status.setText("审核成功");
            holder.status.setTextColor(ContextCompat.getColor(mContext,R.color.green_bt));
        }else if(orderBeanList.get(position).getStatus() == 3){
            holder.failreason.setVisibility(View.VISIBLE);
            if(StringUtils.stringIsNotEmpty(orderBeanList.get(position).getFailReason())) {
                holder.failreason.setText(orderBeanList.get(position).getFailReason());
            }
            holder.status.setText("审核失败");
            holder.status.setTextColor(ContextCompat.getColor(mContext,R.color.red_button));
        }
        holder.item_delete.setOnClickListener(new deleteClickListener(position));
    }

    private class deleteClickListener implements View.OnClickListener{
        int position;

        public deleteClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
           showDialog(position);
        }
    }

    TipDialog tipDialog;
    private void showDialog(final int position) {
        tipDialog = new TipDialog(mContext, false, new TipDialog.TipDialogButtonListener() {
            @Override
            public void onLeftBtnClicked() {
                deleteRecord(position);
                tipDialog.dismiss();
            }

            @Override
            public void onRightBtnClicked() {
                tipDialog.dismiss();
            }

            @Override
            public void onCenterBtnClicked() {

            }
        });
        tipDialog.show();
        tipDialog.setCenterButtonVisibility(false);
        tipDialog.setMsg("确定要删除该兑换记录吗？");

    }

    private void deleteRecord(final int position) {
        DelJfRecordRqs rqs = new DelJfRecordRqs();
        rqs.setCmd("del");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(mContext, "tok", ""));
        rqs.setVer("1");
        DelJfRecordRqs.DatBean datBean = new DelJfRecordRqs.DatBean();
        datBean.setId(orderBeanList.get(position).getId()+"");
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(mContext, gson.toJson(rqs), "shopChargeBill/del", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                if (isSucceed) {
                    Type typeToken = new TypeToken<BaseRes>() {
                    }.getType();
                    BaseRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        orderBeanList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext,res.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext,"网络连接异常",Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        //private TextView item_id;
        private TextView patientName;
        private TextView item_score;
        private TextView item_price;
        private TextView status;
        private TextView item_delete;
        private TextView failreason;
        private SimpleDraweeView img;
        private RelativeLayout layout;
        public ViewHolder(View itemView, RcOnItemClickListener myOnitemclick) {
            super(itemView);
            this.onitemclick = myOnitemclick;
            img = (SimpleDraweeView) itemView.findViewById(R.id.item_bill_img);
            item_score = (TextView) itemView.findViewById(R.id.item_score);
            item_price = (TextView) itemView.findViewById(R.id.item_price);
            patientName = (TextView) itemView.findViewById(R.id.item_patientName);
            status = (TextView) itemView.findViewById(R.id.item_status);
            item_delete = (TextView) itemView.findViewById(R.id.item_delete);
            failreason = (TextView) itemView.findViewById(R.id.failreason);

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
