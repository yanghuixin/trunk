package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/2.
 */

public class AreaRequest extends BaseRqs{

    /**
     * dat : { deviceId, hospitalId, departId, floorNum, roomNum, createId, cpu, storageSize, screen, color, osName, appVersion, status }
     */

    private DatBean dat;
    public DatBean getDat() {
        return dat;
    }
    public void setDat(DatBean dat) {
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

            /**
             * province :
             * city :
             * kind :
             */

            private String province;
            private String city;
            private String kind;

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

            public String getKind() {
                return kind;
            }
            public void setKind(String kind) {
                this.kind = kind;
            }
        }
    }
}
