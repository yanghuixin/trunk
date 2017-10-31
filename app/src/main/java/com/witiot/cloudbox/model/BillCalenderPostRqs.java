package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/15.
 */

public class BillCalenderPostRqs extends BaseRqs{
    private BillCalenderPostRqs.DatBean dat;

    public BillCalenderPostRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(BillCalenderPostRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        String customerId;//用户手机号
        String serviceDay;
        String serviceId;
        int useCount;

        public String getServiceDay() {
            return serviceDay;
        }
        public void setServiceDay(String serviceDay) {
            this.serviceDay = serviceDay;
        }

        public String getServiceId() {
            return serviceId;
        }
        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getCustomerId() {
            return customerId;
        }
        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public int getUseCount() {
            return useCount;
        }
        public void setUseCount(int useCount) {
            this.useCount = useCount;
        }
    }
}
