package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/15.
 */

public class CustInfoRqs extends BaseRqs{
    private CustInfoRqs.DatBean dat;

    public CustInfoRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(CustInfoRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {
        String customerId;//用户手机号

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }
    }
}
