package com.doing.flat.coffee.socket;

/**
 * Created by cym on 2017/11/30.
 */

public class HandTcpData {


    private static HandTcpData instance;

    public static HandTcpData getInstance(){

        if(null==instance){
            instance=new HandTcpData();
        }
        return instance;
    }

    public void handData(byte recData[]){



    }
}
