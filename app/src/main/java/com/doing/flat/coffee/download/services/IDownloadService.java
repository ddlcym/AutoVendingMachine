package com.doing.flat.coffee.download.services;

/**
 * Created by Administrator on 2015/1/28.
 */
public interface IDownloadService {

    void startManage();

    void addTask(String url);

    void pauseTask(String url);

    void deleteTask(String url);

    void continueTask(String url);
}


