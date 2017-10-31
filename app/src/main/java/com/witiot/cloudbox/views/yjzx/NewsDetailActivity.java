package com.witiot.cloudbox.views.yjzx;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.NewsListIndexRes;
import com.witiot.cloudbox.model.NewsListRes;
import com.witiot.cloudbox.model.NewsListRqs;
import com.witiot.cloudbox.model.PostVisitActionRqs;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.views.BaseActivity;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.detail_title)
    TextView detailTitle;
    @BindView(R.id.detail_time)
    TextView detailTime;
    @BindView(R.id.detail_content)
    WebView webView;

    NewsListRes.DatBean.NewsBean newsBean;
    @BindView(R.id.news_detail_title)
    TextView newsDetailTitle;

    String newsId = "";
    NewsListRes newsListRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        getData();
    }

    private void postData(String id) {
        PostVisitActionRqs rqs = new PostVisitActionRqs();
        rqs.setCmd("add");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        PostVisitActionRqs.DatBean datBean = new PostVisitActionRqs.DatBean();
        datBean.setAction("1");
        datBean.setId(id);
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "news/postFavourite", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
            }
        });
    }

    private void getData() {
        String title = getIntent().getStringExtra("title");
        if(StringUtils.stringIsNotEmpty(title)){
            newsDetailTitle.setText(title);
        }
        newsBean = (NewsListRes.DatBean.NewsBean) getIntent().getSerializableExtra("newsBean");
        if (newsBean != null) {
            postData(newsBean.getId() + "");
            initWebView();

        }else{
            newsId = getIntent().getStringExtra("newsId");
            getNewsList();
        }

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
//        paramlist.setKind("1");
//        paramlist.setChannel(mainType);
//        paramlist.setForum(tabType);
        paramlist.setId(newsId);
        datBean.setParamlist(paramlist);
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "news/list", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<NewsListRes>() {
                    }.getType();
                    newsListRes = gson.fromJson(result, typeToken);
                    if (newsListRes.getRet().equals("10000")) {
                       if(newsListRes.getDat() != null && newsListRes.getDat().getRows().size() > 0){
                           newsBean = newsListRes.getDat().getRows().get(0);
                           postData(newsBean.getId()+"");
                           initWebView();
                       }
                    } else {
                        toastShow(newsListRes.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });

    }

    private void initWebView() {
        detailTitle.setText(newsBean.getTitle());
        detailTime.setText(newsBean.getCreateTime() + "\t\t\t来源：" + newsBean.getSource() +
                "\t\t\t浏览 " + newsBean.getVisitCount() + "次");

        if (StringUtils.stringIsEmpty(newsBean.getContent())) {
            return;
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setBlockNetworkImage(false);
        String htmlData = Uri.decode(newsBean.getContent());//text+text1+text2+html;//goodsDetailBean.getContent();
        htmlData = htmlData.replaceAll("&amp;", "");
        htmlData = htmlData.replaceAll("&quot;", "\"");
        htmlData = htmlData.replaceAll("&lt;", "<");
        htmlData = htmlData.replaceAll("&gt;", ">");
        htmlData = htmlData.replaceAll("nbsp;", " ");
        //  重写 WebViewClient
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //  html加载完成之后，调用js的方法
            imgReset();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgress();
        }
    }

    private void imgReset() {
        webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName('img'); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "var img = objs[i];   "
                + "    img.style.width = '100%';   "
                + "    img.style.height = 'auto';   "
                + "}" + "})()");
        dismissProgress();
    }


    @OnClick(R.id.close)
    public void onViewClicked() {
        finish();
    }
}
