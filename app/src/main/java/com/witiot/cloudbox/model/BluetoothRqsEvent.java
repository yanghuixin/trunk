package com.witiot.cloudbox.model;

/**
 * Created by Administrator on 2017/9/18.
 */

public class BluetoothRqsEvent {
    private String eventName;
    private int eventType;// 0 关闭 1打开
    private int  viewId;
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}
