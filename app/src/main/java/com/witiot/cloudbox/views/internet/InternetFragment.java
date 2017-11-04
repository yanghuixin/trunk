package com.witiot.cloudbox.views.internet;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.adapter.AdListAdapter;
import com.witiot.cloudbox.adapter.AppListAdapter;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.AdListRes;
import com.witiot.cloudbox.model.AdListRqs;
import com.witiot.cloudbox.model.AppInfo;
import com.witiot.cloudbox.model.CustInfoRes;
import com.witiot.cloudbox.model.CustInfoRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TextViewTimeCount;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseFragment;
import com.witiot.cloudbox.views.navigation.SplashActivity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * 网上冲浪
 */
public class InternetFragment extends BaseFragment {


    @BindView(R.id.app_rv)
    RecyclerView appRv;
    Unbinder unbinder;

    GridLayoutManager gridLayoutManager;
    @BindView(R.id.flow_record_tv)
    TextView flowRecordTv;
    @BindView(R.id.buy_flow_bt)
    TextView buyFlowBt;
    @BindView(R.id.surplusflow_tv)
    TextView surplusflowTv;
    @BindView(R.id.ad_rv)
    RecyclerView adRv;
    @BindView(R.id.ad_time_tv)
    TextView adTimeTv;
    @BindView(R.id.ad_ll)
    LinearLayout adLl;

    @BindView(R.id.btn_buy_flow)
    TextView btnBuyFlow;

    @BindView(R.id.rl_nodata)
    RelativeLayout rlNodata;

    private List<AppInfo> appInfos = new ArrayList<>();
    AppListAdapter adapter;

    CustInfoRes.CustInfoBean custInfoBean;

    List<AdListRes.DatBean.AdBean> adBeanList;
    LinearLayoutManager layoutManager;
    AdListAdapter adListAdapter;

    TextViewTimeCount textViewTimeCount;

    private int intentPosition = -1;

