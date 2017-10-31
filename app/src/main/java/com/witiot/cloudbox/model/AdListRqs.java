package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class AdListRqs extends BaseRqs{

    private AdListRqs.DatBean dat;

    public AdListRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(AdListRqs.DatBean dat) {
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
            public String title;
            public String position ;
            public String advertType;
            public String status;
            public String hospitalId;   // 医院编号
            public String dateNow;      // 当前日期：yyyy-MM-dd

            public String getTitle() {
                return title;
            }
            public void setTitle(String title) {
                this.title = title;
            }

            public String getPosition() {
                return position;
            }
            public void setPosition(String position) {
                this.position = position;
            }

            public String getAdvertType() {
                return advertType;
            }
            public void setAdvertType(String advertType) {
                this.advertType = advertType;
            }

            public String getStatus() {
                return status;
            }
            public void setStatus(String status) {
                this.status = status;
            }

            public String getHospitalId() {
                return hospitalId;
            }
            public void setHospitalId(String hospitalId) {
                this.hospitalId = hospitalId;
            }

            public String getDateNow() {
                return dateNow;
            }
            public void setDateNow(String dateNow) {
                this.dateNow = dateNow;
            }
        }
    }

}
