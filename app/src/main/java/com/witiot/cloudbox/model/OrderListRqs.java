package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class OrderListRqs extends BaseRqs{

    private OrderListRqs.DatBean dat;

    public OrderListRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(OrderListRqs.DatBean dat) {
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
            public String orderId;public String  deviceId;public String  orderType;public String  beginTime;public String  endTime;public String status;public String  customerId;public String  orderby;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getOrderType() {
                return orderType;
            }

            public void setOrderType(String orderType) {
                this.orderType = orderType;
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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public String getOrderby() {
                return orderby;
            }

            public void setOrderby(String orderby) {
                this.orderby = orderby;
            }
        }
    }

}
