package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class HospitalRequest extends BaseRqs{

    private HospitalRequest.DatBean dat;

    public HospitalRequest.DatBean getDat() {
        return dat;
    }

    public void setDat(HospitalRequest.DatBean dat) {
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

            public String province;
            public String city;
            public String district;
            public String status;

            public String getProvince() {
                return province;
            }
            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }
            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }
            public void setDistrict(String district) {
                this.district = district;
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
