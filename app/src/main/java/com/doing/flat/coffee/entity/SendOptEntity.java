package com.doing.flat.coffee.entity;

/**
 * Created by Administrator on 2015/12/28.
 */
public class SendOptEntity {
    private String msgType;
    private String content;
    private String device_id;
    private String operation;
    private String app_id;
    private String app_ver;
    private DataOptEntity data;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_ver() {
        return app_ver;
    }

    public void setApp_ver(String app_ver) {
        this.app_ver = app_ver;
    }

    public DataOptEntity getData() {
        return data;
    }

    public void setData(DataOptEntity data) {
        this.data = data;
    }
}
