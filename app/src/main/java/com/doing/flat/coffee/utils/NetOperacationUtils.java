package com.doing.flat.coffee.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.fastjson.JSON;
import com.doing.flat.coffee.entity.UserSendData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

public class NetOperacationUtils {
    public static final int LOGIN_QQ = 2;
    public static final int LOGIN_SINA = 1;
    public static final int LOGIN_PHONE = 0;
    private static final String CLIENTTYPE = "1";
    private static final String app_ver = "1.0.0";

    private static AsyncHttpClient asyncHttpClient;

    public static void httpPostObject(Context context, String httpUrl,
                                      String operation, String opcode, Object data, AsyncHttpResponseHandler handler) {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }
//        if (isNeedLogin(operation)) {
//            if (SharePerfenceUtils.getInstance(context).getU_id().length() == 0) {//提示登录或者跳转到登录界面
//                Utils.LogE("need login");
//                return;
//            }
//        }
        UserSendData userSendData = new UserSendData();
//        userSendData.setOperation(operation);
        setBaseData(context, userSendData, opcode);

//        userSendData.setData(data);
        String json = JSON.toJSONString(userSendData);
//        Logger.json(operation, json);
        Utils.LogE(json);
        RequestParams params = new RequestParams();
        params.put("jsonstring", json);
        asyncHttpClient.post(httpUrl, params, handler);
    }

    public static void httpPostObjectWithOpt(Context context, String httpUrl,
                                             String operation, String opcode, Object data, AsyncHttpResponseHandler handler) {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }

        UserSendData userSendData = new UserSendData();
//        userSendData.setOperation(operation);
        setBaseData(context, userSendData, opcode);
//        userSendData.setOpcode(operation + "_" + System.currentTimeMillis() / 1000 + "_" + Math.random() * 100);
//        userSendData.setData(data);
        String json = JSON.toJSONString(userSendData);
        RequestParams params = new RequestParams();
        params.put("jsonstring", json);
        asyncHttpClient.post(httpUrl, params, handler);
    }


    public static void setBaseData(Context context, UserSendData userSendData, String opcode) {
//        userSendData.setOpcode(opcode);
//        userSendData.setU_id(SharePerfenceUtils.getInstance(context).getU_id());
//        if (userSendData.getU_id().length() == 0) {
//            userSendData.setU_id("0");
//        }
//        userSendData.setSession(SharePerfenceUtils.getInstance(context)
//                .getSession());
//        userSendData.setClient_type(CLIENTTYPE);
//        userSendData.setApp_id(SharePerfenceUtils.getInstance(context)
//                .getApp_id());
//        userSendData.setApp_ver(app_ver);

        try {
            if (context != null) {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    PackageInfo packageInfo = packageManager.getPackageInfo(
                            context.getPackageName(), 0);
                    if (packageInfo != null) {
//                        userSendData.setApp_ver(packageInfo.versionName);
                    }
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是WiFi网络
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
