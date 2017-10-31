package com.witiot.cloudbox.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin on 2017/8/26.
 */

public class DeviceRecordRes extends BaseRes {


    /**
     * dat : {"pageindex":1,"total":6,"pageSize":10,"pages":1,"rows":[]}
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
         * total : 6
         * pageSize : 10
         * pages : 1
         * rows : [{"id":21,"author":"Liu","title":"测试文章标题","articleDesc":"测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题测试文章标题","content":"%E6%B5%","kind":1,"source":"","forum":"就医指南","channel":"育儿频道","articleType":1,"videoUrl":"","beginTime":"","endTime":"","visitCount":0,"favoriteCount":0,"status":1,"createTime":"2017-08-26 16:51:13","createId":"admin","msg":"","msg1":"","msg2":"","msg3":""}]
         */

        private int pageindex;
        private int total;
        private int pageSize;
        private int pages;
        private List<RecordBean> rows;

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

        public List<RecordBean> getRows() {
            return rows;
        }
        public void setRows(List<RecordBean> rows) {
            this.rows = rows;
        }

        public static class RecordBean implements Serializable{
             /**
             * id : 21
             */

            private int id;
            private String deviceId;
            private String reportContent;
            private String reportTime;
            private String reportMan;
            private String reportMobile;
            private String repairContent;
            private String repairTime;
            private String repairMan;
            private String checkResult;
            private String checkTime;
            private String checkMan;

            private int status;
            @SerializedName("msg")
            private String msgX;

            public int getId() {
                return id;
            }
            public void setId(int id) {
                this.id = id;
            }

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

            public String getReportMobile() {
                return reportMobile;
            }
            public void setReportMobile(String reportMobile) {
                this.reportMobile = reportMobile;
            }

            public String getReportTime() {
                return reportTime;
            }
            public void setReportTime(String reportTime) {
                this.reportTime = reportTime;
            }

            public String getReportMan() {
                return reportMan;
            }
            public void setReportMan(String reportMan) {
                this.reportMan = reportMan;
            }


            public String getRepairContent() {
                return repairContent;
            }
            public void setRepairContent(String repairContent) {
                this.repairContent = repairContent;
            }

            public String getRepairTime() {
                return repairTime;
            }
            public void setRepairTime(String repairTime) {
                this.repairTime = repairTime;
            }

            public String getRepairMan() {
                return repairMan;
            }
            public void setRepairMan(String repairMan) {
                this.repairMan = repairMan;
            }

            public String getCheckResult() {
                return checkResult;
            }
            public void setCheckResult(String checkResult) {
                this.checkResult = checkResult;
            }

            public String getCheckTime() {
                return checkTime;
            }
            public void setCheckTime(String checkTime) {
                this.checkTime = checkTime;
            }

            public String getCheckMan() {
                return checkMan;
            }
            public void setCheckMan(String checkMan) {
                this.checkMan = checkMan;
            }

            public int getStatus() {
                return status;
            }
            public void setStatus(int status) {
                this.status = status;
            }

            public String getMsgX() {
                return msgX;
            }
            public void setMsgX(String msgX) {
                this.msgX = msgX;
            }
        }
    }
}
