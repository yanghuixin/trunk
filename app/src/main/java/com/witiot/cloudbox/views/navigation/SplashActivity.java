package com.witiot.cloudbox.views.navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.MyApplication;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.AdListRes;
import com.witiot.cloudbox.model.AdListRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.ConstantBox;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.views.login.LoginActivity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    List<AdListRes.DatBean.AdBean> adBeanList;
    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    private AlphaAnimation alphaAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initAdvert();
        ConstantBox.splashAcIsFinish = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConstantBox.splashAcIsFinish = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAdvert();  // 读取接口
    }

    private void getAdvert() {
        // paramlist
        AdListRqs.DatBean.Paramlist paramlist = new AdListRqs.DatBean.Paramlist();
        paramlist.setPosition("2");     // 广告位置 1：网上冲浪，2：开机欢迎页 3：医教中心
        paramlist.setAdvertType("1");   // 1是文本 2是视频
        paramlist.setHospitalId(CommonUtils.getHospitalId( this ));  // 当前所属的医院编号
        paramlist.setDateNow( new SimpleDateFormat("yyyy-MM-dd").format(TimeUtils.getNowDate()));

        // dat
        AdListRqs.DatBean datBean = new AdListRqs.DatBean();
        datBean.setPageindex("1");
        datBean.setPagesize("6");   // 最多6条记录
        datBean.setOrderby("publishTime desc");
        datBean.setParamlist(paramlist);
        // request
        AdListRqs rqs = new AdListRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);
//        showProgress();
        if( ConstantBox.splashAcIsFinish ){
            return;
        }
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "advert/listAllot", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                if( ConstantBox.splashAcIsFinish ){
                    return;
                }
//            dismissProgress();
            if (isSucceed) {
                Type typeToken = new TypeToken<AdListRes>() {}.getType();
                AdListRes res = gson.fromJson(result, typeToken);
                if (res.getRet().equals("10000")) {
                    adBeanList = res.getDat().getRows();
                    if (adBeanList != null && adBeanList.size() >= 1) {
                        // initAdvert();  不显示，只存储数据
                        int index = new Random().nextInt(adBeanList.size());
                        SPUtils.put(MyApplication.getContext(), "ytg_splash_list",  adBeanList.get(index).getVideoUrl());
                    }
                } else {
                    // toastShow(res.getMsg());
//                    initAdvert();
                }
            } else {
                    // toastShow("网络连接异常");
//                initAdvert();
            }
            }
        });
    }

    private void initAdvert() {

        String imgUrl = (String)SPUtils.get(this.getApplicationContext(), "ytg_splash_list", "");

        if (StringUtils.stringIsNotEmpty(imgUrl)) {
            simpleDraweeView.setImageURI(UrlManage.IMG_BASE_URL + imgUrl);
        }else {
            simpleDraweeView.setImageURI(Uri.parse("res://com.witiot.cloudbox/"+ R.mipmap.splash));
        }

        Timer timer1 = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(5000);  // 5 秒关闭
                simpleDraweeView.startAnimation(alphaAnimation);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        enterMainActivity();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                }
            });
            }
        };

        timer1.schedule(timerTask, 100);
    }

    private void enterMainActivity() {
//        if(StringUtils.stringIsNotEmpty((String) SPUtils.get(this,"tok",""))){
//            startActivity(new Intent(this, NavigationActivity.class));
//        }else {
//            Intent it = new Intent(SplashActivity.this, LoginActivity.class);
//            startActivity(it);
//        }
        setResult(2020,null);//InternetFragment onActivityResult处理第三方跳转
        finish();
        overridePendingTransition(R.anim.activity_zoomout, R.anim.activity_zoomin);
    }

}
