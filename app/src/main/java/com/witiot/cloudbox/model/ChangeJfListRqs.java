package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class ChangeJfListRqs extends BaseRqs{

    private ChangeJfListRqs.DatBean dat;

    public ChangeJfListRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(ChangeJfListRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        private DatBean.Paramlist paramlist;

        private String orderby;

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

        public DatBean.Paramlist getParamlist() {
            return paramlist;
        }

        public void setParamlist(DatBean.Paramlist paramlist) {
            this.paramlist = paramlist;
        }

        public String getOrderby() {
            return orderby;
        }

        public void setOrderby(String orderby) {
            this.orderby = orderby;
        }

        public static class Paramlist {
           public String  deviceId;public String  customerId;


            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
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
