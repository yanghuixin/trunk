package com.witiot.cloudbox.model;

import java.util.List;

/**
 * Created by lixin on 2017/8/16.
 */

public class FlowSpecifiRes extends BaseRes{

    /**
     * dat : {"pageindex":1,"total":8,"pageSize":10,"pages":1,"rows":[{"id":20,"hospitalId":"10003","standardId":"liul0001","status":1,"standardType":1,"standardName":"流量","price":21,"freeDay":3,"unit":"天","orginalPrice":10,"discount":10,"serviceType":1,"serviceId":"cd655a515a6ddc6631bf83b64b50fc01","serviceName":"1GB","availableCount":1024,"serviceUnit":"MB","useDesc":"4","serviceOrginalPrice":35,"serviceDiscount":0,"servicePrice":35,"serviceStatus":1,"serviceTid":45},{"id":20,"hospitalId":"10003","standardId":"liul0001","status":1,"standardType":1,"standardName":"流量","price":21,"freeDay":3,"unit":"天","orginalPrice":10,"discount":10,"serviceType":1,"serviceId":"fe1f94cc0088192507a2debe3a0d626a","serviceName":"500MB","availableCount":500,"serviceUnit":"MB","useDesc":"3","serviceOrginalPrice":20,"serviceDiscount":0,"servicePrice":20,"serviceStatus":1,"serviceTid":47},{"id":20,"hospitalId":"10003","standardId":"liul0001","status":1,"standardType":1,"standardName":"流量","price":21,"freeDay":3,"unit":"天","orginalPrice":10,"discount":10,"serviceType":1,"serviceId":"f95397eb0d5e5a29818b32d8aad1d84c","serviceName":"300MB","availableCount":300,"serviceUnit":"MB","useDesc":"2","serviceOrginalPrice":13.5,"serviceDiscount":0,"servicePrice":13.5,"serviceStatus":1,"serviceTid":44},{"id":20,"hospitalId":"10003","standardId":"liul0001","status":1,"standardType":1,"standardName":"流量","price":21,"freeDay":3,"unit":"天","orginalPrice":10,"discount":10,"serviceType":1,"serviceId":"5a1218cc8bfcaa6779aedd10b046e474","serviceName":"100MB","availableCount":100,"serviceUnit":"MB","useDesc":"1","serviceOrginalPrice":5,"serviceDiscount":0,"servicePrice":5,"serviceStatus":1,"serviceTid":46},{"id":2,"hospitalId":"10002","standardId":"s0002","status":1,"standardType":1,"standardName":"流量标准","price":0,"freeDay":0,"unit":"兆","orginalPrice":0,"discount":0,"serviceType":1,"serviceId":"f3","serviceName":"300MB","availableCount":300,"serviceUnit":"MB","useDesc":" ","serviceOrginalPrice":15,"serviceDiscount":0.9,"servicePrice":13.5,"serviceStatus":1,"serviceTid":6},{"id":2,"hospitalId":"10002","standardId":"s0002","status":1,"standardType":1,"standardName":"流量标准","price":0,"freeDay":0,"unit":"兆","orginalPrice":0,"discount":0,"serviceType":1,"serviceId":"f10","serviceName":"1GB","availableCount":1000,"serviceUnit":"MB","useDesc":" ","serviceOrginalPrice":50,"serviceDiscount":0.7,"servicePrice":35,"serviceStatus":1,"serviceTid":8},{"id":2,"hospitalId":"10002","standardId":"s0002","status":1,"standardType":1,"standardName":"流量标准","price":0,"freeDay":0,"unit":"兆","orginalPrice":0,"discount":0,"serviceType":1,"serviceId":"f1","serviceName":"100MB","availableCount":100,"serviceUnit":"MB","useDesc":" ","serviceOrginalPrice":5,"serviceDiscount":0,"servicePrice":5,"serviceStatus":1,"serviceTid":5},{"id":2,"hospitalId":"10002","standardId":"s0002","status":1,"standardType":1,"standardName":"流量标准","price":0,"freeDay":0,"unit":"兆","orginalPrice":0,"discount":0,"serviceType":1,"serviceId":"f4","serviceName":"500MB","availableCount":500,"serviceUnit":"MB","useDesc":" ","serviceOrginalPrice":25,"serviceDiscount":0.8,"servicePrice":20,"serviceStatus":1,"serviceTid":7}]}
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
         * total : 8
         * pageSize : 10
         * pages : 1
         * rows : [{"id":20,"hospitalId":"10003","standardId":"liul0001","status":1,"standardType":1,"standardName":"流量","price":21,"freeDay":3,"unit":"天","orginalPrice":10,"discount":10,"serviceType":1,"serviceId":"cd655a515a6ddc6631bf83b64b50fc01","serviceName":"1GB","availableCount":1024,"serviceUnit":"MB","useDesc":"4","serviceOrginalPrice":35,"serviceDiscount":0,"servicePrice":35,"serviceStatus":1,"serviceTid":45},{"id":20,"hospitalId":"10003","standardId":"liul0001","status":1,"standardType":1,"standardName":"流量","price":21,"freeDay":3,"unit":"天","orginalPrice":10,"discount":10,"serviceType":1,"serviceId":"fe1f94cc0088192507a2debe3a0d626a","serviceName":"500MB","availableCount":500,"serviceUnit":"MB","useDesc":"3","serviceOrginalPrice":20,"serviceDiscount":0,"servicePrice":20,"serviceStatus":1,"serviceTid":47},{"id":20,"hospitalId":"10003","standardId":"liul0001","status":1,"standardType":1,"standardName":"流量","price":21,"freeDay":3,"unit":"天","orginalPrice":10,"discount":10,"serviceType":1,"serviceId":"f95397eb0d5e5a29818b32d8aad1d84c","serviceName":"300MB","availableCount":300,"serviceUnit":"MB","useDesc":"2","serviceOrginalPrice":13.5,"serviceDiscount":0,"servicePrice":13.5,"serviceStatus":1,"serviceTid":44},{"id":20,"hospitalId":"10003","standardId":"liul0001","status":1,"standardType":1,"standardName":"流量","price":21,"freeDay":3,"unit":"天","orginalPrice":10,"discount":10,"serviceType":1,"serviceId":"5a1218cc8bfcaa6779aedd10b046e474","serviceName":"100MB","availableCount":100,"serviceUnit":"MB","useDesc":"1","serviceOrginalPrice":5,"serviceDiscount":0,"servicePrice":5,"serviceStatus":1,"serviceTid":46},{"id":2,"hospitalId":"10002","standardId":"s0002","status":1,"standardType":1,"standardName":"流量标准","price":0,"freeDay":0,"unit":"兆","orginalPrice":0,"discount":0,"serviceType":1,"serviceId":"f3","serviceName":"300MB","availableCount":300,"serviceUnit":"MB","useDesc":" ","serviceOrginalPrice":15,"serviceDiscount":0.9,"servicePrice":13.5,"serviceStatus":1,"serviceTid":6},{"id":2,"hospitalId":"10002","standardId":"s0002","status":1,"standardType":1,"standardName":"流量标准","price":0,"freeDay":0,"unit":"兆","orginalPrice":0,"discount":0,"serviceType":1,"serviceId":"f10","serviceName":"1GB","availableCount":1000,"serviceUnit":"MB","useDesc":" ","serviceOrginalPrice":50,"serviceDiscount":0.7,"servicePrice":35,"serviceStatus":1,"serviceTid":8},{"id":2,"hospitalId":"10002","standardId":"s0002","status":1,"standardType":1,"standardName":"流量标准","price":0,"freeDay":0,"unit":"兆","orginalPrice":0,"discount":0,"serviceType":1,"serviceId":"f1","serviceName":"100MB","availableCount":100,"serviceUnit":"MB","useDesc":" ","serviceOrginalPrice":5,"serviceDiscount":0,"servicePrice":5,"serviceStatus":1,"serviceTid":5},{"id":2,"hospitalId":"10002","standardId":"s0002","status":1,"standardType":1,"standardName":"流量标准","price":0,"freeDay":0,"unit":"兆","orginalPrice":0,"discount":0,"serviceType":1,"serviceId":"f4","serviceName":"500MB","availableCount":500,"serviceUnit":"MB","useDesc":" ","serviceOrginalPrice":25,"serviceDiscount":0.8,"servicePrice":20,"serviceStatus":1,"serviceTid":7}]
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
             * id : 20
             * hospitalId : 10003
             * standardId : liul0001
             * status : 1
             * standardType : 1
             * standardName : 流量
             * price : 21.0
             * freeDay : 3.0
             * unit : 天
             * orginalPrice : 10.0
             * discount : 10.0
             * serviceType : 1
             * serviceId : cd655a515a6ddc6631bf83b64b50fc01
             * serviceName : 1GB
             * availableCount : 1024
             * serviceUnit : MB
             * useDesc : 4
             * serviceOrginalPrice : 35.0
             * serviceDiscount : 0.0
             * servicePrice : 35.0
             * serviceStatus : 1
             * serviceTid : 45
             *
             */

