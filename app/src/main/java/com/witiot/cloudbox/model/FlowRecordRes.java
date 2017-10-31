package com.witiot.cloudbox.model;

import com.google.gson.annotations.SerializedName;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by lixin on 2017/8/14.
 */

public class FlowRecordRes extends BaseRes {


    private FlowRecordBean dat;

    public FlowRecordBean getDat() {
        return dat;
    }

    public void setDat(FlowRecordBean dat) {
        this.dat = dat;
    }

    public static class FlowRecordBean {

        private List<Rows> rows;

        public List<Rows> getRows() {
            return rows;
        }

        public void setRows(List<Rows> rows) {
            this.rows = rows;
        }

        public static class Rows implements Serializable {
            /**
             * id : 8
             * customerId : 13716543250
             * deviceId : 2635ea1b9e1c5b95cfd2001d076fa98b
             * serviceDay : 2017-08-11 00:00:00
             * actualFlow : 80.0
             * msg :
             */

            private int id;
            private String customerId;
            private String deviceId;
            private String serviceDay;
            private double actualFlow;
            @SerializedName("msg")
            private String msgX;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getServiceDay() {
                if( StringUtils.stringIsNotEmpty( serviceDay)){
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    return  df.format(TimeUtils.string2Date(serviceDay));
                }
                else {
                    return serviceDay;
                }

            }

            public void setServiceDay(String serviceDay) {
                this.serviceDay = serviceDay;
            }

            public double getActualFlow() {
                return actualFlow;
            }

            public void setActualFlow(double actualFlow) {
                this.actualFlow = actualFlow;
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
