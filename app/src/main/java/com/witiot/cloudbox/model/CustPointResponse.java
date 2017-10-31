package com.witiot.cloudbox.model;

import com.witiot.cloudbox.utils.TimeUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by lixin on 2017/8/15.
 */

public class CustPointResponse extends BaseRes{

    private String dat;
    public String getDat() {
        return dat;
    }
    public void setDat(String dat) {
        this.dat = dat;
    }

}