            private int id;
            private String hospitalId;
            private String standardId;
            private String standardName;
            private int standardType;
            private int status;

            private double freeDay;
            private String unit;
            private double orginalPrice;
            private double discount;
            private double price;

            private int serviceType;
            private String serviceId;
            private String serviceName;
            private int availableCount;
            private String serviceUnit;

            private String useDesc;
            private double serviceOrginalPrice;
            private double serviceDiscount;
            private double servicePrice;
            private int serviceStatus;
            private int serviceTid;

            private boolean isSelect;

            public boolean isSelect() {
                return isSelect;
            }
            public void setSelect(boolean select) {
                isSelect = select;
            }

            public int getId() {
                return id;
            }
            public void setId(int id) {
                this.id = id;
            }

            public String getHospitalId() {
                return hospitalId;
            }
            public void setHospitalId(String hospitalId) {
                this.hospitalId = hospitalId;
            }

            public String getStandardId() {
                return standardId;
            }
            public void setStandardId(String standardId) {
                this.standardId = standardId;
            }

            public int getStatus() {
                return status;
            }
            public void setStatus(int status) {
                this.status = status;
            }

            public int getStandardType() {
                return standardType;
            }
            public void setStandardType(int standardType) {
                this.standardType = standardType;
            }

            public String getStandardName() {
                return standardName;
            }
            public void setStandardName(String standardName) {
                this.standardName = standardName;
            }

