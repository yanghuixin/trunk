package com.witiot.cloudbox.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;

import com.witiot.cloudbox.R;


/**
 * Created by lixin .
 * 倒计时
 */
public class TimeCount extends CountDownTimer {
    Button checking;
    TextView tvChecking;
    Context context;
    String strDef = "";//默认状态显示的文案
    public TimeCount(Context context, long millisInFuture, long countDownInterval, Button checking, String strDef) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.checking = checking;
        this.context = context;
        this.strDef = strDef;
    }
    public TimeCount(Context context, long millisInFuture, long countDownInterval, TextView checking, String strDef) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.tvChecking = checking;
        this.context = context;
        this.strDef = strDef;
    }
    @Override
    public void onFinish() {//计时完毕时触发
        if(checking != null) {
            checking.setText(strDef);
            checking.setEnabled(true);
            checking.setTextColor(ContextCompat.getColor(context, R.color.red_button));
            checking.setPadding(10, 0, 10, 0);
        }else{
            tvChecking.setText(strDef);
            tvChecking.setEnabled(true);
            tvChecking.setTextColor(ContextCompat.getColor(context,R.color.red_button));
//            tvChecking.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//            tvChecking.getPaint().setAntiAlias(true);//抗锯齿
        }

    }
    @Override
    public void onTick(long millisUntilFinished){//计时过程显示
        if(checking != null) {
            checking.setEnabled(false);
            checking.setTextColor(ContextCompat.getColor(context,R.color.disable));
            checking.setText("重新发送(" + millisUntilFinished / 1000 + "秒)");
            checking.setPadding(10, 0, 10, 0);
        }else {
            tvChecking.setEnabled(false);
            tvChecking.setTextColor(ContextCompat.getColor(context,R.color.disable));
            tvChecking.setText("重新发送(" + millisUntilFinished / 1000 + "秒)");
//            tvChecking.getPaint().setFlags(0); //下划线
        }
    }
}
