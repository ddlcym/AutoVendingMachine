package com.doing.flat.coffee.entity;

import java.util.List;

/**
 * 咖啡机广告策略
 */
public class DBRuleData {

    private long id;
    private String rule_id;//策略ID
    private String start_date;//开始日期，时间戳
    private String end_date;//结束日期，时间戳
    private String start_time;//开始时间段，分钟数
    private String end_time;//结束时间段，分钟数
    private String total_time;//策略总播放时长，秒
    private List<String> resource_array;//策略资源数组
    private String resource_array_string;//
    private String per_count;//每次获取资源数量
//    private String is_success;//1表示下载完成（包括上传下载不成功，上传）  0表示未下载完成
    private String rule_type;//0表示老的，1表示新的，但是在下载的
    private String status;//策略状态，2已接收，3下载完成，4策略生效  0表示下载不成功
    private String status_type;//策略状态  0表示本地设置  1表示接受服务端回调

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public List<String> getResource_array() {
        return resource_array;
    }

    public void setResource_array(List<String> resource_array) {
        this.resource_array = resource_array;
    }

    public String getResource_array_string() {
        return resource_array_string;
    }

    public void setResource_array_string(String resource_array_string) {
        this.resource_array_string = resource_array_string;
    }

    public String getPer_count() {
        return per_count;
    }

    public void setPer_count(String per_count) {
        this.per_count = per_count;
    }

//    public String getIs_success() {
//        return is_success;
//    }
//
//    public void setIs_success(String is_success) {
//        this.is_success = is_success;
//    }
//
//    public String getRule_type() {
//        return rule_type;
//    }
//
//    public void setRule_type(String rule_type) {
//        this.rule_type = rule_type;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_type() {
        return status_type;
    }

    public void setStatus_type(String status_type) {
        this.status_type = status_type;
    }

    public String getRule_type() {
        return rule_type;
    }

    public void setRule_type(String rule_type) {
        this.rule_type = rule_type;
    }
}
