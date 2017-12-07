package com.doing.flat.coffee.utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.doing.flat.coffee.R;
import com.doing.flat.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.doing.flat.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.doing.flat.nostra13.universalimageloader.core.DisplayImageOptions;
import com.doing.flat.nostra13.universalimageloader.core.ImageLoader;
import com.doing.flat.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.doing.flat.nostra13.universalimageloader.core.assist.FailReason;
import com.doing.flat.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.doing.flat.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.doing.flat.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

/**
 * Created by Administrator on 2015/3/13.
 */
public class App extends Application {
    private static App DoingApp;
    public DisplayImageOptions reply_photo_options;

    private ArrayList<String> faceArray = new ArrayList<>();
    private HashMap<String, Integer> faceSign = new HashMap<>();

    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
    static final List<String> displayedHead = Collections.synchronizedList(new LinkedList<String>());

    private Context context;

    private ImageLoadingListener firstImageLoadingListener = new ImageLoadingListener() {

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            ImageView imageView = (ImageView) view;
            // 是否第一次显示
            boolean firstDisplay = !displayedHead.contains(imageUri);
            if (firstDisplay) {
                imageView.setImageResource(R.drawable.shape_head_onload);
            }
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
            boolean firstDisplay = !displayedHead.contains(imageUri);
            if (firstDisplay) {
                displayedHead.add(imageUri);
            }
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };

    private ImageLoadingListener firstHeadLoadingListener = new ImageLoadingListener() {

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            ImageView imageView = (ImageView) view;
            // 是否第一次显示
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                imageView.setImageResource(R.drawable.shape_photo_onload);
            }
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                displayedImages.add(imageUri);
            }
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };

    public ImageLoadingListener getImageLoadingListener() {
        return firstImageLoadingListener;
    }

    public ImageLoadingListener getHeadLoadingListener() {
        return firstHeadLoadingListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DoingApp = this;
        context = this;
        reply_photo_options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.shape_photo_onload)
                .showImageForEmptyUri(R.drawable.shape_photo_onload)
                .showImageOnFail(R.drawable.shape_photo_onload)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

        //
        initImageLoader(getApplicationContext());
        initMap();
    }


    public static App getInstance() {
        return DoingApp;
    }


    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new WeakMemoryCache())
                .memoryCache(new UsingFreqLimitedMemoryCache(100 * 1024 * 1024))
                .memoryCacheSize(100 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(1024 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }


    private void initMap() {
        for (int i = 0; i < faceArray.size(); i++) {
            faceSign.put(faceArray.get(i), i);
        }
    }

    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            String path = getResources().getString(R.string.DEVICE);
            int baudrate = Integer.parseInt(getResources().getString(R.string.BAUDRATE));
            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