    public InternetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_internet, container, false);
        unbinder = ButterKnife.bind(this, view);
        initUI();

        //flowHandler.postDelayed(flowRunnable,  60*1000);

        return view;
    }

    private void initUI() {
        getAppList();
        initAppUI(4);
        adListAdapter = new AdListAdapter(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        adRv.setLayoutManager(layoutManager);
        adRv.setAdapter(adListAdapter);
        adListAdapter.setOnItemClickLIstener(new RcOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(position < 0 && position >= adBeanList.size()){
                    return;
                }
                CommonUtils.intentChrome(getContext(),adBeanList.get(position).getAdvertUrl());
            }
        });

        textViewTimeCount = new TextViewTimeCount(getContext(), 60000, 1000, adTimeTv, "点击关闭广告");
        textViewTimeCount.start();
    }

    private void initAppUI(final int num) {
        adapter = new AppListAdapter(getContext(),num);
        appRv.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getActivity(), num);//设置为一个3列的纵向网格布局
        appRv.setLayoutManager(gridLayoutManager);
        appRv.setAdapter(adapter);
        adapter.setData(appInfos);

        adapter.setOnItemClickLIstener(new RcOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(appInfos.get(position).appIntent);
//                intentPosition = position;
//                startActivityForResult(new Intent(getContext(), SplashActivity.class),2020);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2020 && resultCode == 2020){
            try {
                startActivity(appInfos.get(intentPosition).appIntent);
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
    }

    private void getData() {
        showProgress();
        CustInfoRqs rqs = new CustInfoRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
        rqs.setVer("1");
        CustInfoRqs.DatBean datBean = new CustInfoRqs.DatBean();
        datBean.setCustomerId((String) SPUtils.get(getContext(), "customerId", ""));
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "getCustInfo", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                if (isSucceed) {
                    LogUtil.log("XXX= " +result);

                    Type typeToken = new TypeToken<CustInfoRes>() {
                    }.getType();
                    CustInfoRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        custInfoBean = res.getDat();
                        if (custInfoBean != null && surplusflowTv != null) {
                            surplusflowTv.setText("剩余流量：\t\t" + custInfoBean.getRows().getSurplusFlow() + " MB");
                            if (custInfoBean.getRows().getSurplusFlow() > 0f) {
                                rlNodata.setVisibility(View.GONE);  // 不显示
                            } else {
                                rlNodata.setVisibility(View.VISIBLE); // 显示
                            }
                        }
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
                getAdvert();
            }
        });
    }

    private int currPageIndex =1; int pages=1;
    private void getAdvert() {

        // paramlist
        AdListRqs.DatBean.Paramlist paramlist = new AdListRqs.DatBean.Paramlist();
        paramlist.setPosition("1");     // 广告位置 1：网上冲浪，2：开机欢迎页 3：医教中心
        paramlist.setAdvertType("1");   // 1是文本 2是视频
        paramlist.setHospitalId(CommonUtils.getHospitalId( getContext() ));  // 当前所属的医院编号
        paramlist.setDateNow( new SimpleDateFormat("yyyy-MM-dd").format(TimeUtils.getNowDate()));

        // dat
        AdListRqs.DatBean datBean = new AdListRqs.DatBean();
        datBean.setPageindex(currPageIndex+"");
        datBean.setPagesize("6");   // 最多6条记录
        datBean.setOrderby("publishTime desc");
        datBean.setParamlist(paramlist);

        // request
        AdListRqs rqs = new AdListRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "advert/listAllot", new XRequestCallback() {
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
                            adListAdapter.setData(adBeanList);
                            adListAdapter.notifyDataSetChanged();
                            if(adBeanList.size() == 0 ){
                                textViewTimeCount.cancel();
                                adLl.setVisibility(View.GONE);
                                initAppUI(5);
                            }
                        }

                        // 总页数
                        pages = res.getDat().getPages();
//                        if(currPageIndex<pages+1){
//                            currPageIndex ++;
//                        }
//                        if(currPageIndex==pages+1){
//                            currPageIndex=1;
//                        }
                        // LogUtil.log("总页数: pages=" + pages + " , currPageIndex=" + currPageIndex );
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(textViewTimeCount != null){
            textViewTimeCount.cancel();
        }
    }


    public void onListItemClick(ListView l, View v, int position, long id) {
        // 启动所选应用
//        startActivity(appList.get(position).appIntent);
    }

    /**
     * 获取非系统应用信息列表
     */
    private void getAppList() {
        PackageManager pm = this.getActivity().getPackageManager();
        // Return a List of all packages that are installed on the device.
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0
                    && !packageInfo.packageName.equals("com.witiot.cloudbox"))    // 非系统应用
            {
                AppInfo info = new AppInfo();
                info.appName = packageInfo.applicationInfo.loadLabel(pm)
                        .toString();
                info.pkgName = packageInfo.packageName;
                info.appIcon = packageInfo.applicationInfo.loadIcon(pm);
                // 获取该应用安装包的Intent，用于启动该应用
                info.appIntent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                appInfos.add(info);
            } else {
                // 系统应用　　　　　　　　
            }
        }
    }

    @OnClick({R.id.rl_nodata,R.id.flow_record_tv, R.id.buy_flow_bt, R.id.ad_time_tv, R.id.btn_buy_flow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.flow_record_tv:
                Intent intent = new Intent(getContext(), FlowRecordActivity.class);
                intent.putExtra("custInfoBean", custInfoBean);
                startActivity(intent);
                break;
            case R.id.buy_flow_bt:
                startActivity(new Intent(getActivity(), BuyFlowActivity.class));
                break;
            case R.id.btn_buy_flow:
                startActivity(new Intent(getActivity(), BuyFlowActivity.class));
//                killAll();
                break;
            case R.id.ad_time_tv:
                adLl.setVisibility(View.GONE);
                initAppUI(5);
                break;
        }
    }
    ActivityManager activityMgr;
    private void killAll(){

     activityMgr = (ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);
//     activityMgr.forceStopPackage(packageName);

        for (int i = 0; i < appInfos.size(); i++) {
            activityMgr.killBackgroundProcesses(appInfos.get(i).appName);
        }
    }


    // 定时器 每隔 1分钟切换 广告
    android.os.Handler flowHandler =  new android.os.Handler();

    Runnable flowRunnable = new Runnable() {
        @Override
        public void run() {

            //getAdvert();

            //flowHandler.postDelayed(flowRunnable,  60*1000);
        }
    };

    // 2. 启动计时器
    //handler.postDelayed(runnable, 2000);//每两秒执行一次runnable.
    //3. 停止计时器
    //handler.removeCallbacks(runnable);

}
