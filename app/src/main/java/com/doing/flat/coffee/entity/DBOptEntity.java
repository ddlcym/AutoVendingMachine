package com.doing.flat.coffee.entity;

/**
 * Created by hqg on 2016/8/12.
 */
public class DBOptEntity {
    private Long id;
    private String opt_id;
    private String opt_type;
    private String type;//0表示我接收到出水命令，1表示服务器返回出水回调    4表示发送给服务器完成命令 5表示服务器回调完成命令

    public String getOpt_type() {
        return opt_type;
    }

    public void setOpt_type(String opt_type) {
        this.opt_type = opt_type;
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
