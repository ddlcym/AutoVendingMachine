package com.doing.flat.coffee.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.doing.flat.coffee.entity.ADInfo;
import com.doing.flat.coffee.entity.DBDrinksData;
import com.doing.flat.coffee.entity.DBRuleData;
import com.doing.flat.coffee.entity.Drinks_array;
import com.doing.flat.coffee.xutils.DbUtils;
import com.doing.flat.coffee.xutils.db.sqlite.Selector;
import com.doing.flat.coffee.xutils.db.sqlite.WhereBuilder;
import com.doing.flat.coffee.xutils.exception.DbException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android_serialport_api.sample.Application;

/**
 * Created by doing_hqg on 2016/8/27.
 */
public class RuleUtils {
    // 下载广告策略
    public static void DownloadRule(final Context context) {
        final DbUtils db = Application.getInstance().getDb();
        DBRuleData dbRuleData = null;
        List<ADInfo> adInfo = null;
        try {
            dbRuleData = db.findFirst(Selector.from(DBRuleData.class).where("status", "=", "2"));
            if (dbRuleData != null) {
                dbRuleData.setResource_array(JSON.parseArray(dbRuleData.getResource_array_string(), String.class));
                adInfo = db.findAll(Selector.from(ADInfo.class).where("resource_id", "in", dbRuleData.getResource_array())
                        .and("is_success", "==", "-1"));

//                Utils.LogE("adInfo"+adInfo.size()+"   adInfo="+JSON.toJSONString(adInfo));
            }
            if (adInfo != null && adInfo.size() > 0) {
                httpGetFile(dbRuleData.getRule_id(), db, context);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载文件本地
     *
     * @return
     */
    public static void httpGetFile(final String rule_id, final DbUtils db, final Context context) {
        ADInfo adInfo = null;
        try {
            adInfo = db.findFirst(Selector.from(ADInfo.class)
                    .where("is_success", "==", "-1").orderBy("id", false));
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (adInfo == null)//没有需要下载时
        {
            try {
                DBRuleData dbRuleData = db.findFirst(Selector.from(DBRuleData.class).where("rule_id", "=", rule_id));
                if (dbRuleData != null) {
                    List<ADInfo> temp = db.findAll(Selector.from(ADInfo.class).where("resource_id", "in", dbRuleData.getResource_array())
                            .and("is_success", "==", "1"));
                    if (temp != null && temp.size() > 0) {
                        dbRuleData.setStatus("3");
                        dbRuleData.setStatus_type("0");
                        db.update(dbRuleData, WhereBuilder.b("rule_id", "=", dbRuleData.getRule_id()), "status", "status_type");
                    } else {
                        dbRuleData.setStatus("0");
                        dbRuleData.setStatus_type("0");
                        db.update(dbRuleData, WhereBuilder.b("rule_id", "=", dbRuleData.getRule_id()), "status", "status_type");
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            return;
        }
        Utils.LogE("adInfo=" + adInfo.getResource_id());
        AsyncHttpClient client = new AsyncHttpClient();
        String[] allowedContentTypes = new String[]{".*"};
        final String titlePath = FileOperationUtil.getLocalVideoPath(context.getApplicationContext());
        final String videoPath = titlePath + "/" + adInfo.getFile_key();
        String url = adInfo.getFile_url();
        String number = adInfo.getDownload_number();
        String is_success = adInfo.getIs_success();
        if (number != number && number.length() > 0) {
            number = Integer.parseInt(number) + 1 + "";
            if (Integer.parseInt(number) >= 3) {
                is_success = "0";
            }
        } else {
            number = "1";
        }
        adInfo.setDownload_number(number);
        adInfo.setIs_success(is_success);

        //保存下载几次
        try {
            db.update(adInfo, WhereBuilder.b("resource_id", "=", adInfo.getResource_id()), "download_number", "is_success", "pathUrl");
        } catch (DbException e) {
            e.printStackTrace();
        }

        final ADInfo finalAdInfo = adInfo;
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] data) {
                Utils.LogE("onSuccess");
                while (!saveVedio(db, titlePath, videoPath, data, finalAdInfo)) {
                }
                httpGetFile(rule_id, db, context);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                Utils.LogE("onFailure");
//                httpGetFile(rule_id,db, context);
            }
        });
    }

    //删除过期的广告策略包
    public static void deleteRule() {
        final DbUtils db = Application.getInstance().getDb();
        List<DBRuleData> dbRuleData = null;
        try {
            dbRuleData = db.findAll(Selector.from(DBRuleData.class).and("rule_type", "=", "0"));
//            long currenTime = System.currentTimeMillis() / 1000;
            if (dbRuleData != null && dbRuleData.size() > 0) {
                for (int i = 0; i < dbRuleData.size(); i++) {
//                    if (currenTime < getTime(dbRuleData.get(i).getEnd_date(), dbRuleData.get(i).getEnd_time())) {
                    dbRuleData.get(i).setResource_array(JSON.parseArray(dbRuleData.get(i).getResource_array_string(), String.class));
                    List<ADInfo> temp = db.findAll(Selector.from(ADInfo.class)
                            .where("resource_id", "in", dbRuleData.get(i).getResource_array()));
                    if (temp != null && temp.size() > 0) {
                        for (int jj = 0; jj < temp.size(); jj++) {
                            String path = temp.get(jj).getPathUrl();
                            List<ADInfo> tempSelect= db.findAll(Selector.from(ADInfo.class).where("pathUrl","=",path));
                            if (tempSelect!=null&&tempSelect.size()>1)
                                break;
                            if (path != null) {
                                File file = new File(path);
                                if (file.exists())
                                    file.delete();
                            }
                            db.delete(ADInfo.class, WhereBuilder.b("resource_id", "=", temp.get(jj).getResource_id()));
                        }
                    }
//                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public static long getTime(String end_date, String end_time) {
        String timeTemp = "";
        timeTemp = StringUtils.long2String(Long.parseLong(end_date), "yyyy-MM-dd");
        timeTemp = timeTemp + " " + end_time;
        return StringUtils.getStringToTime(timeTemp, "yyyy-MM-dd HH:mm");
    }

    //零点
    public static long getTimeZero(String end_date) {
        String timeTemp = "";
        timeTemp = StringUtils.long2String(Long.parseLong(end_date), "yyyy-MM-dd");
        timeTemp = timeTemp + " " + "00:00:00";
        return StringUtils.getStringToTime(timeTemp, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 保存视频
     *
     * @param tempPath
     * @param videoPath
     * @param data
     * @return
     */
    public static boolean saveVedio(DbUtils db, String tempPath, String videoPath, byte[] data, ADInfo adInfo) {
        boolean ok = false;
        OutputStream os;
        //判断hash码是否正确
        String hash = null;
        boolean isSava = false;
        try {
            if (data != null) {
                hash = Utils.byteToString(HashUtil.sha1(data));
                if (hash != null) {
                    hash = hash.replace(" ", "");//去空
                    Utils.LogE("hash=" + hash + "   file_hash=" + adInfo.getFile_hash());
                    if (hash.equals(adInfo.getFile_hash())) {
                        isSava = true;
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (!isSava)
            return false;

        Utils.LogE("isSava=" + isSava);

        //保存
        try {
            File file = new File(videoPath);
            if (file.exists())
                file.delete();
            // 创建文件
            file.createNewFile();
            os = new FileOutputStream(file);
            os.write(data);
            os.close();
            ok = true;
            adInfo.setPathUrl(videoPath);
            adInfo.setIs_success("1");
            db.update(adInfo, WhereBuilder.b("resource_id", "=", adInfo.getResource_id()), "pathUrl", "is_success");
            while (getFolderSize(new File(tempPath)) > 500) {//判断并删除之前的视频文件
                ADInfo entity = db.findFirst(ADInfo.class);
                if (FileOperationUtil.isFileExist(entity.getPathUrl())) {
                    FileOperationUtil.delFile(entity.getPathUrl());
                }
                db.delete(entity);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return ok;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static long getFolderSize(File file) {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size / 1048576;
    }

    public static Drinks_array getDrinksArray(DBDrinksData dbDrinksData,String type)
    {

        if (dbDrinksData.getDrinks_array()!=null&&dbDrinksData.getDrinks_array().size()>0) {
            for (int i = 0; i < dbDrinksData.getDrinks_array().size(); i++) {
                if (dbDrinksData.getDrinks_array().get(i).getOpt_type().equals(type)) {
                    return dbDrinksData.getDrinks_array().get(i);
                }
            }
        }
        return null;
    }

}
