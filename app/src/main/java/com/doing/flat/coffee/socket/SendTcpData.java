package com.doing.flat.coffee.socket;

import com.alibaba.fastjson.JSON;
import com.doing.flat.coffee.CoffeeRs232Activity;
import com.doing.flat.coffee.entity.SendSocketEntity;
import com.doing.flat.coffee.utils.HttpUrls;
import com.doing.flat.coffee.utils.L;
import com.doing.flat.coffee.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by cym on 2017/11/30.
 */

public class SendTcpData {
    private static SendTcpData instance;
    public static final String HOST = "59.41.103.97";
    public static final int PORT = 18178;
    private WeakReference<Socket> mSocket;
    private OutputStream os = null;

    private RecTcpData mRecTcpData;

    private boolean socketStat = false;

    public static SendTcpData getInstance() {

        if (null == instance) {
            instance = new SendTcpData();
        }
        return instance;
    }

    private void initSocket() {
        try {
            Socket so = new Socket(HOST, PORT);
            mSocket = new WeakReference<Socket>(so);

            mRecTcpData = new RecTcpData(so);
            mRecTcpData.start();
            socketStat = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            socketStat = false;
            mSocket = null;
            L.e("SendTcpData-UnknownHostException:"+e.getMessage());
//            setBUG();
        } catch (IOException e) {
            e.printStackTrace();
            L.e("SendTcpData-IOException:"+e.getMessage());
            socketStat = false;
            mSocket = null;
//            setBUG();
        }
    }

    public boolean sendTCPData(byte sData[]) {
        initSocket();

        if (null == mSocket || null == mSocket.get() || null == sData || sData.length == 0) {
            return false;
        }
        Socket soc = mSocket.get();
        L.i("sendTCPData-sData:"+new String(sData));
        try {
            L.i("sendTCPData-sData-getHexString:"+L.getHexString(sData));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!soc.isClosed() && !soc.isOutputShutdown()) {
            try {
                if (null == os) {
                    os = soc.getOutputStream();
                }
                os.write(sData);
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
                releaseLastSocket(mSocket);
                return false;
            }
        } else {
            releaseLastSocket(mSocket);
            return false;
        }
        return true;
    }


    public void releaseLastSocket(WeakReference<Socket> mSocket) {

        if(os!=null){
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (sk != null && !sk.isClosed()) {
                    sk.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null != mRecTcpData) {
            mRecTcpData.release();
        }
    }
}
