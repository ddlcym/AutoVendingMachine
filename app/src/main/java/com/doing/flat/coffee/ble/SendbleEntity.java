package com.doing.flat.coffee.ble;

/**
 * Created by hqg on 2016/3/10.
 */
public class SendbleEntity {

    private String data_key;
    private String time;
    private String device_id;//设备ID
    private String f_id_1;//滤芯ID
    private String f_id_2;//滤芯ID
    private String f_id_3;//滤芯ID
    private String f_id_4;//滤芯ID

    private String mesh_id;//滤网ID
    private String filter_element1;//滤芯1
    private String filter_element;//滤芯
    private String filter_element2;//滤芯2
    private String filter_element3;//滤芯3

    private String filter_net;//滤网

    private String command;//命令对应

    private String refrigeration;//制冷
    private String heat;//加热
    private String water_switch;//冷水开关
    private String coffee_switch;//咖啡
    private String tea_switch;//茶
    private String fruit_switch;//果汁
    private String water_for;//水循环

    private String ph;
    private String pm;//pm
    private String tds;
    private String tsd;
    private String temperature;//环境温度
    private String humidity;//湿度
    private String voc;//voc
    private String sum_water;
    private String sum_time;


    private String dixie_cup;//纸杯
    private String dixie_cup_1;//纸杯
    private String dixie_cup_2;//纸杯
    private String dixie_cup_3;//纸杯
    private String dixie_cup_4;//纸杯
    private String dixie_cup_5;//纸杯
    private String dixie_cup_6;//纸杯
    private String dixie_cup_7;//纸杯
    private String dixie_cup_8;//纸杯
    private String dixie_cup_9;//纸杯

    private String water_state;//水机状态
    private String coffee_state;//coffee状态
    private String coffee_bug_state;//coffee故障代码

    private String cntr_state;//售货机状态233
    private String cntr_bug_state;//货仓故障234
    private String cntr_transaction_state;//交易状态 235
//    private String cntr_num;//货道数，1-50
    private String open_door;//开门 0表示关门 1表示开门
    private String sensor;//传感器0正常 1故障


    private String temperature_t2;//
    private String temperature_t2b;//
    private String temperature_t3;//
    private String temperature_exhaust;//排气温度

    private String temperature_hot_water;//热水温度
    private String temperature_cold_water;//冻水温度
    private String windspeed;//风量

    private String water_level_1;//水位
    private String water_level_2;//水位
    private String water_level_3;//水位
    private String water_level_4;//水位

    private String flow_1;//流量
    private String flow_2;//流量


    private String boot_time;//开机时间

    private String temp_err;//温度传感器故障标志位
    private String other_err;//其他传感器故障标志位

    private String mac;//mac地址
    private String mac_1;//mac地址
    private String mac_2;//mac地址

    private String nfc;//

    private String powder_coffee;//咖啡粉量
    private String powder_tea;//奶茶
    private String powder_fruit;//果汁

    private String water_coffee;//咖啡水量
    private String water_tea;//奶茶水量
    private String water_fruit;//果汁水量
    private String water_hot_winter;//冻水水量
    private String water_hot_winter_hot;//热水水量

