package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/2.
 */

public class DeviceAllotRequest extends BaseRqs{

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
        /**
         * deviceId :
         * hospitalId :
         * province :
         * city :
         * district :
         * deviceStatus
         * 其他字段预留
         */

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

            private String deviceId;
            private String hospitalId;
            private String province;
            private String city;
            private String district;
            private int deviceStatus;

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getHospitalId() {
                return hospitalId;
            }

            public void setHospitalId(String hospitalId) {
                this.hospitalId = hospitalId;
            }

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

            public int getDeviceStatus() {
                return deviceStatus;
            }

            public void setDeviceStatus(int deviceStatus) {
                this.deviceStatus = deviceStatus;
            }
        }
    }
}
