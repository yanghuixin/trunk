package com.witiot.cloudbox.views.mall;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.LoginRes;
import com.witiot.cloudbox.model.MallLoginRqs;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.views.BaseActivity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MallActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.back)
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        ButterKnife.bind(this);
        login();
    }

    private void login() {
        showProgress();
        MallLoginRqs loginRqs = new MallLoginRqs();
        loginRqs.setCmd("get");
        loginRqs.setSrc("3");
        loginRqs.setTok((String) SPUtils.get(this, "tok", ""));
        loginRqs.setVer("1");

        MallLoginRqs.DatBean datBean = new MallLoginRqs.DatBean();
        datBean.setCustomerId((String) SPUtils.get(this, "customerId", ""));
        loginRqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(loginRqs), "userLogin", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<LoginRes>() {
                    }.getType();
                    LoginRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        initView(res.getDat());
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });
    }

    private void initView(String webUrl) {
        if (webView == null) {
            return;
        }
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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri.getScheme().equals("ytg")) {
                    // 如果 authority  = 预先约定协议里的 mall，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("mall")) {

                        // 执行JS所需要调用的逻辑
                        System.out.println("js调用了Android的方法");
                        // 可以在协议上带有参数并传递到Android上
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();
                        String action = uri.getQueryParameter("action");
                        if (StringUtils.stringIsNotEmpty(action) && action.equals("back")) {
                            finish();
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
                if (view.canGoBack()) {
                    back.setVisibility(View.VISIBLE);
                } else {
                    back.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);

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
        webView.loadUrl(webUrl);
    }

    @OnClick({R.id.close, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.back:
                if (null != webView && webView.canGoBack()) {
                    webView.goBack();
                } else {
                    this.finish();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != webView && webView.canGoBack()) {
                webView.goBack();
            } else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
