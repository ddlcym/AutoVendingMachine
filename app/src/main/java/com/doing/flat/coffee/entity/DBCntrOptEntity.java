package com.doing.flat.coffee.entity;

/**
 * Created by hqg on 2016/8/12.
 */
public class DBCntrOptEntity {
    private Long id;
    private String opt_id;
    private String number;
    private String type;//0表示我接收到落饮命令，1表示服务器返回出水回调    4表示发送给服务器完成命令 5表示服务器回调完成命令

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpt_id() {
        return opt_id;
    }

    public void setOpt_id(String opt_id) {
        this.opt_id = opt_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
