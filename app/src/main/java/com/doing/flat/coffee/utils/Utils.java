package com.doing.flat.coffee.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.doing.flat.coffee.ble.SendbleEntity;
import com.doing.flat.coffee.entity.DBCntrOptEntity;
import com.doing.flat.coffee.entity.DBDeiveceData;
import com.doing.flat.coffee.entity.DBShopData;
import com.doing.flat.coffee.service.LocationService;
import com.doing.flat.coffee.xutils.DbUtils;
import com.doing.flat.coffee.xutils.db.sqlite.Selector;
import com.doing.flat.coffee.xutils.db.sqlite.WhereBuilder;
import com.doing.flat.coffee.xutils.exception.DbException;
import com.doing.flat.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android_serialport_api.sample.Application;

/**
 * Created by bruce on 14-11-6.
 */
public class Utils {
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    //设置发送给的数据
    public static String setSendData(SendbleEntity entity, String Sendevice_id) {
        String tempStr = "0a";
        int strDir = 0;
        int sex = 0;
        String wc = "";
        String hc = "";
        String head = "0a ff 06";//引导头
        String device_id = "ff ff ff ff";//设备ID
        String backups = "ff ff ff ff";//备份
        String check_sum = "ff ff ff ff";
        String finish = "0505ffff";//结束码

        device_id = Sendevice_id;


        //计算校验码
        strDir = 10;
        String[] heakList = head.split(" ");
        for (int i = 0; i < heakList.length; i++) {
            strDir = strDir ^ Transition16go10(heakList[i]);
        }
        String[] device_idList = device_id.split(" ");
        for (int i = 0; i < device_idList.length; i++) {
            strDir = strDir ^ Transition16go10(device_idList[i]);
        }
//        strDir = strDir ^ Transition16go10(entity.getDerail_switch() + entity.getMake_switch());
//        strDir = strDir ^ Transition16go10(entity.getWind() + entity.getUv());
//        strDir = strDir ^ Transition16go10(entity.getRefrigeration() + entity.getHeat());
//        strDir = strDir ^ Transition16go10(entity.getHeat_switch() + entity.getCold_switch());
//
//        String[] backups_List = backups.split(" ");
//        for (int i = 0; i < backups_List.length; i++) {
//            strDir = strDir ^ Transition16go10(backups_List[i]);
//        }
//
//        //组合发送命令
//        tempStr += head.replace(" ", "");
//        tempStr += device_id.replace(" ", "");
//        tempStr += entity.getDerail_switch() + entity.getMake_switch();
//        tempStr += entity.getWind() + entity.getUv();
//        tempStr += entity.getRefrigeration() + entity.getHeat();
//        tempStr += entity.getHeat_switch() + entity.getCold_switch();
//        tempStr += backups.replace(" ", "");
//        tempStr += AnalysisCheckSum(strDir);
//        tempStr += finish;

        return tempStr;
    }

