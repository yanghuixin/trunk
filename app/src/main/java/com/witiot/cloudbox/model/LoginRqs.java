package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/2.
 */

public class LoginRqs extends BaseRqs{

    /**
     * dat : {"customerId":"15010387145","customerPwd":"E10ADC3949BA59ABBE56E057F20F883E"}
     */

    private DatBean dat;

    public DatBean getDat() {
        return dat;
    }

    public void setDat(DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {
        /**
         * customerId : 15010387145
         * customerPwd : E10ADC3949BA59ABBE56E057F20F883E
         */

        private String customerId;
        private String customerPwd;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerPwd() {
            return customerPwd;
        }

        public void setCustomerPwd(String customerPwd) {
            this.customerPwd = customerPwd;
        }
    }
}
