package com.doing.flat.coffee.entity;

import java.util.List;

/**
 * Created by doing_hqg on 2016/8/29.
 */
public class DBDrinksData {
    private long id;
    private String order_id;
    private List<Drinks_array> drinks_array;
    private String drinks_array_string;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<Drinks_array> getDrinks_array() {
        return drinks_array;
    }

    public void setDrinks_array(List<Drinks_array> drinks_array) {
        this.drinks_array = drinks_array;
    }

    public String getDrinks_array_string() {
        return drinks_array_string;
    }

    public void setDrinks_array_string(String drinks_array_string) {
        this.drinks_array_string = drinks_array_string;
    }
}
