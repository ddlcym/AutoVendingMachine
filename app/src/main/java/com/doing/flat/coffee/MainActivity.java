package com.doing.flat.coffee;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.doing.flat.coffee.ble.BLEService;
import com.doing.flat.coffee.ble.MTBeacon;
import com.doing.flat.coffee.ble.SendbleEntity;
import com.doing.flat.coffee.ui.ArcProgress;
import com.doing.flat.coffee.ui.WaterWaveView;
import com.doing.flat.coffee.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends Activity {
    Window window;
    private WaterWaveView waterWaveView;
    private LinearLayout llHot, llCold;
    private ArcProgress arcprogress, arcprogress1;
    private boolean hotType = false, coldType = false, lockType = true;
    private ImageView image_hot, image_cold, image_lock;
    private ImageView image_cold_circle, image_hot_circle;
    private ImageView text_set;
    private long firstTime = 0;
    private SharedPreferences sharedPreferences;
    private SendbleEntity entity;//数据以及设备状态


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        window.setAttributes(params);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("sysinfo", 0);

        entity = new SendbleEntity();
//        entity.setDerail_switch("1");//开关机
//        entity.setMake_switch("1");//制水开关
//        entity.setWind("1");//风速
//        entity.setUv("1");////NV
        entity.setRefrigeration("0");//////制冷
        entity.setHeat("0");//加热
//        entity.setHeat_switch("0");//热水开关
//        entity.setCold_switch("0");//冷水开关

        waterWaveView = (WaterWaveView) findViewById(R.id.waterWaveView);
        image_hot = (ImageView) findViewById(R.id.image_hot);
        image_cold = (ImageView) findViewById(R.id.image_cold);
        image_lock = (ImageView) findViewById(R.id.image_lock);
        image_cold_circle = (ImageView) findViewById(R.id.image_cold_circle);
        image_hot_circle = (ImageView) findViewById(R.id.image_hot_circle);
        text_set = (ImageView) findViewById(R.id.text_set);
        arcprogress = (ArcProgress) findViewById(R.id.arcprogress);
        arcprogress1 = (ArcProgress) findViewById(R.id.arcprogress1);
        arcprogress.setUnfinishedStrokeColor(getResources().getColor(R.color.colorW));
        arcprogress.setSuffixText("");
        arcprogress.setTextSize(Utils.dp2px(getResources(), getResources().getDimension(R.dimen.main_size)));
        arcprogress1.setUnfinishedStrokeColor(getResources().getColor(R.color.colorW));
        arcprogress1.setSuffixText("");
        arcprogress1.setTextSize(Utils.dp2px(getResources(), getResources().getDimension(R.dimen.main_size)));

        setArcProgress(0, 0);
        setArcProgress(0, 1);

        //停止波动
//        waterWaveView.setMoveSpeedByProgress(0);
//        waterWaveView.setType(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }).start();


        llHot = (LinearLayout) findViewById(R.id.ll_hot);
        llHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lockType)
                    return;
                if (coldType)
                    return;
                if (hotType) {
//                    image_hot.setImageResource(R.drawable.hot_water_selected);
                    image_hot.setVisibility(View.VISIBLE);
                    image_hot_circle.setVisibility(View.INVISIBLE);
                    hotType = false;
//                    entity.setHeat_switch("1");//热水开关
//                    entity.setCold_switch("0");//冷水开关
                    senWrite(entity, "ff ff ff ff");
                } else {
//                    image_hot.setImageResource(R.drawable.hot_water_unselected);
                    image_hot.setVisibility(View.INVISIBLE);
                    image_hot_circle.setVisibility(View.VISIBLE);
//                    entity.setHeat_switch("0");//热水开关
//                    entity.setCold_switch("0");//冷水开关
                    senWrite(entity, "ff ff ff ff");
                    hotType = true;

                }
                setWaterWaveView(hotType);
            }
        });
        llCold = (LinearLayout) findViewById(R.id.ll_cold);
        llCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hotType)
                    return;
                if (coldType) {
//                    image_cold.setImageResource(R.drawable.cold_water_selected);
                    image_cold.setVisibility(View.VISIBLE);
                    image_cold_circle.setVisibility(View.INVISIBLE);
                    coldType = false;
//                    entity.setHeat_switch("0");//热水开关
//                    entity.setCold_switch("1");//冷水开关
                    senWrite(entity, "ff ff ff ff");
                } else {
//                    image_cold.setImageResource(R.drawable.cold_water_unselected);
                    image_cold.setVisibility(View.INVISIBLE);
                    image_cold_circle.setVisibility(View.VISIBLE);
//                    entity.setHeat_switch("0");//热水开关
//                    entity.setCold_switch("0");//冷水开关
                    senWrite(entity, "ff ff ff ff");
                    coldType = true;

                }
                setWaterWaveView(coldType);
            }
        });

        image_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lockType) {
                    lockType = false;
                    image_lock.setImageResource(R.drawable.lock_unselected);
                    image_hot.setImageResource(R.drawable.hot_water_lock_no);
                    image_hot.setVisibility(View.VISIBLE);
                    image_hot_circle.setVisibility(View.INVISIBLE);
                    hotType = false;
                    setWaterWaveView(false);
                } else {
                    lockType = true;
                    image_lock.setImageResource(R.drawable.lock_selected);
                    image_hot.setImageResource(R.drawable.hot_water_selected);
                }
            }
        });

        text_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(intent);
            }
        });

        //开启蓝牙
        OpenBuild();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            waterWaveView.setMoveSpeedByProgress(0);
            waterWaveView.setHeightOffsetByProgress(100);
            return false;
        }
    });


    @Override
    protected void onRestart() {
        super.onRestart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    //设置滤网滤芯
    private void setArcProgress(int value, int type) {
        if (type == 0) {
            int temp = (int) (Math.random() * 100);
            arcprogress.setProgress(temp);
            if (temp <= 20) {
                //字的颜色
                arcprogress.setTextColor(getResources().getColor(R.color.colorred));
                arcprogress.setFinishedStrokeColor(getResources().getColor(R.color.colorred));
            } else {
                arcprogress.setTextColor(getResources().getColor(R.color.colorblue));
                arcprogress.setFinishedStrokeColor(getResources().getColor(R.color.colorblue));
            }
        } else {
            int temp = (int) (Math.random() * 100);
            arcprogress1.setProgress(temp);
            if (temp <= 20) {
                //字的颜色
                arcprogress1.setTextColor(getResources().getColor(R.color.colorred));
                arcprogress1.setFinishedStrokeColor(getResources().getColor(R.color.colorred));
            } else {
                arcprogress1.setTextColor(getResources().getColor(R.color.colorblue));
                arcprogress1.setFinishedStrokeColor(getResources().getColor(R.color.colorblue));
            }
        }
    }

    private void setWaterWaveView(boolean type) {
        if (type) {
//            image_lock.setImageResource(R.drawable.lock_selected);
            waterWaveView.setMoveSpeedByProgress(50);
        } else {
//            image_lock.setImageResource(R.drawable.lock_unselected);
            waterWaveView.setMoveSpeedByProgress(0);
        }
    }


    //下面的都是蓝牙接收数据的代码
    private Intent intent;
    private BluetoothDevice Mydevice;
    private BLEService mBLEService;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private boolean isCloseBuild = true;
    private BluetoothAdapter mBluetoothAdapter = null;
    private List<MTBeacon> scan_devices = new ArrayList<MTBeacon>();
    private List<MTBeacon> scan_devices_dis = new ArrayList<MTBeacon>();
    private int strTemp;
    private DecimalFormat df = new DecimalFormat("0.0");
    private DecimalFormat df_weight = new DecimalFormat("0.0");
    private String show = "双击退出";
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean read_name_flag = false;
    private boolean connect_flag = false;
    private boolean bind_flag = false;
    private int uuidLocation = 2;
    private int uuidSendLocation = 6;
    private int uuidReceiveLocation = 6;
    private boolean ReceiverState = true;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private double floatweight;
    private String[] onReceiv;
    private boolean UploadState_fat = false;//是否把脂肪数据显示到界面
    private boolean UploadState_weight = false;//处理显示脂肪数据的时候，出现临时数据

    private BLEService.LocalBinder binder;

    private BluetoothGattCharacteristic MyBluetoothGattCharacteristic;
    private BluetoothGattCharacteristic Sendcharacteristic;
    //    private BluetoothGatt mBluetoothGatt;
    private boolean bl_Sendsearch_timer = true;
    //false表示不连接设备
    private boolean Bldevives = true;

    private int SDKVersion = 21;

    public void OpenBuild() {
        intent = new Intent(getApplicationContext(), BLEService.class);
        if (mBLEService != null)
            return;
        Log.e("mBLEService", "1");
        final BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext()
                .getSystemService(Context.BLUETOOTH_SERVICE);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mBluetoothAdapter = bluetoothManager.getAdapter();
//                mBluetoothAdapter.enable();
//                Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(mIntent, 1);
//            }
//        }).start();

        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothAdapter.enable();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Connect();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    getApplicationContext().getApplicationContext().bindService(intent, connection,
                            Context.BIND_AUTO_CREATE);
                    Log.e("mBLEService", "2");
                    setBroadcastReceiver(); // 设置广播监听
                    setBroadcastReceiver2(); // 设置接受数据广播监听
//                    ReceiverState = true;
//                    if (SDKVersion <= 20) {
//                        ////        注册广播
//                        setBroadcastReceiver();
//                        setBroadcastReceiver2();
//                        ReceiverState = true;
//                    }
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "不允许蓝牙开启", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }


    //连接蓝牙
    private void Connect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //搜索蓝牙设备
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    // 检查是否是搜索过的设备，并且更新
//                    value = byteToString(scanRecord);
//                    Log.e("device,Bldevives=", device.getName() + "");
                    if (device.getName() != null)
                        if (device.getName().equals("bde spp dev")) {
                            Log.e("device,Bldevives=", Bldevives + "");
                            if (!Bldevives)
                                return;
                            Log.e("device,name", device.getName() + "   device.toString()=" + device.toString() + "   device.getUuids()=" + device.getUuids()
                                    + "   device.getBluetoothClass()=" + device.getBluetoothClass() + "   device.hashCode()=" + device.hashCode() + "   device.getBondState()=" + device.getBondState()
                                    + "   ");

//
                            Mydevice = device;
                            if (Mydevice == null)
                                return;
//                            isCloseBuild=false;
                            connect_flag = true;
                            if (SDKVersion > 20) {
                                unregisterReceiver(bluetoothReceiver1);
                                unregisterReceiver(bluetoothReceiver);
                                // 注册广播
                                setBroadcastReceiver();
                                setBroadcastReceiver2();
                                ReceiverState = true;
                            } else {
//                                //先要结束上一个广播监听，要不然就会出现连接不上的情况
//                                // 注册广播
//                                setBroadcastReceiver();
//                                setBroadcastReceiver2();
                                mBLEService.conectBle(Mydevice);
                            }
                            Log.e("device11111", device.getAddress());
                            new readNameThread().start();
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        }
                    return;
                }
            };
        } else {
            Toast.makeText(getApplicationContext(), "蓝牙不是4.0", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler ConnectedHandler = new Handler(new Handler.Callback() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public boolean handleMessage(Message message) {
            //先要结束上一个广播监听，要不然就会出现连接不上的情况
            if (mBluetoothGattCharacteristic != null)
                mBLEService.mBluetoothGatt.setCharacteristicNotification(
                        mBluetoothGattCharacteristic, false);
            if (MyBluetoothGattCharacteristic != null)
                mBLEService.mBluetoothGatt.setCharacteristicNotification(
                        MyBluetoothGattCharacteristic, false);
            if (SDKVersion > 20) {
                {
                    //先要结束上一个广播监听，要不然就会出现连接不上的情况
                    if (mBLEService.isConnected())
                        mBLEService.disConectBle();
                }
            }
//            }
            MyBluetoothGattCharacteristic = null;
            Sendcharacteristic = null;
            Mydevice = null;
            connect_flag = true;
            bl_Sendsearch_timer = false;
            if (mGattCharacteristics != null)
                mGattCharacteristics.clear();
            mBluetoothGattCharacteristic = null;
            return false;
        }
    });


    // Code to manage Service lifecycle.
    //// 开始扫描设备
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                binder = (BLEService.LocalBinder) service;
                Log.e("mBLEService", "mBLEService");
                mBLEService = binder.getService();
                if (mBLEService.initBle()) {
                    if (!mBLEService.mBluetoothAdapter.isEnabled()) {
                        final Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        scanBle(); // 开始扫描设备
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "蓝牙不是4.0", Toast.LENGTH_SHORT).show();
            }

        }
    };

    // 读取线程
    private class readNameThread extends Thread {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            super.run();
            Log.e("services.size-->", "000000" + " connect_flag=" + connect_flag);
            try {
                //for (int i = 0; i < 10; i++) {
                while (true) {
//                    if (!connect_flag)
                    isCloseBuild = true;
                    connect_flag = false;
                    if (SDKVersion > 20) {
                        mBLEService.conectBle(Mydevice);
                    }
                    for (int j = 0; j < 50; j++) {
                        if (connect_flag) {
                            break;
                        }
                        sleep(100);
                        Log.e("services.size-->", "jjjjjjjjj" + " j=" + j);
                    }
                    if (connect_flag) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("services.size-->" + "111111");
            read_name_flag = false; // 读取设备名
            List<BluetoothGattService> services = null;
            if (mBLEService == null) {
//                new readNameThread().start();
                connect_flag = false;
                return;
            }
            if (mBLEService.mBluetoothGatt == null) {
//                new readNameThread().start();
                connect_flag = false;
                return;
            }
            services = mBLEService.mBluetoothGatt
                    .getServices();

            System.out.println("services.size-->" + "22222222");
            if (services.size() == 0) {
                System.out.println("services.size-->" + "3333333" + "   (Mydevice.getBondState()=");
                if (Mydevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    ConnectedHandler.sendEmptyMessage(0);
                }
                return;
            }
            String uuid;
            int uuidloca = 0;
            uuidSendLocation = 9;
            uuidReceiveLocation = 9;
            if (services.size() > 0)
                for (BluetoothGattService service : services) {
                    List<BluetoothGattCharacteristic> gattCharacteristics = new ArrayList<>();

                    gattCharacteristics.addAll(service
                            .getCharacteristics());
                    if (gattCharacteristics.size() == 0) {
                        uuidloca++;
                        continue;
                    }
                    if (service.getUuid().equals(BLEService.SERVIE_UUID)) {
                        uuidLocation = uuidloca;
                    }
                    uuidloca++;
                    int uuidloca2 = 0;
                    ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();
                    if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            charas.add(gattCharacteristic);
                            Log.e("gattCharacteristic=", gattCharacteristic.getUuid() + "");
                            if (gattCharacteristic.getUuid().equals(BLEService.SERVIE_UUID_1)) {
                                uuidSendLocation = uuidloca2;
                            }
                            if (gattCharacteristic.getUuid().equals(BLEService.SERVIE_UUID_2)) {
                                uuidReceiveLocation = uuidloca2;
                            }
                            uuidloca2++;
                        }
                        mGattCharacteristics.add(charas);
                    }
                }
            //判断ffc3通道是否获取到，否则重新查找
            if (mBLEService != null)
                if (mBLEService.mBluetoothGatt != null) {
                    List<android.bluetooth.BluetoothGattService> TempServices = new ArrayList<>();
                    TempServices.addAll(mBLEService.mBluetoothGatt
                            .getServices());
                    if (TempServices != null) {
                        if (TempServices.size() > uuidLocation) {
                            if (TempServices.get(uuidLocation)
                                    .getCharacteristics().size() > 1) {
                                //打开蓝牙FFc4通道
                                writeLlsAlertLevel1(0);
//                                senWrite(entity, "ff ff ff ff");
                            } else {
                                Log.e("....readNameThread11", services.size() + "");
                                new readNameThread().start();
                            }
                        } else {
                            Log.e("....readNameThread22", services.size() + "");
                            new readNameThread().start();
                        }
                    }
                }

        }
    }

    byte[] user_sendDivData;

    //打开通道4

    private void writeLlsAlertLevel1(int iAlertLevel) {

        Log.e("bb", "   characteristic" + mBLEService.mBluetoothGatt.getServices().get(uuidLocation).getCharacteristics().size() + "     uuidReceiveLocation=" + uuidReceiveLocation);
        if (mBLEService.mBluetoothGatt.getServices().get(uuidLocation).getCharacteristics().size() >= uuidReceiveLocation) {
            BluetoothGattCharacteristic characteristic = mBLEService.mBluetoothGatt
                    .getServices().get(uuidLocation)
                    .getCharacteristics().get(uuidReceiveLocation);
            final int charaProp = characteristic.getProperties();
            Log.e("bb", characteristic.getProperties() + "   characteristic" + characteristic.getUuid());
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                if (mBluetoothGattCharacteristic != null) {
                    mBLEService.mBluetoothGatt.setCharacteristicNotification(
                            mBluetoothGattCharacteristic, false);
                    mBluetoothGattCharacteristic = null;
//                        Log.e("mNotifyCharacteristic111", "null");
                }
                if (mBLEService.mBluetoothGatt != null)
                    mBLEService.mBluetoothGatt.readCharacteristic(characteristic);

            }
            if (characteristic != null) {
                mBluetoothGattCharacteristic = characteristic;
                mBLEService.mBluetoothGatt.setCharacteristicNotification(
                        mBluetoothGattCharacteristic, true);
                if (mBluetoothGattCharacteristic != null) {
                    if (mBluetoothGattCharacteristic.getProperties() > 0) {
                        BluetoothGattDescriptor descriptor = mBluetoothGattCharacteristic
                                .getDescriptor(UUID
                                        .fromString("00002902-0000-1000-8000-00805f9b34fb"));
                        if (descriptor != null) {
                            descriptor
                                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            boolean descriptorBolean = false;
                            if (mBLEService != null)
                                if (mBLEService.mBluetoothGatt != null)
                                    descriptorBolean = mBLEService.mBluetoothGatt.writeDescriptor(descriptor);
                            Log.e("bb", descriptor.toString() + "   descriptor    descriptorBolean=" + descriptorBolean + "  descriptor=" + descriptor.getUuid());
                        } else {
//                            new readNameThread().start();
                        }
                    }
                }
                UploadState_fat = true;
            } else {
//                new readNameThread().start();
            }
        } else {
//            new readNameThread().start();
        }
    }

    //控制设备
    private void senWrite(SendbleEntity entity, String Sendevice_id) {
        if (mBLEService == null) {
            Toast.makeText(getApplicationContext(), "设备尚未连接，请先连接设备", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mBLEService.isConnected()) {
            Toast.makeText(getApplicationContext(), "设备尚未连接，请先连接设备", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mBLEService.mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "设备尚未连接，请先连接设备", Toast.LENGTH_LONG).show();
            return;
        }
        if (mBLEService.mBluetoothGatt == null) {
            Toast.makeText(getApplicationContext(), "设备尚未连接，请先连接设备", Toast.LENGTH_LONG).show();
            return;
        }
        if (mBLEService.mBluetoothGatt.getServices() == null) {
            Toast.makeText(getApplicationContext(), "设备尚未连接，请先连接设备", Toast.LENGTH_LONG).show();
            return;
        }
        Sendcharacteristic = null;
        if (mBLEService.mBluetoothGatt != null)
            if (mBLEService.mBluetoothGatt
                    .getServices().size() >= uuidLocation)
                if (mBLEService.mBluetoothGatt.getServices().get(uuidLocation).getCharacteristics().size() != 0 && mBLEService.mBluetoothGatt.getServices().get(uuidLocation).getCharacteristics().size() > uuidSendLocation) {
                    Sendcharacteristic = mBLEService.mBluetoothGatt
                            .getServices().get(uuidLocation)
                            .getCharacteristics().get(uuidSendLocation);
                    if (!Sendcharacteristic.getUuid().equals(BLEService.SERVIE_UUID_1)) {
                        //不知道为啥，
                        if (mBLEService.mBluetoothGatt
                                .getServices().get(uuidLocation)
                                .getCharacteristics().size() > 1) {
                            Sendcharacteristic = mBLEService.mBluetoothGatt
                                    .getServices().get(uuidLocation)
                                    .getCharacteristics().get(1);
                        }
                    }
                    final int charaProp = Sendcharacteristic.getProperties();
                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                        MyBluetoothGattCharacteristic = Sendcharacteristic;
                        mBLEService.mBluetoothGatt.setCharacteristicNotification(
                                MyBluetoothGattCharacteristic, true);
                    }
//                    final int charaProp = Sendcharacteristic.getProperties();
//                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
//                        MyBluetoothGattCharacteristic = Sendcharacteristic;
//                        mBLEService.mBluetoothGatt.setCharacteristicNotification(
//                                MyBluetoothGattCharacteristic, true);
//                    }
                }


//        user_sendDivData = Utils.setbyte(Utils.setSendData(entity, Sendevice_id));
        user_sendDivData = Utils.setbyte("0a0aff06111100010505ffff");
        Log.e("user_sendDivData=", Utils.setSendData(entity, Sendevice_id) + "   0a0aff06111100010505ffff");
        boolean status = false;
        if (MyBluetoothGattCharacteristic != null) {
            MyBluetoothGattCharacteristic.setValue(user_sendDivData);
            if (mBLEService.mBluetoothGatt != null)
                status = mBLEService.mBluetoothGatt.writeCharacteristic(MyBluetoothGattCharacteristic);

        }
    }


    //扫描设备
    private void scanBle() {
        search_timer.sendEmptyMessageDelayed(0, 1000);
    }

    // 开始扫描
    private int scan_timer_select = 0;
    private boolean scan_flag = true;
    private Handler search_timer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!isCloseBuild)
                return;
            search_timer.sendEmptyMessageDelayed(0, 500);

            if (!scan_flag) {
                return;
            }

            if (!mBLEService.mBluetoothAdapter.isEnabled()) {
                return;
            }
            mBLEService.scanBle(mLeScanCallback);
        }

    };

    // 设置配对广播监听
    private BluetoothReceiver1 bluetoothReceiver1 = null;

    private void setBroadcastReceiver() {
        // 创建一个IntentFilter对象，将其action指定为BluetoothDevice.ACTION_FOUND
        IntentFilter intentFilter = new IntentFilter(
                BLEService.ACTION_READ_Descriptor_OVER);
        intentFilter.addAction(BLEService.ACTION_ServicesDiscovered_OVER);
        intentFilter.addAction(BLEService.ACTION_OVER);
        intentFilter.addAction(BLEService.ACTION_STATE_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        bluetoothReceiver1 = new BluetoothReceiver1();
        // 注册广播接收器
        this.registerReceiver(bluetoothReceiver1, intentFilter);
    }

    private class BluetoothReceiver1 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BLEService.ACTION_READ_Descriptor_OVER.equals(action)) {
                if (BluetoothGatt.GATT_SUCCESS == intent.getIntExtra("value", -1)) {
                    read_name_flag = true;
                } else {
                }
                return;
            }
            if (BLEService.ACTION_ServicesDiscovered_OVER.equals(action)) {
                uuidSendLocation = 0;
                connect_flag = true;
                return;
            }
            if (BLEService.ACTION_STATE_CONNECTED.equals(action)) {
                uuidSendLocation = 0;
                return;
            }
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                if (BluetoothDevice.BOND_BONDED == Mydevice.getBondState()) {
                } else if (BluetoothDevice.BOND_BONDING == Mydevice.getBondState()) {
                    bind_flag = true;
                    System.out.println("正在配对");
                }
                return;
            }
            if (BLEService.ACTION_OVER.equals(action)) {
//                ConnectedHandler.sendEmptyMessage(0);2016
            }

        }

    }

    // 设置广播监听
    private BroadcastReceiver bluetoothReceiver;

    private void setBroadcastReceiver2() {
        // 创建一个IntentFilter对象，将其action指定为BluetoothDevice.ACTION_FOUND
        IntentFilter intentFilter = new IntentFilter(
                BLEService.ACTION_DATA_CHANGE);
        intentFilter.addAction(BLEService.ACTION_READ_OVER);
        intentFilter.addAction(BLEService.ACTION_RSSI_READ);
        intentFilter.addAction(BLEService.ACTION_STATE_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_STATE_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_WRITE_OVER);
        bluetoothReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // 数据改变通知
                if (BLEService.ACTION_DATA_CHANGE.equals(action)) {
                    Log.e("value1111111111", Utils.byteToString(intent.getByteArrayExtra("value")));
                    Toast.makeText(context, "value=" + Utils.byteToString(intent.getByteArrayExtra("value")) + "", Toast.LENGTH_LONG).show();
//                    displayData(Utils.byteToString(intent.getByteArrayExtra("value")).toString());
                    return;
                }
                // 读取数据
                if (BLEService.ACTION_READ_OVER.equals(action)) {
                    Log.e("value", Utils.byteToString(intent.getByteArrayExtra("value")));
                    return;
                }

                // 连接状态改变
                if (BLEService.ACTION_STATE_CONNECTED.equals(action)) {
                    bind_flag = true;
                    Toast.makeText(getApplicationContext(), "蓝牙设备连接成功！",
                            Toast.LENGTH_SHORT).show();
                    bl_Sendsearch_timer = true;
                }
                if (BLEService.ACTION_STATE_DISCONNECTED.equals(action)) {
                    bl_Sendsearch_timer = false;
                }
                if (BLEService.ACTION_WRITE_OVER.equals(action)) {
                }

            }
        };
        // 注册广播接收器
        this.registerReceiver(bluetoothReceiver, intentFilter);
    }

    //2015-9-4
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectedHandler.sendEmptyMessage(0);
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
        }
        if (ReceiverState & bluetoothReceiver != null) {
            ReceiverState = false;
            unregisterReceiver(bluetoothReceiver);
            unregisterReceiver(bluetoothReceiver1);
            System.exit(0);
        }
    }

    //解析数据
    private void displayData(String autoValue) {
        String[] str1 = autoValue.split(" ");
//        Log.e("displayData=", autoValue);
        if (str1.length > 60) {
            int check = 0;
            for (int i = 0; i < str1.length - 8; i++) {
                if (i == 0) {
                    check = Utils.Transition16go10(str1[i]);
                }
                check = check ^ Utils.Transition16go10(str1[i]);
                Log.e("check i=", i + "  =str1[i]" + str1[i]);
            }
            Log.e("Utils.T", "" + Utils.Transition16go10(autoValue.subSequence(str1.length - 10, str1.length - 1).toString().replace(" ", "")));
            if (check == Utils.Transition16go10(autoValue.subSequence(str1.length - 10, str1.length - 1).toString().replace(" ", ""))) {
//                entity.setDerail_switch(str1[37].subSequence(0, 1).toString());
//                entity.setMake_switch(str1[37].subSequence(1, 2).toString());
//                entity.setWind(str1[38].subSequence(0, 1).toString());
//                entity.setUv(str1[38].subSequence(1, 2).toString());
//                entity.setRefrigeration(str1[39].subSequence(0, 1).toString());
//                entity.setHeat(str1[39].subSequence(1, 2).toString());
//                entity.setHeat_switch(str1[40].subSequence(0, 1).toString());
//                entity.setCold_switch(str1[40].subSequence(1, 2).toString());

                entity.setPh(Utils.Transition16go10(str1[42] + str1[41]) + "");
                entity.setTds(Utils.Transition16go10(str1[44] + str1[43]) + "");
                entity.setPm(Utils.Transition16go10(str1[46] + str1[45]) + "");
                entity.setTemperature(Utils.Transition16go10(str1[47]) + "");
                entity.setHumidity(Utils.Transition16go10(str1[48]) + "");
                entity.setSum_water(Utils.Transition16go10(str1[50] + str1[49]) + "");
                entity.setSum_time(Utils.Transition16go10(str1[42] + str1[51]) + "");

//                if (entity.getHeat_switch().equals("0")) {
//                    image_hot.setVisibility(View.VISIBLE);
//                    image_hot_circle.setVisibility(View.GONE);
//                    hotType = true;
//                } else {
//                    image_hot.setVisibility(View.GONE);
//                    image_hot_circle.setVisibility(View.VISIBLE);
//                    hotType = false;
//                }
//                if (entity.getCold_switch().equals("0")) {
//                    image_cold.setVisibility(View.VISIBLE);
//                    image_cold_circle.setVisibility(View.GONE);
//                    coldType = true;
//                } else {
//                    image_cold.setVisibility(View.GONE);
//                    image_cold_circle.setVisibility(View.VISIBLE);
//                    coldType = false;
//                }

                setArcProgress(Integer.parseInt(entity.getFilter_net()), 0);
                setArcProgress(Integer.parseInt(entity.getFilter_element()), 1);

//                waterWaveView.

            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            outApp();
        }
        return true;
    }

    private void outApp() {
        firstTime = sharedPreferences.getLong("lastbackkey", 0);
        long secondTime = System.currentTimeMillis();

        if (secondTime - firstTime > 3000) {
            // 如果两次按键时间间隔大于3秒，则不退出
            Toast.makeText(this, show, Toast.LENGTH_SHORT).show();
            sharedPreferences.edit().putLong("lastbackkey", secondTime)
                    .commit();
        } else {
            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.disable();
            }
            // 两次按键小于3秒时，退出应用
            sharedPreferences.edit().putBoolean("fristIn", true).commit();
            finish();
        }
    }

}
