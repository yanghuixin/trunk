package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/24.
 */

public class PostVisitActionRqs extends BaseRqs {
    private DatBean dat;

    public DatBean getDat() {
        return dat;
    }

    public void setDat(DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        private String id;
        private String action;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }
}
