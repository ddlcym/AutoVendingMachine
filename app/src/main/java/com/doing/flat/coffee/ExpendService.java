package com.doing.flat.coffee;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.doing.flat.coffee.Unionpay.InitUnionpay;
import com.doing.flat.coffee.utils.CommonData;
import com.doing.flat.coffee.utils.Utils;
import com.doing.flat.nostra13.universalimageloader.utils.L;

import java.io.IOException;
import java.io.InputStream;

import RF610_Package.RF610_USB;
import android_serialport_api.SerialPort;
import android_serialport_api.sample.Application;


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

        if (intent != null) {
            int serverMessage = intent.getIntExtra(CommonData.Service_Message, 0);
            if(CommonData.Service_GetICCard==serverMessage){

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String device = "ttyUSB2";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        InitUnionpay.getInstance(Application.getInstance()).closeQuickPass();
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



}
