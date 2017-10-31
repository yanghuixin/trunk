package com.witiot.cloudbox.views.member;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.adapter.BoxOrderListAdapter;
import com.witiot.cloudbox.adapter.FlowOrderListAdapter;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.AdListRes;
import com.witiot.cloudbox.model.AdListRqs;
import com.witiot.cloudbox.model.OrderListRes;
import com.witiot.cloudbox.model.OrderListRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.RecycleViewDivider;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.widget.BackgroundDarkPopupWindow;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrderActivity extends BaseActivity {

    @BindView(R.id.flow_tab)
    RadioButton flowTab;
    @BindView(R.id.box_tab)
    RadioButton boxTab;
    @BindView(R.id.tab_group)
    RadioGroup tabGroup;
    @BindView(R.id.back)
    TextView back;

    @BindView(R.id.ad_left)
    SimpleDraweeView adLeft;
    @BindView(R.id.ad_right)
    SimpleDraweeView adRight;

    List<AdListRes.DatBean.AdBean> adBeanList;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    LinearLayoutManager layoutManager;
    FlowOrderListAdapter flowAdapter;
    BoxOrderListAdapter boxAdapter;
    private List<OrderListRes.DatBean.OrderBean> orderList = new ArrayList<>();
    int orderType = 1 ;// 0云头柜，1流量
    private int payPosition = 0;
    long repeatTime = 2000;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_my_order);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        orderType = getIntent().getIntExtra("orderType",1);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.layout_margin_1);
        RecycleViewDivider divider = new RecycleViewDivider(
               this, LinearLayoutManager.VERTICAL, spacingInPixels, ContextCompat.getColor(this, R.color.bg_grey));

        recyclerview.addItemDecoration(divider);
        flowAdapter = new FlowOrderListAdapter(this,payHandler);
        recyclerview.setAdapter(flowAdapter);

        boxAdapter = new BoxOrderListAdapter(this);

        flowAdapter.setOnItemClickLIstener(new RcOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
//                if(orderList.get(position).getStatus() == 0){
//                    payPosition = position;
//
//                }
            }
        });

        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.flow_tab){
                    orderType = 1;
                    recyclerview.setAdapter(flowAdapter);
                }else {
                    orderType = 0;
                    recyclerview.setAdapter(boxAdapter);
                }
                getOrderList();
            }
        });
        if(orderType == 0){
            boxTab.performClick();
        }else {
            flowTab.performClick();
        }
        getOrderList();
    }

    Handler payHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            payPosition = msg.what;
            getEwm();
        }
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
        paramlist.setStatus("2");//只获取已支付订单
        paramlist.setCustomerId((String) SPUtils.get(this, "customerId", ""));
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
                        if (orderList != null) {
                            if(orderType == 0) {
                                boxAdapter.setData(orderList);
                            }else if (orderType == 1){
                                flowAdapter.setData(orderList);
                            }
                        }
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }

                // 读取广告
                getAdvert();
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }


    private void getEwm() {
        String serverip = UrlManage.HOST_URL;

        String pm= orderList.get(payPosition).getOrderId()+"|"+orderList.get(payPosition).getCustomerId()+"|"+orderList.get(payPosition).getAmount()+"|msg" ;  //订单编号，会员编号， 金额，备注（可为空）。
        String shopName = "流量服务";
        if(orderType == 0){
            shopName = "云头柜服务";
        }
        String payurl = serverip + "wxpay_qrcode.jsp?WIDout_trade_no=" + orderList.get(payPosition).getOrderId() + "&WIDsubject=" + Uri.encode(shopName)
                + "&WIDtotal_fee=" + orderList.get(payPosition).getAmount()       + "&WIDbody=" + Uri.encode(pm) +
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
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.clear)));
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.setDarkStyle(-1);
        popup.setDarkColor(Color.parseColor("#a0000000"));
        popup.resetDarkPosition();
        popup.darkFillScreen();
        popup.showAtLocation(tabGroup.getRootView(), Gravity.CENTER, 0, 0);
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
                    getOrderPayStatus();

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


    private void getOrderPayStatus() {
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
                                    orderList.get(payPosition).setStatus(2);
                                    if(orderType == 0){
                                       if(boxAdapter != null){
                                           boxAdapter.notifyDataSetChanged();
                                       }
                                    }else {
                                        if(flowAdapter != null){
                                            flowAdapter.notifyDataSetChanged();
                                        }
                                    }
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


    // 广告
    private void getAdvert() {

        // paramlist
        AdListRqs.DatBean.Paramlist paramlist = new AdListRqs.DatBean.Paramlist();
        paramlist.setPosition("4");     // 广告位置 1：网上冲浪，2：开机欢迎页 3：医教中心，4：我的订单，5：兑换记录
        paramlist.setAdvertType("1");   // 1是文本 2是视频
        paramlist.setHospitalId(CommonUtils.getHospitalId( this ));  // 当前所属的医院编号
        paramlist.setDateNow( new SimpleDateFormat("yyyy-MM-dd").format(TimeUtils.getNowDate()));

        // dat
        AdListRqs.DatBean datBean = new AdListRqs.DatBean();
        datBean.setPageindex("1");
        datBean.setPagesize("2");           // 最多2条记录
        datBean.setOrderby("publishTime desc");
        datBean.setParamlist(paramlist);

        // request package
        AdListRqs rqs = new AdListRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "advert/listAllot", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<AdListRes>() {
                    }.getType();
                    AdListRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        adBeanList = res.getDat().getRows();
                        if (adBeanList != null) {
                            initAdvert();
                        }
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });
    }

    private void initAdvert() {
        if (adBeanList.size() >= 2) {
            adLeft.setImageURI(UrlManage.IMG_BASE_URL + adBeanList.get(0).getVideoUrl());
            adRight.setImageURI(UrlManage.IMG_BASE_URL + adBeanList.get(1).getVideoUrl());
        }
    }

    @OnClick({R.id.ad_left, R.id.ad_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ad_left:
                CommonUtils.intentChrome(MyOrderActivity.this,adBeanList.get(0).getAdvertUrl());
                break;
            case R.id.ad_right:
                CommonUtils.intentChrome(MyOrderActivity.this,adBeanList.get(1).getAdvertUrl());
                break;
        }
    }

}
