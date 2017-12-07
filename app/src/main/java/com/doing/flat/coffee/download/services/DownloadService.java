package com.doing.flat.coffee.download.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.doing.flat.coffee.download.utils.MyIntents;


public class DownloadService extends Service {

    private DownloadManager mDownloadManager;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = new DownloadManager(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
        if (intent != null && intent.hasExtra(MyIntents.TYPE)) {
            int type = intent.getIntExtra(MyIntents.TYPE, -1);
            String url;
            url = intent.getStringExtra(MyIntents.URL);
            if (type == MyIntents.Types.ADD)
                if (mDownloadManager.hasTask(url)) {
                    mDownloadManager.pauseTask(url);//先暂停
                    type = MyIntents.Types.CONTINUE;//再开始下载
                }
            switch (type) {
                case MyIntents.Types.ADD:
                    Log.e("url=", url);
                    if (!TextUtils.isEmpty(url) && !mDownloadManager.hasTask(url)) {
                        mDownloadManager.addTask(url);
                    }
                    break;
                case MyIntents.Types.CONTINUE:
//                    url = intent.getStringExtra(MyIntents.URL);
                    Log.e("url22=", url);
                    if (!TextUtils.isEmpty(url)) {
                        mDownloadManager.continueTask(url);
                    }
                    break;

                case MyIntents.Types.PAUSE:
//                    url = intent.getStringExtra(MyIntents.URL);
                    if (!TextUtils.isEmpty(url)) {
                        mDownloadManager.pauseTask(url);
                    }
                    break;

                default:
                    break;

            }
        }

    }


}
