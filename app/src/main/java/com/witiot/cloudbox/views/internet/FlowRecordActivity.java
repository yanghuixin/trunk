package com.witiot.cloudbox.views.internet;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.CustInfoRes;
import com.witiot.cloudbox.model.FlowRecordRes;
import com.witiot.cloudbox.model.FlowRecordRqs;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.widget.AreaChart01View;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlowRecordActivity extends BaseActivity {

    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.chart)
    RelativeLayout chart;
    @BindView(R.id.totalflow_tv)
    TextView totalflowTv;
    @BindView(R.id.usedflow_tv)
    TextView usedflowTv;
    @BindView(R.id.surplusflow_tv)
    TextView surplusflowTv;
    @BindView(R.id.buy_flow_bt)
    TextView buyFlowBt;
    private List<FlowRecordRes.FlowRecordBean.Rows> rows;
    AreaChart01View chart01View;
    CustInfoRes.CustInfoBean custInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_record);
        ButterKnife.bind(this);
        getData();
        initUI();
    }

    private void initUI() {
        custInfoBean = (CustInfoRes.CustInfoBean) getIntent().getSerializableExtra("custInfoBean");
        totalflowTv.setText("套餐流量："+ custInfoBean.getRows().getTotalFlow()+" MB");
        surplusflowTv.setText("剩余："+ custInfoBean.getRows().getSurplusFlow()+" MB");
        usedflowTv.setText("已使用："+ custInfoBean.getRows().getActualFlow()+" MB");
    }

    private void getData() {
        showProgress();
        FlowRecordRqs rqs = new FlowRecordRqs();
        rqs.setCmd("list");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");

        FlowRecordRqs.DatBean datBean = new FlowRecordRqs.DatBean();
        FlowRecordRqs.DatBean.Paramlist paramlist = new FlowRecordRqs.DatBean.Paramlist();
        paramlist.setCustomerId((String) SPUtils.get(this, "customerId", ""));
//        paramlist.setBeginTime("2017-08-01 00:00:00");
//        paramlist.setEndTime("2017-08-14 23:00:00");
        paramlist.setDeviceId(DeviceUtils.getAndroidID());
        datBean.setParamlist(paramlist);
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "bill/getBillFlowStat", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<FlowRecordRes>() {
                    }.getType();
                    FlowRecordRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        rows = res.getDat().getRows();
                        initChartView();
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });
    }



    private void initChartView() {
        //图表的使用方法:
        //使用方式一:
        // 1.新增一个Activity
        // 2.新增一个View,继承Demo中的GraphicalView或DemoView都可，依Demo中View目录下例子绘制图表.
        // 3.将自定义的图表View放置入Activity对应的XML中，将指明其layout_width与layout_height大小.
        // 运行即可看到效果. 可参考非ChartsActivity的那几个图的例子，都是这种方式。

        //使用方式二:
        //代码调用 方式有下面二种方法:
        //方法一:
        //在xml中的FrameLayout下增加图表和ZoomControls,这是利用了现有的xml文件.
        // 1. 新增一个View，绘制图表.
        // 2. 通过下面的代码得到控件，addview即可
        //LayoutInflater factory = LayoutInflater.from(this);
        //View content = (View) factory.inflate(R.layout.activity_multi_touch, null);
        if(rows == null || rows.size() == 0){
            return;
        }
        chart01View = new AreaChart01View(this, rows);
        //方法二:
        //完全动态创建,无须XML文件.
        FrameLayout content = new FrameLayout(this);

        //缩放控件放置在FrameLayout的上层，用于放大缩小图表
        FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParm.gravity = Gravity.BOTTOM | Gravity.RIGHT;

		   /*
          //缩放控件放置在FrameLayout的上层，用于放大缩小图表
	       mZoomControls = new ZoomControls(this);
	       mZoomControls.setIsZoomInEnabled(true);
	       mZoomControls.setIsZoomOutEnabled(true);
		   mZoomControls.setLayoutParams(frameParm);
		   */

        //图表显示范围在占屏幕大小的90%的区域内
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scrWidth = (int) (dm.widthPixels * 1);
        int scrHeight = (int) (dm.heightPixels - 120);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                scrWidth, scrHeight);

        //居中显示
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        final RelativeLayout chartLayout = new RelativeLayout(this);

        chartLayout.addView(chart01View, layoutParams);

        //增加控件
        ((ViewGroup) content).addView(chartLayout);
        //((ViewGroup) content).addView(mZoomControls);
//        setContentView(content);
        chart.addView(content);
        //放大监听
        //  mZoomControls.setOnZoomInClickListener(new OnZoomInClickListenerImpl());
        //缩小监听
        //  mZoomControls.setOnZoomOutClickListener(new OnZoomOutClickListenerImpl());
    }


    @OnClick({R.id.close, R.id.buy_flow_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.buy_flow_bt:
                startActivity(new Intent(FlowRecordActivity.this, BuyFlowActivity.class));
                break;
        }
    }
}
