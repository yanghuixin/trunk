package com.witiot.cloudbox.views.yjzx;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.adapter.NewsListAdapter;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.AdListRes;
import com.witiot.cloudbox.model.AdListRqs;
import com.witiot.cloudbox.model.NewsListRes;
import com.witiot.cloudbox.model.NewsListRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.RecycleViewDivider;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseActivity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsListActivity extends BaseActivity {

    NewsListRes newsListRes;
    @BindView(R.id.close)
    TextView close;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    LinearLayoutManager layoutManager;
    @BindView(R.id.yjzx_title)
    TextView yjzxTitle;
    @BindView(R.id.ad_left)
    SimpleDraweeView adLeft;
    @BindView(R.id.ad_right)
    SimpleDraweeView adRight;

    private int totalPage = 0;
    private int currentPage = 1;

    private Context mContext;
    NewsListAdapter newsListAdapter;
    private List<NewsListRes.DatBean.NewsBean> newsBeanList = new ArrayList<>();

    boolean isFirst = true;

    String mainType;
    String tabType;

    List<AdListRes.DatBean.AdBean> adBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ButterKnife.bind(this);
        mContext = this;
        initData();
    }

    private void initData() {
        mainType = getIntent().getStringExtra("mainType");//频道
        tabType = getIntent().getStringExtra("tabType");// "就医指南";

        if (StringUtils.stringIsNotEmpty(mainType)) {
            yjzxTitle.setText("医教中心 > " + mainType);
        } else {
            yjzxTitle.setText("医教中心 > " + tabType);
        }
        initUI();
        getNewsList();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (newsListAdapter != null) {
            newsListAdapter.notifyDataSetChanged();
        }
    }

    private void initUI() {

        xrefreshview.setPinnedTime(500);
        xrefreshview.setPullLoadEnable(false);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.enableReleaseToLoadMore(false);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        recyclerview.setHasFixedSize(true);

        newsListAdapter = new NewsListAdapter(mContext);
        newsListAdapter.setCustomLoadMoreView(new XRefreshViewFooter(mContext));
        recyclerview.setAdapter(newsListAdapter);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.layout_margin_1);
        recyclerview.addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.VERTICAL, spacingInPixels, ContextCompat.getColor(mContext, R.color.bg_grey)));

        newsListAdapter.setData(newsBeanList);

        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshList();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                if (currentPage < totalPage) {
                    currentPage++;
                    getNewsList();
                }
            }
        });

        newsListAdapter.setOnItemClickLIstener(new RcOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("newsBean", newsBeanList.get(position));
                startActivity(intent);
                int v = newsBeanList.get(position).getVisitCount() + 1;
                newsBeanList.get(position).setVisitCount(v);
            }
        });
    }

    private void refreshList() {
        newsBeanList.clear();
        getNewsList();
    }

    private void getNewsList() {
        showProgress();
        NewsListRqs rqs = new NewsListRqs();
        rqs.setCmd("list");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        NewsListRqs.DatBean datBean = new NewsListRqs.DatBean();
//        医教中心：articleType":1,"kind":”1”
//        手术意外险： articleType":"1","kind":"2"
        NewsListRqs.DatBean.Paramlist paramlist = new NewsListRqs.DatBean.Paramlist();
//        paramlist.setArticleType("1");
        paramlist.setKind("1");
        paramlist.setChannel(mainType);
        paramlist.setForum(tabType);
        paramlist.setHospitalId(CommonUtils.getHospitalId(this));
        datBean.setParamlist(paramlist);
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "news/listAllotArticle", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<NewsListRes>() {
                    }.getType();
                    newsListRes = gson.fromJson(result, typeToken);
                    if (newsListRes.getRet().equals("10000")) {
                        newsBeanList.addAll(newsListRes.getDat().getRows());
                    } else {
                        toastShow(newsListRes.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
                xrefreshview.stopRefresh();
                xrefreshview.stopLoadMore();
                newsListAdapter.notifyDataSetChanged();
                getAdvert();
            }
        });

        if (isFirst) {
            isFirst = false;
        }
    }

    private void getAdvert() {

        // paramlist
        AdListRqs.DatBean.Paramlist paramlist = new AdListRqs.DatBean.Paramlist();
        paramlist.setPosition("3");     // 广告位置 1：网上冲浪，2：开机欢迎页 3：医教中心
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

    @OnClick(R.id.close)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.ad_left, R.id.ad_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ad_left:
                CommonUtils.intentChrome(NewsListActivity.this,adBeanList.get(0).getAdvertUrl());
                break;
            case R.id.ad_right:
                CommonUtils.intentChrome(NewsListActivity.this,adBeanList.get(1).getAdvertUrl());
                break;
        }
    }



}
