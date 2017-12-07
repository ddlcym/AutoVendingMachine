package com.doing.flat.coffee.entity;

import java.util.List;

/**
 * Created by doing_hqg on 2016/7/21.
 */
public class DataOptEntity {
    private String opt_id;
    private String opt_type;
    private String machine_code;
    private String machine_code_2;
    private String device_id;
    private String num;
    private String cntr_type;//销售机设备类型，默认1

    private String code;
    private String refresh_id;//请求刷新唯一标识码
    private String status;

    //状态，2已接收，3下载完成，4策略生效  status
    private String rule_id;//策略ID
    private List<String> resource_array;

    private String order_id;

    private String network;//  2是wifi  1是移动网络

    private List<String> order_array;
    private List<String> complete_array;
    private List<String> produce_array;
    private List<String> drop_array;
    private List<String> failed_array;
    private List<String> err_motor_array;

    private String coffee_status;
    private String coffee_error;

    private List<Container_array> container_array;
    private String drop_status;


//    private String count;//剩余库存
//    private String number;//货道编号


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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getOrder_array() {
        return order_array;
    }

    public void setOrder_array(List<String> order_array) {
        this.order_array = order_array;
    }

    public List<String> getComplete_array() {
        return complete_array;
    }

    public void setComplete_array(List<String> complete_array) {
        this.complete_array = complete_array;
    }

    public List<String> getProduce_array() {
        return produce_array;
    }

    public void setProduce_array(List<String> produce_array) {
        this.produce_array = produce_array;
    }

    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public List<String> getResource_array() {
        return resource_array;
    }

    public void setResource_array(List<String> resource_array) {
        this.resource_array = resource_array;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCoffee_status() {
        return coffee_status;
    }

    public void setCoffee_status(String coffee_status) {
        this.coffee_status = coffee_status;
    }

    public String getCoffee_error() {
        return coffee_error;
    }

    public void setCoffee_error(String coffee_error) {
        this.coffee_error = coffee_error;
    }

    public String getCntr_type() {
        return cntr_type;
    }

    public void setCntr_type(String cntr_type) {
        this.cntr_type = cntr_type;
    }

//    public String getCount() {
//        return count;
//    }
//
//    public void setCount(String count) {
//        this.count = count;
//    }
//
//    public String getNumber() {
//        return number;
//    }
//
//    public void setNumber(String number) {
//        this.number = number;
//    }

    public List<String> getDrop_array() {
        return drop_array;
    }

    public void setDrop_array(List<String> drop_array) {
        this.drop_array = drop_array;
    }

    public String getRefresh_id() {
        return refresh_id;
    }

    public void setRefresh_id(String refresh_id) {
        this.refresh_id = refresh_id;
    }

    public String getMachine_code_2() {
        return machine_code_2;
    }

    public void setMachine_code_2(String machine_code_2) {
        this.machine_code_2 = machine_code_2;
    }

    public List<String> getFailed_array() {
        return failed_array;
    }

    public void setFailed_array(List<String> failed_array) {
        this.failed_array = failed_array;
    }

    public List<Container_array> getContainer_array() {
        return container_array;
    }

    public void setContainer_array(List<Container_array> container_array) {
        this.container_array = container_array;
    }

    public String getDrop_status() {
        return drop_status;
    }

    public void setDrop_status(String drop_status) {
        this.drop_status = drop_status;
    }

    public List<String> getErr_motor_array() {
        return err_motor_array;
    }

    public void setErr_motor_array(List<String> err_motor_array) {
        this.err_motor_array = err_motor_array;
    }
}
