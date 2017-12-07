package com.doing.flat.coffee.entity;


import com.doing.flat.coffee.xutils.db.annotation.Id;

/**
 * Created by doing_hqg on 2016/7/22.
 */


public class DBDeviceIdData {
    @Id(column = "id")
    private Long id;
    private String device_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
