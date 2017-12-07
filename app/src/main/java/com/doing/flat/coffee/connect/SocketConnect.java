package com.doing.flat.coffee.connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.doing.flat.coffee.entity.SendSocketEntity;
import com.doing.flat.coffee.entity.UserSendData;
import com.doing.flat.coffee.utils.HttpUrls;
import com.doing.flat.coffee.utils.NetOperacationUtils;
import com.doing.flat.coffee.utils.StringUtils;
import com.doing.flat.coffee.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public class SocketConnect implements Runnable {
    private Socket socket = null;
    private Handler handler = null;
    private BufferedReader br = null;
    private long time = 5000;
    private boolean stop = false;
    // 服务器server/IP地址(当前PC的IP地址)
    private final String ADDRESS = "120.25.235.111";
    // 服务器端口
//    private final int PORT = 12300;
    private final int PORT = HttpUrls.SOCKETPORT;

    public static final int RESULT_CODE = 0x123;
    public static final int RESULT_CODE_WATER_OK = 0x124;
    public static final int RESULT_CODE_ENVIRONTION_OK = 0x125;
    public static final int RESULT_CODE_NOW_OK = 0x126;
    public static final int RESULT_CODE_ERROR = 0x127;

//    private ResultWaterEntity waterEntity = null;

    private Context context;

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public SocketConnect(Context context, Handler handler) throws IOException {
        this.handler = handler;
        this.context = context;
        if (isNetworkAvailable(context)) {
            socket = new Socket(ADDRESS, PORT);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    private void error(String str) {
        Message message = new Message();
        message.what = RESULT_CODE_ERROR;
        message.obj = str;
        handler.sendMessage(message);
    }

    /**
     * 循环读取数据
     */
    @Override
    public void run() {
//        if (isNetworkAvailable(context)) {
//            try {
//                String connet = null;
//                while (!stop) {
//                    if ((connet = br.readLine()) != null) {
//                        Utils.LogE(connet);
//                        ResultWaterEntity waterEntity = null;
//                        if (connet.startsWith("{") && connet.contains("result")) {
//                            waterEntity = JSON.parseObject(connet, ResultWaterEntity.class);
//                        }
//
//                        if (waterEntity != null && waterEntity.getResult().equals("0")) {
//                            String dataStr = waterEntity.getData();
//                            if (dataStr.length() > 2 && dataStr.startsWith("[")) {
//                                JSONArray array = JSON.parseArray(dataStr);
//                                String typeStr = JSON.toJSONString(array.get(0));
//                                if (waterEntity.getOperation().equals(OperationConfig.OPERTION_SYNC_WATER)) {//同步水
//                                    syncWater(waterEntity, array);
//                                } else if (waterEntity.getOperation().equals(OperationConfig.OPERTION_SYNC_ENVIRONMENT)) {//同步环境
//                                    syncEnvironment(waterEntity, array);
//                                }
//                            } else {
//                                if (waterEntity.getOperation().equals(OperationConfig.OPERTION_SYNC_WATER)) {//同步水
//                                    if (waterEntity.getComplete().equals("1")) {
//                                        sendEnvironment();
//                                    } else {
//                                        sendWater();
//                                    }
//                                } else if (waterEntity.getOperation().equals(OperationConfig.OPERTION_SYNC_ENVIRONMENT)) {//同步环境
//                                    if (waterEntity.getComplete().equals("0")) {
//                                        sendEnvironment();
//                                    }
//                                }
//                            }
//                        } else if (waterEntity != null) {//服务器返回错误原因
//                            error(waterEntity.getResult_message());
//                        }
//                    } else {
//                        try {
//                            socket.sendUrgentData(0xFF);
//                        } catch (IOException e) {
//                            reConnect();
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private String getAverage(String phAverage) {
        String[] strPH = phAverage.split(",");
        int size = strPH.length;
        float sum = 0;
        for (int i = 0; i < size; i++) {
            String str = strPH[i];
            float numPH = 0;
            if (!str.equals("FF") && str.length() != 0)
                numPH = Float.parseFloat(str);
            sum += numPH;
        }
        return sum / size + "";
    }


    /**
     * 发送同步实时数据准备
     */
    public void sendNow() {
//        SendDataIdEntity sendTCPData = new SendDataIdEntity();
//        try {
//            List<ResultEnvironmentData> waterList = db.findAll(Selector.from(ResultEnvironmentData.class).orderBy("data_id", true).limit(1).where("u_id",
//                    "like", SharePerfenceUtils.getInstance(context).getU_id()));
//            if (waterList != null && waterList.size() != 0) {
//                sendTCPData.setData_id(waterList.get(0).getData_id() + "");
//            } else {
//                sendTCPData.setData_id(0 + "");
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        sendTCPData(OperationConfig.OPERTION_SYNC_ENVIRONMENT, System.currentTimeMillis() + "", sendTCPData);
    }

    /**
     * 重新连接
     */
    private void reConnect() {
        if (isNetworkAvailable(context)) {
            try {
                Looper.prepare();
                socket = new Socket(ADDRESS, PORT);
            } catch (Exception e1) {
                initiate();
            }
        }
        Looper.loop();
    }

    /**
     * 向服务器发送数据
     *
     * @param operation
     * @param opcode
     * @param data
     */
    public void sendData(String operation, String opcode, Object data) {
        if (!isNetworkAvailable(context)) {
            return;
        }
        if (socket != null) {
            try {
                socket.sendUrgentData(0xFF);
            } catch (IOException e) {
                reConnect();
            }
        } else {
            reConnect();
        }
        //原本数据格式
        UserSendData userSendData = new UserSendData();
//        userSendData.setOperation(operation);
        userSendData.setDevice_id(operation);
        NetOperacationUtils.setBaseData(context, userSendData, opcode);

        userSendData.setData(data);
        String json = JSON.toJSONString(userSendData);
        //socket数据格式封装
        SendSocketEntity entity = new SendSocketEntity();
        entity.setMsgType("mobile");
        entity.setContent(StringUtils.encode64String(json));
        String dataStr = JSON.toJSONString(entity) + "\n";
        Utils.LogE(dataStr);
        if (socket != null) {
            try {
                OutputStream out = socket.getOutputStream();
                out.write(dataStr.getBytes("utf-8"));
//                DataOutputStream dos = new DataOutputStream(
//                        socket.getOutputStream());
//                // 向服务器写数据
//                dos.writeUTF(dataStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动重连线程
     */
    private void initiate() {
        if (SocketConnect.isNetworkAvailable(context)) {
            Thread isAgainThread = new Thread(SocketConnect.this);
            isAgainThread.start();
        }
    }

    public static boolean isNetworkAvailable(Context activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
