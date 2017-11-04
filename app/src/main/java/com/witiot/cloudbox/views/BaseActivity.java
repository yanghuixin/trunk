package com.witiot.cloudbox.views;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.witiot.cloudbox.R;
import com.witiot.cloudbox.utils.LogUtil;
//import com.witiot.cloudbox.utils.statusbar.StatusBarFontHelper;
import com.witiot.cloudbox.utils.Utils;
import com.witiot.cloudbox.utils.statusbar.StatusBarFontHelper;
import com.witiot.cloudbox.widget.MyProgressDialog;

import butterknife.ButterKnife;

public class BaseActivity extends FragmentActivity {



    public MyProgressDialog progressDialog;
    public Activity mActivity;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setStateBarColor();
        mActivity = this;
        Utils.init(this);
        progressDialog = new MyProgressDialog(this,R.style.MyProgressDialog);

    }

    protected void setStateBarColor() {
        //设置状态栏字体颜色为暗色
        StatusBarFontHelper.setStatusBarMode(this,true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getColor(R.color.blue_title));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        mActivity = this;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStateBarColor(R.color.line_bg_grey);
        LogUtil.log("baseac","onCreate");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



//    protected void setUpCommonBackTooblBar(int toolbarId, String title) {
//        Toolbar mToolbar = (Toolbar) findViewById(toolbarId);
//        mToolbar.setTitle(title);
//        setSupportActionBar(mToolbar);
//        toobarAsBackButton(mToolbar);
//    }

    /**
     * toolbar点击返回，模拟系统返回
     * 设置toolbar 为箭头按钮
     * app:navigationIcon="?attr/homeAsUpIndicator"
     *
     * @param
     */
//    public void toobarAsBackButton(Toolbar toolbar) {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
////        toolbar.setNavigationIcon(R.mipmap.ic_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//    }

    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void showProgress(){
        if(progressDialog == null){
            progressDialog = new MyProgressDialog(this,R.style.MyProgressDialog);
        }else{
            if(!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }
    public void dismissProgress(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}
