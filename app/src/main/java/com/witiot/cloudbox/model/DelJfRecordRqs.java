package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class DelJfRecordRqs extends BaseRqs{

    private DelJfRecordRqs.DatBean dat;

    public DelJfRecordRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(DelJfRecordRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
