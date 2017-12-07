/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api.sample;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.doing.flat.coffee.R;
import com.doing.flat.coffee.xutils.DbUtils;
import com.doing.flat.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.doing.flat.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.doing.flat.nostra13.universalimageloader.core.DisplayImageOptions;
import com.doing.flat.nostra13.universalimageloader.core.ImageLoader;
import com.doing.flat.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.doing.flat.nostra13.universalimageloader.core.assist.FailReason;
import com.doing.flat.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.doing.flat.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.doing.flat.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.bugly.crashreport.CrashReport;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class Application extends android.app.Application {
    private static Application CoffeeApp;
    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;
    public DbUtils db;
    private final String BUGLY = "6af5c04e4b";

    public DisplayImageOptions reply_photo_options;

    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
    static final List<String> displayedHead = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public void onCreate() {
        super.onCreate();
        CoffeeApp = this;
        initDb();
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

        //错误信息上传
//        CrashHandler.getInstance().init(this);
        CrashReport.initCrashReport(getApplicationContext(), BUGLY, true);//腾讯bugly
    }

    public DbUtils getDb() {
        return db;
    }

    public static Application getInstance() {
        return CoffeeApp;
    }

    private void initDb() {
        DbUtils.DaoConfig config = new DbUtils.DaoConfig(this);
        config.setDbName("coffee"); //db名
        config.setDbVersion(8);  //db版本
        db = DbUtils.create(config);//db还有其他的一些构造方法，比如含有更新表版本的监听器的
        db.configAllowTransaction(true);
    }

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
//			SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
//			String path = sp.getString("DEVICE", "");
//			int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));
//            String path = "/dev/smd8";
//            String path = "";

            String path = "/dev/ttyS2";
            int baudrate = 115200;
//            String path = "";
//            int baudrate = 9600;

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

}
