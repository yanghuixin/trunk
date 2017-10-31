package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/2.
 */

public class BaseRes {


    /**
     * ret : 10000
     * msg : 发送成功
     * ver : 1
     */

    private String ret;
    private String msg;
    private String ver;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
