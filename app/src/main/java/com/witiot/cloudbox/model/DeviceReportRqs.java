package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/2.
 */

public class DeviceReportRqs extends BaseRqs{

    /**
     * dat : { deviceId, reportContent,  reportMan ,reportMobile, repairTime, status  }
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
         * reportContent :
         * reportMan :
         * reportMobile :
         * reportTime :
         * status
         * 其他字段预留
         */

        private String deviceId;
        private String reportContent;
        private String reportMan;
        private String reportMobile;
        private String reportTime;
        private String status;

        public String getDeviceId() {
            return deviceId;
        }
        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getReportContent() {
            return reportContent;
        }
        public void setReportContent(String reportContent) {
            this.reportContent = reportContent;
        }

        public String getReportMan() {
            return reportMan;
        }
        public void setReportMan(String reportMan) {
            this.reportMan = reportMan;
        }

        public String getReportMobile() {
            return reportMobile;
        }
        public void setReportMobile(String reportMobile) { this.reportMobile = reportMobile;   }

        public String getReportTime() {
            return reportTime;
        }
        public void setReportTime(String reportTime) {
            this.reportTime = reportTime;
        }

        public String getStatus() {
            return status;
        }
        public void setStatus(String status) { this.status = status;   }
    }
}
