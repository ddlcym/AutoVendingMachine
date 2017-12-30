package com.doing.flat.coffee.Unionpay;

import com.doing.flat.coffee.download.utils.TextUtils;

import android_serialport_api.sample.Application;

/**
 * Created by cym on 2017/12/11.
 */

public class ReadICCard extends Thread {

    private boolean flag = true;
    private RCCallBack rcCallBack;

    public ReadICCard(RCCallBack callBack) {
        this.rcCallBack = callBack;
    }

    @Override
    public void run() {
        super.run();
        InitUnionpay.getInstance(Application.getInstance()).connectQuickPass();
        while (flag) {
            String card = InitUnionpay.getInstance(Application.getInstance()).getICCard();
            if (!TextUtils.isEmpty(card)) {
                rcCallBack.dealCard(card);
                flag = false;
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        InitUnionpay.getInstance(Application.getInstance()).closeQuickPass();
    }

    public interface RCCallBack {
        void dealCard(String card);
    }
}
