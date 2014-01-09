package org.liuhuo.spring.model;

import java.util.Date;
import java.sql.Timestamp;

public class RemoteData {
    private int id;
    private String ip;
    private String serviceName;
    private String serviceCategory;
    private String eventType;
    private String eventTime;
    private int eventCount;
    private String recordTime;
    private boolean testFlag;
    private String errorMsg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(Timestamp time) {
        this.eventTime = (time == null)?null:time.toString();
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int count) {
        this.eventCount = count;
    }
    // private Date recordTime;
    // private boolean testFlag;
    // private String errorMsg;

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Timestamp time) {
        this.recordTime = (time == null)?null:time.toString();
    }

    public boolean getTestFlag() {
        return testFlag;
    }

    public void setTestFlag(boolean flag) {
        this.testFlag = flag;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
