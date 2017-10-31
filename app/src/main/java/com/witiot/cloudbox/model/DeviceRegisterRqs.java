package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/2.
 */

public class DeviceRegisterRqs extends BaseRqs{

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
         * departId :
         * floorNum :
         * roomNum :
         * roomType:
         * status
         * 其他字段预留
         */

        private String deviceId;
        private String hospitalId;
        private String departId;
        private String floorNum;
        private String roomNum;
        private String roomType;
        private String status;

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

        public String getDepartId() {
            return departId;
        }
        public void setDepartId(String departId) {
            this.departId = departId;
        }

        public String getFloorNum() {
            return floorNum;
        }
        public void setFloorNum(String floorNum) {
            this.floorNum = floorNum;
        }

        public String getRoomNum() {
            return roomNum;
        }
        public void setRoomNum(String roomNum) { this.roomNum = roomNum;   }

        public String getRoomType() {
            return roomType;
        }
        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public String getStatus() {
            return status;
        }
        public void setStatus(String status) { this.status = status;   }
    }
}
