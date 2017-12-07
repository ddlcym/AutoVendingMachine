package com.doing.flat.coffee.entity;

/**
 * Created by doing_hqg on 2016/7/23.
 */
public class DBImageData {
    private long id;
    private String qrcode;
    private String path;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
