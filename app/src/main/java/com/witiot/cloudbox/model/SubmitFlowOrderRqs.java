package com.witiot.cloudbox.model;

/**
 * Created by Administrator on 2017/8/17.
 */

public class SubmitFlowOrderRqs extends BaseRqs {
    private SubmitFlowOrderRqs.DatBean dat;
    public SubmitFlowOrderRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(SubmitFlowOrderRqs.DatBean dat) {
        this.dat = dat;
    }
    public static class DatBean {
        /**
         * customerId : 15010387145
         */
        private String customerId;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }
    }
}
