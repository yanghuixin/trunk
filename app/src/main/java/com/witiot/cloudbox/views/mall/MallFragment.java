package com.witiot.cloudbox.views.mall;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.HttpUtil;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.LoginRes;
import com.witiot.cloudbox.model.LoginRqs;
import com.witiot.cloudbox.model.MallLoginRqs;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.views.BaseFragment;
import com.witiot.cloudbox.views.login.LoginActivity;
import com.witiot.cloudbox.views.navigation.NavigationActivity;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * 商城
 */
public class MallFragment extends BaseFragment {


    @BindView(R.id.webview)
    WebView webView;
    Unbinder unbinder;

    public MallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mall, container, false);

        unbinder = ButterKnife.bind(this, view);
//        login();
        return view;
    }

    private void login() {
        showProgress();
            MallLoginRqs loginRqs = new MallLoginRqs();
            loginRqs.setCmd("get");
            loginRqs.setSrc("3");
            loginRqs.setTok("");
            loginRqs.setVer("1");

            MallLoginRqs.DatBean datBean = new MallLoginRqs.DatBean();
            datBean.setCustomerId((String) SPUtils.get(getContext(),"customerId",""));
            loginRqs.setDat(datBean);

            final Gson gson = new Gson();
            XRequest xRequest = new XRequest();
            xRequest.sendPostRequest(getContext(), gson.toJson(loginRqs), "userLogin", new XRequestCallback() {
                @Override
                public void callback(boolean isSucceed, String result) {
                    dismissProgress();
                    if(isSucceed){
                        Type typeToken = new TypeToken<LoginRes>() {
                        }.getType();
                        LoginRes res = gson.fromJson(result, typeToken);
                        if(res.getRet().equals("10000")){
                            initView(res.getDat());
                        }else {
                            toastShow(res.getMsg());
                        }
                    }else {
                        toastShow("网络连接异常");
                    }
                }
            });
    }

    private void initView(String webUrl) {
        if(webView == null){
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
                    public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }


}
