package com.doing.flat.coffee.entity;

import java.util.List;

/**
 * Created by hqg on 2016/6/9.
 */
public class DataEntity {
    private String machine_code;
    private String qrcode;
    private String is_update;
    private String download_url;
    private String content;
    private String opt_id;
    private String opt_type;
    private String device_id;
    private String rule_id;
    private String is_rule;
    private String device_type;
    private String cntr_type;
    private List<Price_array> price_array;


    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getIs_update() {
        return is_update;
    }

    public void setIs_update(String is_update) {
        this.is_update = is_update;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOpt_id() {
        return opt_id;
    }

    public void setOpt_id(String opt_id) {
        this.opt_id = opt_id;
    }

    public String getOpt_type() {
        return opt_type;
    }

    public void setOpt_type(String opt_type) {
        this.opt_type = opt_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getIs_rule() {
        return is_rule;
    }

    public void setIs_rule(String is_rule) {
        this.is_rule = is_rule;
    }

    public List<Price_array> getPrice_array() {
        return price_array;
    }

    public void setPrice_array(List<Price_array> price_array) {
        this.price_array = price_array;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getCntr_type() {
        return cntr_type;
    }

    public void setCntr_type(String cntr_type) {
        this.cntr_type = cntr_type;
    }
}
