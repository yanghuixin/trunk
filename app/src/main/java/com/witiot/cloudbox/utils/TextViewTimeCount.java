package com.witiot.cloudbox.utils;

import android.content.Context;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;

import com.witiot.cloudbox.R;


/**
 * Created by lixin .
 * 倒计时
 */
public class TextViewTimeCount extends CountDownTimer {
    TextView tvChecking;
    Context context;
    String strDef = "";//默认状态显示的文案
    public TextViewTimeCount(Context context, long millisInFuture, long countDownInterval, TextView checking, String strDef) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.tvChecking = checking;
        this.context = context;
        this.strDef = strDef;
    }
    @Override
    public void onFinish() {//计时完毕时触发
            tvChecking.setText(strDef);
            tvChecking.setEnabled(true);
            //tvChecking.setTextColor(ContextCompat.getColor(context,R.color.white));
        tvChecking.setTextColor(ContextCompat.getColor(context,R.color.blue_text));
            tvChecking.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tvChecking.getPaint().setAntiAlias(true);//抗锯齿
    }
    @Override
    public void onTick(long millisUntilFinished){//计时过程显示
            tvChecking.setEnabled(false);
            //tvChecking.setTextColor(ContextCompat.getColor(context,R.color.disable));
            tvChecking.setText("关闭广告(" + millisUntilFinished / 1000 + "秒)");
            tvChecking.getPaint().setFlags(0); //下划线
            tvChecking.getPaint().setAntiAlias(true);//抗锯齿
    }
}
