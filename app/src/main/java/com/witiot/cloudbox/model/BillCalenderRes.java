package com.witiot.cloudbox.model;

import java.util.List;

/**
 * Created by lixin on 2017/8/16.
 */

public class BillCalenderRes extends BaseRes{

    /**
     * dat : {"pageindex":1,"total":2,"pageSize":10,"pages":1,"rows":[{"id":13,"customerId":"13716543250","serviceDay":"2017-08-16 00:00:00","serviceId":"s1","availableCount":0,"actualCount":0,"msg":""},{"id":15,"customerId":"13716543250","serviceDay":"2017-08-16 00:00:00","serviceId":"s3","availableCount":6,"actualCount":0,"msg":""}]}
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
         * pageindex : 1
         * total : 2
         * pageSize : 10
         * pages : 1
         * rows : [{"id":13,"customerId":"13716543250","serviceDay":"2017-08-16 00:00:00","serviceId":"s1","availableCount":0,"actualCount":0,"msg":""},{"id":15,"customerId":"13716543250","serviceDay":"2017-08-16 00:00:00","serviceId":"s3","availableCount":6,"actualCount":0,"msg":""}]
         */

        private int pageindex;
        private int total;
        private int pageSize;
        private int pages;
        private List<RowsBean> rows;

        public int getPageindex() {
            return pageindex;
        }
        public void setPageindex(int pageindex) {
            this.pageindex = pageindex;
        }

        public int getTotal() {
            return total;
        }
        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageSize() {
            return pageSize;
        }
        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }
        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<RowsBean> getRows() {
            return rows;
        }
        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * id : 13
             * customerId : 13716543250
             * serviceDay : 2017-08-16 00:00:00
             * serviceId : s1
             * availableCount : 0
             * actualCount : 0
             * msg :
             */

            private int id;
            private String customerId;
            private String serviceDay;
            private String serviceId;
            private String serviceName;
            private String deviceType;
            private int seqIndex;
            private int availableCount;
            private int actualCount;
            private String msg;

            public int getId() {
                return id;
            }
            public void setId(int id) {
                this.id = id;
            }

            public String getCustomerId() {
                return customerId;
            }
            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

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

            public String getServiceName() {
                return serviceName;
            }
            public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
            }

            public String getDeviceType() {
                return deviceType;
            }
            public void setDeviceType(String deviceType) {
                this.deviceType = deviceType;
            }

            public int getSeqIndex() {
                return seqIndex;
            }
            public void setSeqIndex(int seqIndex) {
                this.seqIndex = seqIndex;
            }

            public int getAvailableCount() {
                return availableCount;
            }
            public void setAvailableCount(int availableCount) {
                this.availableCount = availableCount;
            }

            public int getActualCount() {
                return actualCount;
            }
            public void setActualCount(int actualCount) {
                this.actualCount = actualCount;
            }

            public String getMsg() {
                return msg;
            }
            public void setMsg(String msg) {
                this.msg = msg;
            }
        }
    }
}
