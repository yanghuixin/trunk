package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/1.
 */

public class SendMsgRqs extends BaseRqs{


    /**
     * cmd : get
     * dat : {"customerId":"15010387145"}
     * tok :
     * ver : 1
     * src : 2
     */


    private DatBean dat;
    public DatBean getDat() {
        return dat;
    }

    public void setDat(SendMsgRqs.DatBean dat) {
        this.dat = dat;
    }
    public static class DatBean {
        /**
         * customerId : 15010387145
         */

        private String customerId;

        public String getCustomerId() {
            return customerId;
        }
        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        private int operaType;

        public int getOperaType() {
            return operaType;
        }
        public void setOperaType(int operaType) {
            this.operaType = operaType;
        }
    }
}
