package com.doing.flat.coffee.entity;

import java.util.List;

/**
 * Created by doing_hqg on 2016/7/22.
 */
public class DBDeiveceData {
    private long id;

    private String opt_id;
    private String opt_type;
    private String machine_code;
//    private String num;

    private String data_key;//
    private String time;//
    private String device_id;//
    private String f_id_1;//滤芯ID 1
    private String f_id_2;//
    private String f_id_3;//
    private String f_id_4;//
    private String mesh_id;//滤网ID
    private String f_r_1;//滤芯1剩余寿命
    private String f_r_2;//
    private String f_r_3;//
    private String f_r_4;//
    private String mesh_residual;//滤网剩余寿命
    private String cup_1;//饮料1杯数
    private String cup_2;//
    private String cup_3;//
    private String cup_4;//
    private String cup_5;//
    private String cup_6;//
    private String cup_7;//
    private String cup_8;//
    private String cup_9;//
    private String cup_10;//
    private String w_status;//00/FF水机状态 00:待机  0x01:开机,不制水;  0X11:开机制水;  0x03:故障停机;
    private String is_on;//
    private String is_manuf;//
    private String c_status;////FF咖啡机工作状态  0x00:准备就绪;  0x01:咖啡机正忙  0x02:故障;
    private String c_error;////FF咖啡机故障代码  1 表示错误,0 表示正常; BIT7:首次加热中; BIT6:保留; BIT5:保留; BIT4:保留; BIT3:E04 温度传感器故障;BIT2:E03 同时无水无杯; BIT1:E02 无杯; BIT0:E01 无水;
    private String pm25;//pm2.5
    private String tds_1;//原水
    private String tds_2;//净水
    private String temp;//温度
    private String humidity;//湿度
    private String voc;//
    private String t2;//
    private String t2b;//
    private String t3;//
    private String exhaust_temp;//排气温度
    private String h_w_temp;//热水温度
    private String c_w_temp;//冷水温度
    private String windspeed;//风速
    private String w_lv_1;//水位1
    private String w_lv_2;//
    private String w_lv_3;//
    private String flowrate_1;//流量1
    private String flowrate_2;//
    private String boot_times;//设备开机时间
    private String temp_err;//温度传感器故障标志位
    private String other_err;//其他传感器故障标志位
    private String mac;//mac地址
    private String nfc;//校验时候有NFC协议，不校验时候为FFFFFFFF

    private String powder_coffee;//咖啡粉量
    private String powder_tea;//奶茶
    private String powder_fruit;//果汁
    private String water_coffee;//咖啡水量
    private String water_tea;//奶茶水量
    private String water_fruit;//果汁水量
    private String water_hot_winter;//热水冻水水量

//    private List<DBShopData> cntr_ary;//销售机货道数组
    private String cntr_status;//销售机状态
    private String cntr_err;//销售机错误代码

    private String longitude;//记录经度（可为空）
    private String latitude;//记录纬度（可为空）

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
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

    public String getF_r_1() {
        return f_r_1;
    }

    public void setF_r_1(String f_r_1) {
        this.f_r_1 = f_r_1;
    }

    public String getF_r_2() {
        return f_r_2;
    }

    public void setF_r_2(String f_r_2) {
        this.f_r_2 = f_r_2;
    }

    public String getF_r_3() {
        return f_r_3;
    }

    public void setF_r_3(String f_r_3) {
        this.f_r_3 = f_r_3;
    }

    public String getF_r_4() {
        return f_r_4;
    }

    public void setF_r_4(String f_r_4) {
        this.f_r_4 = f_r_4;
    }

    public String getMesh_residual() {
        return mesh_residual;
    }

    public void setMesh_residual(String mesh_residual) {
        this.mesh_residual = mesh_residual;
    }

    public String getCup_1() {
        return cup_1;
    }

    public void setCup_1(String cup_1) {
        this.cup_1 = cup_1;
    }

    public String getCup_2() {
        return cup_2;
    }

    public void setCup_2(String cup_2) {
        this.cup_2 = cup_2;
    }

    public String getCup_3() {
        return cup_3;
    }

    public void setCup_3(String cup_3) {
        this.cup_3 = cup_3;
    }

    public String getCup_4() {
        return cup_4;
    }

