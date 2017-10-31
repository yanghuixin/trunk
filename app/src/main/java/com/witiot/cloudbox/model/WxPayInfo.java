package com.witiot.cloudbox.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lixin on 2017/8/31.
 */

public class WxPayInfo {

    /**
     * appid : wx5cb3c3d01c6d54b9
     * partnerid : 1487741482
     * prepayid : wx20170831222342f73be187f00378259711
     * package : Sign=WXPay
     * noncestr : 2223424272
     * timestamp : 1504189422
     * sign : DE6FC0A9F1CF5A8A7EFD397FF64A6FFD
     */

    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
