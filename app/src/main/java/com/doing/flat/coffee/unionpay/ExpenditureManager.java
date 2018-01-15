package com.doing.flat.coffee.unionpay;

/**
 * Created by cym on 2018/1/4.
 */

public class ExpenditureManager {
    private static ExpenditureManager instance;

    public static ExpenditureManager getInstance() {

        if (null == instance) {
            instance = new ExpenditureManager();
        }
        return instance;
    }

    public boolean expendOp(){
        boolean isSuc=false;



        return isSuc;
    }


}