    private String sales;//N号货仓出货：


    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }


    public String getData_key() {
        return data_key;
    }

    public void setData_key(String data_key) {
        this.data_key = data_key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getF_id_1() {
        return f_id_1;
    }

    public void setF_id_1(String f_id_1) {
        this.f_id_1 = f_id_1;
    }

    public String getF_id_2() {
        return f_id_2;
    }

    public void setF_id_2(String f_id_2) {
        this.f_id_2 = f_id_2;
    }

    public String getF_id_3() {
        return f_id_3;
    }

    public void setF_id_3(String f_id_3) {
        this.f_id_3 = f_id_3;
    }

    public String getF_id_4() {
        return f_id_4;
    }

    public void setF_id_4(String f_id_4) {
        this.f_id_4 = f_id_4;
    }

    public String getMesh_id() {
        return mesh_id;
    }

    public void setMesh_id(String mesh_id) {
        this.mesh_id = mesh_id;
    }

    public String getTsd() {
        return tsd;
    }

    public void setTsd(String tsd) {
        this.tsd = tsd;
    }

    public String getVoc() {
        return voc;
    }

    public void setVoc(String voc) {
        this.voc = voc;
    }

    public String getFilter_element1() {
        return filter_element1;
    }

    public void setFilter_element1(String filter_element1) {
        this.filter_element1 = filter_element1;
    }

    public String getDixie_cup() {
        return dixie_cup;
    }

    public void setDixie_cup(String dixie_cup) {
        this.dixie_cup = dixie_cup;
    }

    public String getDixie_cup_1() {
        return dixie_cup_1;
    }

    public void setDixie_cup_1(String dixie_cup_1) {
        this.dixie_cup_1 = dixie_cup_1;
    }

    public String getDixie_cup_2() {
        return dixie_cup_2;
    }

    public void setDixie_cup_2(String dixie_cup_2) {
        this.dixie_cup_2 = dixie_cup_2;
    }

    public String getDixie_cup_3() {
        return dixie_cup_3;
    }

    public void setDixie_cup_3(String dixie_cup_3) {
        this.dixie_cup_3 = dixie_cup_3;
    }

    public String getDixie_cup_4() {
        return dixie_cup_4;
    }

    public void setDixie_cup_4(String dixie_cup_4) {
        this.dixie_cup_4 = dixie_cup_4;
    }

    public String getDixie_cup_5() {
        return dixie_cup_5;
    }

    public void setDixie_cup_5(String dixie_cup_5) {
        this.dixie_cup_5 = dixie_cup_5;
    }

    public String getDixie_cup_6() {
        return dixie_cup_6;
    }

    public void setDixie_cup_6(String dixie_cup_6) {
        this.dixie_cup_6 = dixie_cup_6;
    }

    public String getDixie_cup_7() {
        return dixie_cup_7;
    }

    public void setDixie_cup_7(String dixie_cup_7) {
        this.dixie_cup_7 = dixie_cup_7;
    }

    public String getDixie_cup_8() {
        return dixie_cup_8;
    }

    public void setDixie_cup_8(String dixie_cup_8) {
        this.dixie_cup_8 = dixie_cup_8;
    }

    public String getDixie_cup_9() {
        return dixie_cup_9;
    }

    public void setDixie_cup_9(String dixie_cup_9) {
        this.dixie_cup_9 = dixie_cup_9;
    }

    public String getWater_state() {
        return water_state;
    }

    public void setWater_state(String water_state) {
        this.water_state = water_state;
    }

    public String getCoffee_state() {
        return coffee_state;
    }

    public void setCoffee_state(String coffee_state) {
        this.coffee_state = coffee_state;
    }

    public String getCoffee_bug_state() {
        return coffee_bug_state;
    }

    public void setCoffee_bug_state(String coffee_bug_state) {
        this.coffee_bug_state = coffee_bug_state;
    }

    public String getTemperature_t2() {
        return temperature_t2;
    }

    public void setTemperature_t2(String temperature_t2) {
        this.temperature_t2 = temperature_t2;
    }

    public String getTemperature_t2b() {
        return temperature_t2b;
    }

    public void setTemperature_t2b(String temperature_t2b) {
        this.temperature_t2b = temperature_t2b;
    }

    public String getTemperature_t3() {
        return temperature_t3;
    }

    public void setTemperature_t3(String temperature_t3) {
        this.temperature_t3 = temperature_t3;
    }

    public String getTemperature_exhaust() {
        return temperature_exhaust;
    }

    public void setTemperature_exhaust(String temperature_exhaust) {
        this.temperature_exhaust = temperature_exhaust;
    }

    public String getTemperature_hot_water() {
        return temperature_hot_water;
    }

    public void setTemperature_hot_water(String temperature_hot_water) {
        this.temperature_hot_water = temperature_hot_water;
    }

    public String getTemperature_cold_water() {
        return temperature_cold_water;
    }

    public void setTemperature_cold_water(String temperature_cold_water) {
        this.temperature_cold_water = temperature_cold_water;
    }

    public String getWater_level_1() {
        return water_level_1;
    }

    public void setWater_level_1(String water_level_1) {
        this.water_level_1 = water_level_1;
    }

    public String getWater_level_2() {
        return water_level_2;
    }

    public void setWater_level_2(String water_level_2) {
        this.water_level_2 = water_level_2;
    }

    public String getWater_level_3() {
        return water_level_3;
    }

    public void setWater_level_3(String water_level_3) {
        this.water_level_3 = water_level_3;
    }

    public String getFlow_1() {
        return flow_1;
    }

    public void setFlow_1(String flow_1) {
        this.flow_1 = flow_1;
    }

    public String getFlow_2() {
        return flow_2;
    }

    public void setFlow_2(String flow_2) {
        this.flow_2 = flow_2;
    }


    public String getBoot_time() {
        return boot_time;
    }

    public void setBoot_time(String boot_time) {
        this.boot_time = boot_time;
    }

    public String getNfc() {
        return nfc;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }

    public String getFilter_element() {
        return filter_element;
    }

    public void setFilter_element(String filter_element) {
        this.filter_element = filter_element;
    }

    public String getFilter_net() {
        return filter_net;
    }

    public void setFilter_net(String filter_net) {
        this.filter_net = filter_net;
    }

    public String getRefrigeration() {
        return refrigeration;
    }

    public void setRefrigeration(String refrigeration) {
        this.refrigeration = refrigeration;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }


    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSum_water() {
        return sum_water;
    }

    public void setSum_water(String sum_water) {
        this.sum_water = sum_water;
    }

    public String getSum_time() {
        return sum_time;
    }

    public void setSum_time(String sum_time) {
        this.sum_time = sum_time;
    }

    public String getWater_switch() {
        return water_switch;
    }

    public void setWater_switch(String water_switch) {
        this.water_switch = water_switch;
    }

    public String getCoffee_switch() {
        return coffee_switch;
    }

    public void setCoffee_switch(String coffee_switch) {
        this.coffee_switch = coffee_switch;
    }

    public String getTea_switch() {
        return tea_switch;
    }

    public void setTea_switch(String tea_switch) {
        this.tea_switch = tea_switch;
    }

    public String getFruit_switch() {
        return fruit_switch;
    }

    public void setFruit_switch(String fruit_switch) {
        this.fruit_switch = fruit_switch;
    }

    public String getFilter_element2() {
        return filter_element2;
    }

    public void setFilter_element2(String filter_element2) {
        this.filter_element2 = filter_element2;
    }

    public String getFilter_element3() {
        return filter_element3;
    }

    public void setFilter_element3(String filter_element3) {
        this.filter_element3 = filter_element3;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public String getTemp_err() {
        return temp_err;
    }

    public void setTemp_err(String temp_err) {
        this.temp_err = temp_err;
    }

    public String getOther_err() {
        return other_err;
    }

    public void setOther_err(String other_err) {
        this.other_err = other_err;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac_1() {
        return mac_1;
    }

    public void setMac_1(String mac_1) {
        this.mac_1 = mac_1;
    }

    public String getMac_2() {
        return mac_2;
    }

    public void setMac_2(String mac_2) {
        this.mac_2 = mac_2;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getWater_level_4() {
        return water_level_4;
    }

    public void setWater_level_4(String water_level_4) {
        this.water_level_4 = water_level_4;
    }

    public String getPowder_coffee() {
        return powder_coffee;
    }

    public void setPowder_coffee(String powder_coffee) {
        this.powder_coffee = powder_coffee;
    }

    public String getPowder_tea() {
        return powder_tea;
    }

    public void setPowder_tea(String powder_tea) {
        this.powder_tea = powder_tea;
    }

    public String getPowder_fruit() {
        return powder_fruit;
    }

    public void setPowder_fruit(String powder_fruit) {
        this.powder_fruit = powder_fruit;
    }

    public String getWater_coffee() {
        return water_coffee;
    }

    public void setWater_coffee(String water_coffee) {
        this.water_coffee = water_coffee;
    }

    public String getWater_tea() {
        return water_tea;
    }

    public void setWater_tea(String water_tea) {
        this.water_tea = water_tea;
    }

    public String getWater_fruit() {
        return water_fruit;
    }

    public void setWater_fruit(String water_fruit) {
        this.water_fruit = water_fruit;
    }

    public String getWater_hot_winter() {
        return water_hot_winter;
    }

    public void setWater_hot_winter(String water_hot_winter) {
        this.water_hot_winter = water_hot_winter;
    }

    public String getWater_for() {
        return water_for;
    }

    public void setWater_for(String water_for) {
        this.water_for = water_for;
    }

    public String getWater_hot_winter_hot() {
        return water_hot_winter_hot;
    }

    public void setWater_hot_winter_hot(String water_hot_winter_hot) {
        this.water_hot_winter_hot = water_hot_winter_hot;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }


    public String getCntr_state() {
        return cntr_state;
    }

    public void setCntr_state(String cntr_state) {
        this.cntr_state = cntr_state;
    }

    public String getCntr_bug_state() {
        return cntr_bug_state;
    }

    public void setCntr_bug_state(String cntr_bug_state) {
        this.cntr_bug_state = cntr_bug_state;
    }

    public String getCntr_transaction_state() {
        return cntr_transaction_state;
    }

    public void setCntr_transaction_state(String cntr_transaction_state) {
        this.cntr_transaction_state = cntr_transaction_state;
    }

    public String getOpen_door() {
        return open_door;
    }

    public void setOpen_door(String open_door) {
        this.open_door = open_door;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }
//    public String getCntr_num() {
//        return cntr_num;
//    }
//
//    public void setCntr_num(String cntr_num) {
//        this.cntr_num = cntr_num;
//    }
//
//    public String getCntr_count() {
//        return cntr_count;
//    }
//
//    public void setCntr_count(String cntr_count) {
//        this.cntr_count = cntr_count;
//    }
}
