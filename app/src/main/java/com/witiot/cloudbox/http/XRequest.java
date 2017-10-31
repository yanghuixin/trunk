package com.witiot.cloudbox.http;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.NewsListRes;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.ConstantBox;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.NetworkUtils;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.views.login.LoginActivity;
import com.witiot.cloudbox.views.navigation.NavigationActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.Map;


/**
 * Created by lixin on 16/3/25.
 */
public class XRequest {

    private static final int RETURN = 1;
    private static final int NULL= -1;
    private XRequestCallback callback;
    private String resultStr;
    Callback.Cancelable cancelable;
    Gson gson ;
    Context context;
    String url = "";
    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

                switch (msg.what) {
                    case RETURN:
                        if(StringUtils.stringIsEmpty(resultStr)){//resultStr为空返回true
                            callback.callback(false,resultStr);//public void callback(boolean isSucceed, String result);
                            return;
                        }
                        //在运行期间无法得知泛型参数的类型，对这个类的对象进行序列化和反序列化都不能正常进行。
                        // Gson通过借助TypeToken类来解决这个问题。只要将需要获取类型的泛型类作为TypeToken的泛
                        // 型参数构造一个匿名的子类，就可以通过getType()方法获取到我们使用的泛型类的泛型参数类型
                        Type typeToken = new TypeToken<BaseRes>() {
                        }.getType();//获取到我们使用的泛型类的泛型参数类型
                          BaseRes baseRes = gson.fromJson(resultStr, typeToken);
                        // TODO: 2017/10/31 如果 resultStr不为空就解析出 BaseRes对象此语句是什么意思
                        if (!url.equals("advert/list") && baseRes.getRet().equals("10002")
                                && baseRes.getMsg().contains("tok")) {
                            SPUtils.remove(context,"tok");
                            SPUtils.remove(context,"customerId");
//                            callback.callback(false,resultStr);
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }else {
                            callback.callback(true, resultStr);
                        }
                        break;
                    case NULL:
                        LogUtil.log("HttpException",resultStr);
                        if( resultStr.contains("SocketTimeoutException")){
//                            Toast.makeText(context,"你的网络好像有问题哎，再试试呗",Toast.LENGTH_SHORT).show();
                            resultStr = "网络连接超时，请稍后重试";
                        }else if( resultStr.contains("HttpException") ){
//                            Toast.makeText(context,"你的网络好像有问题哎",Toast.LENGTH_SHORT).show();
                            resultStr = "网络连接异常，请稍后重试";
                        }
                        callback.callback(false,resultStr);
                        break;
                }
        }

    };



    public void sendPostRequest(Context context,String json, String url,XRequestCallback xRequestCallback){//PostRequest
        this.callback = xRequestCallback;
        this.context = context;
//        if(NetworkUtils.isConnected()){
//            resultStr = "你的网络好像有问题哎";
//            mHandler.sendEmptyMessage(NULL);
//            return;
//        }
        if(gson == null) {
            gson = new Gson();
        }
        this.url = url;
        RequestParams params = new RequestParams(UrlManage.BASE_URL+url);
        params.addBodyParameter(json,"");
        params.setConnectTimeout(60000);
        params.setReadTimeout(100000);
//        LogUtil.log("post data ",json);

        LogUtil.log("post data ",gson.toJson(params));
        cancelable =  x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {//result为空时返回空 （ callback.callback(false,resultStr) ），有值时返回result对应的值
                LogUtil.log(XRequest.this.url+"  result",result);
                resultStr = result;
                mHandler.sendEmptyMessage(RETURN);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {//网络请求失败时返回 resultStr = "网络连接超时，请稍后重试";或者 resultStr = "网络连接异常，请稍后重试";
                resultStr = ex.toString();
                mHandler.sendEmptyMessage(NULL);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public void sendPostImgRequest(Context context,String json, String url,XRequestCallback xRequestCallback){
        this.callback = xRequestCallback;
        this.context = context;
//        if(NetworkUtils.isConnected()){
//            resultStr = "你的网络好像有问题哎";
//            mHandler.sendEmptyMessage(NULL);
//            return;
//        }
        if(gson == null) {
            gson = new Gson();
        }
        this.url = url;
        RequestParams params = new RequestParams(UrlManage.BASE_URL+url);
        params.addBodyParameter(json,"");
        params.setConnectTimeout(60000);
        params.setReadTimeout(100000);
//        LogUtil.log("post data ",json);

        LogUtil.log("post data ",gson.toJson(params));
        cancelable =  x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {//result为空时返回空 （ callback.callback(false,resultStr) ），有值时返回result对应的值
                LogUtil.log("result",result);
                resultStr = result;
                mHandler.sendEmptyMessage(RETURN);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {//网络请求失败时返回 resultStr = "网络连接超时，请稍后重试";或者 resultStr = "网络连接异常，请稍后重试";
                resultStr = ex.toString();
                mHandler.sendEmptyMessage(NULL);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }



}
