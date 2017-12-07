package com.doing.flat.coffee.entity;

/**
 * Created by hqg on 2017/1/3.
 */
public class DBShopData {
    private long id;
    private String num;//货道数，1-50
    private String count;//销售机货道库存

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
