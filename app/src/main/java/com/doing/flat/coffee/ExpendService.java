package com.doing.flat.coffee;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.doing.flat.coffee.utils.Utils;
import com.doing.flat.nostra13.universalimageloader.utils.L;

import java.io.IOException;
import java.io.InputStream;

import RF610_Package.RF610_USB;
import android_serialport_api.SerialPort;


/**
 * Created by cym on 2017/11/27.
 */

public class ExpendService extends Service {
    private SerialPort upSerialPort = null;
    private InputStream mInputStream;
    public static String[] SMsg = new String[2];
    public static int OK = 0;
    private RF610_USB rf610;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String device = "ttyUSB2";
        connectDevices();
        getICCard();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDev();
    }


    private void connectDevices() {
        rf610 = new RF610_USB();
        byte[] _Version = new byte[50];
        int nRet = rf610.RF610_USB_OpenRU(ExpendService.this);
        if (nRet == 0) {
            nRet = rf610.RF610_USB_GetHardVersion(_Version);
            if (nRet == 0) {
                Toast.makeText(ExpendService.this, "连接成功，设备版本为：" + ByteToString(_Version), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ExpendService.this, "连接成功，但是通讯失败，请检查设备是否正常", Toast.LENGTH_SHORT).show();
                rf610.RF610_USB_CloseRU();
            }
        } else {
            Toast.makeText(ExpendService.this, "连接失败", Toast.LENGTH_SHORT).show();

        }
    }

    private void closeDev() {
        int nRet = rf610.RF610_USB_CloseRU();
        if (nRet == 0) {
            Toast.makeText(ExpendService.this, "断开成功", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(ExpendService.this, "断开失败", Toast.LENGTH_SHORT).show();
    }

    private void getICCard() {
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
            ShowMessage("获取银行卡号成功，卡号为：" + str);
        } else
            ShowMessage("获取银行卡号失败,错误代码为：" + rf610.ErrorCode(nRet, 0));
    }

    private class TestRFThread extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getICCard();
            }
        }
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int size;
                try {
                    byte[] buffer = new byte[20];
                    if (mInputStream == null) return;
                    L.i("upSerialPort--readbegin");
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        L.i("upSerialPort" + Utils.byteToString(buffer));
                    }
                } catch (IOException e) {
                    L.e("upSerialPort-ReadThread-error:" + e.getMessage());
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private void active() {
        byte[] Atr = new byte[300];
        int[] len = new int[2];

        int nRet = rf610.RF610_USB_CPUCardPowerOn(Atr);
        if (nRet == 0) {
            Toast.makeText(ExpendService.this, "卡片激活成功", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(ExpendService.this, "卡片激活失败", Toast.LENGTH_SHORT).show();
    }

    public void ShowMessage(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExpendService.this);
        builder.setTitle("提示");
        builder.setMessage(str);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    public String ByteToString(byte[] by) {
        String str = "";
        char ch = '\0';
        for (int i = 0; by[i] != '\0'; i++) {
            ch = (char) by[i];
            str += ch;
        }
        return str;
    }
}
