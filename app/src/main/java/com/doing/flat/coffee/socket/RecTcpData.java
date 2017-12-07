package com.doing.flat.coffee.socket;

import com.doing.flat.coffee.utils.L;
import com.doing.flat.coffee.utils.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by cym on 2017/11/30.
 */

public class RecTcpData extends Thread {

    private WeakReference<Socket> mWeakSocket;
    private boolean isStart = true;
    private byte recData[] = null;
    private InputStream is = null;


    public RecTcpData(Socket so) {
        mWeakSocket = new WeakReference<Socket>(so);
    }

    public void release() {
        isStart = false;
        releaseLastSocket(mWeakSocket);
    }

    @Override
    public void run() {
        super.run();
        int length = 0;

        Socket socket = mWeakSocket.get();
        if (null != socket) {
            try {
                is = socket.getInputStream();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                L.i("rectcp-SocketException");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (isStart) {
                try {
                    if (socket.isClosed()) {
                        isStart = false;
                        break;
                    }

                    length = is.available();
                    if (length <= 0) {
                        continue;
                    }
                    recData = new byte[length];
                    is.read(recData);

                    HandTcpData.getInstance().handData(recData);
                    try {
                        L.i("--recTCPData--------" + L.getHexString(recData)
                                + "--socket--" + socket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;
                }

            }

            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void releaseLastSocket(WeakReference<Socket> mSocket) {


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
    }
}
