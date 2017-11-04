package com.witiot.cloudbox.views.secure;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.NewsListRes;
import com.witiot.cloudbox.model.NewsListRqs;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.views.BaseFragment;
import com.witiot.cloudbox.views.yjzx.NewsDetailActivity;
import com.witiot.cloudbox.widget.BackgroundDarkPopupWindow;

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
 */
public class SecureFragment extends BaseFragment {


    @BindView(R.id.ywx_what)
    TextView ywxWhat;
    @BindView(R.id.ywx_need_know)
    TextView ywxNeedKnow;
    @BindView(R.id.ywx_yiyi)
    TextView ywxYiyi;
    @BindView(R.id.ywx_question)
    TextView ywxQuestion;
    @BindView(R.id.ywx_baofei)
    TextView ywxBaofei;
    @BindView(R.id.ywx_introduce)
    TextView ywxIntroduce;
    @BindView(R.id.ywx_jifen)
    TextView ywxJifen;
    @BindView(R.id.ywx_anli)
    TextView ywxAnli;
    Unbinder unbinder;
    @BindView(R.id.ywx_checkbox)
    CheckBox ywxCheckbox;
    @BindView(R.id.ywx_book)
    TextView ywxBook;
    @BindView(R.id.ywx_online)
    TextView ywxOnline;
    @BindView(R.id.h5_content)
    RelativeLayout h5Content;
    @BindView(R.id.close)
    TextView close;

    @BindView(R.id.video_hint)
    TextView video_hint;

    private List<NewsListRes.DatBean.NewsBean> newsBeanList = new ArrayList<>();
    NewsListRes newsListRes;
    WebView webView;

    public SecureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_secure, container, false);
        getNewsList();
        unbinder = ButterKnife.bind(this, view);
        initWebView();
        return view;
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
        webView.loadUrl(UrlManage.HOST_URL + "video_baoxian.html");
        h5Content.addView(webView);
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
        paramlist.setKind("2");
//        paramlist.setChannel("育儿频道");
//        paramlist.setForum("就医指南");
        datBean.setParamlist(paramlist);
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "news/list", new XRequestCallback() {
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
            }
        });

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


    @OnClick({R.id.ywx_checkbox, R.id.ywx_book, R.id.ywx_online,R.id.close, R.id.ywx_what, R.id.ywx_need_know, R.id.ywx_yiyi, R.id.ywx_question, R.id.ywx_baofei, R.id.ywx_introduce, R.id.ywx_jifen, R.id.ywx_anli})
    public void onViewClicked(View view) {
        if (newsBeanList == null || newsBeanList.size() == 0) {
            toastShow("数据获取中，请稍后重试");
            getNewsList();
            return;
        }
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        switch (view.getId()) {
            case R.id.ywx_what:
                intent.putExtra("newsBean", getBean("1"));
                intent.putExtra("title", "手术意外险 > 资讯详情");
                startActivity(intent);
                break;
            case R.id.ywx_need_know:
                intent.putExtra("newsBean", getBean("2"));
                intent.putExtra("title", "手术意外险 > 资讯详情");
                startActivity(intent);
                break;
            case R.id.ywx_yiyi:
                intent.putExtra("newsBean", getBean("3"));
                intent.putExtra("title", "手术意外险 > 资讯详情");
                startActivity(intent);
                break;
            case R.id.ywx_question:
                intent.putExtra("newsBean", getBean("4"));
                intent.putExtra("title", "手术意外险 > 资讯详情");
                startActivity(intent);
                break;
            case R.id.ywx_baofei:
                intent.putExtra("newsBean", getBean("5"));
                intent.putExtra("title", "手术意外险 > 资讯详情");
                startActivity(intent);
                break;
            case R.id.ywx_introduce:
                intent.putExtra("newsBean", getBean("6"));
                intent.putExtra("title", "手术意外险 > 资讯详情");
                startActivity(intent);
                break;
            case R.id.ywx_jifen:
                intent.putExtra("newsBean", getBean("7"));
                intent.putExtra("title", "手术意外险 > 资讯详情");
                startActivity(intent);
                break;
            case R.id.ywx_anli:
                intent.putExtra("newsBean", getBean("8"));
                intent.putExtra("title", "手术意外险 > 资讯详情");
                startActivity(intent);
                break;
            case R.id.close:
                h5Content.setVisibility(View.GONE);
                video_hint.setVisibility(View.GONE);
                if (webView != null) {
                    webView.destroy();
                    webView = null;
                    close.setVisibility(View.GONE);
                }
                break;
            case R.id.ywx_checkbox:
                break;
            case R.id.ywx_book:
                showBookPopup();
                break;
            case R.id.ywx_online:
                if(!ywxCheckbox.isChecked()){
                    toastShow("请查看并同意《医疗意外知情同意书》");
                    return;
                }
                showEwmPopup();
                break;

        }

    }

    private NewsListRes.DatBean.NewsBean getBean(String channel) {
        NewsListRes.DatBean.NewsBean bean = null;
        for (int i = 0; i < newsBeanList.size(); i++) {
            if (StringUtils.stringIsNotEmpty(newsBeanList.get(i).getChannel())) {
                if (newsBeanList.get(i).getChannel().equals(channel)) {
                    bean = newsBeanList.get(i);
                    int v = newsBeanList.get(i).getVisitCount() + 1;
                    newsBeanList.get(i).setVisitCount(v);
                    break;
                }
            }
        }
        return bean;
    }
    BackgroundDarkPopupWindow popup;
    private void showBookPopup() {
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.pop_secure_book, null);
        TextView ok = (TextView) popupView.findViewById(R.id.ok_bt);
        WebView webView = (WebView) popupView.findViewById(R.id.pop_webview);
//        webView.loadUrl("file:///android_asset/medical_accident.html");
        webView.loadUrl(UrlManage.HOST_URL+"medical_accident.html");
        popup = new BackgroundDarkPopupWindow(
                popupView, 1500,
                WindowManager.LayoutParams.MATCH_PARENT);
        popup.setFocusable(true);
        popup.setOutsideTouchable(false);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.clear)));
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.setDarkStyle(-1);
        popup.setDarkColor(Color.parseColor("#a0000000"));
        popup.resetDarkPosition();
        popup.darkFillScreen();
        popup.showAtLocation(ywxBook.getRootView(), Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popup = null;
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });
    }

    private void showEwmPopup() {
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.pop_secure_ewm, null);
        TextView ok = (TextView) popupView.findViewById(R.id.ok_bt);
        popup = new BackgroundDarkPopupWindow(
                popupView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setOutsideTouchable(false);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.clear)));
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.setDarkStyle(-1);
        popup.setDarkColor(Color.parseColor("#a0000000"));
        popup.resetDarkPosition();
        popup.darkFillScreen();
        popup.showAtLocation(ywxBook.getRootView(), Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popup = null;
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });
    }
}