    //设置发送给的数据
    public static String setSendData2(SendbleEntity entity, String Sendevice_id,int serial_number) {
        String tempStr = "a5";
        int strDir = 0;
        String head = "a5ff20";//引导头
        String device_id = " ffffffff ";//设备ID
        String backups = " ffffffff ";//备份
        String check_sum = " ffffffff ";
        String finish = " 5050ffff ";//结束码

        device_id = Sendevice_id;


        //计算校验码
        strDir = 165;
//        String[] heakList = head.split(" ");
//        for (int i = 0; i < heakList.length; i++) {
//            strDir = strDir ^ Transition16go10(heakList[i]);
//        }
//        String[] device_idList = device_id.split(" ");
//        for (int i = 0; i < device_idList.length; i++) {
//            strDir = strDir ^ Transition16go10(device_idList[i]);
//        }
//        String[] backups_List = backups.split(" ");
//        for (int i = 0; i < backups_List.length; i++) {
//            strDir = strDir ^ Transition16go10(backups_List[i]);
//        }

        //组合发送命令
//        tempStr += head.replace(" ", "");
//        tempStr += device_id.replace(" ", "");
        tempStr += head;
        tempStr += device_id;
        tempStr += "ffffffff ";
        tempStr += "ffffffff ";
        tempStr += "ffffffff ";
        tempStr += "ffffffff ";
        tempStr += "ffffffff ";
        tempStr += "ffffffff ";
        tempStr += "ffffffff ";
        tempStr += "ffffffff ";
        tempStr += "ffffffff ";
        tempStr += AnalysisChecLongkSum(entity.getCommand()) + " ";
        tempStr += "ffffffff ";
        tempStr += "ffffffff ";

        tempStr += "" + entity.getCoffee_switch() + "";
        tempStr += "" + entity.getTea_switch() + "";
        tempStr += "" + entity.getWater_switch() + "";
        tempStr += "ff";
//        tempStr += "" + entity.getFruit_switch() + "";
//        tempStr += "" + entity.getWater_switch() + "";

//        tempStr += " ffffffff";//
//        tempStr += " ffffffff";


        tempStr += " " + entity.getDixie_cup() + "";//
        tempStr += "" + entity.getDixie_cup_1() + "";
        tempStr += " " + entity.getDixie_cup_2() + "";
        tempStr += "" + entity.getDixie_cup_3();


        tempStr += " " + entity.getWater_for() + "";//加了個水循环这里就少了一位
        tempStr += "ffffff";

        tempStr += " " + entity.getWater_coffee() + "";
        tempStr += "" + entity.getWater_tea() + "";
        tempStr += " " + entity.getWater_hot_winter_hot() + "";
        tempStr += "" + entity.getWater_hot_winter();
        tempStr += " ffffff";
        String serial_number_temp=Integer.toHexString(serial_number);
        if (serial_number_temp.length() == 1) {
            serial_number_temp = "0" + serial_number_temp;
        } else if (serial_number_temp.length() == 0) {
            serial_number_temp = "00";
        }
        tempStr +=serial_number_temp ;
        String sales = ((entity.getSales()));
        if (sales.length() == 1) {
            sales = "0" + sales;
        } else if (sales.length() == 0) {
            sales = "00";
        }
        tempStr += " " + sales + "ff";
        tempStr +=  entity.getSensor() + "";//检测传感器
        tempStr += "ff";

        for (int i = 0; i < 7; i++) {
            tempStr += " ffffffff";
        }
        tempStr += " ffffffff";
        Log.e("tempStr=", tempStr);

        long check_sum_long = 0;
        tempStr = tempStr.replace(" ", "");
        for (int i = 0; i <= 119; i++) {
            if (i * 2 + 2 <= tempStr.length()) {
                check_sum_long += Transition16go10(tempStr.substring(i * 2, (i * 2 + 2)));
//                Log.e("check_sum_long=", check_sum_long + "   tempStr.substring(i*2,(i*2+2))=" + tempStr.substring(i * 2, (i * 2 + 2))+"   i="+i);
            }
        }
//        Log.e("check_sum_long=", check_sum_long+"");
        String strLong = AnalysisChecLongkSum(check_sum_long);


//        Log.e("strLong=", strLong + "");
        tempStr += strLong;
        tempStr += finish;
        tempStr = tempStr.replace(" ", "");
        Log.e("tempStr22=", tempStr);
        return tempStr;
    }

    //设置发送自动售货机的数据
    public static String setSendAVM(SendbleEntity entity, String Sendevice_id,int goodsChannel) {
        String tempStr = "a5";
        String head = "a5ff20 ";//引导头
        String device_id = " ffffffff ";//设备ID
        String backups = " ffffffff ";//备份
        String check_sum = " ffffffff ";
        String finish = " 5050ffff ";//结束码

        device_id = Sendevice_id;
        tempStr += head;
        tempStr += device_id;
        tempStr += " 00000000 ";
        tempStr += "00000000 ";
        tempStr += "00000000 ";
        tempStr += "00000000 ";
        tempStr += "00000000 ";
        tempStr += "00000000 ";
        tempStr += "00000000 ";
        tempStr += "00000000 ";
        tempStr += "00000000 ";
        tempStr += AnalysisChecLongkSum(entity.getCommand()) + " ";//命令号，每次发送自动加1
        tempStr += "00000000 ";
        tempStr += "00000000";
        tempStr += " 00000000";
        tempStr += " 00000000";
        tempStr += " 00000000";
        tempStr += " 00000000";
        tempStr += " 00000000";
        tempStr += " 00000000";
        tempStr += " 00000000";


        String serial_number_temp=Integer.toHexString(goodsChannel);
        if (serial_number_temp.length() == 1) {
            serial_number_temp = " 0" + serial_number_temp;
        } else if (serial_number_temp.length() == 0) {
            serial_number_temp = " 00";
        }
        tempStr +=serial_number_temp ;//货道号
        String sales = "ff";
        tempStr += sales + "ff";
        tempStr +=  "ff" + "";//检测传感器

        for (int i = 0; i < 8; i++) {
            tempStr += " 00000000";
        }
        Log.e("tempStr=", tempStr);

        long check_sum_long = 0;
        tempStr = tempStr.replace(" ", "");
        for (int i = 0; i <= 119; i++) {
            if (i * 2 + 2 <= tempStr.length()) {
                check_sum_long += Transition16go10(tempStr.substring(i * 2, (i * 2 + 2)));
//                Log.e("check_sum_long=", check_sum_long + "   tempStr.substring(i*2,(i*2+2))=" + tempStr.substring(i * 2, (i * 2 + 2))+"   i="+i);
            }
        }
//        Log.e("check_sum_long=", check_sum_long+"");
        String strLong = AnalysisChecLongkSum(check_sum_long);


//        Log.e("strLong=", strLong + "");
        tempStr += strLong;
        tempStr += finish;
        tempStr = tempStr.replace(" ", "");
        Log.e("sendAVM=", tempStr);
        return tempStr;
    }

