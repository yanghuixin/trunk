package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/15.
 */

public class CustPointRequest extends BaseRqs{
    private CustPointRequest.DatBean dat;

    public CustPointRequest.DatBean getDat() {
        return dat;
    }

    public void setDat(CustPointRequest.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        String customerId;  // 用户手机号

        public String getCustomerId() {
            return customerId;
        }
        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }
    }
}
