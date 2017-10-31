package com.witiot.cloudbox.http;

import com.witiot.cloudbox.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by lixin on 15/3/26.
 */
public class HttpUtil {


    public static String combineParam() {


        String result = "Android" ;

        return result;
    }


    public static Map<String,String> generateEncrypt( Map<String,String> map ) {


//        Map<String,String> result = new TreeMap<>();
//
//        result.put("appkey",appKey);
//        long timestamp = Calendar.getInstance().getTimeInMillis();
//        result.put("timestamp",timestamp+"");
//        LogUtil.log("generateEncrypt timestamp",timestamp+"");
//
//        String privateKey = appKey.substring(0,26) + sourceString;
//        result.put("appid","xhm021962632");
//        map.putAll(result);

//        String signString = getSignStr(map);
//
//        String sign = getHmacMd5Str(signString.toLowerCase(),privateKey);
//        LogUtil.log("generateEncrypt signString md5",sign);
//
//        map.put("sign",sign);
//
//        LogUtil.log("generateEncrypt data",map.toString());

        return map;
    }

    private static String getSignStr(Map<String, String> paramMap) {
        StringBuilder result = new StringBuilder();
        Map<String,String> tempMap = new TreeMap<>();
        for(Map.Entry<String,String> entry:paramMap.entrySet()){
            tempMap.put(entry.getKey(),entry.getValue());
        }

        for (Map.Entry<String, String> entry : tempMap.entrySet()) {
            LogUtil.log(entry.getKey() + ":" + entry.getValue());
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
//        appid=xhm021962632appkey=54aba4a577b9f4dd6228bfd99eb2a780code=123456phone=13721364233timestamp=1491990761325
        return  result.toString();
    }

    public static String getHmacMd5Str(String s, String keyString)
    {
        String sEncodedString = null;
        LogUtil.log("getHmacMd5Str sEncodedString",s);
        try
        {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(key);

            byte[] bytes = mac.doFinal(s.getBytes());

            StringBuffer hash = new StringBuffer();

            for (int i=0; i<bytes.length; i++) {
                String hex = Integer.toHexString(0xFF &  bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }

            sEncodedString = hash.toString();
        }
        catch (UnsupportedEncodingException e) {}
        catch(InvalidKeyException e){}
        catch (NoSuchAlgorithmException e) {}
        return sEncodedString ;
    }


    public static String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};// 用来将字节转换成16进制表示的字符
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();// MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2];// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
            // 进制需要 32 个字符
            int k = 0;// 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {// 从第一个字节开始，对 MD5 的每一个字节// 转换成 16
                // 进制字符的转换
                byte byte0 = tmp[i];// 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换,// >>>
                // 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换

            }
            s = new String(str);// 换后的结果转换为字符串

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }
}
