package com.witiot.cloudbox.model;

/**
 * Created by Administrator on 2017/9/18.
 */

public class BluetoothResEvent {
    private String eventName;
    private int eventType;// 0 关闭 1打开
    private boolean eventRes;// true 成功  false 失败
    private byte[] buffer;

    public boolean isEventRes() {
        return eventRes;
    }

    public void setEventRes(boolean eventRes) {
        this.eventRes = eventRes;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}