    public static String getCheckSum(String temp) {
        String[] backups_List = temp.split(" ");
        Long strDir = 0L;
        for (int i = 0; i <= backups_List.length; i++) {
            if (backups_List[i].length() > 0) {
                Long temp8 = Transition16Longgo10(backups_List[i]);
                Log.e("backups_List[i]", backups_List[i] + "    i=" + i + "   temp8=" + temp8);
                strDir = strDir + temp8;
            }
        }
        String srTemp = AnalysisChecLongkSum(strDir);
        Log.e("strDir", srTemp + "");
        if (srTemp.length() > 8) {
            srTemp = srTemp.substring(srTemp.length() - 8, srTemp.length());
        }
        Log.e("strDir", srTemp + "");
        return srTemp;

//        if (strDir > 10000000) {
////            strDir = strDir % 10000000;
//            String strTemp = strDir + "";
//            Log.e("strDir", strTemp + "");
//            strTemp = strTemp.substring(strTemp.length() - 8, strTemp.length());
//            strDir = Long.parseLong(strTemp);
//            Log.e("strDir", strTemp + "");
//        }
//        return strDir;
    }


//    //设置发送给的数据
//    public static String setSendData2(SendbleEntity entity, String Sendevice_id) {
//        String tempStr = "a0";
//        int strDir = 0;
//        String head = "a0 ff 06";//引导头
//        String device_id = "ff ff ff ff";//设备ID
//        String backups = "ff ff ff ff";//备份
//        String check_sum = "ff ff ff ff";
//        String finish = "5050ffff";//结束码
//
//        device_id = Sendevice_id;
//
//
//        //计算校验码
//        strDir = 10;
//        String[] heakList = head.split(" ");
//        for (int i = 0; i < heakList.length; i++) {
//            strDir = strDir ^ Transition16go10(heakList[i]);
//        }
//        String[] device_idList = device_id.split(" ");
//        for (int i = 0; i < device_idList.length; i++) {
//            strDir = strDir ^ Transition16go10(device_idList[i]);
//        }
//        strDir = strDir ^ Transition16go10(entity.getDerail_switch() + entity.getMake_switch());
//        strDir = strDir ^ Transition16go10(entity.getWind() + entity.getUv());
//        strDir = strDir ^ Transition16go10(entity.getRefrigeration() + entity.getHeat());
//        strDir = strDir ^ Transition16go10(entity.getHeat_switch() + entity.getCold_switch());
//
//        String[] backups_List = backups.split(" ");
//        for (int i = 0; i < backups_List.length; i++) {
//            strDir = strDir ^ Transition16go10(backups_List[i]);
//        }
//
//        //组合发送命令
//        tempStr += head.replace(" ", "");
//        tempStr += device_id.replace(" ", "");
//        if (entity.getHeat_switch().equals("1")) {
//            tempStr += "1111000011000000";
//        } else {
//            tempStr += "0000111111000000";
//        }
//        tempStr += "0000000000000000";
////        tempStr += backups.replace(" ", "");
//        tempStr += AnalysisCheckSum(strDir);
//        tempStr += finish;
//        Log.e("tempStr=", tempStr);
//        return tempStr;
//    }

