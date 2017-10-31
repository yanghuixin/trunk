package com.witiot.cloudbox.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;

/**
 * Created by lixin on 2017/2/28.
 */

public class StringUtils {


    /**
     * 判断字符串是否为空
     * <p/>
     * 空值返回 true
     *
     * @param str
     */
    public static boolean stringIsEmpty(String str) {
        boolean b = true;
        if (str != null && !str.equals("") && !str.equals("null")) {
            b = false;
        }
        return b;
    }

    public static boolean stringIsNotEmpty(String str) {
        boolean b = true;
        if (stringIsEmpty(str)) {
            b = false;
        }
        return b;
    }
    /**
     * 设置商品价格字符串  ￥1000.00
     */
    public static SpannableString goodsPirceStr(String price, float size){
        if(TextUtils.isEmpty(price)){
            return  null;
        }
        String strTag1= "";
        String strTag2 = "";
        String []numArray = price.split("\\.");//split() : 把一个字符串分割成字符串数组，例如var Arr = "2:3:4:5".split(":"); //将返回["2", "3", "4", "5"]
        if(numArray.length == 2){
            strTag1 = numArray[0];
            strTag2 = numArray[1];
        }
        //用SpannableString打造绚丽多彩的文本显示效果http://www.jianshu.com/p/84067ad289d2
        SpannableString span = new SpannableString(price);
        //RelativeSizeSpan，设置文字相对大小，在TextView原有的文字大小的基础上，相对设置文字大小
        span.setSpan(new RelativeSizeSpan(1.0f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(size), 1, strTag1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.8f), strTag1.length()+1, strTag1.length()+strTag2.length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }


    public static SpannableString serviceDetailPirceStr(String price){
        if(TextUtils.isEmpty(price)){
            return  null;
        }
        SpannableString span = new SpannableString(price);
        span.setSpan(new RelativeSizeSpan(1.0f), 0, price.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(0.5f), price.length()-1, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     *
     * @param str  需要修改的字符串
     * @param size  保留几个字符
     * @return
     */
    public static String setStrEllipsize(String str,int size){//str截取size的长度，之后剖用...表示
        String resultStr;
        if(str != null && str.length() > size){
            resultStr = str.substring(0,size)+"…";
        }else{
            resultStr = str;
        }

        return resultStr;
    }

}
