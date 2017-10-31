package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/24.
 */

public class PostFlowRqs extends BaseRqs {
    private DatBean dat;

    public DatBean getDat() {
        return dat;
    }

    public void setDat(DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        private String customerId;
        private String deviceId;
        private String postTime;
        private String flowSize;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getPostTime() {
            return postTime;
        }

        public void setPostTime(String postTime) {
            this.postTime = postTime;
        }

        public String getFlowSize() {
            return flowSize;
        }

        public void setFlowSize(String flowSize) {
            this.flowSize = flowSize;
        }
    }
}
