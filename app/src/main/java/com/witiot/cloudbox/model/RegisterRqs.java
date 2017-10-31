package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/2.
 */

public class RegisterRqs extends BaseRqs{

    /**
     * dat : {"customerId":"15010387145","customerPwd":"E10ADC3949BA59ABBE56E057F20F883E","smsCode":"2776","deviceToken":"12343223","clientId":"3543654645"}
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
         * smsCode : 2776
         * deviceToken : 12343223
         * clientId : 3543654645
         */

        private String customerId;
        private String customerPwd;
        private String smsCode;
        private String deviceToken;
        private String clientId;

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

        public String getsmsCode() {
            return smsCode;
        }

        public void setsmsCode(String smsCode) {
            this.smsCode = smsCode;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }
    }
}
