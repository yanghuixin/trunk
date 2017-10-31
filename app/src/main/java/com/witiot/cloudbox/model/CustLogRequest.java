package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class CustLogRequest extends BaseRqs{

    private CustLogRequest.DatBean dat;

    public CustLogRequest.DatBean getDat() {
        return dat;
    }

    public void setDat(CustLogRequest.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean extends BaseRequest {

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

            public String customerId;
            public String deviceId;
            private int operType;
            private String beginTime;
            private String endTime;
            private String operPage;
            private String operContent;

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

            public int getOperType() {
                return operType;
            }
            public void setOperType(int operType) {
                this.operType = operType;
            }

            public String getOperPage() {
                return operPage;
            }
            public void setOperPage(String operPage) {
                this.operPage = operPage;
            }

            public String getOperContent() {
                return operContent;
            }
            public void setOperContent(String  operContent) {
                this.operContent = operContent;
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