    //16转10
    public static int Transition16go10(String temp) {
//        return Integer.parseInt(temp, 16);
        return (int) Long.parseLong(temp, 16);
    }

    //16转10
    public static Long Transition16Longgo10(String temp) {
//        return Integer.parseInt(temp, 16);
        return Long.parseLong(temp, 16);
    }

    public static String AnalysisCheckSum(int temp) {
        String strTemp = Integer.toHexString(temp);
        switch (strTemp.length()) {
            case 0:
                return "00000000";
            case 1:
                return "0000000" + strTemp;
            case 2:
                return "000000" + strTemp;
            case 3:
                return "00000" + strTemp;
            case 4:
                return "0000" + strTemp;
            case 5:
                return "000" + strTemp;
            case 6:
                return "00" + strTemp;
            case 7:
                return "0" + strTemp;
        }
        return "00000000";
    }

    public static String Analysis(String temp) {
        String strTemp = Integer.toHexString(Integer.parseInt(temp));
        switch (strTemp.length()) {
            case 0:
                return "ffff";
            case 1:
                return "000" + strTemp;
            case 2:
                return "00" + strTemp;
            case 3:
                return "0" + strTemp;
            case 5:
            case 6:
            case 7:
                return "ffff";
        }
        return strTemp;
    }


    public static String AnalysisChecLongkSum(Long temp) {
        String strTemp = Long.toHexString(temp);
        switch (strTemp.length()) {
            case 0:
                return "00000000";
            case 1:
                return "0000000" + strTemp;
            case 2:
                return "000000" + strTemp;
            case 3:
                return "00000" + strTemp;
            case 4:
                return "0000" + strTemp;
            case 5:
                return "000" + strTemp;
            case 6:
                return "00" + strTemp;
            case 7:
                return "0" + strTemp;
            case 8:
                return strTemp;
        }
        return strTemp;
    }

    public static String AnalysisChecLongkSum(String temp) {
        String strTemp = temp;
        switch (strTemp.length()) {
            case 0:
                return "00000000";
            case 1:
                return "0000000" + strTemp;
            case 2:
                return "000000" + strTemp;
            case 3:
                return "00000" + strTemp;
            case 4:
                return "0000" + strTemp;
            case 5:
                return "000" + strTemp;
            case 6:
                return "00" + strTemp;
            case 7:
                return "0" + strTemp;
            case 8:
                return strTemp;
        }
        return strTemp;
    }


