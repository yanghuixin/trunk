package com.witiot.cloudbox.views.yjzx;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.adapter.ChannelViewPageAdapter;
import com.witiot.cloudbox.adapter.NewsListIndexAdapter;
import com.witiot.cloudbox.adapter.NewsListTitleAdapter;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.NewsListIndexRes;
import com.witiot.cloudbox.model.NewsListRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.RecycleViewDivider;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.views.BaseFragment;
import com.witiot.cloudbox.widget.AutoScrollViewPager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 *  YjzxFragment医教中心
 */
public class YjzxFragment extends BaseFragment {


    Unbinder unbinder;

    @BindView(R.id.child_more)
    TextView childMore;
    @BindView(R.id.child_recyclerview)
    RecyclerView childRecyclerview;

    @BindView(R.id.old_more)
    TextView oldMore;
    @BindView(R.id.old_recyclerview)
    RecyclerView oldRecyclerview;

    @BindView(R.id.man_more)
    TextView manMore;
    @BindView(R.id.man_recyclerview)
    RecyclerView manRecyclerview;

    @BindView(R.id.women_more)
    TextView womenMore;
    @BindView(R.id.women_recyclerview)
    RecyclerView womenRecyclerview;

    @BindView(R.id.jyzn_more)
    TextView jyznMore;
    @BindView(R.id.jyzn_recyclerview)
    RecyclerView jyznRecyclerview;

    @BindView(R.id.jksd_more)
    TextView jksdMore;
    @BindView(R.id.jksd_recyclerview)
    RecyclerView jksdRecyclerview;

    @BindView(R.id.jkys_more)
    TextView jkysMore;
    @BindView(R.id.jkys_recyclerview)
    RecyclerView jkysRecyclerview;

    @BindView(R.id.jfss_more)
    TextView jfssMore;
    @BindView(R.id.jfss_recyclerview)
    RecyclerView jfssRecyclerview;

    @BindView(R.id.xlty_more)
    TextView xltyMore;
    @BindView(R.id.xlty_recyclerview)
    RecyclerView xltyRecyclerview;

