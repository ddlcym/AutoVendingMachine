package com.doing.flat.coffee.Unionpay;

import android.content.Context;

import RF610_Package.RF610_USB;

/**
 * Created by cym on 2017/12/11.
 */

public class InitUnionpay {

    private static InitUnionpay instance;
    private RF610_USB rf610;
    private Context con;


    public static InitUnionpay getInstance(Context context){

        if(null==instance){
            instance=new InitUnionpay(context);
        }
        return instance;
    }

    public InitUnionpay(Context context) {
        this.con = context;
    }

    public boolean connectQuickPass() {
        rf610 = new RF610_USB();
        byte[] _Version = new byte[50];
        int nRet = rf610.RF610_USB_OpenRU(con);
        if (nRet == 0) {
            nRet = rf610.RF610_USB_GetHardVersion(_Version);
            if (nRet == 0) {
               return true;
            } else {

                rf610.RF610_USB_CloseRU();
                return false;
            }
        } else {
           return false;

        }
    }

    public boolean closeQuickPass(){
        if(null==rf610){
            return true;
        }
        int nRet = rf610.RF610_USB_CloseRU();
        if(nRet == 0)
        {
            //断开成功
            rf610=null;
            return true;
        }else{//断开失败
            rf610=null;
            return false;
        }
    }

    public String  getICCard() {
        int[] len = new int[2];
        byte[] CardID = new byte[20];
        String str = "";
        int nRet = rf610.RF610_USB_TypeAReadICCardNum(len, CardID);
        if (nRet == 0) {
            if (len[0] % 2 == 0) {
                for (int i = 0; i < len[0] / 2; i++) {
                    if ((CardID[i] & 0xF0) > 0)
                        str += Integer.toHexString(CardID[i] & 0xFF).toUpperCase();
                    else
                        str += "0" + Integer.toHexString(CardID[i] & 0xFF).toUpperCase();
                }
            } else {
                for (int i = 0; i < (len[0] - 1) / 2; i++) {
                    if ((CardID[i] & 0xF0) > 0)
                        str += Integer.toHexString(CardID[i] & 0xFF).toUpperCase();
                    else
                        str += "0" + Integer.toHexString(CardID[i] & 0xFF).toUpperCase();
                }
                str += Integer.toHexString(((CardID[(len[0] - 1) / 2] & 0xF0) / 16) & 0xFF).toUpperCase();
            }
            return str;
//            ShowMessage("获取银行卡号成功，卡号为：" + str);
        } else {
            return null;
//            ShowMessage("获取银行卡号失败,错误代码为：" + rf610.ErrorCode(nRet, 0));
        }
    }


}