    public static String byteToString(byte[] tmp_byte) {
        String tmp = "";
        for (int i = 0; i < tmp_byte.length; i++) {
            String hex = Integer.toHexString(tmp_byte[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            tmp += ' ';
            tmp = tmp + hex;
        }
        return tmp;
    }


    public static byte[] setbyte(String jj) {
        String tmp_str = "";
        byte[] tmp_byte = null;
        byte[] write_msg_byte = null;
        tmp_str = jj;
        if (0 == tmp_str.length())
            return null;
        tmp_byte = tmp_str.getBytes();
        write_msg_byte = new byte[tmp_byte.length / 2 + tmp_byte.length % 2];
        for (int i = 0; i < tmp_byte.length; i++) {
            if ((tmp_byte[i] <= '9') && (tmp_byte[i] >= '0')) {
                if (0 == i % 2)
                    write_msg_byte[i / 2] = (byte) (((tmp_byte[i] - '0') * 16) & 0xFF);
                else
                    write_msg_byte[i / 2] |= (byte) ((tmp_byte[i] - '0') & 0xFF);
            } else {
                if (0 == i % 2)
                    write_msg_byte[i / 2] = (byte) (((tmp_byte[i] - 'a' + 10) * 16) & 0xFF);
                else
                    write_msg_byte[i / 2] |= (byte) ((tmp_byte[i] - 'a' + 10) & 0xFF);
            }
        }
        return write_msg_byte;
    }


    /**
     * 动态广场显示图片
     *
     * @param url 链接
     * @param v   imageview
     */
    public static void setCanReplyImage(String url, ImageView v) {
//        if (url != null) {
//            if (!url.startsWith("http:") && !url.startsWith("file://")) {
//                url = "file://" + url;
//            }
//        }
        if (url != null) {
            if (!url.startsWith("http:") && !url.startsWith("file://") && !url.startsWith("assets://") && !url.startsWith("drawable://")) {
                url = "file://" + url;
            }
        }
        ImageLoader.getInstance().displayImage(url,
                v, Application.getInstance().reply_photo_options, Application.getInstance().getImageLoadingListener());
    }

    public static final boolean DEBUG = true;
    public static final boolean NEEDTOAST = false;
    private static final String TAG = "data";

    public static void LogE(String txt) {
        if (DEBUG)
            Log.e(TAG, txt);
    }

    /**
     * 日期转字符串
     *
     * @param time       日期时间戳
     * @param outFormate
     * @return
     */
    public static String long2String(long time, String outFormate) {
        SimpleDateFormat formatter = new SimpleDateFormat(outFormate);
        Date curDate = new Date(time * 1000);
        String str = formatter.format(curDate);
        return str;
    }

    //下载apk程序代码
    public static File downLoadFile(Context context, String httpUrl) {
        // TODO Auto-generated method stub
        String fileName = System.currentTimeMillis() + "_coffee.apk";
        //+System.currentTimeMillis()+
        String imagePath = Environment.getExternalStorageDirectory()
                + "/slab/";
        File tempFile = new File(imagePath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        File file = new File(imagePath + File.separator, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogE("httpUrl=" + httpUrl);
        LogE("file=" + file.getAbsolutePath());
        try {
            URL url = new URL(httpUrl);
            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[64];
//                byte[] buf = new byte[256];
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {
                    Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    while (count <= 100) {
                        if (is != null) {
                            int numRead = is.read(buf);
                            if (numRead <= 0) {
                                break;
                            } else {
                                fos.write(buf, 0, numRead);
                            }

                        } else {
                            break;
                        }
                    }
                }
                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }
    //打开APK程序代码

    public static void openFile(Context context, File file) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    // / 没有连接

    public static final int NETWORN_NONE = 0;
    // / wifi连接
    public static final int NETWORN_WIFI = 1;
    // / 手机网络数据连接
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;

    /**
     * 返回当前网络连接类型
     *
     * @param context 上下文
     * @return
     */
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connManager)
            return NETWORN_NONE;
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORN_NONE;
        }
        // Wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORN_WIFI;
                }
        }
        // 网络
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORN_2G;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORN_3G;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORN_4G;
                        default://有机型返回16,17
                            //中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return NETWORN_3G;
                            } else {
                                return NETWORN_MOBILE;
                            }
                    }
                }
        }
        return NETWORN_NONE;
    }

    public static String getAppInfo(Context context) {
        try {
            String pkName = context.getPackageName();
            return pkName + ":" + System.currentTimeMillis() + ":" + getCurProcessName(context) + ":" + context;
        } catch (Exception e) {
        }
        return null;
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.pid + ":" + appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取货的数据
     */
    public static void getCntrData(Map<String, DBShopData> dataMap, DbUtils dbUtils) {
        if (dataMap == null)
            dataMap = new HashMap<>();
        List<DBShopData> listdata = null;
        try {
            listdata = dbUtils.findAll(Selector.from(DBShopData.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (listdata != null && listdata.size() > 0) {
            for (int i = 0; i < listdata.size(); i++) {
                dataMap.put(listdata.get(i).getNum(), listdata.get(i));
            }
        }
    }

    /**
     * 获取货的数据
     */
    public static void getCntrDataList(Map<String, DBShopData> dataMap, List<DBShopData> dataList) {
        dataList.addAll(dataMap.values());
        if (dataList.size() == 0) {
            DbUtils dbUtils = Application.getInstance().getDb();
            try {
                List<DBShopData> temp = dbUtils.findAll(Selector.from(DBShopData.class));
                if (temp != null) {
                    for (int i = 0; i < temp.size(); i++) {
                        dataMap.put(temp.get(i).getNum(), temp.get(i));
                    }
                    dataList.addAll(dataMap.values());
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        LogE(JSON.toJSONString(dataList));
    }

    /**
     *
     */
    public static void saveNewData(DbUtils dbUtils, Map<String, DBShopData> dataMap) {
        try {
            List<DBShopData> temp = new ArrayList<>();
            boolean is_update = true;
            if (!dbUtils.tableIsExist(DBShopData.class)) {
                is_update = true;
            } else {
                dbUtils.createTableIfNotExist(DBShopData.class);
                temp = dbUtils.findAll(Selector.from(DBShopData.class));
                if (temp != null && temp.size() > 0)
                    is_update = false;
            }
            temp = new ArrayList<>();
            if (is_update) {
                for (int i = 1; i <= 50; i++) {
                    DBShopData dbShopData = new DBShopData();
                    dbShopData.setNum("" + i);
                    dbShopData.setCount("0");
                    temp.add(dbShopData);
                    dataMap.put("" + i, dbShopData);
                }
                dbUtils.saveAll(temp);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    //修改或者保存
    public static void updataCntrOpt(String opt_id, String number, String type, DbUtils dbUtils) {
        if (opt_id != null && type != null && opt_id.length() > 0) {
            DBCntrOptEntity dbOptEntity = new DBCntrOptEntity();
            dbOptEntity.setOpt_id(opt_id);
            if (number != null)
                dbOptEntity.setNumber(number);
            dbOptEntity.setType(type);
            DBCntrOptEntity temp = null;
            try {
                temp = dbUtils.findFirst(Selector.from(DBCntrOptEntity.class).where("opt_id", "=", opt_id));
                if (temp != null) {
                    int temp_1 = Integer.parseInt(temp.getType());
                    int temp_2 = Integer.parseInt(type);
                    Utils.LogE("temp_1=" + temp_1 + " temp_2=" + temp_2);
                    if (temp_1 <= temp_2)
                        dbUtils.update(dbOptEntity, WhereBuilder.b("opt_id", "=", opt_id), "type");
                } else
                    dbUtils.save(dbOptEntity);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<DBCntrOptEntity> getloclCntrOpt(DbUtils dbUtils) {
        List<DBCntrOptEntity> temp = new ArrayList<>();
        try {
            temp = dbUtils.findAll(Selector.from(DBCntrOptEntity.class).where(WhereBuilder.b("type", "=", "0").or("type", "=", "4").or("type", "=", "2")));
        } catch (DbException e) {
            e.printStackTrace();
        }
//        Utils.LogE("temp.size()=" + temp.size());
        return temp;
    }

    //获取所有杯料个数
    public static int getcupSum(DBDeiveceData dbDeiveceData) {
        int cup_sum = 0;
        if (dbDeiveceData.getCup_1() != null && dbDeiveceData.getCup_1().length() > 0 && !dbDeiveceData.getCup_1().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_1(), 16));
        }
        if (dbDeiveceData.getCup_2() != null && dbDeiveceData.getCup_2().length() > 0 && !dbDeiveceData.getCup_2().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_2(), 16));
        }
        if (dbDeiveceData.getCup_3() != null && dbDeiveceData.getCup_3().length() > 0 && !dbDeiveceData.getCup_3().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_3(), 16));
        }
        if (dbDeiveceData.getCup_4() != null && dbDeiveceData.getCup_4().length() > 0 && !dbDeiveceData.getCup_4().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_4(), 16));
        }
        if (dbDeiveceData.getCup_5() != null && dbDeiveceData.getCup_5().length() > 0 && !dbDeiveceData.getCup_5().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_5(), 16));
        }
        if (dbDeiveceData.getCup_6() != null && dbDeiveceData.getCup_6().length() > 0 && !dbDeiveceData.getCup_6().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_6(), 16));
        }
        if (dbDeiveceData.getCup_7() != null && dbDeiveceData.getCup_7().length() > 0 && !dbDeiveceData.getCup_7().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_7(), 16));
        }
        if (dbDeiveceData.getCup_8() != null && dbDeiveceData.getCup_8().length() > 0 && !dbDeiveceData.getCup_8().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_8(), 16));
        }
        if (dbDeiveceData.getCup_9() != null && dbDeiveceData.getCup_9().length() > 0 && !dbDeiveceData.getCup_9().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_9(), 16));
        }
        if (dbDeiveceData.getCup_10() != null && dbDeiveceData.getCup_10().length() > 0 && !dbDeiveceData.getCup_10().equals("ff")) {
            cup_sum += (Integer.parseInt(dbDeiveceData.getCup_10(), 16));
        }
        LogE("cup_sum=" + cup_sum);
        return cup_sum;
    }
    public static void getLocation(Context context) {
        //定位服务
        Intent intent = new Intent(context, LocationService.class);
        context.startService(intent);
    }


    /**
     * 从Assets中拷贝文件
     *
     * @param context
     * @param fileName
     * @param file
     * @return
     */
    public static boolean copyApkFromAssets(Context context, String fileName, File file) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }


}
