package com.doing.flat.coffee.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.doing.flat.coffee.utils.SharePerfenceUtils;

public class LocationService extends Service {

    private String lat, lon;
    private Context context;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        context = this;
        startGaoDeLocation();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public AMapLocationClient mLocationClient = null;

    private void startGaoDeLocation() {
//// 初始化定位，只采用网络定位
//        mLocationManagerProxy = LocationManagerProxy.getInstance(context);
//        mLocationManagerProxy.setGpsEnable(false);
//        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
//        // 在定位结束后，在合适的生命周期调用destroy()方法
//        // 其中如果间隔时间为-1，则定位只定一次,
//        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
//        mLocationManagerProxy.requestLocationData(
//                LocationManagerProxy.NETWORK_PROVIDER, 2 * 1000, 15, aMapLocationListener);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(aMapLocationListener);
        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);

        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationOption.setInterval(1000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                // 定位成功回调信息，设置相关消息
                if (amapLocation.getLatitude() != 0 && amapLocation.getLongitude() != 0) {
                    lat = amapLocation.getLatitude() + "";
                    lon = amapLocation.getLongitude() + "";
                    SharePerfenceUtils.getInstance(context).setAtitude(lat);
                    SharePerfenceUtils.getInstance(context).setLongitude(lon);
                    Log.e("lat=", lat + "  lon=" + lon);
                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
//                    mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
//                    Intent intent = new Intent(DBCacheConfig.ACTION_BUSINESS_CARD_GROUPING_LOC_COMPLETE);
//                    sendBroadcast(intent);
                    stopSelf();
                }
            } else {
//                Intent intent = new Intent(DBCacheConfig.ACTION_BUSINESS_CARD_GROUPING_LOC_ERR);
//                sendBroadcast(intent);
                Log.e("AmapErr", "Location ERR:" + amapLocation.getErrorCode());
            }
        }


    };
}
