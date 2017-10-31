package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/15.
 */

public class BillCalenderRqs extends BaseRqs{
    private BillCalenderRqs.DatBean dat;

    public BillCalenderRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(BillCalenderRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        private String pageindex;
        private String pagesize;

        public String getPageindex() {
            return pageindex;
        }
        public void setPageindex(String pageindex) {
            this.pageindex = pageindex;
        }

        public String getPagesize() {
            return pagesize;
        }
        public void setPagesize(String pagesize) {
            this.pagesize = pagesize;
        }

        private String orderby;
        public String getOrderby(){
            return orderby;
        }
        public void setOrderby(String orderby){
            this.orderby = orderby;
        }

        private Paramlist paramlist;
        public Paramlist getParamlist() {
            return paramlist;
        }
        public void setParamlist(Paramlist paramlist) {
            this.paramlist = paramlist;
        }

        public static class Paramlist {
            String customerId;//用户手机号
            String serviceDay;
            String serviceId;

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
        }
    }
}