    public void setCup_4(String cup_4) {
        this.cup_4 = cup_4;
    }

    public String getCup_5() {
        return cup_5;
    }

    public void setCup_5(String cup_5) {
        this.cup_5 = cup_5;
    }

    public String getCup_6() {
        return cup_6;
    }

    public void setCup_6(String cup_6) {
        this.cup_6 = cup_6;
    }

    public String getCup_7() {
        return cup_7;
    }

    public void setCup_7(String cup_7) {
        this.cup_7 = cup_7;
    }

    public String getCup_8() {
        return cup_8;
    }

    public void setCup_8(String cup_8) {
        this.cup_8 = cup_8;
    }

    public String getCup_9() {
        return cup_9;
    }

    public void setCup_9(String cup_9) {
        this.cup_9 = cup_9;
    }

    public String getCup_10() {
        return cup_10;
    }

    public void setCup_10(String cup_10) {
        this.cup_10 = cup_10;
    }

    public String getW_status() {
        return w_status;
    }

    public void setW_status(String w_status) {
        this.w_status = w_status;
    }

    public String getIs_on() {
        return is_on;
    }

    public void setIs_on(String is_on) {
        this.is_on = is_on;
    }

    public String getIs_manuf() {
        return is_manuf;
    }

    public void setIs_manuf(String is_manuf) {
        this.is_manuf = is_manuf;
    }

    public String getC_status() {
        return c_status;
    }

    public void setC_status(String c_status) {
        this.c_status = c_status;
    }

    public String getC_error() {
        return c_error;
    }

    public void setC_error(String c_error) {
        this.c_error = c_error;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getTds_1() {
        return tds_1;
    }

    public void setTds_1(String tds_1) {
        this.tds_1 = tds_1;
    }

    public String getTds_2() {
        return tds_2;
    }

    public void setTds_2(String tds_2) {
        this.tds_2 = tds_2;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getVoc() {
        return voc;
    }

    public void setVoc(String voc) {
        this.voc = voc;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT2b() {
        return t2b;
    }

    public void setT2b(String t2b) {
        this.t2b = t2b;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getExhaust_temp() {
        return exhaust_temp;
    }

    public void setExhaust_temp(String exhaust_temp) {
        this.exhaust_temp = exhaust_temp;
    }

    public String getH_w_temp() {
        return h_w_temp;
    }

    public void setH_w_temp(String h_w_temp) {
        this.h_w_temp = h_w_temp;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public String getW_lv_1() {
        return w_lv_1;
    }

    public void setW_lv_1(String w_lv_1) {
        this.w_lv_1 = w_lv_1;
    }

    public String getW_lv_2() {
        return w_lv_2;
    }

    public void setW_lv_2(String w_lv_2) {
        this.w_lv_2 = w_lv_2;
    }

    public String getW_lv_3() {
        return w_lv_3;
    }

    public void setW_lv_3(String w_lv_3) {
        this.w_lv_3 = w_lv_3;
    }

    public String getFlowrate_1() {
        return flowrate_1;
    }

    public void setFlowrate_1(String flowrate_1) {
        this.flowrate_1 = flowrate_1;
    }

    public String getFlowrate_2() {
        return flowrate_2;
    }

    public void setFlowrate_2(String flowrate_2) {
        this.flowrate_2 = flowrate_2;
    }

    public String getBoot_times() {
        return boot_times;
    }

    public void setBoot_times(String boot_times) {
        this.boot_times = boot_times;
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

    public String getNfc() {
        return nfc;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }

    public String getC_w_temp() {
        return c_w_temp;
    }

    public void setC_w_temp(String c_w_temp) {
        this.c_w_temp = c_w_temp;
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

    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
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

//    public List<DBShopData> getCntr_ary() {
//        return cntr_ary;
//    }
//
//    public void setCntr_ary(List<DBShopData> cntr_ary) {
//        this.cntr_ary = cntr_ary;
//    }

    public String getCntr_status() {
        return cntr_status;
    }

    public void setCntr_status(String cntr_status) {
        this.cntr_status = cntr_status;
    }

    public String getCntr_err() {
        return cntr_err;
    }

    public void setCntr_err(String cntr_err) {
        this.cntr_err = cntr_err;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
