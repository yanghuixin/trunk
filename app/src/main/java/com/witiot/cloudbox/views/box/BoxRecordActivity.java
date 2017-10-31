package com.witiot.cloudbox.views.box;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.MyApplication;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.adapter.CustLogAdapter;
import com.witiot.cloudbox.adapter.DeviceRecordAdapter;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.BillCalenderRes;
import com.witiot.cloudbox.model.BillCalenderRqs;
import com.witiot.cloudbox.model.CustLogRequest;
import com.witiot.cloudbox.model.CustLogResponse;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.RecycleViewDivider;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.SpanUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseActivity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BoxRecordActivity extends BaseActivity {

    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.pre_date)
    ImageView preDate;
    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.next_date)
    ImageView nextDate;

    @BindView(R.id.pw_tip1)
    TextView pwTip1;
    @BindView(R.id.pw_tip2)
    TextView pwTip2;
    @BindView(R.id.jhq_tip1)
    TextView jhqTip1;
    @BindView(R.id.jhq_tip2)
    TextView jhqTip2;

    @BindView(R.id.xdg_tip1)
    TextView xdgTip1;
    @BindView(R.id.xdg_tip2)
    TextView xdgTip2;
    @BindView(R.id.xdg_tip3)
    TextView xdgTip3;

    @BindView(R.id.jsq_tip1)
    TextView jsqTip1;
    @BindView(R.id.jsq_tip2)
    TextView jsqTip2;
    @BindView(R.id.jsq_tip3)
    TextView jsqTip3;

    @BindView(R.id.tv_hint)
    TextView tvHint;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;

    LinearLayoutManager layoutManager;
    private int totalPage = 0;
    private int currentPage = 1;

    private Context mContext;
    private CustLogAdapter recordAdapter;

    String currentDate = "";
    List<CustLogResponse.DatBean.CustLogBean> recordBeanList = new ArrayList<>();

    List<BillCalenderRes.DatBean.RowsBean> billCalenders;// = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_record);
        ButterKnife.bind(this);
        mContext = this;

        initUI();

        currentDate = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd"));
        dateTv.setText(currentDate);

        refreshList();

        getCalenderList();
    }

    private void initUI() {

//        xrefreshview.setPinnedTime(500);
//        xrefreshview.setPullLoadEnable(false);
//        xrefreshview.setAutoLoadMore(false);
//        xrefreshview.enableReleaseToLoadMore(false);
//        xrefreshview.enableRecyclerViewPullUp(true);
//        xrefreshview.enablePullUpWhenLoadCompleted(true);

        recyclerview.setHasFixedSize(true);

        recordAdapter = new CustLogAdapter(mContext);
        recordAdapter.setCustomLoadMoreView(new XRefreshViewFooter(mContext));

        recyclerview.setAdapter(recordAdapter);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.layout_margin_1);
        recyclerview.addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.VERTICAL, spacingInPixels, ContextCompat.getColor(mContext, R.color.bg_grey)));

        recordAdapter.setData(recordBeanList);

        recordAdapter.setOnItemClickLIstener(new RcOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
            }
        });

        /*
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshList();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                xrefreshview.stopLoadMore();
                if (currentPage < totalPage) {
//                    currentPage++;
//                    getCustLog(currentDate);
                }
            }
        });
        */
    }

    private void refreshList() {
        recordBeanList.clear();
        getCustLog(currentDate);
    }

    private void getCalenderList() {

        // paramlist
        BillCalenderRqs.DatBean.Paramlist paramlist = new BillCalenderRqs.DatBean.Paramlist();
        paramlist.setCustomerId((String) SPUtils.get(this, "customerId", ""));
        paramlist.setServiceDay(currentDate);
        //paramlist.setDeviceId("");

        // dat
        BillCalenderRqs.DatBean datBean = new BillCalenderRqs.DatBean();
        datBean.setParamlist(paramlist);

        // request
        BillCalenderRqs rqs = new BillCalenderRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        // post
        showProgress();
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "bill/getBillCalenderList", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<BillCalenderRes>() { }.getType();

                    BillCalenderRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        billCalenders = res.getDat().getRows();

                        if (billCalenders != null) {
                            initViewCalender();
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

    private void initViewCalender() {
        for (int i = 0; i < billCalenders.size(); i++) {
            if (billCalenders.get(i).getDeviceType().equals("mms")) {
                pwTip2.setText(new SpanUtils().append("已用").append(billCalenders.get(i).getActualCount() + "")
                        .append("次").create()
                );
            } else if (billCalenders.get(i).getDeviceType().equals("jhq")) {
                jhqTip2.setText(new SpanUtils().append("已用").append(billCalenders.get(i).getActualCount() + "")
                        .append("分钟").create()
                );
            }  else if (billCalenders.get(i).getDeviceType().equals("xdg")) {
                xdgTip1.setText(new SpanUtils().append("共").append(billCalenders.get(i).getAvailableCount() + "")
                        .append("次").create()
                );
                xdgTip2.setText(new SpanUtils().append("已用").append(billCalenders.get(i).getActualCount() + "")
                        .append("次").create()
                );
                xdgTip3.setText(new SpanUtils().append("可用").append((billCalenders.get(i).getAvailableCount()-billCalenders.get(i).getActualCount()) + "")
                        .append("次").create()
                );
            }else if (billCalenders.get(i).getDeviceType().equals("jsq")) {
                jsqTip1.setText(new SpanUtils().append("共").append(billCalenders.get(i).getAvailableCount() + "")
                        .append("次").create()
                );
                jsqTip2.setText(new SpanUtils().append("已用").append(billCalenders.get(i).getActualCount() + "")
                        .append("次").create()
                );
                jsqTip3.setText(new SpanUtils().append("可用").append((billCalenders.get(i).getAvailableCount()-billCalenders.get(i).getActualCount()) + "")
                        .append("次").create()
                );
            }
        }

        if(billCalenders.size()>0) {
            tvHint.setVisibility( View.INVISIBLE );

            pwTip1.setVisibility(View.VISIBLE);
            pwTip2.setVisibility(View.VISIBLE);

            jhqTip1.setVisibility(View.VISIBLE);
            jhqTip2.setVisibility(View.VISIBLE);

            xdgTip1.setVisibility(View.VISIBLE);
            xdgTip2.setVisibility(View.VISIBLE);
            xdgTip3.setVisibility(View.VISIBLE);

            jsqTip1.setVisibility(View.VISIBLE);
            jsqTip2.setVisibility(View.VISIBLE);
            jsqTip3.setVisibility(View.VISIBLE);
        }
        else {
            tvHint.setVisibility( View.VISIBLE);

            pwTip1.setVisibility(View.INVISIBLE);
            pwTip2.setVisibility(View.INVISIBLE);

            jhqTip1.setVisibility(View.INVISIBLE);
            jhqTip2.setVisibility(View.INVISIBLE);

            xdgTip1.setVisibility(View.INVISIBLE);
            xdgTip2.setVisibility(View.INVISIBLE);
            xdgTip3.setVisibility(View.INVISIBLE);

            jsqTip1.setVisibility(View.INVISIBLE);
            jsqTip2.setVisibility(View.INVISIBLE);
            jsqTip3.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.close)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.pre_date, R.id.next_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_date:
                currentDate = TimeUtils.generateDateString(currentDate,-1);
                dateTv.setText(currentDate);

                getCalenderList();
                refreshList();

                break;
            case R.id.next_date:
                currentDate = TimeUtils.generateDateString(currentDate,1);
                dateTv.setText(currentDate);

                getCalenderList();
                refreshList();
                break;
        }
    }


    /**
     * 读取当天的日志
     * @param currentDate
     */
    private void getCustLog(String currentDate) {
        // paramlist
        CustLogRequest.DatBean.Paramlist paramlist = new CustLogRequest.DatBean.Paramlist();
        paramlist.setCustomerId((String) SPUtils.get(this, "customerId", ""));
        paramlist.setDeviceId(DeviceUtils.getAndroidID());
        paramlist.setOperType( 4 );  // 4 云头柜
        paramlist.setBeginTime(currentDate + " 00:00:00");
        paramlist.setEndTime(currentDate + " 23:59:59");

        // dat
        CustLogRequest.DatBean datBean = new CustLogRequest.DatBean();
        datBean.setParamlist(paramlist);

        // request
        CustLogRequest rqs = new CustLogRequest();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        // post
        showProgress();
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "getCustLog", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<CustLogResponse>() {
                    }.getType();

                    CustLogResponse res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {

                        recordBeanList.addAll(res.getDat().getRows());
                        totalPage = res.getDat().getPages();
                        recordAdapter.notifyDataSetChanged();
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });
    }


}