            public double getPrice() {
                return price;
            }
            public void setPrice(double price) {
                this.price = price;
            }

            public double getFreeDay() {
                return freeDay;
            }
            public void setFreeDay(double freeDay) {
                this.freeDay = freeDay;
            }

            public String getUnit() {
                return unit;
            }
            public void setUnit(String unit) {
                this.unit = unit;
            }

            public double getOrginalPrice() {
                return orginalPrice;
            }
            public void setOrginalPrice(double orginalPrice) {
                this.orginalPrice = orginalPrice;
            }

            public double getDiscount() {
                return discount;
            }
            public void setDiscount(double discount) {
                this.discount = discount;
            }

            public int getServiceType() {
                return serviceType;
            }
            public void setServiceType(int serviceType) {
                this.serviceType = serviceType;
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

            public int getAvailableCount() {
                return availableCount;
            }
            public void setAvailableCount(int availableCount) {
                this.availableCount = availableCount;
            }

            public String getServiceUnit() {
                return serviceUnit;
            }
            public void setServiceUnit(String serviceUnit) {
                this.serviceUnit = serviceUnit;
            }

            public String getUseDesc() {
                return useDesc;
            }
            public void setUseDesc(String useDesc) {
                this.useDesc = useDesc;
            }

            public double getServiceOrginalPrice() {
                return serviceOrginalPrice;
            }
            public void setServiceOrginalPrice(double serviceOrginalPrice) {
                this.serviceOrginalPrice = serviceOrginalPrice;
            }

            public double getServiceDiscount() {
                return serviceDiscount;
            }
            public void setServiceDiscount(double serviceDiscount) {
                this.serviceDiscount = serviceDiscount;
            }

            public double getServicePrice() {
                return servicePrice;
            }
            public void setServicePrice(double servicePrice) {
                this.servicePrice = servicePrice;
            }

            public int getServiceStatus() {
                return serviceStatus;
            }
            public void setServiceStatus(int serviceStatus) {
                this.serviceStatus = serviceStatus;
            }

            public int getServiceTid() {
                return serviceTid;
            }
            public void setServiceTid(int serviceTid) {
                this.serviceTid = serviceTid;
            }
        }
    }
}
