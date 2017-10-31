package com.witiot.cloudbox.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.witiot.cloudbox.MyApplication;
import com.witiot.cloudbox.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixin on 2017/7/18.
 */

public class CommonUtils {

    /**
     * 获取APP版本信息
     *
     * @param con
     * @return
     */
    public static String getAppVersionName(Context con) {

        String _versionName;
        PackageInfo pInfo = null;
        try {
            PackageManager pm = con.getPackageManager();
            pInfo = pm.getPackageInfo(con.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            // TODO: handle exception
        }

        _versionName = pInfo.versionName;

        return _versionName;
    }

    public static void intentChrome(Context context,String urlStr) {
        //根据用户的数据类型打开相应的Activity
        if(StringUtils.stringIsEmpty(urlStr)){
            return;
        }
        /**
         *  Intent.ACTION_VIEW
         * String android.intent.action.VIEW
         * 用于显示用户的数据。
         * 比较通用，会根据用户的数据类型打开相应的Activity。
         * 比如 tel:13400010001打开拨号程序，http://www.g.cn则会打开浏览器等。
         */
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(urlStr);
        intent.setData(url);
        context.startActivity(intent);
    }
    /**
     * 初始化热门指示点
     * @param llImageCount
     * @param totalNum 总数
     * @param pageSize 每页显示数量
     */
    public static void initPageControl(Context mContext, LinearLayout llImageCount, int totalNum, int pageSize) {
        if(totalNum <= 0){
            return;
        }
        int totalpage = (totalNum-1)/pageSize+1;
        try {
            llImageCount.removeAllViews();
        }catch (Exception e){

        }
        if(totalNum < 4){
            totalpage = 1;
        }
        for (int i = 0; i < totalpage; i++) {
            TextView textView = new TextView(mContext);
            textView.setText(""+(i+1));
            textView.setTag(i);
            textView.setGravity(Gravity.CENTER);
            if (i > 0) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(12, 0, 12, 0);
                textView.setLayoutParams(layoutParams);
                textView.setBackgroundResource(R.drawable.blue_circle_unsle);
            }else{
                textView.setBackgroundResource(R.drawable.blue_circle);
            }

            llImageCount.addView(textView);
        }
    }

    /**
     * 修改热门指示点状态
     * @param llImageCount
     * @param position
     */
    public static void setupCurrentPage(LinearLayout llImageCount, int position) {
        for (int i = 0; i < llImageCount.getChildCount(); i++) {
            TextView textView = (TextView) llImageCount.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(12, 0, 12, 0);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.blue_circle_unsle);
            if (i == position) {
                textView.setBackgroundResource(R.drawable.blue_circle);
            }
        }
    }

    public static String byteToMBOrGB(long size){//byte转化为MB或者GB

        long kb = 1024;
        long mb = kb*1024;
        long gb = mb*1024;
        if (size >= gb){
            //(float)size/gb得到的商值，保留一位小数并且加上GB为单位
            return String.format("%.1f GB",(float)size/gb);
        }else if (size >= mb){
            float f = (float) size/mb;
            return String.format(f > 100 ?"%.0f MB":"%.1f MB",f);
        }else if (size > kb){
            float f = (float) size / kb;
            return String.format(f>100?"%.0f KB":"%.1f KB",f);
        }else {
            return String.format("%d B",size);
        }
    }

    public static double byteToMB(long size){
        long kb = 1024;
        long mb = kb*1024;
        double d = (double) size/mb;
        return d;
    }

    // 默认size的单位是 MB
    public static String formatMBOrGB(long size){
        long gb = 1024;
        if (size >= gb){
            return String.format("%.1f GB",(float)size/gb);
        }else {
            return String.format("%d MB",size);
        }
    }

    public static Bitmap generateBitmap(String content, int width, int height) {
        //生成QR二维码代码：http://dditblog.com/itshare_423.html
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static long getTimesMonthMorning() {//获取年月日
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }

    /**
     *  获取屏幕宽度
     * @param context
     * @return
     */
    public static int getWidth(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
    /**
     *  获取屏幕高度
     * @param context
     * @return
     */
    public static int getHeight(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     *
     * @param mContext
     * @param textview
     * @param drawbleId
     */
    public static void setTextviewRightDrawable(Context mContext, TextView textview, int drawbleId) {
        Drawable rightDrawable = ContextCompat.getDrawable(mContext, drawbleId);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
        textview.setCompoundDrawables(null, null, rightDrawable, null);
    }

    public static void setTextviewLeftDrawable(Context mContext, TextView textview,int drawbleId) {
        Drawable leftDrawable = ContextCompat.getDrawable(mContext, drawbleId);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        textview.setCompoundDrawables(leftDrawable, null, null, null);
    }

    public static void setTextviewTopDrawable(Context mContext, TextView textview,int drawbleId) {
        Drawable leftDrawable = ContextCompat.getDrawable(mContext, drawbleId);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        textview.setCompoundDrawables(null, leftDrawable, null, null);
    }

    public static void setTextviewDrawableNull(Context mContext, TextView textview) {
        textview.setCompoundDrawables(null, null, null, null);
    }

    // 得到医院编号
    public static String getHospitalId(Context context){
        if(context != null ) {
            return (String) SPUtils.get(context, "ytg_hospitalId", "10002");//得到保存数据
        }else {
            return (String) SPUtils.get(MyApplication.getContext(), "ytg_hospitalId", "10002");
        }
    }
    //设置医院编号
    public static void setHospitalId(Context context, String hospitalId){
        if(context != null) {
            SPUtils.put(context, "ytg_hospitalId", hospitalId);
        }else {
            SPUtils.put(MyApplication.getContext(), "ytg_hospitalId", hospitalId);
        }
    }

    public static String getHospitalName(Context context){
        if(context == null) {
            context = MyApplication.getContext();
        }
        return (String) SPUtils.get(context, "ytg_hospitalName", "10002");
    }

    public static void setHospitalName(Context context, String hospitalName){
        if(context == null) {
            context = MyApplication.getContext();
        }
        SPUtils.put(context, "ytg_hospitalName", hospitalName);
    }
}
