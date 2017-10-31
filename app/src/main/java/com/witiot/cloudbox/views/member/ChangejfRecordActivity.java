package com.witiot.cloudbox.views.member;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.adapter.ChangejfListAdapter;
import com.witiot.cloudbox.adapter.FlowOrderListAdapter;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.AdListRes;
import com.witiot.cloudbox.model.AdListRqs;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.ChangeJfListRes;
import com.witiot.cloudbox.model.ChangeJfListRqs;
import com.witiot.cloudbox.model.OrderListRes;
import com.witiot.cloudbox.model.OrderListRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.RecycleViewDivider;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseActivity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangejfRecordActivity extends BaseActivity {

    @BindView(R.id.back)
    TextView back;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.ad_left)
    SimpleDraweeView adLeft;
    @BindView(R.id.ad_right)
    SimpleDraweeView adRight;

    List<AdListRes.DatBean.AdBean> adBeanList;

    LinearLayoutManager layoutManager;
    ChangejfListAdapter jfListAdapter;

    private List<ChangeJfListRes.DatBean.RowsBean> orderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changejf_record);
        ButterKnife.bind(this);
        initUI();
        getList();
    }

    private void initUI() {
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.layout_margin_1);
        RecycleViewDivider divider = new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, spacingInPixels, ContextCompat.getColor(this, R.color.bg_grey));

        recyclerview.addItemDecoration(divider);
        jfListAdapter = new ChangejfListAdapter(this);
        recyclerview.setAdapter(jfListAdapter);

    }

    private void getList() {
        showProgress();
        ChangeJfListRqs rqs = new ChangeJfListRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        ChangeJfListRqs.DatBean datBean = new ChangeJfListRqs.DatBean();
        ChangeJfListRqs.DatBean.Paramlist paramlist = new ChangeJfListRqs.DatBean.Paramlist();
        paramlist.setCustomerId((String) SPUtils.get(this, "customerId", ""));
        datBean.setParamlist(paramlist);
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "shopChargeBill/list", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<ChangeJfListRes>() {
                    }.getType();
                    ChangeJfListRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        orderList = res.getDat().getRows();
                        if (orderList != null) {
                            jfListAdapter.setData(orderList);
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


    // 广告
    private void getAdvert() {

        // paramlist
        AdListRqs.DatBean.Paramlist paramlist = new AdListRqs.DatBean.Paramlist();
        paramlist.setPosition("5");     // 广告位置 1：网上冲浪，2：开机欢迎页 3：医教中心，4：我的订单，5：兑换记录
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
                CommonUtils.intentChrome(ChangejfRecordActivity.this,adBeanList.get(0).getAdvertUrl());
                break;
            case R.id.ad_right:
                CommonUtils.intentChrome(ChangejfRecordActivity.this,adBeanList.get(1).getAdvertUrl());
                break;
        }
    }


}
