package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/2.
 */

public class NewVersionRequest extends BaseRqs{

    /**
     * dat : { deviceId, hospitalId, departId, floorNum, roomNum, createId, cpu, storageSize, screen, color, osName, appVersion, status }
     */
// TODO: 2017/10/30 谁的数据源？
    private DatBean dat;
    public DatBean getDat() {
        return dat;
    }
    public void setDat(DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

        private Paramlist paramlist;
        public Paramlist getParamlist() {
            return paramlist;
        }
        public void setParamlist(Paramlist paramlist) {
            this.paramlist = paramlist;
        }

        public static class Paramlist {

            private String versionNumber;

            public String getVersionNumber() {
                return versionNumber;
            }

            public void setVersionNumber(String versionNumber) {
                this.versionNumber = versionNumber;
            }
        }
    }
}
