package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/16.
 */

public class FlowSpecificRqs extends BaseRqs{

    private FlowSpecificRqs.DatBean dat;

    public FlowSpecificRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(FlowSpecificRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        private FlowSpecificRqs.DatBean.Paramlist paramlist;

        public FlowSpecificRqs.DatBean.Paramlist getParamlist() {
            return paramlist;
        }

        public void setParamlist(FlowSpecificRqs.DatBean.Paramlist paramlist) {
            this.paramlist = paramlist;
        }

        public static class Paramlist {
            String hospitalId;
            String   standardType;
            String  standardId;
            String   standardName;
            String serviceType;
            String   status;
            String   orderby;

            public String getServiceType() {
                return serviceType;
            }

            public void setServiceType(String serviceType) {
                this.serviceType = serviceType;
            }

            public String getHospitalId() {
                return hospitalId;
            }

            public void setHospitalId(String hospitalId) {
                this.hospitalId = hospitalId;
            }

            public String getStandardType() {
                return standardType;
            }

            public void setStandardType(String standardType) {
                this.standardType = standardType;
            }

            public String getStandardId() {
                return standardId;
            }

            public void setStandardId(String standardId) {
                this.standardId = standardId;
            }

            public String getStandardName() {
                return standardName;
            }

            public void setStandardName(String standardName) {
                this.standardName = standardName;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
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
