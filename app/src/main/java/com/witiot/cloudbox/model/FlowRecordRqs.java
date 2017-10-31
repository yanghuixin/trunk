package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class FlowRecordRqs extends BaseRqs{

    private FlowRecordRqs.DatBean dat;

    public FlowRecordRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(FlowRecordRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        private DatBean.Paramlist paramlist;

        public DatBean.Paramlist getParamlist() {
            return paramlist;
        }

        public void setParamlist(DatBean.Paramlist paramlist) {
            this.paramlist = paramlist;
        }

        public static class Paramlist {
            String customerId;//用户手机号
            String deviceId;
            String beginTime;
            String endTime;

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

            public String getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(String beginTime) {
                this.beginTime = beginTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }
        }
    }

}
