package com.witiot.cloudbox.model;

import java.util.List;

/**
 * Created by Administrator on 2017/9/28.
 */

public class UpdateVersionRes extends BaseRes{

    /**
     * dat : {"rows":[{"id":1,"versionName":"v1.1.2","versionNumber":112,"updateType":1,"content":"版本强制更新","updateUrl":"http://app.yuntougui/imgs/appUpdate/v112.apk","createTime":"2017-09-26 10:29:53","createId":"jackie","status":1}]}
     */

    private DatBean dat;

    public DatBean getDat() {
        return dat;
    }

    public void setDat(DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {
        private List<UpdateBean> rows;

        public List<UpdateBean> getRows() {
            return rows;
        }

        public void setRows(List<UpdateBean> rows) {
            this.rows = rows;
        }

        public static class UpdateBean {
            /**
             * id : 1
             * versionName : v1.1.2
             * versionNumber : 112
             * updateType : 1
             * content : 版本强制更新
             * updateUrl : http://app.yuntougui/imgs/appUpdate/v112.apk
             * createTime : 2017-09-26 10:29:53
             * createId : jackie
             * status : 1
             */

            private int id;
            private String versionName;
            private int versionNumber;
            private int updateType;
            private String content;
            private String updateUrl;
            private String createTime;
            private String createId;
            private int status;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public int getVersionNumber() {
                return versionNumber;
            }

            public void setVersionNumber(int versionNumber) {
                this.versionNumber = versionNumber;
            }

            public int getUpdateType() {
                return updateType;
            }

            public void setUpdateType(int updateType) {
                this.updateType = updateType;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUpdateUrl() {
                return updateUrl;
            }

            public void setUpdateUrl(String updateUrl) {
                this.updateUrl = updateUrl;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCreateId() {
                return createId;
            }

            public void setCreateId(String createId) {
                this.createId = createId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
