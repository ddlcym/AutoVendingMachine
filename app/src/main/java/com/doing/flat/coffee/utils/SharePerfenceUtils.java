package com.doing.flat.coffee.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePerfenceUtils {
    private static SharePerfenceUtils sharePerfenceUtils;
    private SharedPreferences sharedPreferences;

    private SharePerfenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences("logindata", 0);
    }

    public static SharePerfenceUtils getInstance(Context context) {
        if (sharePerfenceUtils == null) {
            sharePerfenceUtils = new SharePerfenceUtils(context);
        }
        return sharePerfenceUtils;
    }

    public String getValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setValue(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public void setAtitude(String atitude) {
        setValue("atitude", atitude);
    }

    public String getAtitude() {
        return getValue("atitude");
    }

    public void setLongitude(String longitude) {
        setValue("longitude", longitude);
    }

    public String getLongitude() {
        return getValue("longitude");
    }


    public void setHash(String hash) {
        setValue("hash", hash);
    }

    public String getHash() {
        return getValue("hash");
    }

    public void setHashDownloadCount(String HashDownloadCount) {
        setValue("HashDownloadCount", HashDownloadCount);
    }

    public String getHashDownloadCount() {
        return getValue("HashDownloadCount");
    }


    public void setVideoWidth(String videoWidth) {
        setValue("videoWidth", videoWidth);
    }

    public String getVideoWidth() {
        return getValue("videoWidth");
    }

    public void setVideoHeight(String videoHeight) {
        setValue("videoHeight", videoHeight);
    }

    public String getVideoHeight() {
        return getValue("videoHeight");
    }

}