    @BindView(R.id.zxmr_more)
    TextView zxmrMore;
    @BindView(R.id.zxmr_recyclerview)
    RecyclerView zxmrRecyclerview;
    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.h5_content)
    RelativeLayout h5Content;

    @BindView(R.id.video_hint)
    TextView video_hint;
    @BindView(R.id.autoScrollViewPager)
    AutoScrollViewPager autoScrollViewPager;
    @BindView(R.id.llViewCount)
    LinearLayout llViewCount;

    public YjzxFragment() {
        // Required empty public constructor
    }

    NewsListIndexRes newsListIndexRes;

    NewsListIndexAdapter childAdapter;
    NewsListIndexAdapter oldAdapter;
    NewsListIndexAdapter manAdapter;
    NewsListIndexAdapter womendAdapter;

    NewsListTitleAdapter jyznAdapter;
    NewsListTitleAdapter jksdAdapter;
    NewsListTitleAdapter bjysAdapter;
    NewsListTitleAdapter jfssAdapter;
    NewsListTitleAdapter xltyAdapter;
    NewsListTitleAdapter zxmrAdapter;

    LinearLayoutManager layoutManager;
    WebView webView;

    boolean isCloseVideo = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yjzx, container, false);
        unbinder = ButterKnife.bind(this, view);
        initUI();
        initWebView();
        getNewsList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.log("YjzxFragment onResume");
        if(isCloseVideo && autoScrollViewPager != null){
            autoScrollViewPager.startAutoScroll();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.log("YjzxFragment onStop");
        if(autoScrollViewPager != null){
            autoScrollViewPager.stopAutoScroll();
        }
    }

    private void initWebView() {
        h5Content.removeAllViews();
        webView = new WebView(getContext());
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setDefaultFontSize(12);
        //设置自适应屏幕
        webView.getSettings().setUseWideViewPort(true);
        //设置可以访问文件
        webView.getSettings().setAllowFileAccess(true);
        //支持通过JS打开新窗口
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // 缩放至屏幕的大小
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri.getScheme().equals("ytg")) {
                    // 如果 authority  = 预先约定协议里的 mall，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("video")) {

                        // 执行JS所需要调用的逻辑
                        System.out.println("js调用了Android的方法");
                        // 可以在协议上带有参数并传递到Android上
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();
                        String action = uri.getQueryParameter("action");
                        if (StringUtils.stringIsNotEmpty(action) && action.equals("close")) {
                            close.performClick();
                        }
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissProgress();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                view.loadUrl("about:blank");// 避免出现默认的错误界面
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                // 这个方法在6.0才出现
//                int statusCode = errorResponse.getStatusCode();
//                System.out.println("onReceivedHttpError code = " + statusCode);
//                if (404 == statusCode || 500 == statusCode) {
//                view.loadUrl("about:blank");// 避免出现默认的错误界面
//                    view.loadUrl("file:///android_asset/404.html");
//                }
            }
        });

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        result.confirm();
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return true;
                    }
                });
                // 禁止响应按back键的事件
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        };

        // 设置setWebChromeClient对象
        webView.setWebChromeClient(wvcc);
        webView.loadUrl(UrlManage.HOST_URL + "video_yijiao.html");
        h5Content.addView(webView);
    }

    private void initUI() {
        childAdapter = new NewsListIndexAdapter(getContext());
        oldAdapter = new NewsListIndexAdapter(getContext());
        manAdapter = new NewsListIndexAdapter(getContext());
        womendAdapter = new NewsListIndexAdapter(getContext());

        jyznAdapter = new NewsListTitleAdapter(getContext());
        jksdAdapter = new NewsListTitleAdapter(getContext());
        bjysAdapter = new NewsListTitleAdapter(getContext());
        jfssAdapter = new NewsListTitleAdapter(getContext());
        xltyAdapter = new NewsListTitleAdapter(getContext());
        zxmrAdapter = new NewsListTitleAdapter(getContext());

        layoutManager = new LinearLayoutManager(getContext());
        childRecyclerview.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getContext());
        oldRecyclerview.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getContext());
        manRecyclerview.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getContext());
        womenRecyclerview.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getContext());
        jyznRecyclerview.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(getContext());
        jksdRecyclerview.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(getContext());
        jkysRecyclerview.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(getContext());
        jfssRecyclerview.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(getContext());
        xltyRecyclerview.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(getContext());
        zxmrRecyclerview.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.layout_margin_1);

        RecycleViewDivider divider = new RecycleViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, spacingInPixels, ContextCompat.getColor(getContext(), R.color.bg_grey));

        childRecyclerview.addItemDecoration(divider);
        oldRecyclerview.addItemDecoration(divider);
        manRecyclerview.addItemDecoration(divider);
        womenRecyclerview.addItemDecoration(divider);

        jyznRecyclerview.addItemDecoration(divider);
        jksdRecyclerview.addItemDecoration(divider);
        jkysRecyclerview.addItemDecoration(divider);
        jfssRecyclerview.addItemDecoration(divider);
        xltyRecyclerview.addItemDecoration(divider);
        zxmrRecyclerview.addItemDecoration(divider);

        jyznRecyclerview.setAdapter(jyznAdapter);
        jksdRecyclerview.setAdapter(jksdAdapter);
        jkysRecyclerview.setAdapter(bjysAdapter);
        jfssRecyclerview.setAdapter(jfssAdapter);
        xltyRecyclerview.setAdapter(xltyAdapter);
        zxmrRecyclerview.setAdapter(zxmrAdapter);

        childRecyclerview.setAdapter(childAdapter);
        oldRecyclerview.setAdapter(oldAdapter);
        manRecyclerview.setAdapter(manAdapter);
        womenRecyclerview.setAdapter(womendAdapter);

        h5Content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getNewsList() {
        showProgress();
        NewsListRqs rqs = new NewsListRqs();
        rqs.setCmd("list");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
        rqs.setVer("1");
        NewsListRqs.DatBean datBean = new NewsListRqs.DatBean();
//        医教中心：articleType":1,"kind":”1”
//        手术意外险： articleType":"1","kind":"2"
        NewsListRqs.DatBean.Paramlist paramlist = new NewsListRqs.DatBean.Paramlist();
//        paramlist.setArticleType("1");
//        paramlist.setKind("1");
//        paramlist.setChannel(mainType);
//        paramlist.setForum(tabType);
        paramlist.setHospitalId(CommonUtils.getHospitalId(getContext()));
        datBean.setParamlist(paramlist);
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "news/listAllotIndex", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<NewsListIndexRes>() {
                    }.getType();
                    newsListIndexRes = gson.fromJson(result, typeToken);
                    if (newsListIndexRes.getRet().equals("10000")) {
                        initData();
                    } else {
                        toastShow(newsListIndexRes.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });

    }

    private void initData() {
        List<NewsListIndexRes.ItemBean> items = newsListIndexRes.getDat();
        List<NewsListIndexRes.ItemBean> viewpagelist = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals("育儿频道")) {

                childAdapter.setData(items.get(i).getData());
            } else if (items.get(i).getName().equals("老人频道")) {
                oldAdapter.setData(items.get(i).getData());
            } else if (items.get(i).getName().equals("女性频道")) {
                womendAdapter.setData(items.get(i).getData());
            } else if (items.get(i).getName().equals("男性频道")) {
                manAdapter.setData(items.get(i).getData());
            }else if (items.get(i).getName().equals("就医指南")) {
//                jyznAdapter.setData(items.get(i).getData());
                viewpagelist.add(items.get(i));
            } else if (items.get(i).getName().equals("健康速递")) {
//                jksdAdapter.setData(items.get(i).getData());
                viewpagelist.add(items.get(i));
            } else if (items.get(i).getName().equals("保健饮食")) {
//                bjysAdapter.setData(items.get(i).getData());
                viewpagelist.add(items.get(i));
            } else if (items.get(i).getName().equals("减肥健身")) {
//                jfssAdapter.setData(items.get(i).getData());
                viewpagelist.add(items.get(i));
            } else if (items.get(i).getName().equals("心理体验")) {
//                xltyAdapter.setData(items.get(i).getData());
                viewpagelist.add(items.get(i));
            } else if (items.get(i).getName().equals("整形美容")) {
//                zxmrAdapter.setData(items.get(i).getData());
                viewpagelist.add(items.get(i));
            }
        }

        ChannelViewPageAdapter channelViewPageAdapter = new ChannelViewPageAdapter(getContext());
        autoScrollViewPager.setOffscreenPageLimit(0);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setInterval(5000);
        autoScrollViewPager.setPageMargin(0);
        autoScrollViewPager.setAdapter(channelViewPageAdapter);
        autoScrollViewPager.stopAutoScroll();
        channelViewPageAdapter.setData(viewpagelist);
        CommonUtils.initPageControl(getContext(),llViewCount,viewpagelist.size(),1);
        llViewCount.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < llViewCount.getChildCount(); i++) {
                    TextView textView = (TextView) llViewCount.getChildAt(i);
                    textView.setOnClickListener(new onNumClick(i));
                }
            }
        },1000);
        autoScrollViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (autoScrollViewPager.getAdapter() != null) {
                    CommonUtils.setupCurrentPage(llViewCount,position);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class onNumClick implements View.OnClickListener{
        int position;

        public onNumClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            autoScrollViewPager.setCurrentItem(position);
            autoScrollViewPager.stopAutoScroll();
            autoScrollViewPager.startAutoScroll();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    @OnClick({R.id.child_more, R.id.old_more, R.id.man_more, R.id.women_more, R.id.jyzn_more, R.id.jksd_more, R.id.jkys_more, R.id.jfss_more, R.id.xlty_more, R.id.zxmr_more})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getContext(), NewsListActivity.class);
        switch (view.getId()) {
            case R.id.child_more:
                intent.putExtra("mainType", "育儿频道");
                break;
            case R.id.old_more:
                intent.putExtra("mainType", "老人频道");
                break;
            case R.id.man_more:
                intent.putExtra("mainType", "男性频道");
                break;
            case R.id.women_more:
                intent.putExtra("mainType", "女性频道");
                break;
            case R.id.jyzn_more:
                intent.putExtra("tabType", "就医指南");
                break;
            case R.id.jksd_more:
                intent.putExtra("tabType", "健康速递");
                break;
            case R.id.jkys_more:
                intent.putExtra("tabType", "保健饮食");
                break;
            case R.id.jfss_more:
                intent.putExtra("tabType", "减肥健身");
                break;
            case R.id.xlty_more:
                intent.putExtra("tabType", "心理体验");
                break;
            case R.id.zxmr_more:
                intent.putExtra("tabType", "整形美容");
                break;
        }

        startActivity(intent);
    }

    @OnClick(R.id.close)
    public void onViewClicked() {
        h5Content.setVisibility(View.GONE);
        video_hint.setVisibility(View.GONE);
        if (webView != null) {
            webView.destroy();
            webView = null;
            close.setVisibility(View.GONE);
        }
        isCloseVideo = true;
        autoScrollViewPager.startAutoScroll();
    }


//    public void onViewClicked(View view) {
//        Intent intent = new Intent(getContext(),ChildrensChannelActivity.class);
//        switch (view.getId()) {
//            case R.id.yjzd_baby_iv:
//                intent.putExtra("mainType","育儿频道");
//                break;
//            case R.id.yjzd_baby_tv:
//                intent.putExtra("mainType","育儿频道");
//                break;
//            case R.id.yjzd_old_iv:
//                intent.putExtra("mainType","老人频道");
//                break;
//            case R.id.yjzd_old_tv:
//                intent.putExtra("mainType","老人频道");
//                break;
//            case R.id.yjzx_women_iv:
//                intent.putExtra("mainType","女性频道");
//                break;
//            case R.id.yjzx_women_tv:
//                intent.putExtra("mainType","女性频道");
//                break;
//            case R.id.yjzd_man_iv:
//                intent.putExtra("mainType","男性频道");
//                break;
//            case R.id.yjzd_man_tv:
//                intent.putExtra("mainType","男性频道");
//                break;
//        }
//        startActivity(intent);
//    }
}
