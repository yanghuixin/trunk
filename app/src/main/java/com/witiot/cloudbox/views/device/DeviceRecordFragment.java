package com.witiot.cloudbox.views.device;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.adapter.DeviceRecordAdapter;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.DeviceRecordRes;
import com.witiot.cloudbox.model.DeviceRecordRqs;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.RecycleViewDivider;
import com.witiot.cloudbox.views.BaseFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceRecordFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerview;

    LinearLayoutManager layoutManager;
    Unbinder unbinder;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;

    private int totalPage = 0;
    private int currentPage = 1;

    private Context mContext;
    private DeviceRecordAdapter recordAdapter;
    private List<DeviceRecordRes.DatBean.RecordBean> recordBeanList = new ArrayList<>();

    DeviceRecordRes recordRes;

    public DeviceRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_record, container, false);
        unbinder = ButterKnife.bind(this, view);

        mContext = this.getContext();
        initData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initData() {
        initUI();
        getRecordList();
    }

    private void initUI() {
        xrefreshview.setPinnedTime(500);
        xrefreshview.setPullLoadEnable(false);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.enableReleaseToLoadMore(false);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        recyclerview.setHasFixedSize(true);

        recordAdapter = new DeviceRecordAdapter(mContext);
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
//                Intent intent = new Intent(mContext, NewsDetailActivity.class);
//                intent.putExtra("newsBean", newsBeanList.get(position));
//                startActivity(intent);
//                int v = newsBeanList.get(position).getVisitCount() + 1;
//                newsBeanList.get(position).setVisitCount(v);
            }
        });

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
//                    getRecordList();
                }
            }
        });

    }


    private void refreshList() {
        recordBeanList.clear();
        getRecordList();
    }

    private void getRecordList() {
        showProgress();
        DeviceRecordRqs rqs = new DeviceRecordRqs();
        rqs.setCmd("list");
        rqs.setSrc("3");
        rqs.setTok(""); // (String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        DeviceRecordRqs.DatBean datBean = new DeviceRecordRqs.DatBean();
        DeviceRecordRqs.DatBean.Paramlist paramlist = new DeviceRecordRqs.DatBean.Paramlist();
        paramlist.setDeviceId(DeviceUtils.getAndroidID());
        datBean.setParamlist(paramlist);
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "/device/getRepairList", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                xrefreshview.stopLoadMore();
                xrefreshview.stopRefresh();
                if (isSucceed) {
                    Type typeToken = new TypeToken<DeviceRecordRes>() {
                    }.getType();
                    recordRes = gson.fromJson(result, typeToken);
                    if (recordRes.getRet().equals("10000")) {
                        recordBeanList.addAll(recordRes.getDat().getRows());
                        totalPage = recordRes.getDat().getPages();
//                        if(totalPage > currentPage){
//                            xrefreshview.setPullLoadEnable(true);
//                            xrefreshview.setAutoLoadMore(true);
//                        }else {
//                            xrefreshview.setPullLoadEnable(false);
//                            xrefreshview.setAutoLoadMore(false);
//                        }
                        recordAdapter.notifyDataSetChanged();
                    } else {
                        toastShow(recordRes.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }

                //recordListAdapter.notifyDataSetChanged();
            }
        });

    }

}

