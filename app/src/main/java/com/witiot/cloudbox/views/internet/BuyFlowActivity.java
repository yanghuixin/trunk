package com.witiot.cloudbox.views.internet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andview.refreshview.XRefreshViewFooter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.adapter.FlowSpecificAdapter;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.BuyFlowRqs;
import com.witiot.cloudbox.model.FlowSpecifiRes;
import com.witiot.cloudbox.model.FlowSpecificRqs;
import com.witiot.cloudbox.model.OrderListRes;
import com.witiot.cloudbox.model.OrderListRqs;
import com.witiot.cloudbox.model.WxPayInfo;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.RecycleViewDivider;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.views.box.BuyServiceActivity;
import com.witiot.cloudbox.views.member.MyOrderActivity;
import com.witiot.cloudbox.widget.BackgroundDarkPopupWindow;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyFlowActivity extends BaseActivity {

    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.pay_bt)
    LinearLayout payBt;

    @BindView(R.id.order_num)
    TextView orderNum;
    @BindView(R.id.flow_specifi)
    TextView flowSpecifi;
    @BindView(R.id.price)
    TextView price;
    Context mContext;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private LinearLayoutManager linearLayoutManager;
    FlowSpecificAdapter adapter;
    private List<FlowSpecifiRes.DatBean.RowsBean> rowsBeanList;
    int selectPosition = -1;
    WxPayInfo wxPayInfo;
     Gson gson;

    private List<OrderListRes.DatBean.OrderBean> orderList = new ArrayList<>();
    int orderType = 1 ;// 0云头柜，1流量
    long repeatTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_flow);
        ButterKnife.bind(this);
        mContext = this;
        initUI();
        getData();
    }

    private void getData() {
        showProgress();

        // paramlist
        FlowSpecificRqs.DatBean.Paramlist paramlist = new FlowSpecificRqs.DatBean.Paramlist();
//        serviceType=0  云头柜 freeDay 免费天数 price 为云头柜价格
//        serviceType=1 流量 使用的是Service开头的 servicePrice 流量价格
        paramlist.setHospitalId(CommonUtils.getHospitalId(this));
        paramlist.setStandardId("");
        paramlist.setStandardName("");
        paramlist.setStandardType("1");
        paramlist.setStatus("");

        // dat
        FlowSpecificRqs.DatBean datBean = new FlowSpecificRqs.DatBean();
        datBean.setParamlist(paramlist);

        // request
        FlowSpecificRqs rqs = new FlowSpecificRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        // post
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "bill/hospital/service/listAllot", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                        Type typeToken = new TypeToken<FlowSpecifiRes>() {
                        }.getType();

                        FlowSpecifiRes res = gson.fromJson(result, typeToken);

                        if(res.getRet().equals("10000")){
                            rowsBeanList = res.getDat().getRows();
                            if(rowsBeanList != null && rowsBeanList.size() > 0){
                                selectPosition = 0;
                                rowsBeanList.get(selectPosition).setSelect(true);
                            }
                            adapter.setData(rowsBeanList);
                            changeOrderData();
                        }else {
                            toastShow(res.getMsg());
                        }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });
    }

    private void initUI() {

        recyclerview.setHasFixedSize(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.listview_divider);
        recyclerview.addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.VERTICAL, spacingInPixels, ContextCompat.getColor(mContext, R.color.bg_grey)));
        adapter = new FlowSpecificAdapter(mContext);
        recyclerview.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter.setOnItemClickLIstener(new RcOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(selectPosition != -1 && selectPosition < rowsBeanList.size()){
                    rowsBeanList.get(selectPosition).setSelect(false);
                }
                rowsBeanList.get(position).setSelect(true);
                selectPosition = position;
                        adapter.notifyDataSetChanged();
                changeOrderData();
            }
        });
    }

    private void changeOrderData() {
        if(selectPosition != -1 && selectPosition < rowsBeanList.size()) {
          flowSpecifi.setText( rowsBeanList.get(selectPosition).getServiceName());
          price.setText(rowsBeanList.get(selectPosition).getServicePrice() + "元");
        }else {
          flowSpecifi.setText("流量包规格：");
          price.setText("0");
        }
    }

    @OnClick({R.id.close, R.id.pay_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.pay_bt:
                subimtOrder();

                break;
        }
    }

    BuyFlowRqs.DatBean paramlist;
    private void subimtOrder() {
        String deviceId = DeviceUtils.getAndroidID();
        String customerId = (String) SPUtils.get(this, "customerId", "");

        //paramlist.setOrderId(java.util.UUID.randomUUID().toString().replace("-",""));
        String orderId = DeviceUtils.getAndroidID() + new SimpleDateFormat("yyyyMMddHHmmss").format( TimeUtils.getNowDate() ) ;//UUID.randomUUID().toString().replace("-", "");
        String orderType = "1"; // 0 云头柜  1 流量
        String status = "0";    // =0

        String amount = rowsBeanList.get(selectPosition).getServicePrice()+"";
        amount = "1";

        // detail
        List<BuyFlowRqs.DatBean.Detail> detailArr = new ArrayList<>();
        BuyFlowRqs.DatBean.Detail detail = new BuyFlowRqs.DatBean.Detail();

        detail.setAmount(rowsBeanList.get(selectPosition).getServicePrice()+"");
        detail.setBuyCount(rowsBeanList.get(selectPosition).getAvailableCount() +"");
        detail.setPrice(rowsBeanList.get(selectPosition).getServicePrice()+"");
        detail.setServiceId(rowsBeanList.get(selectPosition).getServiceId());

        detailArr.add(detail);

        // paramlist
        paramlist = new BuyFlowRqs.DatBean();
//        serviceType=0  云头柜 freeDay 免费天数 price 为云头柜价格
//        serviceType=1 流量 使用的是Service开头的 servicePrice 流量价格
        paramlist.setAmount(amount);
        paramlist.setDiscount(rowsBeanList.get(selectPosition).getServiceDiscount()+"");            // 使用从表的折扣， .getDiscount()+"");
        paramlist.setOrginalAmount(rowsBeanList.get(selectPosition).getServiceOrginalPrice()+"");   // 使用从表的原价， .getOrginalPrice()+"");
        paramlist.setBeginTime("");
        paramlist.setEndTime("");

        paramlist.setOrderId(orderId );
        paramlist.setOrderType(orderType);      //0 云头柜  1 流
        paramlist.setStatus(status);            //支付状态
        paramlist.setCustomerId(customerId);
        paramlist.setDeviceId(deviceId);
        paramlist.setDetail(detailArr);         // detail

        // request
        BuyFlowRqs rqs = new BuyFlowRqs();
        rqs.setCmd("add");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(paramlist);

        // post
        showProgress();
        gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "/bill/addorder", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<BaseRes>() {
                    }.getType();

                    BaseRes res = gson.fromJson(result, typeToken);

                    if(res.getRet().equals("10000")){
                        getEwm();
                    }else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });
    }

    private void getEwm() {
        String serverip = UrlManage.HOST_URL;

        String pm= paramlist.getOrderId()+"|"+paramlist.getCustomerId()+"|"+paramlist.getAmount()+"|msg" ;  //订单编号，会员编号， 金额，备注（可为空）。

        String payurl = serverip + "wxpay_qrcode.jsp?WIDout_trade_no=" + paramlist.getOrderId() + "&WIDsubject=" + Uri.encode("流量服务")
                + "&WIDtotal_fee=" + paramlist.getAmount()       + "&WIDbody=" + Uri.encode(pm) +
                "&WIDshow_url=" + Uri.encode(serverip + "wxpay_notify_app.jsp");

        LogUtil.log("getEwm pm",pm);
        LogUtil.log("getEwm payurl",payurl);
        RequestParams params = new RequestParams(payurl);
        params.setConnectTimeout(60000);
        params.setReadTimeout(100000);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
               if(result != null){
                   LogUtil.log("getEwm onSuccess",result);
//                   Type typeToken = new TypeToken<WxPayInfo>() {
//                   }.getType();
//                   wxPayInfo = gson.fromJson(result,typeToken);
                   showpopup(result.trim());
               }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.log("getEwm onError",ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    BackgroundDarkPopupWindow popup;

    private void showpopup(String url) {
        if(StringUtils.stringIsEmpty(url)){
            toastShow("支付信息获取失败，请重试");
            return;
        }
        LogUtil.log("payInfo",url);
        View popupView = getLayoutInflater().inflate(R.layout.pay_ewm, null);
        TextView lookorder = (TextView) popupView.findViewById(R.id.look_order);
        ImageView imageView = (ImageView) popupView.findViewById(R.id.ewm);
        imageView.setImageBitmap(CommonUtils.generateBitmap(url, 300, 300));
        popup = new BackgroundDarkPopupWindow(
                popupView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(false);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.clear)));
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.setDarkStyle(-1);
        popup.setDarkColor(Color.parseColor("#a0000000"));
        popup.resetDarkPosition();
        popup.darkFillScreen();
        popup.showAtLocation(payBt, Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popup = null;
                handlerTimer.sendEmptyMessage(1);
            }
        });
        lookorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
                // toastShow("功能开发中……");
            }
        });
        handlerTimer.sendEmptyMessage(0);
    }



    private Handler handlerTimer = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handlerTimer.removeMessages(0);
                    // app的功能逻辑处理
                    getOrderList();

                    break;

                case 1:
                    // 直接移除，定时器停止
                    handlerTimer.removeMessages(0);
                    break;

                default:
                    break;
            }
        };
    };


    private void getOrderList() {
        showProgress();
        OrderListRqs rqs = new OrderListRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        OrderListRqs.DatBean datBean = new OrderListRqs.DatBean();
        OrderListRqs.DatBean.Paramlist paramlist = new OrderListRqs.DatBean.Paramlist();
        paramlist.setOrderType(orderType+"");
        paramlist.setCustomerId((String) SPUtils.get(this, "customerId", ""));
        paramlist.setOrderId(paramlist.getOrderId());
        datBean.setParamlist(paramlist);
        rqs.setDat(datBean);

        gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "bill/orderlist", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<OrderListRes>() {
                    }.getType();
                    OrderListRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        orderList = res.getDat().getRows();
                        if (orderList != null && orderList.size() >0 ) {
                            if(orderList.get(0).getStatus() == 2){
                                if(popup != null){
                                    popup.dismiss();
                                    toastShow("支付成功");
                                    Intent intent = new Intent(BuyFlowActivity.this, MyOrderActivity.class);
                                    intent.putExtra("orderType",1);
                                    startActivity(intent);
                                    finish();
                                }
                            }else {
                                // 再次发出msg，循环更新
                                handlerTimer.sendEmptyMessageDelayed(0,repeatTime );
                            }
                        }else {
                            // 再次发出msg，循环更新
                            handlerTimer.sendEmptyMessageDelayed(0, repeatTime);
                        }
                    } else {
                        toastShow(res.getMsg());
                        // 再次发出msg，循环更新
                        handlerTimer.sendEmptyMessageDelayed(0, repeatTime);
                    }
                } else {
                    toastShow("网络连接异常");
                    // 再次发出msg，循环更新
                    handlerTimer.sendEmptyMessageDelayed(0, repeatTime);
                }
            }
        });
    }

}
