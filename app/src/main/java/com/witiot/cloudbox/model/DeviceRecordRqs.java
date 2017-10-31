package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class DeviceRecordRqs extends BaseRqs{

    private DeviceRecordRqs.DatBean dat;

    public DeviceRecordRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(DeviceRecordRqs.DatBean dat) {
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

            private String deviceId, hospitalId, reportMobile, reportMan, status ;

            public String getDeviceId() {
                return deviceId;
            }
            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getStatus() {
                return status;
            }
            public void setStatus(String status) {
                this.status = status;
            }

        }
    }

}
