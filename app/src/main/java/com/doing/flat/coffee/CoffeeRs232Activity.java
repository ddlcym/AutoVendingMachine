package com.doing.flat.coffee;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doing.flat.coffee.ble.SendbleEntity;
import com.doing.flat.coffee.download.services.DownloadService;
import com.doing.flat.coffee.download.utils.MyIntents;
import com.doing.flat.coffee.entity.ADInfo;
import com.doing.flat.coffee.entity.Container_array;
import com.doing.flat.coffee.entity.DBCntrOptEntity;
import com.doing.flat.coffee.entity.DBDeiveceData;
import com.doing.flat.coffee.entity.DBDeviceIdData;
import com.doing.flat.coffee.entity.DBDrinksData;
import com.doing.flat.coffee.entity.DBHourDeiveceData;
import com.doing.flat.coffee.entity.DBImageData;
import com.doing.flat.coffee.entity.DBOptEntity;
import com.doing.flat.coffee.entity.DBRuleData;
import com.doing.flat.coffee.entity.DBShopData;
import com.doing.flat.coffee.entity.DataEntity;
import com.doing.flat.coffee.entity.DataOptEntity;
import com.doing.flat.coffee.entity.Drinks_array;
import com.doing.flat.coffee.entity.Price_array;
import com.doing.flat.coffee.entity.SendOptEntity;
import com.doing.flat.coffee.entity.SendSocketEntity;
import com.doing.flat.coffee.ui.viewpagercycle.lib.CycleViewPager;
import com.doing.flat.coffee.utils.DrawUtil;
import com.doing.flat.coffee.utils.FileOperationUtil;
import com.doing.flat.coffee.utils.HttpUrls;
import com.doing.flat.coffee.utils.L;
import com.doing.flat.coffee.utils.RuleUtils;
import com.doing.flat.coffee.utils.SharePerfenceUtils;
import com.doing.flat.coffee.utils.Utils;
import com.doing.flat.coffee.xutils.DbUtils;
import com.doing.flat.coffee.xutils.db.sqlite.Selector;
import com.doing.flat.coffee.xutils.db.sqlite.WhereBuilder;
import com.doing.flat.coffee.xutils.exception.DbException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.tencent.bugly.crashreport.CrashReport;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android_serialport_api.sample.Application;
import android_serialport_api.sample.SerialPortActivity;


/**
 * Created by hqg on 2016/3/20.
 */
public class CoffeeRs232Activity extends SerialPortActivity {
    private TestProductsDialog testProductsDialog;

    private CycleViewPager viewpager;
    private FrameLayout ccccc;
    private ImageView img;
    private View task;
    private ProgressBar pb;
    private VideoView video;
    private ImageView imageCode;
    private ImageView imagePublicity;
    private long firstTime = 0;
    private SharedPreferences sharedPreferences;
    private LinearLayout ll_image_code;
    private LinearLayout ll_auto;
    private LinearLayout llClass;
    private TextView text_title;
    private TextView text_time;
    private TextView text_3g;
    private ImageView wifi_image;
    private TextView text_bug;
    private TextView textHint;
    private TextView textGoodsEerror;
    private List<String> goodsErrorList=new ArrayList<>();
//    textNumber

    private TextView textNumber;
    private LinearLayout llAdvertising;

    private TextView textGood1;
    private TextView textGood2;
    private TextView textGood3;
    private TextView textGood4;

    private ImageView barcode_image;
    private LinearLayout fl_image;

    private Context context;
    private List<String> imageList = new ArrayList<>();
    //    private List<Integer> imageListInt = new ArrayList<>();
    private Thread timerThread;
    private List<View> views = new ArrayList<View>();
    private List<com.doing.flat.coffee.ui.VideoView> videoViews = new ArrayList<com.doing.flat.coffee.ui.VideoView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private List<DBRuleData> ruleInfos = new ArrayList<DBRuleData>();
    //
    private SendbleEntity entity;//数据以及设备状态
    private String android_id, android_id_2;

    private float width = 0;
    private float Height = 0;
    private String show = "双击退出";
    Window window;
    private String setApp_ver = "";

    private String order_bindDevice = "CntrBindDevice-2";//绑定接口
    private String order_Callback = "Callback-2";//回调
    private String order_ProduceDrink = "ProduceDrink-1";//出水接口 生产饮料
    private String order_Complete = "Complete-1";//出水完毕
    private String order_Produce = "Produce-1";//制作回调

    private String order_RecentData = "CntrRecentData";//实时数据
    private String order_UploadData = "CntrUploadData";//每小时上传的数据
    private String order_Updata_App = "UpdateCntr-1";//推送更新

    private String order_Device_status = "DeviceStatus";//获取设备状态
    private String order_Device_cntr_status = "CntrStatus";//获取销售机设备状态

    private String order_RuleData = "RuleData-1";//获取广告策略
    private String order_ResourceData = "ResourceData-1";//获取策略资源
    private String order_RuleCallback = "RuleCallback";//策略回调

    private String order_UpdateDrinks = "UpdateDrinks";//下发控制饮料粉料和水量
    private String order_MsgCallback = "OrderMsgCallback";//控制命令回调
    private String order_CntrOrderMsg_CB = "CntrOrderMsgCB";//

    private String CRRL_TICKET = "TicketDrink-1";//出票
    private String SELES_DRINK_1 = "DropDrinks-1";//落饮命令
    private String CNTR_ORDER_CB_1 = "CntrOrderCB-1";//落饮命令-命令接收回调
    private String CNTR_DROP_CB_1 = "CntrDropCB-1";//设备落饮回调
    private String CNTR_DROP_FAILED_CB = "CntrDropFailedCB";//设备落饮回调
    private String CRTR_OPT_CB = "CntrOptCB";//上传opt数据库 type为0和4的数据  断线操作回调
    private String order_OptCallback = "OptCallback";//上传opt数据库 type为0和4的数据
    private String CNTR_REFES = "RefeshCntr";//获取实时数据
    private String CNTR_REFESH_DEVICE = "RefeshDevice";//请求设备实时数据
    private String CNTR_REFESH_DEVICE_CB = "RefreshDeviceCB";//请求设备状态回调

    private String UploadCntrDoor = "UploadCntrDoor";//开门上报
    private String ExamineCntr = "ExamineCntr";//检测设备
    private String ExamineCntrCB = "ExamineCntrCB";//检测设备结果
    private String ErrorMotorCB = "ErrorMotorCB";//马达故障回调
//    private String CntrOptCB = "CntrOptCB";//操作回调

    private String rule_id;//新策略ID

//    private Map<String, DBShopData> cntrData;//货到数据

    private boolean Rs232State = false;
    private boolean isShowQRCode = false;//开机前8分钟不显示二维码
    private int showQRCodeTime = 1;//8分钟
//    private int showQRCodeTime = 10;//8分钟

    private boolean isConnect = true;//判断是否可以连接socket
    private List<Container_array> testing_array = new ArrayList<>();//需要检测的货道列表
    private List<Container_array> testing_array_error = new ArrayList<>();//出现问题的货道列表
    private int goods_error = 0;//货道出错显示时间
    private int goods_error_time = 0;//出错显示时间
//    private String drop_status;//落货状态，1正常，2故障

    private DbUtils dbUtils;
    private String device_id;

    private TextView textShow;

    private NetWorkBroadcast myReceiver;
    private boolean isReg = false;
    private List<Price_array> price_array;
    private int serial_number=0;//83位流水号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        dbUtils = Application.getInstance().getDb();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_coffee_2);
//        imageList.add("assets://img/v_image_1.png");
//        imageList.add("assets://img/v_image_2.png");
//        imageList.add("assets://img/v_image_3.png");
//        imageList.add("assets://img/v_image_4.png");
//        imageList.add("assets://img/v_image_5.png");
//        imageList.add("assets://img/v_image_6.png");
//        imageList.add("assets://img/576cd66678d89.mp4");

        File f = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/coffee_video/video_test.mp4");
//                        + "fex/db_weather.db");
        String path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/coffee_video/";
        if (!FileOperationUtil.isFileExist(path + "video_test.mp4")) {
            if (!FileOperationUtil.isFileExist(path)) {
                FileOperationUtil.mkdir(path);
            }
            FileOperationUtil.copyApkFromAssets(this, "video_test.mp4", f);
        }

        imageList.add(f.getPath());

//        ADInfo adInfo = new ADInfo();
//        adInfo.setPathUrl("file://" + "/mnt/internal_sd/wenj.mp4");
//        adInfo.setIs_success("1");
//        adInfo.setFile_type("2");
//        adInfo.setRemain_time("71");
//        infos.add(adInfo);


//        for (int i = 0; i < imageList.size(); i++) {
//            ADInfo adInfo1 = new ADInfo();
//            adInfo1.setPathUrl(imageList.get(i));
//            adInfo1.setIs_success("1");
//            adInfo1.setFile_type("1");
//            adInfo1.setRemain_time("5");
//            infos.add(adInfo1);
//        }

        setDefault();


        entity = new SendbleEntity();
        entity.setCoffee_switch("11");//
        entity.setTea_switch("11");//
        entity.setFruit_switch("11");//
        entity.setWater_switch("11");//
        entity.setRefrigeration("0");//////制冷
        entity.setHeat("0");//加热

//新的android_id生成方式
        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        android_id_2 = deviceUuid.toString();

        sharedPreferences = getSharedPreferences("sysinfo", 0);
        android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        initializeSqlData();
        try {
            setApp_ver = (context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        new InitSocketThread().start();
        DBImageData dbImageData = new DBImageData();
        try {
            dbImageData = dbUtils.findFirst(Selector.from(DBImageData.class).where("id", ">", -1));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (dbImageData != null) {
            image_path = dbImageData.getPath();
            image_qc = dbImageData.getQrcode();
            image_dit = DrawUtil.scaleImage(image_path);
        }

        findViews();
        Utils.getLocation(context);
        WifiInfoControl();
        timerSocket();//连接服务器时钟

        //每个小时上传数据的定时器
        getSaveData();
        getRule(System.currentTimeMillis() / 1000);
        initialize();
        getPriceArray();
        if (!isReg) {
            myReceiver = new NetWorkBroadcast();
            IntentFilter filter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(myReceiver, filter);
            isReg = true;
        }
//        cntrData = new HashMap<>();
//        Utils.saveNewData(dbUtils, cntrData);//判断是否有货仓数据，没有就初始化
//        Utils.getCntrData(cntrData, dbUtils);//获取货道信息

//        Intent intent =new Intent(CoffeeRs232Activity.this,ExpendService.class);
//        startService(intent);

    }


    //初始化
    private void initializeSqlData() {
        try {
//            dbUtils.dropTable(DBHourDeiveceData.class);删除表格
            if (!dbUtils.tableIsExist(DBDeviceIdData.class)) {
                dbUtils.createTableIfNotExist(DBDeviceIdData.class);
            }
            if (!dbUtils.tableIsExist(DBDeiveceData.class)) {
                dbUtils.createTableIfNotExist(DBDeiveceData.class);
            }
            if (!dbUtils.tableIsExist(DBHourDeiveceData.class)) {
                dbUtils.createTableIfNotExist(DBHourDeiveceData.class);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                String temp = Utils.byteToString(buffer);
                Log.e("autoValue", temp);
                if (temp.contains(" 00 a5 a5 ") ||
                        temp.contains("00 00 a5 a5 ") ||
                        temp.contains("a5 a5 ") ||
                        temp.contains(" 00 aa 55 ") || temp.contains(" 00 00 aa 55 ") || temp.contains(" aa 55 ff")) {
                    displayData(temp);
                }
            }
        });
    }

    @Override
    protected void DisplayError(int resourceId) {
        setRs232Bug();
        Rs232State = true;
    }

    private String tempTds;
    private String is_Produce = "0";//0表示正常  1表示在制作中  就发送出水回调
    private String is_Complete = "0";//0表示正常  1表示在制作中 ff和2 表示异常
    private String is_deivece = "3";//给服务器发送机器状态 1正常，2制水中，3异常ff, 4表示有其他故障
    private String coffee_bug_state = "2";//00表示正常，其他表示异常
    private String coffee_state = "ff";//00表示正常，其他表示异常
    private String Cntr_state = "0";//233  00-空闲 01-出货中 05-设备故障 04-维护中
    private String Cntr_bug_state = "0";//234  0：全部仓良好；1-50：某号仓故障；101-150：某号仓已修好；（百位1表示修好，十位个位表示仓号）FF：全部仓故障；其他数值无意义
    private String Cntr_transaction_state = "0";  //235 交易执行状态  00-空闲  01-出货进行中 02-出货成功（发3次） 03-出货失败（发3次） （交易结束，发3次出货成功或失败后，该字节置成00）
    private String open_door = "";//234 开门 0表示关门 1表示开门
    private String sensor = "";//236  传感器0正常 1故障
//    private String sensor_state = "";//传感器0正常 1故障  用来是否显示二维码的

    private int cup_sum = -1;//纸杯总数  -1是初始化

    private long timeResultDeiveceData = 0;//接受到机器最后一次正确数据的时间

    //解析数据
    private void displayData(String autoValue) {
        autoValue = autoValue.replace(" 00 aa 55", " aa 55");
        autoValue = autoValue.replace("00 aa 55", " aa 55");
        autoValue = autoValue.replace(" 00 00 aa 55", " aa 55");
        autoValue = autoValue.replace("  aa 55", " aa 55");

        autoValue = autoValue.replace(" 00 a5 a5", " a5 a5");
        autoValue = autoValue.replace("00 a5 a5", " a5 a5");
        autoValue = autoValue.replace(" 00 00 a5 a5", " a5 a5");
        autoValue = autoValue.replace("  a5 a5", " a5 a5");
//        L.i("autoValue:"+autoValue.replace(" ", " "));
        String[] str1 = autoValue.split(" ");

        if (str1.length > 252 && autoValue.startsWith("aa 55 ff") && autoValue.contains("50 05 ff ff") ||
                autoValue.startsWith(" aa 55 ff") && str1.length > 252 && autoValue.contains("50 05 ff ff")) {

            long check_sum = Utils.Transition16Longgo10(str1[249] + str1[250] + str1[251] + str1[252]);
            long check = 0;
            for (int i = 1; i < 249; i++) {
//                Log.e("check i=", i + "  =str1[i]" + str1[i] + "     " + Utils.Transition16go10(str1[i]) + " str1=" + str1.length);
                check += Utils.Transition16go10(str1[i]);
            }

//            L.i("check_sum:"+check_sum+"   check:"+check);
            if (check_sum != check)
                return;//表示错误数据

            Utils.LogE("check_sum=" + check_sum + "  check=" + check);
            timeResultDeiveceData = System.currentTimeMillis() / 1000;

            entity.setDevice_id((str1[9] + str1[10] + str1[11] + str1[12]) + "");//设备ID
            entity.setF_id_1((str1[25] + str1[26] + str1[27] + str1[28]) + "");
            entity.setFilter_element(Utils.Transition16go10(str1[29] + str1[30] + str1[31] + str1[32]) / 60 + "");
            entity.setF_id_2((str1[33] + str1[34] + str1[35] + str1[36]) + "");
            entity.setFilter_element1(Utils.Transition16go10(str1[37] + str1[38] + str1[39] + str1[40]) / 60 + "");
            entity.setF_id_3((str1[41] + str1[42] + str1[43] + str1[44]) + "");
            entity.setFilter_element2(Utils.Transition16go10(str1[45] + str1[46] + str1[47] + str1[48]) / 60 + "");
            entity.setF_id_4((str1[49] + str1[50] + str1[51] + str1[52]) + "");
            entity.setFilter_element3(Utils.Transition16go10(str1[53] + str1[54] + str1[55] + str1[56]) / 60 + "");
            entity.setMesh_id((str1[57] + str1[58] + str1[59] + str1[60]) + "");
            entity.setFilter_net(Utils.Transition16go10(str1[61] + str1[62] + str1[63] + str1[64]) / 60 + "");


//            entity.setDevice_id(Utils.Transition16go10(str1[5] + str1[6] + str1[7] + str1[8]) + "");
//            entity.setElement_id_1(Utils.Transition16go10(str1[9] + str1[10] + str1[11] + str1[12]) + "");
//            entity.setElement_id_2(Utils.Transition16go10(str1[13] + str1[14] + str1[15] + str1[16]) + "");
//            entity.setElement_id_3(Utils.Transition16go10(str1[17] + str1[18] + str1[19] + str1[20]) + "");
//            entity.setElement_id_4(Utils.Transition16go10(str1[21] + str1[22] + str1[23] + str1[24]) + "");
//            entity.setNet_id(Utils.Transition16go10(str1[25] + str1[26] + str1[27] + str1[28]) + "");
//            entity.setFilter_element(Utils.Transition16go10(str1[29] + str1[30] + str1[31] + str1[32]) + "");
//            entity.setFilter_element1(Utils.Transition16go10(str1[33] + str1[34] + str1[35] + str1[36]) + "");
//            entity.setFilter_net(Utils.Transition16go10(str1[37] + str1[38] + str1[39] + str1[40]) + "");


            entity.setDixie_cup(Utils.Transition16go10(str1[181]) + "");//dixie_cup_1
            entity.setDixie_cup_1(Utils.Transition16go10(str1[182]) + "");//dixie_cup_1
            entity.setDixie_cup_2(Utils.Transition16go10(str1[183]) + "");//dixie_cup_1
            entity.setDixie_cup_3(Utils.Transition16go10(str1[184]) + "");//dixie_cup_1
            entity.setDixie_cup_4(Utils.Transition16go10(str1[185]) + "");//dixie_cup_1
            entity.setDixie_cup_5(Utils.Transition16go10(str1[186]) + "");//dixie_cup_1
            entity.setDixie_cup_6(Utils.Transition16go10(str1[187]) + "");//dixie_cup_1
            entity.setDixie_cup_7(Utils.Transition16go10(str1[188]) + "");//dixie_cup_1
            entity.setDixie_cup_8(Utils.Transition16go10(str1[221]) + "");//dixie_cup_1
            entity.setDixie_cup_9(Utils.Transition16go10(str1[222]) + "");//dixie_cup_1

//            if (entity.getDixie_cup_1()!=null&&entity.getDixie_cup_1().length()>0&&!entity.getDixie_cup_1().equals(""255))
//            {
//                entity.setDixie_cup_1();
//            }


            entity.setWater_state((str1[189]) + "");
            entity.setCoffee_state((str1[189]) + "");
            entity.setCoffee_bug_state((str1[190]) + "");


//            entity.setCntr_state((str1[233]) + "");
//            entity.setCntr_bug_state((str1[234]) + "");
//            entity.setCntr_num((str1[235]) + "");
//            entity.setCntr_count((str1[236]) + "");
//            entity.setCntr_state(Integer.parseInt(str1[276], 16) + "");
//            entity.setCntr_bug_state(Integer.parseInt(str1[277], 16) + "");
//            entity.setCntr_num(Integer.parseInt(str1[278], 16) + "");
//            entity.setCntr_count(Integer.parseInt(str1[279], 16) + "");

            //协议修改前
//            entity.setCntr_state(Integer.parseInt(str1[233], 16) + "");
//            entity.setCntr_bug_state(Integer.parseInt(str1[234], 16) + "");
//            entity.setOpen_door(Integer.parseInt(str1[234], 16) + "");
//            entity.setCntr_transaction_state(Integer.parseInt(str1[235], 16) + "");
//            entity.setSensor(Integer.parseInt(str1[236], 16) + "");

            //协议修改后
            entity.setCntr_state(Integer.parseInt(str1[41], 16) + "");
            entity.setCntr_bug_state(Integer.parseInt(str1[42], 16) + "");
            entity.setOpen_door(Integer.parseInt(str1[42], 16) + "");
            entity.setCntr_transaction_state(Integer.parseInt(str1[41], 16) + "");
            entity.setSensor(Integer.parseInt(str1[44], 16) + "");
            L.i(" Cntr_state41:--   "+entity.getCntr_state()+"   --Cntr_bug_state42:   "+entity.getCntr_bug_state()+"     Cntr_transaction_state41:     "+entity.getCntr_transaction_state());

//            entity.setCntr_num(Integer.parseInt(str1[235], 16) + "");
//            entity.setCntr_count(Integer.parseInt(str1[236], 16) + "");

            Cntr_state = entity.getCntr_state();
            Cntr_bug_state = entity.getCntr_bug_state();
            Cntr_transaction_state = entity.getCntr_transaction_state();
//            if (!entity.getSensor().equals("ff") || !entity.getSensor().equals("255")) {
//                sensor_state = entity.getSensor();
//            }

            Utils.LogE("Cntr_state=" + Cntr_state + " Cntr_bug_state=" + Cntr_bug_state);
//            if (textShow != null)
//                textShow.setText("Coffee_state=" + entity.getCoffee_state());
//

            entity.setPm(Utils.Transition16go10(str1[81] + str1[82]) + "");//
            entity.setTsd(Utils.Transition16go10(str1[83] + str1[84]) + "");
            entity.setTemperature(Utils.Transition16go10(str1[85]) + "");
            entity.setHumidity(Utils.Transition16go10(str1[86]) + "");
            entity.setVoc(Utils.Transition16go10(str1[87]) + "");
            entity.setTds(Utils.Transition16go10(str1[88]) + "");


            entity.setTemperature_t2(Utils.Transition16go10(str1[113]) + "");
            entity.setTemperature_t2b(Utils.Transition16go10(str1[114]) + "");
            entity.setTemperature_t3(Utils.Transition16go10(str1[115]) + "");
            entity.setTemperature_exhaust(Utils.Transition16go10(str1[116]) + "");

            entity.setTemperature_hot_water(Utils.Transition16go10(str1[109]) + "");
            entity.setTemperature_cold_water(Utils.Transition16go10(str1[110]) + "");
            entity.setWindspeed(Utils.Transition16go10(str1[111]) + "");

            entity.setWater_level_1(Utils.Transition16go10(str1[125]) + "");//水位1
            entity.setWater_level_2(Utils.Transition16go10(str1[126]) + "");
            entity.setWater_level_3(Utils.Transition16go10(str1[127]) + "");
            entity.setWater_level_4(Utils.Transition16go10(str1[128]) + "");

            entity.setFlow_1(Utils.Transition16go10(str1[129] + str1[130]) + "");
            entity.setFlow_2(Utils.Transition16go10(str1[131] + str1[132]) + "");

            entity.setBoot_time(Utils.Transition16go10(str1[81] + str1[82]) + "");

            entity.setTemp_err("ff");
            entity.setOther_err("ff");


//            dbDeiveceData.setBoot_times(sendbleEntity.getBoot_time());
//
//            dbDeiveceData.setTemp_err(sendbleEntity.getTemp_err());
//            dbDeiveceData.setOther_err(sendbleEntity.getOther_err());
//
//            dbDeiveceData.setMac(sendbleEntity.getMac());
//            dbDeiveceData.setNfc(sendbleEntity.getNfc());

            entity.setMac(str1[65] + str1[66] + str1[67] + str1[68]);
            entity.setMac_1(str1[69] + str1[70] + str1[71] + str1[72]);
            entity.setMac_2(str1[73] + str1[74] + str1[75] + str1[76]);
            entity.setNfc("ffffffff");


            entity.setPowder_coffee(Utils.Transition16go10(str1[213] + str1[214]) + "");//咖啡机粉量
            entity.setPowder_tea(Utils.Transition16go10(str1[215] + str1[216]) + "");
            entity.setPowder_fruit(Utils.Transition16go10(str1[217] + str1[218]) + "");

            entity.setWater_coffee(Utils.Transition16go10(str1[225] + str1[226]) + "");//咖啡水量
            entity.setWater_tea(Utils.Transition16go10(str1[227] + str1[228]) + "");
            entity.setWater_fruit(Utils.Transition16go10(str1[229] + str1[230]) + "");  //现在是热水
            entity.setWater_hot_winter(Utils.Transition16go10(str1[231] + str1[232]) + "");//这里是冷水
//            textShow.setText("Coffee_state=" + entity.getCoffee_state() + "    Coffee_bug_state=" + entity.getCoffee_bug_state());
//            Toast.makeText(context, "Coffee_state=" + entity.getCoffee_state() + "    Coffee_bug_state=" + entity.getCoffee_bug_state() + "   autoValue=" + autoValue, Toast.LENGTH_SHORT).show();
            if (newdbDrinksData != null) {//判断设置水量和粉量后的的判定
                numDrinksData++;

                if (numDrinksData > 300) {
                    newdbDrinksData = null;
                    String temp1 = str1[213] + str1[214]
                            + str1[215] + str1[216]
                            + str1[217] + str1[218]
                            + str1[219] + str1[220]
                            + str1[221] + str1[222]
                            + str1[223] + str1[224]
                            + str1[225] + str1[226]
                            + str1[227] + str1[228]
                            + str1[229] + str1[230];
                    java.lang.Throwable throwable = new Throwable("未完成=" + temp1);
                    CrashReport.postCatchedException(throwable);
                    return;
                }
                Utils.LogE("newdbDrinksData=" + JSON.toJSONString(newdbDrinksData));
                Utils.LogE("temp=" + entity.getPowder_coffee() + "   " + entity.getPowder_tea() + "   "
                        + entity.getWater_coffee() + "  " + entity.getWater_tea() + "   " + entity.getWater_hot_winter());
                int isDrink = 0;
                Drinks_array drinks_array_coffee = RuleUtils.getDrinksArray(newdbDrinksData, "1");
                if (drinks_array_coffee != null && drinks_array_coffee.getPowder().equals(entity.getPowder_coffee())) {
                    isDrink++;
                }
                if (drinks_array_coffee != null && drinks_array_coffee.getWater_yield().equals(entity.getWater_coffee())) {
                    isDrink++;
                }


                Drinks_array drinks_array_tea = RuleUtils.getDrinksArray(newdbDrinksData, "3");
                if (drinks_array_tea != null && drinks_array_tea.getPowder().equals(entity.getPowder_tea())) {
                    isDrink++;
                }
                if (drinks_array_tea != null && drinks_array_tea.getWater_yield().equals(entity.getWater_tea())) {
                    isDrink++;
                }

                Drinks_array drinks_array_water = RuleUtils.getDrinksArray(newdbDrinksData, "9");
                if (drinks_array_water != null && drinks_array_water.getWater_yield().equals(entity.getWater_hot_winter())) {
                    isDrink++;
                }

                Drinks_array drinks_array_water_hot = RuleUtils.getDrinksArray(newdbDrinksData, "7");
                if (drinks_array_water_hot != null && drinks_array_water_hot.getWater_yield().equals(entity.getWater_fruit())) {   //
                    isDrink++;
                }

                if (isDrink == 6) {
                    newdbDrinksData = null;
                    String temp = str1[213] + str1[214]
                            + str1[215] + str1[216]
                            + str1[217] + str1[218]
                            + str1[219] + str1[220]
                            + str1[221] + str1[222]
                            + str1[223] + str1[224]
                            + str1[225] + str1[226]
                            + str1[227] + str1[228]
                            + str1[229] + str1[230];
                    java.lang.Throwable throwable = new Throwable("已完成=" + temp);
                    CrashReport.postCatchedException(throwable);
                }
            }

//            Utils.LogE("  Powder_coffee="+entity.getPowder_coffee()+"   "+entity.getPowder_fruit()+"   "+entity.getPowder_fruit()
//            +" Water_coffee="+"  "+entity.getWater_coffee()+"   " +entity.getWater_tea());


            DBDeiveceData dbDeiveceData = null;
            if (entity.getCoffee_state() != null && !entity.getCoffee_state().equals("1") && !entity.getCoffee_state().equals("01")) {
                dbDeiveceData = SavaData(entity);
            }
            String pm = "";
            int temp = 0;
            if (entity.getPm() != null && entity.getPm().length() > 0) {
                temp = Integer.parseInt(entity.getPm());
                if (temp < 150) {
                    pm = getString(R.string.excellent);
                } else if (temp >= 150 && temp < 300) {
                    pm = getString(R.string.good);
                } else {
                    pm = getString(R.string.bad);
                }
            }


            String voc = "";
            if (entity.getVoc() != null && entity.getVoc().length() > 0) {
                temp = Integer.parseInt(entity.getVoc());
                if (temp <= 1) {
                    voc = getString(R.string.excellent);
                } else if (temp > 2 && temp < 3) {
                    voc = getString(R.string.good);
                } else {
                    voc = getString(R.string.bad);
                }
            }

            String tds = tempTds;
            if (entity.getTds() != null && entity.getTds().length() > 0) {
                temp = Integer.parseInt(entity.getTds());
                if (temp > 50) {
                    if (temp % 2 == 0) {
                        tds = "49";
                    } else {
                        tds = "48";
                    }
                } else {
                    tds = entity.getTds();
                }
                tempTds = tds;
            }
//            text_title.setText("温度:  " + entity.getTemperature() + "℃     湿度:  " + entity.getHumidity() + "%     PM2.5:  " + pm + "     VOC:  " + voc + "     tds:  " + tds);
//            text_title.setText("温度：" + entity.getTemperature() + "℃     湿度： " + entity.getHumidity() + " %     PM2.5: " + pm + "    VOC: " + voc + "     TDS: " + tds);
//            text_title.setText(getString(R.string.temp) + ": " + entity.getTemperature() + "℃    " + getString(R.string.humidity) + ": " + entity.getHumidity() + " %    PM2.5: " + pm + "   VOC: " + voc + "    TDS: " + tds);
//            entity.setCoffee_bug_state text_bug   setCoffee_state


            String command = (Utils.Transition16go10(str1[47] + str1[78] + str1[79] + str1[80]) + "");
            coffee_bug_state = entity.getCoffee_bug_state();
            coffee_state = entity.getCoffee_state();


            text_title.setText("售卖机状态=" + Cntr_state + "   交易状态=" + Cntr_transaction_state);

            if (textGoodsEerror.getVisibility()!=View.INVISIBLE)
            {
                textGoodsEerror.setVisibility(View.INVISIBLE);
            }

                //小售货机不需要开门20180111
//            if (!open_door.equals(entity.getOpen_door()))//判断开门是否改变
//            {
//                open_door = entity.getOpen_door();
//                if (mSocket != null && open_door.equals("1") && dataEntity != null) {
//                    //加一个发送服务器命令
//                    DataOptEntity dataOptEntity = new DataOptEntity();
//                    dataOptEntity.setDevice_id(dataEntity.getDevice_id());
//                    sendData2(UploadCntrDoor, dataOptEntity);
//
//                }
//            }

            if (entity.getCoffee_state() != null) {
                String coffee_state = entity.getCoffee_state();
                if (coffee_state.equals("01") || coffee_state.equals("1")) {
                    text_bug.setVisibility(View.GONE);
                    imageCode.setVisibility(View.VISIBLE);
                    imageCode.setImageResource(R.drawable.cup_image);
                    textHint.setText(getString(R.string.making));
                    textHint.setVisibility(View.VISIBLE);
                    if (is_Produce.equals("0") && opt_id.length() > 0 && sendSucceedInt == 2) {
                        is_Produce = "1";
                        updataOpt(opt_id, opt_type, "2");
                        sendBC(order_Produce, opt_id, opt_type, sendSucceedInt + "");//出水回调
                    }
                    is_Complete = "1";
                    is_deivece = "2";
                } else if (coffee_state.equals("02") || coffee_state.equals("2") || coffee_state.equals("ff")
                        || Cntr_state.equals("05") || Cntr_state.equals("5")
                        || Cntr_state.equals("4") || Cntr_state.equals("04") || Cntr_state.equals("256")) {
                    is_deivece = "3";
//                    if (!entity.getCoffee_bug_state().equals("00")) {
                    if (isShowQRCode) {
                        imageCode.setVisibility(View.VISIBLE);
                        imageCode.setImageResource(R.drawable.device_bug);
                        if (!coffee_bug_state.equals("00")) {
                            text_bug.setVisibility(View.VISIBLE);
                            text_bug.setText(getString(R.string.error_code) + "：" + coffee_bug_state);
                        } else {
                            if (Cntr_state.equals("4")) {
                                text_bug.setVisibility(View.VISIBLE);
                                text_bug.setText(getString(R.string.machine_maintenance) + "：" + Cntr_bug_state);
//                                textHint.setText(getString(R.string.machine_stoppage_shop));
                            } else if (Cntr_state.equals("5")) {
                                text_bug.setVisibility(View.VISIBLE);
                                text_bug.setText(getString(R.string.machine_stoppage_shop) + "：" + Cntr_bug_state);
//                                textHint.setText(getString(R.string.machine_maintenance));
                            }
                        }
                        textHint.setVisibility(View.GONE);
                    } else {
                        imageHler.sendEmptyMessage(123);
                    }
//                    }
//                    text_bug.setText("错误代码：" + entity.getCoffee_bug_state());
                } else if ((coffee_state.equals("00") || coffee_state.equals("0")) && !Cntr_state.equals("1")) {
                    is_deivece = "1";
                    if (coffee_bug_state.equals("00") || coffee_bug_state.equals("0")) {
                        isShowQRCode = true;
                        imageHler.sendEmptyMessage(123);
//                        L.i("coffee_bug_state"+coffee_bug_state);
                    }
                    if (mSocket != null) {
                        text_bug.setVisibility(View.GONE);
                        imageCode.setVisibility(View.VISIBLE);
                        if (coffee_bug_state.equals("00") || coffee_bug_state.equals("0"))
                            imageHler.sendEmptyMessage(123);
                        else {
                            if (isShowQRCode) {
                                imageCode.setImageResource(R.drawable.device_bug);
                                text_bug.setVisibility(View.GONE);
                                text_bug.setText(getString(R.string.error_code) + "：" + coffee_bug_state);
                                textHint.setText(getString(R.string.machine_stoppage));
                            } else {
                                imageHler.sendEmptyMessage(123);
                            }
//                            text_bug.setVisibility(View.VISIBLE);
//                            imageCode.setVisibility(View.GONE);
//                            text_bug.setText("错误代码：" + entity.getCoffee_bug_state());
//                            textHint.setVisibility(View.GONE);
                        }

                        textHint.setVisibility(View.VISIBLE);
                        is_Produce = "0";
                        if (is_Complete.equals("1") && opt_type != null && sendSucceedInt == 2) {//放出去后，卡了
                            if (dbDeiveceData != null) {
                                List<DBShopData> dbShopDataList = new ArrayList<>();
//                                Utils.getCntrDataList(cntrData, dbShopDataList);
//                                dbDeiveceData.setCntr_ary(dbShopDataList);
                                String longitude = SharePerfenceUtils.getInstance(context).getLongitude();
                                String latitude = SharePerfenceUtils.getInstance(context).getAtitude();
                                if (longitude == null)
                                    longitude = "";
                                if (latitude == null)
                                    latitude = "";
                                dbDeiveceData.setLongitude(longitude);
                                dbDeiveceData.setLatitude(latitude);
                                sendData(order_RecentData, dbDeiveceData);//实时数据
                            }
                            updataOpt(opt_id, opt_type, "4");
                            sendBC(order_Complete, opt_id, opt_type, null);//完成
                            is_Complete = "0";
                        }

                        if (image_dit != null) {
                        } else {
                        }
                    } else {
                        codeHan.sendEmptyMessage(123);
                    }
                }
                //同时判断机器两个状态，都可用才能显示二维码
//                L.i("test1"+"Cntr_state: "+Cntr_state+" Cntr_transaction_state: "+Cntr_transaction_state
//                        +"Open_door: "+entity.getOpen_door()+" Sensor: "+entity.getSensor());

                //临时注释测试使用
//                if ((Cntr_state.equals("0") || Cntr_state.equals("00"))
//                        && (Cntr_transaction_state.equals("0") || Cntr_transaction_state.equals("00"))
//                        && entity.getOpen_door().equals("0")
//                        && entity.getSensor().equals("0")
//                        ) {
                if(true){
                    if (coffee_bug_state.equals("00") || coffee_bug_state.equals("0")) {
                        imageHler.sendEmptyMessage(123);
                        if (mSocket != null) {
                            text_bug.setVisibility(View.GONE);
                            imageCode.setVisibility(View.VISIBLE);
                            if (coffee_bug_state.equals("00") || coffee_bug_state.equals("0")) {
                                imageHler.sendEmptyMessage(123);
                                String temp11=textGoodsEerror.getText().toString();
                                if (temp11!=null&&temp11.length()>0)
                                {
                                    textGoodsEerror.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (isShowQRCode) {
                                    imageCode.setImageResource(R.drawable.device_bug);
                                    text_bug.setVisibility(View.GONE);
                                    text_bug.setText(getString(R.string.error_code) + "：" + coffee_bug_state);
                                    textHint.setText(getString(R.string.machine_stoppage));
                                } else {
                                    imageHler.sendEmptyMessage(123);
                                }
                            }
                        } else {
                            codeHan.sendEmptyMessage(123);
                        }
                    }
                }
//                else if (Cntr_state.equals("1")) {
//                    text_bug.setVisibility(View.GONE);
//                    imageCode.setVisibility(View.VISIBLE);
//                    imageCode.setImageResource(R.drawable.cup_image);
//                    textHint.setText(getString(R.string.making));
//                    textHint.setVisibility(View.VISIBLE);
//
//                }
                if (Cntr_transaction_state.equals("02") || Cntr_transaction_state.equals("2")) {
                    imageCode.setImageResource(R.drawable.device_bug);
                    text_bug.setVisibility(View.GONE);
                    textHint.setVisibility(View.VISIBLE);
                    textHint.setText(getString(R.string.cntr_success));

                    if (goodsErrorList.size()>0&&Sales_opt_type!=null) {
                        goodsErrorList.remove(Sales_opt_type);
                        handlerGoodsError.sendEmptyMessage(123);
                    }

                    //设备落饮回调
                    if (Sales_opt_id != null && Sales_opt_id.length() > 0) {
                        DataOptEntity dataOptEntity = new DataOptEntity();
                        dataOptEntity.setDevice_id(dataEntity.getDevice_id());
                        dataOptEntity.setOpt_id(Sales_opt_id);
                        //保存落饮状态
//                    Utils.updataCntrOpt(Sales_opt_id, Sales_opt_type, "2", dbUtils);
                        sendData2(CNTR_DROP_CB_1, dataOptEntity);
                        Sales_opt_id = "";
                        if (dbDeiveceData != null) {
//                        DBShopData data=new DBShopData();
//                        data=cntrData.get(Sales_opt_type);
//                        if (data!=null)
//                        {
//                            int i=Integer.parseInt(data.getCount());
//                            if (i>0)
//                            {
//                                data.setCount((i-1)+"");
//                            }
//                        }
//                        data.setNum(Sales_opt_type);
//                        data.setCount(Sales_opt_type);
//                        try {
//                            dbUtils.delete(DBShopData.class, WhereBuilder.b("num", "=", data.getNum()));
//                            dbUtils.save(data);
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }

//                        cntrData.put(Sales_opt_type, data);
//                        List<DBShopData> dataList = new ArrayList<>();
//                        Utils.getCntrDataList(cntrData, dataList);
//                        dbDeiveceData.setCntr_ary(dataList);
                            sendData(order_RecentData, dbDeiveceData);
                        }
                    }
                } else if (Cntr_transaction_state.equals("03") || Cntr_transaction_state.equals("3")) {
                    goods_error_time=5;
                    if (Sales_opt_id != null && Sales_opt_id.length() > 0) {
                        DataOptEntity dataOptEntity = new DataOptEntity();
                        dataOptEntity.setDevice_id(dataEntity.getDevice_id());
                        dataOptEntity.setOpt_id(Sales_opt_id);
                        //保存落饮状态
                        Utils.updataCntrOpt(Sales_opt_id, Sales_opt_type, "6", dbUtils);
                        sendData2(CNTR_DROP_FAILED_CB, dataOptEntity);
                        Sales_opt_id = "";
                    }
                }

//                if (entity.getOpen_door().equals("1")) {
//                    imageCode.setImageResource(R.drawable.device_bug);
//                    text_bug.setVisibility(View.VISIBLE);
//                    text_bug.setText(getString(R.string.open_door_error));
//                    textHint.setText(getString(R.string.machine_stoppage));
//                }
                if (Cntr_transaction_state.equals("2")) {
                    sensor = "0";
                }
//                if (sensor_state.equals("1")) {
//                    imageCode.setImageResource(R.drawable.device_bug);
//                    text_bug.setVisibility(View.GONE);
////                    text_bug.setText(getString(R.string.sensor_error));
//                    textHint.setVisibility(View.VISIBLE);
//                    textHint.setText(getString(R.string.sensor_error));
//                }

                if (sensor.equals("1")) {
                    imageCode.setImageResource(R.drawable.device_bug);
                    text_bug.setVisibility(View.GONE);
//                    text_bug.setText(getString(R.string.sensor_error));
                    textHint.setVisibility(View.VISIBLE);
                    textHint.setText(getString(R.string.sensor_error));
                }

                if (Cntr_state.equals("5") || Cntr_state.equals("05")) {
                    imageCode.setImageResource(R.drawable.device_bug);
                    text_bug.setVisibility(View.VISIBLE);
                    text_bug.setText(getString(R.string.goods_error) + Sales_opt_type);
                    textHint.setText(getString(R.string.machine_stoppage));
                    goods_error = 5;
                    testing_status = "2";

                    if (!goodsErrorList.contains(Sales_opt_type))
                    {
                        goodsErrorList.add(Sales_opt_type);
                    }
                    handlerGoodsError.sendEmptyMessage(123);

                    if (item_testing != -1 && testing_array != null && testing_array.size() > 0 && item_testing < testing_array.size()
                            && Sales_opt_type.equals(testing_array.get(item_testing).getNum())) {
                        testing_array.get(item_testing).setStatus("2");
                    }
//                    opt_id
                    if (Sales_opt_id != null && Sales_opt_id.length() > 0 && Sales_opt_type != null) {
                        DataOptEntity dataOptEntity = new DataOptEntity();
                        dataOptEntity.setDevice_id(dataEntity.getDevice_id());
                        dataOptEntity.setOpt_id(Sales_opt_id);
                        sendData2(ErrorMotorCB, dataOptEntity);
                        //保存落饮状态
                        Utils.updataCntrOpt(Sales_opt_id, Sales_opt_type, "7", dbUtils);
                        Sales_opt_id = "";
                    }
                } else if (Cntr_state.equals("0") || Cntr_state.equals("00")) {
                    if (item_testing != -1 && testing_array != null && testing_array.size() > 0 && item_testing < testing_array.size()
                            && Sales_opt_type.equals(testing_array.get(item_testing).getNum())
                            && !testing_status.equals("2")) {
                        testing_array.get(item_testing).setStatus("1");
                    }
                }
            } else {
            }
//            if (goods_error > 0) {//货道异常
//                goods_error--;
//                imageCode.setImageResource(R.drawable.device_bug);
//                text_bug.setVisibility(View.VISIBLE);
//                text_bug.setText(getString(R.string.goods_error) + Sales_opt_type);
//                textHint.setText(getString(R.string.machine_stoppage));
//
//                textGoodsEerror.setText(getString(R.string.goods_error) + Sales_opt_type);
//                textGoodsEerror.setVisibility(View.VISIBLE);
//            }

//            if (is_testing)//检测中
//            {
//                imageCode.setImageResource(R.drawable.device_bug);
//                text_bug.setVisibility(View.GONE);
//                textHint.setVisibility(View.VISIBLE);
//                textHint.setText(getString(R.string.device_self_test));
//            }
//            if (open_door != null && open_door.equals("1"))//前面是否打开
//            {
//                imageCode.setImageResource(R.drawable.device_bug);
//                text_bug.setVisibility(View.GONE);
//                textHint.setVisibility(View.VISIBLE);
//                textHint.setText(getString(R.string.open_door_inspection));
//            }
//
//            if (Cntr_transaction_state.equals("02") || Cntr_transaction_state.equals("2")) {
//                imageCode.setImageResource(R.drawable.device_bug);
//                text_bug.setVisibility(View.GONE);
//                textHint.setVisibility(View.VISIBLE);
//                textHint.setText(getString(R.string.cntr_success));
//                Utils.LogE("Cntr_transaction_state="+Cntr_transaction_state);
//
//            }
//
//            if ((Cntr_transaction_state.equals("03") || Cntr_transaction_state.equals("3"))||goods_error_time>0) {
//                goods_error_time--;
//                imageCode.setImageResource(R.drawable.device_bug);
//                text_bug.setVisibility(View.GONE);
//                textHint.setVisibility(View.VISIBLE);
//                textHint.setText(getString(R.string.cntr_failure));
//                Utils.LogE("Cntr_transaction_state=" + Cntr_transaction_state);
//            }
        }
    }

    Handler handlerGoodsError=  new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (goodsErrorList.size()>0)
            {
                String tempGoodsError=goodsErrorList.get(0);
                for (int i=1;i<goodsErrorList.size();i++)
                {
                    tempGoodsError+="、"+goodsErrorList.get(i);
                }
                textGoodsEerror.setText(getString(R.string.goods_error) + tempGoodsError);
            }
            else
            {
                textGoodsEerror.setText("");
            }
        }
    };

    private DBDeiveceData SavaData(SendbleEntity sendbleEntity) {
        DBDeiveceData dbDeiveceData = new DBDeiveceData();
        dbDeiveceData.setTime(System.currentTimeMillis() / 1000 + "");

        //滤芯滤网
        dbDeiveceData.setF_id_1(sendbleEntity.getF_id_1());
        dbDeiveceData.setF_id_2(sendbleEntity.getF_id_2());
        dbDeiveceData.setF_id_3(sendbleEntity.getF_id_3());
        dbDeiveceData.setF_id_4(sendbleEntity.getF_id_4());
        dbDeiveceData.setMesh_id(sendbleEntity.getMesh_id());
        dbDeiveceData.setF_r_1(sendbleEntity.getFilter_element());
        dbDeiveceData.setF_r_2(sendbleEntity.getFilter_element1());
        dbDeiveceData.setF_r_3(sendbleEntity.getFilter_element2());
        dbDeiveceData.setF_r_4(sendbleEntity.getFilter_element3());
        dbDeiveceData.setMesh_residual(sendbleEntity.getFilter_net());

        //饮料杯数
        dbDeiveceData.setCup_1(sendbleEntity.getDixie_cup());
        dbDeiveceData.setCup_2(sendbleEntity.getDixie_cup_1());
        dbDeiveceData.setCup_3(sendbleEntity.getDixie_cup_2());
        dbDeiveceData.setCup_4(sendbleEntity.getDixie_cup_3());
        dbDeiveceData.setCup_5(sendbleEntity.getDixie_cup_4());
        dbDeiveceData.setCup_6(sendbleEntity.getDixie_cup_5());
        dbDeiveceData.setCup_7(sendbleEntity.getDixie_cup_6());
        dbDeiveceData.setCup_8(sendbleEntity.getDixie_cup_7());
        dbDeiveceData.setCup_9(sendbleEntity.getDixie_cup_8());
        dbDeiveceData.setCup_10(sendbleEntity.getDixie_cup_9());

        //水机状态
        dbDeiveceData.setW_status(sendbleEntity.getWater_state());
        String Water_state = sendbleEntity.getWater_state();
        if (Water_state != null)
            if (Water_state.equals("00")) {
                dbDeiveceData.setIs_on("0");
                dbDeiveceData.setIs_manuf("0");
            } else if (Water_state.equals("01")) {
                dbDeiveceData.setIs_on("1");
                dbDeiveceData.setIs_manuf("0");
            } else if (Water_state.equals("11")) {
                dbDeiveceData.setIs_on("1");
                dbDeiveceData.setIs_manuf("1");
            } else if (Water_state.equals("03")) {
                dbDeiveceData.setIs_on("0");
                dbDeiveceData.setIs_manuf("0");
            } else {
                dbDeiveceData.setIs_on("0");
                dbDeiveceData.setIs_manuf("0");

            }
        dbDeiveceData.setC_status(sendbleEntity.getCoffee_state());
        dbDeiveceData.setC_error(sendbleEntity.getCoffee_bug_state());

        //环境状态
        dbDeiveceData.setPm25(sendbleEntity.getPm());
        dbDeiveceData.setTds_1(sendbleEntity.getTds());
        dbDeiveceData.setTds_2(sendbleEntity.getTsd());
        dbDeiveceData.setTemp(sendbleEntity.getTemperature());
        dbDeiveceData.setHumidity(sendbleEntity.getHumidity());
        dbDeiveceData.setVoc(sendbleEntity.getVoc());
        dbDeiveceData.setT2(sendbleEntity.getTemperature_t2());
        dbDeiveceData.setT2b(sendbleEntity.getTemperature_t2b());
        dbDeiveceData.setT3(sendbleEntity.getTemperature_t3());
        dbDeiveceData.setExhaust_temp(sendbleEntity.getTemperature_exhaust());

        dbDeiveceData.setH_w_temp(sendbleEntity.getTemperature_hot_water());
        dbDeiveceData.setC_w_temp(sendbleEntity.getTemperature_cold_water());
        dbDeiveceData.setWindspeed(sendbleEntity.getWindspeed());

        ////水位
        dbDeiveceData.setW_lv_1(sendbleEntity.getWater_level_1());
        dbDeiveceData.setW_lv_2(sendbleEntity.getWater_level_2());
        dbDeiveceData.setW_lv_3(sendbleEntity.getWater_level_3());

        //流量
        dbDeiveceData.setFlowrate_1(sendbleEntity.getFlow_1());
        dbDeiveceData.setFlowrate_2(sendbleEntity.getFlow_2());

        //
        dbDeiveceData.setBoot_times(sendbleEntity.getBoot_time());

        dbDeiveceData.setTemp_err(sendbleEntity.getTemp_err());
        dbDeiveceData.setOther_err(sendbleEntity.getOther_err());

        dbDeiveceData.setMac(sendbleEntity.getMac());
        dbDeiveceData.setNfc(sendbleEntity.getNfc());

        dbDeiveceData.setPowder_coffee(entity.getPowder_coffee());//咖啡机粉量
        dbDeiveceData.setPowder_tea(entity.getPowder_tea());
        dbDeiveceData.setPowder_fruit(entity.getPowder_fruit());

        dbDeiveceData.setWater_coffee(entity.getWater_coffee());//咖啡水量
        dbDeiveceData.setWater_tea(entity.getWater_tea());
        dbDeiveceData.setWater_fruit(entity.getWater_fruit());
        dbDeiveceData.setWater_hot_winter(entity.getWater_hot_winter());

        dbDeiveceData.setCntr_status(entity.getCntr_state());
        dbDeiveceData.setCntr_err(entity.getCntr_bug_state());


        if (device_id != null && device_id.length() > 0) {
            dbDeiveceData.setDevice_id(device_id);
        } else {
            DBDeviceIdData dbDeviceIdData = new DBDeviceIdData();
            try {
                dbDeviceIdData = dbUtils.findFirst(Selector.from(DBDeviceIdData.class));
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (dbDeviceIdData != null && dbDeviceIdData.getDevice_id() != null) {
                device_id = dbDeviceIdData.getDevice_id();
                dbDeiveceData.setDevice_id(dbDeviceIdData.getDevice_id());
            }
        }
        dbDeiveceData.setData_key(System.currentTimeMillis() + "_" + dbDeiveceData.getDevice_id());
        if (device_id != null) {
            try {
                dbUtils.delete(DBDeiveceData.class, WhereBuilder.b("id", ">", -1));
//                dbUtils.update(dbDeiveceData, WhereBuilder.b("device_id", "=", device_id));//保存到实时数据
                dbUtils.save(dbDeiveceData);//保存到实时数据

                //保存每小时数据
                DBHourDeiveceData dbHourDeiveceData = new DBHourDeiveceData();
                String json = JSON.toJSONString(dbDeiveceData);
                dbHourDeiveceData = JSON.parseObject(json, DBHourDeiveceData.class);
                if (dbHourDeiveceData != null) {
                    dbHourDeiveceData.setTime_hore(Utils.long2String(System.currentTimeMillis() / 1000, "yyyy-M-d HH"));
                    dbUtils.delete(DBHourDeiveceData.class, WhereBuilder.b("time_hore", "=", dbHourDeiveceData.getTime_hore()));
                    dbUtils.save(dbHourDeiveceData);
                }
//                if (dbHourDeiveceData.getCntr_ary() != null && dbDeiveceData.getCntr_status().length() > 0) {
//                    if (dbHourDeiveceData.getCntr_ary() != null && dbDeiveceData.getCntr_status().length() > 0) {
//                        dbHourDeiveceData.setCntr_ary_string(JSON.toJSONString(dbDeiveceData.getCntr_status()));
//                    } else {
//                        dbHourDeiveceData.setCntr_ary_string("");
//                    }
//                    dbHourDeiveceData.setTime_hore(Utils.long2String(System.currentTimeMillis() / 1000, "yyyy-M-d HH"));
//                    dbUtils.delete(DBHourDeiveceData.class, WhereBuilder.b("time_hore", "=", dbHourDeiveceData.getTime_hore()));
//                    dbUtils.save(dbHourDeiveceData);
//                }

            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        //保存和上传销售数据
//        if (!sendbleEntity.getCntr_num().equals("ff") && !sendbleEntity.getCntr_count().equals("ff")) {
//            DBShopData data = new DBShopData();
//            data.setNum(sendbleEntity.getCntr_num());
//            data.setCount(sendbleEntity.getCntr_count());
////            cntrData.put(sendbleEntity.getCntr_num(), data);
//            try {
//                boolean is_update = true;
//                DBShopData dataTemp = dbUtils.findFirst(Selector.from(DBShopData.class).where("num", "=", data.getNum()));
//                if (dataTemp != null) {
//                    if (dataTemp.getCount().equals(data.getCount())) {
//                        is_update = false;
//                    }
//                }
//                if (is_update) {
//                    dbUtils.delete(DBShopData.class, WhereBuilder.b("num", "=", data.getNum()));
//                    dbUtils.save(data);
//                    List<DBShopData> dbShopDataList = new ArrayList<>();
////                    Utils.getCntrDataList(cntrData, dbShopDataList);
////                    dbDeiveceData.setCntr_ary(dbShopDataList);
//                    //上传实时数据
//                    sendTCPData(order_RecentData, dbDeiveceData);//实时数据
//                }
//
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
//
//        }
//        cup_sum
        //保存和上传咖啡机杯料数据
        int temp = Utils.getcupSum(dbDeiveceData);
        if (cup_sum == -1)
            cup_sum = temp;
        if (temp != cup_sum) {
            //上传实时数据
            List<DBShopData> dbShopDataList = new ArrayList<>();
//            Utils.getCntrDataList(cntrData, dbShopDataList);
//            dbDeiveceData.setCntr_ary(dbShopDataList);
            sendData(order_RecentData, dbDeiveceData);//实时数据
        }
        return dbDeiveceData;
    }

    private void setRs232Bug() {
//        text_bug.setVisibility(View.VISIBLE);
//        imageCode.setVisibility(View.GONE);
//        text_bug.setText("错误,连接串口失败！");
    }

    public static String long2String(long time, String outFormate) {
        SimpleDateFormat formatter = new SimpleDateFormat(outFormate);
        Date curDate = new Date(time * 1000);
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-03-20 11:43:34 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        viewpager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.viewpager);
        ccccc = (FrameLayout) findViewById(R.id.ccccc);
        img = (ImageView) findViewById(R.id.img);
        task = (View) findViewById(R.id.task);
        pb = (ProgressBar) findViewById(R.id.pb);
        video = (VideoView) findViewById(R.id.video);
        imageCode = (ImageView) findViewById(R.id.image_code);
        imagePublicity = (ImageView) findViewById(R.id.image_publicity);
        ll_image_code = (LinearLayout) findViewById(R.id.ll_image_code);
        ll_auto = (LinearLayout) findViewById(R.id.ll_auto);
        llClass = (LinearLayout) findViewById(R.id.llClass);
        text_title = (TextView) findViewById(R.id.text_title);
        text_time = (TextView) findViewById(R.id.text_time);
        text_3g = (TextView) findViewById(R.id.text_3g);
        wifi_image = (ImageView) findViewById(R.id.wifi_image);
        text_bug = (TextView) findViewById(R.id.text_bug);
        textHint = (TextView) findViewById(R.id.textHint);
        barcode_image = (ImageView) findViewById(R.id.barcode_image);
        fl_image = (LinearLayout) findViewById(R.id.fl_image);
        textShow = (TextView) findViewById(R.id.textShow);
        textGoodsEerror = (TextView) findViewById(R.id.textGoodsEerror);


        textNumber = (TextView) findViewById(R.id.textNumber);
        llAdvertising = (LinearLayout) findViewById(R.id.llAdvertising);

        textGood1 = (TextView) findViewById(R.id.textGood1);
        textGood2 = (TextView) findViewById(R.id.textGood2);
        textGood3 = (TextView) findViewById(R.id.textGood3);
        textGood4 = (TextView) findViewById(R.id.textGood4);
//        textAuto.setText("福能达空气水饮水机");
//        textAuto.init(getWindowManager());
//        textAuto.startScroll();

        Long time = System.currentTimeMillis() / 1000;
        String timeString = long2String(time, "yyyy-MM-dd HH:mm");
        text_time.setText(timeString);
        new TimeThread().start();//时间计时器


        width = DrawUtil.getScreenSize(context)[0];
        Height = DrawUtil.getScreenSize(context)[1];

//        if (Height < width) {
//            Height = width;
//            width = DrawUtil.getScreenSize(context)[1];
//        }


////        Toast.makeText(context, "width=" + width + "   Height= " + Height + "   (int) (width / 1920 * 320)=" + (int) (width / 1920 * 320), Toast.LENGTH_LONG).show();  //758 480
//        LinearLayout.LayoutParams layoutParmas = new LinearLayout.LayoutParams((int) (width / 3), (int) (width / 3));
//        imageCode.setLayoutParams(layoutParmas);
////        imageCode.setPadding(20, 20, 20, 20);
//
//
//        LinearLayout.LayoutParams layoutParmasImage = new LinearLayout.LayoutParams((int) (width / 3), (int) (width / 3));
//        barcode_image.setLayoutParams(layoutParmasImage);
////        barcode_image.setPadding(20, 20, 20, 20);
//
//        LinearLayout.LayoutParams layoutParmasfl = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        fl_image.setLayoutParams(layoutParmasfl);
//
//
////        LinearLayout.LayoutParams layoutParmas1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Height / 1920 * 90));
////        ll_auto.setLayoutParams(layoutParmas1);
//
////        LinearLayout.LayoutParams llAllparams = new LinearLayout.LayoutParams((int) (width / 1920 * 520), ViewGroup.LayoutParams.MATCH_PARENT);
////        ll_image_code.setLayoutParams(llAllparams);
//        LinearLayout.LayoutParams llAllparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width / 1390 * 1000));
//        llAdvertising.setLayoutParams(llAllparams);
//
//        LinearLayout.LayoutParams layoutParmasClass = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Height * 0.08));
//        int temp = (int) (Height * 0.10) / 3;
//        layoutParmasClass.setMargins(temp, temp, temp, temp);
//        llClass.setLayoutParams(layoutParmasClass);


        LinearLayout.LayoutParams layoutParmas = new LinearLayout.LayoutParams((int) (width / 1920 * 350), (int) (width / 1920 * 350));
        imageCode.setLayoutParams(layoutParmas);
        imageCode.setPadding(20, 20, 20, 20);

//        LinearLayout.LayoutParams layoutParmasImage = new LinearLayout.LayoutParams((int) (width / 1920 * 350), (int) (width / 1920 * 350));
//        barcode_image.setLayoutParams(layoutParmasImage);
//        barcode_image.setPadding(20, 20, 20, 20);

//        LinearLayout.LayoutParams layoutParmasfl = new LinearLayout.LayoutParams((int) (width / 1920 * 350), (int) (width / 1920 * 350));
//        fl_image.setLayoutParams(layoutParmasfl);


//        LinearLayout.LayoutParams layoutParmas1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Height / 1080 * 90));
//        ll_auto.setLayoutParams(layoutParmas1);

//        LinearLayout.LayoutParams llAllparams = new LinearLayout.LayoutParams((int) (width / 1920 * 520), ViewGroup.LayoutParams.MATCH_PARENT);
//        ll_image_code.setLayoutParams(llAllparams);

        text_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Container_array temp = new Container_array();
                temp.setNum("1");
                temp.setStatus("");

                Container_array temp2 = new Container_array();
                temp2.setNum("2");
                temp2.setStatus("");
                List<Container_array> testing_array = new ArrayList<Container_array>();
                testing_array.add(temp);
                testing_array.add(temp2);
                timerTesting(testing_array);
            }
        });

        textNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayData(" aa 55 ff 20 00 00 00 8b 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0b b8 20 8d 11 94 06 82 00" +
                        " 00 11 1b 00 03 01 00 00 00 02 02 03 00 ff ff ff 11 00 00 00 a1 08 07 1d 2e 00 ff 0c 0c 1c 53 ff ff ff ff 04 ff ff ff" +
                        " 00 02 00 00 09 b6 ff ff 20 00 ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff 00 00 0f ff 00 00 00" +
                        " 00 00 00 0b 28 70 2d 22 41 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +
                        " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +//倒数第四个
                        " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +
//                        " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +
                        " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 50 05 ff ff");
            }
        });

        imageCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(null== testProductsDialog){
                    testProductsDialog =new TestProductsDialog(CoffeeRs232Activity.this,testHandler);
                }
                testProductsDialog.show();
                testHandler.sendEmptyMessageDelayed(TestProductsDialog.delayDismiss,TestProductsDialog.delayTime);
//                entity.setCoffee_switch("11");//
//                entity.setTea_switch("11");//
//                entity.setFruit_switch("11");//
//                entity.setWater_switch("00");//
//                senWrite(entity, " ffffffff "); //37  39
//
//                displayData(" aa 55 ff 20 00 00 00 8b 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0b b8 20 8d 11 94 06 82 00" +
//                        " 00 11 1b 00 03 01 00 00 00 02 02 03 00 ff ff ff 11 00 00 00 a1 08 07 1d 2e 00 ff 0c 0c 1c 53 ff ff ff ff 04 ff ff ff" +
//                        " 00 02 00 00 09 b6 ff ff 20 00 ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff 00 00 0f ff 00 00 00" +
//                        " 00 00 00 0b 28 70 2d 22 41 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +
//                        " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +//倒数第四个
//                        " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +
////                        " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +
//                        " 00 00 00 00 00 05 00 00 ff 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 50 05 ff ff");

//                System.exit(0);
//                Intent i = getBaseContext().getPackageManager()
//                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//                isConnect = false;
            }
        });
        imagePublicity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entity.setCoffee_switch("ff");//
                entity.setTea_switch("ff");//
                entity.setFruit_switch("ff");//
                entity.setWater_switch("00");//
                senWrite(entity, " ffffffff ");
            }
        });
//        new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(4000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        setViewHandler.sendEmptyMessage(123);
//                    }
//                }).start();
    }

    private Handler setViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LinearLayout.LayoutParams layoutParmas = new LinearLayout.LayoutParams((int) (width / 3), (int) (width / 3));
            imageCode.setLayoutParams(layoutParmas);
            LinearLayout.LayoutParams layoutParmasImage = new LinearLayout.LayoutParams((int) (width / 3), (int) (width / 3));
            barcode_image.setLayoutParams(layoutParmasImage);

            LinearLayout.LayoutParams layoutParmasfl = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            fl_image.setLayoutParams(layoutParmasfl);

            RelativeLayout.LayoutParams llAllparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width / 1390 * 1000));
            llAdvertising.setLayoutParams(llAllparams);

            LinearLayout.LayoutParams layoutParmasClass = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Height * 0.08));
            int temp = (int) (Height * 0.10) / 3;
            layoutParmasClass.setMargins(temp, temp, temp, temp);
            llClass.setLayoutParams(layoutParmasClass);
        }
    };


    //实时显示时间
    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
//                    msg.what = msgKey1;
                    mTimeHandler.sendEmptyMessage(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            long sysTime = System.currentTimeMillis();
            CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", sysTime);
//            Utils.LogE("sysTimeStr=" + sysTimeStr);
            text_time.setText(sysTimeStr);
            if (sysTimeStr.toString().contains("00:00:00")) {
                senHandler.sendEmptyMessage(100);
            }
            ruleCount++;
            if (ruleCount >= 60) {//每分钟判断是否更换广告
                ruleCount = 0;
                getRule(sysTime / 1000);
            }
//            if (mSocket != null) {
//                Utils.LogE("isClosed()=" + mSocket.get().isClosed() + "    isConnected()=" + mSocket.get().isConnected() + "    isInputShutdown()=" + mSocket.get().isInputShutdown() + "    isOutputShutdown()=" + mSocket.get().isOutputShutdown());
//            }
//            isShowQRCode
            if (!isShowQRCode) {
                if (showQRCodeTime > 0) {
                    showQRCodeTime--;
                    Utils.LogE("showQRCodeTime=" + showQRCodeTime);
                    if (showQRCodeTime == 0) {
                        isShowQRCode = true;
                    }
                }
            } else {

            }
//            Utils.LogE("sysTimeStr=" + sysTimeStr);
        }
    };

    //每一分钟都查询广告数据
    private int ruleCount = 0;


    //控制设备
    private void senWrite(SendbleEntity entity, String Sendevice_id) {

        String command = System.currentTimeMillis() / 1000 + "";
        entity.setCommand(command.substring(2));
        String Temp = Utils.setSendData2(entity, Sendevice_id,serial_number);
//        Toast.makeText(context, Temp, Toast.LENGTH_LONG).show();
        L.i("senWrite-Temp: "+Temp);
        try {
            if (mOutputStream != null) {
                mOutputStream.write(Utils.setbyte(Temp));
                mOutputStream.write('\n');
//                ProduceDrink_count++;
//                Utils.LogE("ProduceDrink_count=" + ProduceDrink_count);
//                if (textShow != null)
//                    textShow.setText("ProduceDrink_count=" + ProduceDrink_count);
            } else {
                setRs232Bug();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler testHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case TestProductsDialog.delayDismiss:
                    testProductsDialog.dismiss();
                    break;
                case TestProductsDialog.shipment:
                    int position=msg.arg1;
                    String command = System.currentTimeMillis() / 1000 + "";
                    entity.setCommand(command.substring(2));
                    String temp=Utils.setSendAVM(entity,"ffffffff",position);
                    L.i("test:sendtemp:"+temp);
                    try {
                        if (mOutputStream != null) {
                            mOutputStream.write(Utils.setbyte(temp));
                            mOutputStream.write('\n');
                        } else {
                            setRs232Bug();
                        }
                    } catch (IOException e) {
                        L.i("sendtestdata error"+e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
            return false;
        }
    });

    Handler senHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
//            Utils.LogE("message.arg1=" + message.arg1 + "  message.what=" + message.what);
//            Toast.makeText(context, "message.arg1=" + message.arg1 + "  message.what=" + message.what, Toast.LENGTH_LONG).show();
            entity.setCoffee_switch("ff");//
            entity.setTea_switch("ff");//
            entity.setFruit_switch("ff");//
            entity.setWater_switch("ff");//
            entity.setWater_for("ff");//

            entity.setDixie_cup("ffff");   //咖啡
            entity.setDixie_cup_1("ffff");  //奶茶
            entity.setDixie_cup_2("ffff");  //果汁
            entity.setDixie_cup_3("ffff");  //冷热水

            entity.setWater_coffee("ffff");
            entity.setWater_tea("ffff");
            entity.setWater_hot_winter_hot("ffff");
            entity.setWater_hot_winter("ffff");
            entity.setSales("ff");
            entity.setSensor("ff");
//            Sales_opt_type
            switch (message.what) {
                case 1:
                    entity.setCoffee_switch("01");//热咖啡
                    break;
                case 2:
                    entity.setCoffee_switch("02");//
                    break;
                case 3:
                    entity.setTea_switch("01");//热奶茶
                    break;
                case 4:
                    entity.setTea_switch("02");//
                    break;
                case 5:
                    entity.setFruit_switch("01");//热果汁
                    break;
                case 6:
                    entity.setFruit_switch("02");//
                    break;
                case 7:
                    entity.setWater_switch("01");//热水
                    break;
                case 8:
                    entity.setWater_switch("02");//冷水
                    break;
                case 9:
                    entity.setWater_switch("02");//温水   没有常温水了，就出冷水
                    break;
                case 100:
                    entity.setWater_for("11");//水循环
                    break;
                case 101://粉量，水量
                    if (oldDrinksData != null) {
                        List<Drinks_array> drinks_array = null;
                        if (oldDrinksData.getDrinks_array() != null && oldDrinksData.getDrinks_array().size() > 0) {
                            drinks_array = oldDrinksData.getDrinks_array();
                            for (int i = 0; i < drinks_array.size(); i++) {
                                switch (drinks_array.get(i).getOpt_type()) {
                                    case "1":
                                        entity.setDixie_cup(Utils.Analysis(drinks_array.get(i).getPowder()));   //咖啡
                                        break;
                                    case "3":
                                        entity.setDixie_cup_1(Utils.Analysis(drinks_array.get(i).getPowder()));   //奶茶
                                        break;
//                                    case "7":
//                                        entity.setDixie_cup_2(Utils.Analysis(drinks_array.get(i).getPowder()));   //
//                                        break;

                                    case "9":
                                        entity.setDixie_cup_3("ffff");   //
                                        break;
                                }
                            }
                        }
                    }
                    break;
                case 102:
                    if (oldDrinksData != null) {
                        List<Drinks_array> drinks_array = null;
                        if (oldDrinksData.getDrinks_array() != null && oldDrinksData.getDrinks_array().size() > 0) {
                            drinks_array = oldDrinksData.getDrinks_array();
                            for (int i = 0; i < drinks_array.size(); i++) {
                                switch (drinks_array.get(i).getOpt_type()) {
                                    case "1":
                                        entity.setWater_coffee(Utils.Analysis(drinks_array.get(i).getWater_yield()));
                                        break;
                                    case "3":
                                        entity.setWater_tea(Utils.Analysis(drinks_array.get(i).getWater_yield()));//
                                        break;
                                    case "7":
                                        entity.setWater_hot_winter_hot(Utils.Analysis(drinks_array.get(i).getWater_yield()));//
                                        break;
                                    case "9":
                                        entity.setWater_hot_winter(Utils.Analysis(drinks_array.get(i).getWater_yield()));//
                                        break;
                                }
                            }
                        }
                    }
                    break;
                case 201:
                    String sales = Integer.toHexString(Integer.parseInt(Sales_opt_type));
                    entity.setSales(sales);
                    break;
                case 301:
                    entity.setSensor("01");
                    break;
            }
//            if (message.what != 101)
            senWrite(entity, " ffffffff ");
//            if (message.what == 1) {
//                entity.setCoffee_switch("11");//
//                entity.setTea_switch("11");//
//                entity.setFruit_switch("11");//
//                entity.setWater_switch("11");//
//
//            } else {
//                entity.setCoffee_switch("11");//
//                entity.setTea_switch("11");//
//                entity.setFruit_switch("11");//
//                entity.setWater_switch("11");//
//                senWrite(entity, "ff ff ff ff");
//            }
            return false;
        }
    });


    @SuppressLint("NewApi")
    private void initialize() {
        views.clear();
        videoViews.clear();

        // 将最后一个ImageView添加进来
        views.add(getImageView(this, infos.get(infos.size() - 1)));
        for (int i = 0; i < infos.size(); i++) {
            views.add(getImageView(this, infos.get(i)));
        }
        // 将第一个ImageView添加进来
        views.add(getImageView(this, infos.get(0)));

        // 设置循环，在调用setData方法前调用
        viewpager.setCycle(true);

        // 在加载数据前设置是否循环
        viewpager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        viewpager.setWheel(true);

        // 设置轮播时间，默认5000ms
        viewpager.setTime(5000);

        //设置圆点指示图标组居中显示，默认靠右
        viewpager.setIndicatorCenter();

        //高度释放
        viewpager.releaseHeight();
    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (viewpager.isCycle()) {
            }
        }

        @Override
        public void onIndicator(ADInfo info, final int postio) {
            videoHandler.sendEmptyMessage(postio);
//            if (postio < videoViews.size()) {
//                VideoView videoView = (VideoView) views.get(postio).findViewById(R.id.videoView);
//                videoViews.get(postio).setVideoPath(infos.get(postio).getPathUrl());
//                videoViews.get(postio).start();
//                videoViews.get(postio).requestFocus();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
////                    sendWater();
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        videoHandler.sendEmptyMessage(postio);
//                    }
//                }).start();

//            }
        }
    };


//    private Handler videoHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            int postio = msg.what + 1;
//            if (postio < videoViews.size()) {
//                videoViews.get(postio).setVisibility(View.VISIBLE);
////                Utils.LogE("postio=" + infos.get(postio - 1).getPathUrl());
//                videoViews.get(postio).setVideoPath(infos.get(postio - 1).getPathUrl());
//                videoViews.get(postio).start();
//            }
//
//        }
//    };

    private Handler videoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final int postio = msg.what + 1;
            if (postio < videoViews.size()) {
                String url = videoViews.get(postio).getUrl();
                if (url != null && url.length() > 0) {
//                    videoViews.get(postio).setVisibility(View.GONE);
//                    videoViews.get(postio).setVisibility(View.VISIBLE);
                    videoViews.get(postio).pause();
//                    videoViews.get(postio).setVideoPath(infos.get(postio - 1).getPathUrl());
                    videoViews.get(postio).reOpen();
//                    videoViews.get(postio).start();
                } else {
                    videoViews.get(postio).setVideoPath(infos.get(postio - 1).getPathUrl());
                    videoViews.get(postio).start();
                }

                videoViews.get(postio).setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Utils.LogE("setOnErrorListener");
                        videoViews.get(postio).release();
                        videoViews.get(postio).setVisibility(View.GONE);
                        videoViews.get(postio).setVisibility(View.VISIBLE);
                        videoViews.get(postio).setVideoPath(infos.get(postio - 1).getPathUrl());
                        videoViews.get(postio).start();
                        return false;
                    }
                });
                videoViews.get(postio).setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoViews.get(postio).start();
//                    record_preview.setLooping(true);
                    }
                });
                videoViews.get(postio).setOnPlayStateListener(new com.doing.flat.coffee.ui.VideoView.OnPlayStateListener() {
                    @Override
                    public void onStateChanged(boolean isPlaying) {

                    }

                    @Override
                    public void onDuration(int duration) {
//                        String temp = duration / 1000 + "";//
//                        if (duration > 0 && infos.get(postio - 1).getFile_type().equals("2") && !temp.equals(infos.get(postio - 1).getRemain_time())) {
//                            infos.get(postio - 1).setRemain_time(temp);
////                            viewpagerHandler.sendEmptyMessage(duration / 1000);
//                            try {
//                                dbUtils.update(infos.get(postio - 1), WhereBuilder.b("id", "=", infos.get(postio - 1).getId()).and("file_type", "=", "2"), "remain_time");
//                            } catch (DbException e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }
                });
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            outApp();
        }
        return true;
    }


    public View getImageView(Context context, ADInfo urlInt) {
        View bannerView = LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);

        ImageView imageView = (ImageView) bannerView.findViewById(R.id.imageView);
        FrameLayout frameLayout = (FrameLayout) bannerView.findViewById(R.id.ccccc);
//        com.doing.flat.coffee.ui.VideoView
//        com.doing.flat.coffee.ui.VideoView videoView = (com.doing.flat.coffee.ui.VideoView) bannerView.findViewById(R.id.videoView);
        imageView.setVisibility(View.GONE);
//        videoView.setVisibility(View.GONE);

        final com.doing.flat.coffee.ui.VideoView record_preview = new com.doing.flat.coffee.ui.VideoView(context, null);
//        record_preview.setAlpha(0.5f);
        record_preview.setBackgroundResource(R.color.colorb);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        record_preview.setLayoutParams(layoutParams);
        videoViews.add(record_preview);

        Utils.LogE("url=" + urlInt.getPathUrl());
        if (urlInt.getFile_type().equals("1")) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Utils.setCanReplyImage(urlInt.getPathUrl(), imageView);
        } else {
            frameLayout.addView(record_preview, 0);
            record_preview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    record_preview.reOpen();
                    record_preview.start();
                    return false;
                }
            });
            record_preview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    record_preview.start();
                    record_preview.setLooping(true);
                }
            });
        }
        return bannerView;
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
            // 两次按键小于3秒时，退出应用
            sharedPreferences.edit().putBoolean("fristIn", true).commit();
            finish();
            System.exit(0);
        }
    }

    private static final long HEART_BEAT_RATE = 5 * 1000;
    public static final String HOST = HttpUrls.SOCKETHOST;
    //    public static final String HOST = "airwater.fndtech.com";// "192.168.1.21";//
    //    public static final String HOST = "120.25.235.111";// "192.168.1.21";//
    public static final int PORT = HttpUrls.SOCKETPORT;

    public static final String SEND_STR = "send_string";
    public static final String SEND_STR_REFRESH = "send_string_refresh";
    private int atInt = 0;
    private int sendSucceedInt = 0;//发送几次才发送成功

    private ReadThread mReadThread;


    private WeakReference<Socket> mSocket;


    private long sendTime = 0L;
    private Handler mHandler = new Handler();
    boolean isSuccess = false;
    private Runnable heartBeatRunnable = new Runnable() {

        @Override
        public void run() {
            Utils.LogE("heartBeatRunnable=timeResultDeiveceData=  sendTime="+sendTime+"  "+System.currentTimeMillis()+
                    "   "+(System.currentTimeMillis() - sendTime));
            if (System.currentTimeMillis() - sendTime >= 4000) {
                Utils.LogE("heartBeatRunnable=timeResultDeiveceData=  sendTime="+sendTime);
//                atInt++;
                isSuccess = sendMsg("@\n");////就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
                if (atInt == 1)//判断如果发送了1个@,没有回我@的话，就断开重连
                {
                    isSuccess = false;
                    socketStat = false;
                    atInt = 0;
                }
                atInt = 1;
                Long temp = System.currentTimeMillis() / 1000;
                if (temp - timeResultDeiveceData > 8) { //如果现在的时间大于机器最后一次上传数据5秒，就判断为异常
                    bugdeiveceHan.sendEmptyMessage(123);
                }
                Utils.LogE("temp=" + temp + "  timeResultDeiveceData=" + timeResultDeiveceData);

                if (!isSuccess) {
                    Utils.LogE("isSuccess=" + isSuccess + "  socketStat=" + socketStat);
                    setBUG();
                    mHandler.removeCallbacks(heartBeatRunnable);
                    mReadThread.release();
                    releaseLastSocket(mSocket);
                    mSocket = null;
                    if (!socketStat)
                        new InitSocketThread().start();
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    public boolean sendMsg(final String msg) {
        if (msg.equals(SEND_STR)) {
            Log.e("msg.equals(SEND_STR)=", SEND_STR);
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    sendWater();
                    sendNow(order_bindDevice);
                }
            }).start();
        } else if (msg.equals(SEND_STR_REFRESH)) {//
//            Log.e("msg.equals(SEND_STR_REFRESH)=", SEND_STR_REFRESH);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendNow(order_bindDevice);
                }
            }).start();
        } else {
//            Log.e("msg.equals(SEND_STR_REFRESH)=", SEND_STR_REFRESH);
            Log.e("msg.equals(SEND_STR)=", "0");
            if (mSocket == null)
                return false;
            Log.e("msg.equals(SEND_STR)=", "1");
            boolean is_data=false;
            DataOptEntity dataEntity1 = new DataOptEntity();
            //查询本地是否有未制作完成的命令
            List<DBOptEntity> dbOptEntityList = new ArrayList<>();
            dbOptEntityList = getloclOpt();
            List<DBCntrOptEntity> dbCntrOptEntityList = null;
            dbCntrOptEntityList = Utils.getloclCntrOpt(dbUtils);

            if ((dbOptEntityList != null && dbOptEntityList.size() > 0)||(dbCntrOptEntityList != null && dbCntrOptEntityList.size() > 0)) {
                List<String> complete_array = new ArrayList<>();
                List<String> produce_array = new ArrayList<>();
                List<String> failed_array = new ArrayList<>();
                List<String> err_motor_array = new ArrayList<>();

                List<String> order_array = new ArrayList<>();
                List<String> drop_array = new ArrayList<>();

                dataEntity1.setOrder_array(order_array);
                dataEntity1.setProduce_array(produce_array);
                dataEntity1.setComplete_array(complete_array);
                dataEntity1.setFailed_array(failed_array);
                dataEntity1.setErr_motor_array(err_motor_array);

                dataEntity1.setOrder_array(order_array);
                dataEntity1.setDrop_array(drop_array);

                if (dbOptEntityList != null && dbOptEntityList.size() > 0) {
                    is_data = true;
                    if (dataEntity != null && dataEntity.getDevice_id() != null)
                        dataEntity1.setDevice_id(dataEntity.getDevice_id());

                    for (int i = 0; i < dbOptEntityList.size(); i++) {
                        if (dbOptEntityList.get(i).getOpt_id() != null && dbOptEntityList.get(i).getOpt_id().length() > 0) {
//                        if (dbOptEntityList.get(i).getType().equals("0")) {
//                            order_array.add(dbOptEntityList.get(i).getOpt_id());
//                        } else if (dbOptEntityList.get(i).getType().equals("2")) {
//                            produce_array.add(dbOptEntityList.get(i).getOpt_id());
//                        } else if (dbOptEntityList.get(i).getType().equals("4")) {
//                            complete_array.add(dbOptEntityList.get(i).getOpt_id());
//                        }
                            if (dbOptEntityList.get(i).getType().equals("0")) {
                                failed_array.add(dbOptEntityList.get(i).getOpt_id());
                            }
                            if (dbOptEntityList.get(i).getType().equals("7")) {
                                err_motor_array.add(dbOptEntityList.get(i).getOpt_id());
                            }
                        }
//                    complete_array.add(dbOptEntityList.get(i).getType());
//                    produce_array.add(dbOptEntityList.get(i).getType());
                    }
                    dataEntity1.setOrder_array(order_array);
                    dataEntity1.setProduce_array(produce_array);
                    dataEntity1.setComplete_array(complete_array);
                    dataEntity1.setFailed_array(failed_array);
                    dataEntity1.setErr_motor_array(err_motor_array);
//                boolean temp = sendData2(CRTR_OPT_CB, dataEntity1);
//                sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
//                return temp;
                }
                if (dbCntrOptEntityList != null && dbCntrOptEntityList.size() > 0) {
                    is_data=true;
                    if (dataEntity != null && dataEntity.getDevice_id() != null)
                        dataEntity1.setDevice_id(dataEntity.getDevice_id());
                    for (int i = 0; i < dbCntrOptEntityList.size(); i++) {
                        if (dbCntrOptEntityList.get(i).getOpt_id() != null && dbCntrOptEntityList.get(i).getOpt_id().length() > 0) {
                            if (dbCntrOptEntityList.get(i).getType().equals("0")) {
                                order_array.add(dbCntrOptEntityList.get(i).getOpt_id());
                                drop_array.add(dbCntrOptEntityList.get(i).getNumber());
                            }
                        }
                    }
                    dataEntity1.setOrder_array(order_array);
                    dataEntity1.setDrop_array(drop_array);
                }
                boolean temp = sendData2(CRTR_OPT_CB, dataEntity1);
                sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
                return temp;
            }
            Log.e("msg.equals(SEND_STR)=", "2");
            //断线操作回调

            Log.e("msg.equals(SEND_STR)=", "3");
            Socket soc = mSocket.get();
            try {
                if (soc != null && !soc.isClosed() && !soc.isOutputShutdown()) {
                    OutputStream os = soc.getOutputStream();
                    String message = msg + "\n";
                    os.write(message.getBytes("utf-8"));
                    os.flush();
                    sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
                } else {
//                    Log.e("msg.equals(SEND_STR_REFRESH)=", "soc");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
//                Log.e("msg.equals(SEND_STR_REFRESH)=", "e=" + e.getMessage());
                return false;
            }
            return true;
        }
        return true;
    }

    private boolean socketStat = false;

    private void initSocket() {//初始化Socket0
        try {
            socketStat = true;
            Socket so = new Socket(HOST, PORT);
            mSocket = new WeakReference<Socket>(so);
            mReadThread = new ReadThread(so);
            mReadThread.start();
            Utils.LogE(" sendMsg(SEND_STR_REFRESH);");
            isSuccess = true;
            sendMsg(SEND_STR_REFRESH);//发送
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
        } catch (UnknownHostException e) {
            e.printStackTrace();
            socketStat = false;
            mSocket = null;
            L.i("initSocket-error:"+e.getMessage());
            setBUG();
        } catch (IOException e) {
            e.printStackTrace();
            socketStat = false;
            mSocket = null;
            L.i("initSocket-error:"+e.getMessage());
            setBUG();
        }
    }

    private void setBUG() { //明天需要改的地方
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        codeHan.sendEmptyMessage(123);
//            }
//        }).start();

    }

    Handler codeHan = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (isShowQRCode) {
                L.i("codeHan==isShowQRCode");
                //yanshi临时注释
//                imageCode.setImageResource(R.drawable.device_bug);
//                textHint.setText(getString(R.string.network_error));
            } else {
                imageHler.sendEmptyMessage(123);
            }
//            textHint.setText("机器故障,请稍后再来");
            return false;
        }
    });
    Handler bugdeiveceHan = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (isShowQRCode) {
                //yanshi临时注释
//                imageCode.setImageResource(R.drawable.device_bug);
//                textHint.setVisibility(View.VISIBLE);
//                textHint.setText(getString(R.string.machine_stoppage_port));
            } else {
                imageHler.sendEmptyMessage(123);
            }
//            textHint.setText("机器故障");
//            textHint.setText("机器故障,请稍后再来");
            return false;
        }
    });

    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (sk != null && !sk.isClosed()) {
                    sk.close();
                }
//                sk = null;
//                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //修改或者保存
    private void updataOpt(String opt_id, String opt_type, String type) {
        if (opt_id != null && type != null && opt_id.length() > 0) {
            DBOptEntity dbOptEntity = new DBOptEntity();
            dbOptEntity.setOpt_id(opt_id);
            if (opt_type != null)
                dbOptEntity.setOpt_type(opt_type);
            dbOptEntity.setType(type);
            DBOptEntity temp = null;
            try {
                temp = dbUtils.findFirst(Selector.from(DBOptEntity.class).where("opt_id", "=", opt_id));
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

    private List<DBOptEntity> getloclOpt() {
        List<DBOptEntity> temp = new ArrayList<>();
        try {
//            temp = dbUtils.findAll(Selector.from(DBOptEntity.class).where(WhereBuilder.b("type", "=", "0").or("type", "=", "4").or("type", "=", "2")));
//            temp = dbUtils.findAll(Selector.from(DBOptEntity.class).where("type", "=", "6"));
            temp = dbUtils.findAll(Selector.from(DBOptEntity.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
//        Utils.LogE("temp.size()=" + temp.size());
        return temp;
    }


    private String toast_connet = "";
    Handler toastHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(context, toast_connet, Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    private DataEntity dataEntity = null;
    private Bitmap image_dit = null;
    private String opt_id = "";
    private String opt_type = "";
    private String image_path = "";
    private String image_qc = "";
    private String Sales_opt_type = "1";//出货数量
    private String Sales_opt_id = "";

    private long ProduceDrink_count = 0;

    class InitSocketThread extends Thread {

        @Override
        public void run() {
            super.run();
            if (isConnect)
                initSocket();
        }
    }

    // Thread to read content from Socket
    class ReadThread extends Thread {
        private WeakReference<Socket> mWeakSocket;
        private boolean isStart = true;

        public ReadThread(Socket socket) {
            mWeakSocket = new WeakReference<Socket>(socket);
        }

        public void release() {
            isStart = false;
            releaseLastSocket(mWeakSocket);
        }

        @Override
        public void run() {
            super.run();
            Socket socket = mWeakSocket.get();
            if (null != socket) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    int length = 0;
                    String connet = null;
                    while (!socket.isClosed() && !socket.isInputShutdown()
                            && isStart && (connet = br.readLine()) != null) {
                        length = connet.length();
                        if (length > 0) {
                            atInt = 0;
                            ////收到服务器过来的消息，就通过Broadcast发送出去
                            if (connet.equals("@")) {//处理心跳回复
                            } else {
                                L.i("ReadTCPThread:"+" mWeakSocket.get()=" + connet);
//                                toast_connet = connet;
//                                toastHandler.sendEmptyMessage(123);
//                                ResultWaterEntity waterEntity = null;
//                                ResultWaterErrorEntity waterErrorEntity = null;
                                JSONObject object = null;
                                if (connet.startsWith("{") && connet.contains("result")) {
                                    object = JSON.parseObject(connet);
                                }
//                                if (operation != null && operation.equals(order_Callback))
//                                    return;
                                if (object != null && object.getString("result").equals("0")) {
                                    String operation = object.getString("operation");
                                    if (operation.equals(order_ProduceDrink)) {//出水
                                        if (object.getString("result").equals("0")) {
                                            JSONObject data = object.getJSONObject("data");
                                            opt_id = data.getString("opt_id");
                                            opt_type = data.getString("opt_type");
                                            updataOpt(opt_id, opt_type, "0");
                                            sendBC(order_Callback, opt_id, opt_type, null);//出水命令返回回调
                                            sendSucceedInt = 0;
                                            if (opt_type != null) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
//                                                        try {
                                                        if (opt_type != null && opt_type.length() > 0) {
                                                            sendSucceedInt++;
                                                            senHandler.sendEmptyMessage(Integer.parseInt(opt_type));
                                                            try {
                                                                Thread.sleep(4000);
                                                                sendSucceedInt++;
                                                                if (opt_type != null) {
                                                                    senHandler.sendEmptyMessage(Integer.parseInt(opt_type));
                                                                }
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }).start();
                                            }
                                        }
                                    } else if (operation.equals(order_bindDevice))//绑定设备
                                    {
                                        String data = object.getString("data");
                                        if (data != null) {
                                            DataEntity tempData = null;
                                            tempData = JSON.parseObject(data, DataEntity.class);
                                            dataEntity = tempData;
//                                            android_id=tempData.getDevice_id();
                                            CrashReport.setUserId(dataEntity.getDevice_id());
                                            if (dataEntity != null && dataEntity.getDevice_id() != null && dataEntity.getDevice_id().length() > 0) {
                                                //开机时接到绑定数据，查找数据是否有历史小时数据
                                                UpdataLocHoreData();
                                                if (dataEntity.getDevice_id() != null) {
                                                    DBDeviceIdData dbDeviceIdData = new DBDeviceIdData();
                                                    dbDeviceIdData.setDevice_id(dataEntity.getDevice_id());
                                                    device_id = dataEntity.getDevice_id();
                                                    try {
                                                        dbUtils.delete(DBDeviceIdData.class, WhereBuilder.b("id", ">", -1));
                                                        dbUtils.save(dbDeviceIdData);
                                                    } catch (DbException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        imageHler.sendEmptyMessage(12);
                                                    }
                                                }).start();
                                                if (dataEntity.getIs_update().equals("1") && dataEntity.getDevice_type().equals(HttpUrls.DEVICE_TYPE)
                                                        && dataEntity.getCntr_type().equals(HttpUrls.CNTR_TYPE)) {//更新APP
//                                                    httpDownLoadHler.sendEmptyMessage(12);
                                                    new Thread(networkTask).start();
                                                }
                                                if (dataEntity.getIs_rule() != null && !dataEntity.getIs_rule().equals("0"))//有新的下载广告策略
                                                {
                                                    ruleNew(dataEntity.getRule_id());
                                                }

                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        DownloadRulehandn.sendEmptyMessage(123);
                                                    }
                                                }).start();

                                                if (dataEntity.getPrice_array() != null && dataEntity.getPrice_array().size() > 0) {
                                                    savePriceArray(dataEntity.getPrice_array());
                                                }
                                                Utils.getLocation(context);
                                            } else {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        sendNow(order_bindDevice);
                                                    }
                                                }).start();
                                            }

                                        }
                                    } else if (operation.equals(order_Updata_App)) {//更新推送
//                                        JSONObject data = object.getJSONObject("data");
                                        String app_ver = object.getString("app_ver");
                                        String is_update = object.getString("is_update");
                                        String download_url = object.getString("download_url");
                                        String device_type = object.getString("device_type");
                                        String cntr_type = object.getString("cntr_type");
//                                        Log.e("app_ver=" + app_ver, "  is_update=" + is_update + "  download_url=" + download_url + "  device_type=" + device_type);
                                        if (is_update != null && app_ver != null && download_url != null) {
                                            if (!setApp_ver.equals(app_ver) && is_update.equals("1") && device_type.equals(HttpUrls.DEVICE_TYPE) && cntr_type.equals(HttpUrls.CNTR_TYPE)) {
                                                if (dataEntity == null)
                                                    dataEntity = new DataEntity();
                                                dataEntity.setDownload_url(download_url);
                                                dataEntity.setIs_update("1");
                                                newtWork = true;
                                                new Thread(networkTask).start();
                                            }
                                        }
                                    } else if (operation.equals(order_Callback)) {//制作命令返回回调  order_Produce
//                                        Utils.LogE("order_Callback");
                                        JSONObject data = object.getJSONObject("data");
                                        String opt_id = data.getString("opt_id");
                                        String opt_type = data.getString("opt_type");
                                        updataOpt(opt_id, opt_type, "1");
                                    } else if (operation.equals(order_Produce)) {//制水中命令返回回调
                                        JSONObject data = object.getJSONObject("data");
                                        String opt_id = data.getString("opt_id");
                                        String opt_type = data.getString("opt_type");
                                        updataOpt(opt_id, opt_type, "3");
                                    } else if (operation.equals(order_Complete)) {//制作完成命令返回回调
//                                        Utils.LogE("order_Complete");
                                        JSONObject data = object.getJSONObject("data");
                                        String opt_id = data.getString("opt_id");
                                        String opt_type = data.getString("opt_type");
                                        updataOpt(opt_id, opt_type, "5");//
                                    } else if (operation.equals(CNTR_DROP_FAILED_CB)) {//出饮料失败返回回调
//                                        Utils.LogE("order_Complete");
                                        JSONObject data = object.getJSONObject("data");
                                        String opt_id = data.getString("opt_id");
                                        String opt_type = data.getString("opt_type");
                                        updataOpt(opt_id, opt_type, "7");//
                                    } else if (operation.equals(order_Device_status)) {//服务器获取机器是否能出水
//                                        Utils.LogE("order_Complete");
                                        JSONObject data = object.getJSONObject("data");
                                        String code = data.getString("code");
                                        Long temp = System.currentTimeMillis() / 1000;
                                        String temp_deivece = is_deivece;
                                        if (temp - timeResultDeiveceData > 5) { //如果现在的时间大于机器最后一次上传数据5秒，就判断为异常
                                            temp_deivece = "4";
                                        }
//                                        if (coffee_bug_state != null && temp_deivece.equals("1")) {
//                                            if (!coffee_bug_state.equals("00")) {
//                                                temp_deivece = "4";
//                                            }
//                                        }
                                        if (!isShowQRCode) {//是否在水循环
                                            temp_deivece = "5";
                                        }
                                        if (sendSucceedInt == 1)
                                            temp_deivece = "2";

                                        if (Cntr_state.equals("01") || Cntr_state.equals("1")) {
                                            temp_deivece = "2";
                                        }

                                        DataOptEntity dataEntity1 = new DataOptEntity();
                                        dataEntity1.setCode(code);
                                        dataEntity1.setStatus(temp_deivece);
                                        dataEntity1.setDevice_id(dataEntity.getDevice_id());
                                        dataEntity1.setCoffee_status(coffee_state);
                                        dataEntity1.setCoffee_error(coffee_bug_state);
                                        sendData2(operation, dataEntity1);
                                    } else if (operation.equals(order_RuleData))//获取广告策略
                                    {
                                        JSONObject data = object.getJSONObject("data");
                                        DBRuleData dbRuleData = JSON.parseObject(data.toJSONString(), DBRuleData.class);
                                        if (dbRuleData != null) {
                                            rule_id = dbRuleData.getRule_id();
                                            dbRuleData.setResource_array_string(JSON.toJSONString(dbRuleData.getResource_array()));
                                            DBRuleData temp = null;
                                            try {
                                                temp = dbUtils.findFirst(Selector.from(DBRuleData.class).where("rule_id", "=", dbRuleData.getRule_id()));
                                            } catch (DbException e) {
                                                e.printStackTrace();
                                            }
                                            if (temp == null)  //保存策略
                                            {
                                                dbRuleData.setStatus("2");
                                                dbRuleData.setStatus_type("0");
                                                dbRuleData.setRule_type("1");
                                                try {
                                                    dbUtils.save(dbRuleData);
                                                } catch (DbException e) {
                                                    e.printStackTrace();
                                                }
                                                List<DBRuleData> loadDbrule = null; //把之前那些变成老的策略包
                                                try {
                                                    loadDbrule = dbUtils.findAll(Selector.from(DBRuleData.class)
                                                            .where("rule_id", "!=", dbRuleData.getRule_id()));
                                                } catch (DbException e) {
                                                    e.printStackTrace();
                                                }
                                                if (loadDbrule != null && loadDbrule.size() > 0) {
                                                    for (int i = 0; i < loadDbrule.size(); i++) {
                                                        loadDbrule.get(i).setRule_type("0");
                                                        try {
                                                            dbUtils.update(loadDbrule.get(i), WhereBuilder.b("rule_id", "=", loadDbrule.get(i).getRule_id()), "rule_type");
                                                        } catch (DbException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                if (dbRuleData.getResource_array() != null) {
                                                    DataOptEntity dataOptEntity = new DataOptEntity();
                                                    dataOptEntity.setDevice_id(dataEntity.getDevice_id());
                                                    dataOptEntity.setRule_id(dbRuleData.getRule_id());
                                                    dataOptEntity.setResource_array(dbRuleData.getResource_array());
                                                    sendData2(order_ResourceData, dataOptEntity);
                                                }
                                            }
                                        }
                                    } else if (operation.equals(order_ResourceData))//获取策略资源
                                    {
                                        com.alibaba.fastjson.JSONArray data = object.getJSONArray("data");
                                        List<ADInfo> adInfo = JSON.parseArray(data.toJSONString(), ADInfo.class);
//                                        List<String> resource_array= new ArrayList<>();
                                        if (adInfo != null && adInfo.size() > 0) {
                                            for (int i = 0; i < adInfo.size(); i++) {
                                                adInfo.get(i).setRule_id(rule_id);
                                                adInfo.get(i).setIs_success("-1");
//                                                resource_array.add(adInfo.get(i).getRule_id());
                                                try {
                                                    ADInfo tempadinfo = dbUtils.findFirst(Selector.from(ADInfo.class).where("file_key", "=", adInfo.get(i).getFile_key()));
                                                    if (tempadinfo == null) {
                                                        dbUtils.save(adInfo.get(i));
                                                    } else {
                                                        dbUtils.update(adInfo.get(i),
                                                                WhereBuilder.b("resource_id", "=", adInfo.get(i).getResource_id()),
                                                                "remain_time");
                                                    }
                                                } catch (DbException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    DownloadRulehandn.sendEmptyMessage(123);
                                                }
                                            }).start();
                                        }
                                        httpRuleCallback("2");
                                    } else if (operation.equals(order_RuleCallback))//策略回调
                                    {
                                        JSONObject data = object.getJSONObject("data");
                                        String temp_rule_id = data.getString("rule_id");
                                        String temp_status = data.getString("status");
                                        DBRuleData temp = null;
                                        try {
                                            temp = dbUtils.findFirst(Selector.from(DBRuleData.class).where("rule_id", "=", temp_rule_id));
                                            if (temp != null) {
                                                if (temp.getStatus().equals(temp_status)) {
                                                    temp.setStatus_type("1");
                                                    dbUtils.update(temp, WhereBuilder.b("rule_id", "=", temp_rule_id), "status_type");
                                                }
                                            }
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (operation.equals(order_UpdateDrinks)) {//修改机器出水料和水量
                                        JSONObject data = object.getJSONObject("data");
                                        DBDrinksData dbDrinksData = JSON.parseObject(data.toJSONString(), DBDrinksData.class);
                                        newdbDrinksData = dbDrinksData;
                                        numDrinksData = 0;

                                        if (data.containsKey("price_array")) {
                                            JSONArray tempPrice_array = data.getJSONArray("price_array");
                                            List<Price_array> temp = JSON.parseArray(tempPrice_array.toJSONString(), Price_array.class);
                                            if (temp != null && temp.size() > 0) {
                                                savePriceArray(temp);
                                            }
                                        }

                                        if (dbDrinksData != null) {
                                            httpOrderMsgRuleCallback(dbDrinksData.getOrder_id(), "2");//控制命令回调
//                                            oldDrinksArray=dbDrinksData.getDrinks_array();
                                            dbDrinksData.setDrinks_array_string(JSON.toJSONString(dbDrinksData.getDrinks_array()));
                                            try {
                                                dbUtils.save(dbDrinksData);
                                            } catch (DbException e) {
                                                e.printStackTrace();
                                            }
//                                            oldDrinksData = dbDrinksData;
                                            if (dbDrinksData.getDrinks_array() != null) {
                                                for (int i = 0; i < dbDrinksData.getDrinks_array().size(); i++) {
                                                    try {
                                                        Thread.sleep(600);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    oldDrinksData = new DBDrinksData();
                                                    Drinks_array drinks_array = new Drinks_array();
                                                    drinks_array = dbDrinksData.getDrinks_array().get(i);
                                                    List<Drinks_array> listDri = new ArrayList<>();
                                                    listDri.add(drinks_array);
                                                    oldDrinksData.setDrinks_array(listDri);
                                                    senHandler.sendEmptyMessage(101);

                                                    try {
                                                        Thread.sleep(600);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    senHandler.sendEmptyMessage(102);
                                                }
                                            }
                                        }
                                    } else if (operation.equals(CRRL_TICKET)) {
//                                        usbHandler.sendEmptyMessage(123);
                                    } else if (operation.equals(order_Device_cntr_status)) {//服务器获取机器是否能售卖饮料
//                                        Utils.LogE("order_Complete");
                                        JSONObject data = object.getJSONObject("data");
                                        String code = data.getString("code");
                                        String number = data.getString("number");
                                        Long temp = System.currentTimeMillis() / 1000;
                                        String temp_deivece = is_deivece;

                                        if (temp - timeResultDeiveceData > 5) { //如果现在的时间大于机器最后一次上传数据5秒，就判断为异常
                                            temp_deivece = "3";
                                        }
                                        if (Cntr_state != null) {
//                                            if (Cntr_state.equals("0") || Cntr_state.equals("00")) {
//                                                temp_deivece = "1";
//                                            } else if (Cntr_state.equals("01") || Cntr_state.equals("1")) {
//                                                temp_deivece = "2";
//                                            } else if (Cntr_state.equals("02") || Cntr_state.equals("1")
//                                                    ||Cntr_state.equals("03") || Cntr_state.equals("3")) {
//                                                temp_deivece = "1";
//                                            } else if (Cntr_state.equals("04") || Cntr_state.equals("4")
//                                                    ||Cntr_state.equals("05") || Cntr_state.equals("5")) {
//                                                temp_deivece = "4";
//                                            }

                                            int i = Integer.parseInt(Cntr_state);
                                            temp_deivece = (i + 1) + "";

                                            if (coffee_state.equals("01") || coffee_state.equals("1")) {
                                                temp_deivece = "2";
                                            }
                                        }
                                        if (is_testing)
                                            temp_deivece = "8";

                                        DataOptEntity dataEntity1 = new DataOptEntity();
//                                        DBShopData dbShopData = new DBShopData();
////                                        dbShopData = cntrData.get(number);
//                                        if (dbShopData != null) {
//                                            dataEntity1.setCount(dbShopData.getCount());
//                                            dataEntity1.setNumber(dbShopData.getNum());
//                                            if (dataEntity1.getCount().equals("0")) {
//                                                temp_deivece = "7";
//                                            }
//                                        } else {
//                                            dataEntity1.setCount("0");
//                                            dataEntity1.setNumber(number);
//                                        }
                                        dataEntity1.setCode(code);
                                        dataEntity1.setStatus(temp_deivece);
                                        dataEntity1.setDevice_id(dataEntity.getDevice_id());
                                        sendData2(operation, dataEntity1);
                                    } else if (operation.equals(SELES_DRINK_1)) {
                                        JSONObject data = object.getJSONObject("data");
                                        Sales_opt_id = data.getString("opt_id");
                                        Sales_opt_type = data.getString("number");
                                        senHandler.sendEmptyMessage(201);
                                        //保存落饮状态
                                        Utils.updataCntrOpt(Sales_opt_id, Sales_opt_type, "0", dbUtils);
                                        //命令接收回调
                                        DataOptEntity dataOptEntity = new DataOptEntity();
                                        dataOptEntity.setDevice_id(dataEntity.getDevice_id());
                                        dataOptEntity.setOpt_id(Sales_opt_id);
                                        sendData2(CNTR_ORDER_CB_1, dataOptEntity);
                                        serial_number++;
                                        if(serial_number>200)
                                        {
                                            serial_number=0;
                                        }
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    boolean is_send = false;
                                                    Thread.sleep(1500);
                                                    if (Cntr_state.equals("0") || Cntr_state.equals("00")) {
                                                        if (Sales_opt_id != null && Sales_opt_id.length() > 0) {
                                                            senHandler.sendEmptyMessage(201);
                                                        }
                                                    } else if (Cntr_state.equals("1") || Cntr_state.equals("01")) {
                                                        is_send = true;
                                                    }
                                                    if (!is_send) {
                                                        Thread.sleep(1500);
                                                        if (Cntr_state.equals("0") || Cntr_state.equals("00")) {
                                                            if (Sales_opt_id != null && Sales_opt_id.length() > 0) {
                                                                senHandler.sendEmptyMessage(201);
                                                            }
                                                        }
                                                    }
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();

                                    } else if (operation.equals(CNTR_ORDER_CB_1)) {//制作命令返回回调  order_Produce
//                                        Utils.LogE("order_Callback");
                                        JSONObject data = object.getJSONObject("data");
                                        String opt_id = data.getString("opt_id");
//                                        String opt_type = data.getString("number");
                                        //保存落饮状态
                                        Utils.updataCntrOpt(opt_id, "", "1", dbUtils);
                                    } else if (operation.equals(CNTR_DROP_CB_1)) {//制水中命令返回回调
                                        JSONObject data = object.getJSONObject("data");
                                        String opt_id = data.getString("opt_id");
//                                        String opt_type = data.getString("number");
                                        //保存落饮状态
                                        Utils.updataCntrOpt(opt_id, "", "3", dbUtils);
                                    } else if (operation.equals(CNTR_REFES)) {//获取机器实时数据
//                                        JSONObject data = object.getJSONObject("data");
//                                        String code = data.getString("code");
                                        DBDeiveceData dbDeiveceData = null;
                                        try {
                                            dbDeiveceData = dbUtils.findFirst(Selector.from(DBDeiveceData.class).orderBy("id", true));
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
                                        if (dbDeiveceData != null) {
                                            List<DBShopData> dbShopDataList = new ArrayList<>();
//                                            Utils.getCntrDataList(cntrData, dbShopDataList);
//                                            dbDeiveceData.setCntr_ary(dbShopDataList);
                                            sendData(order_RecentData, dbDeiveceData);
                                        }
//                                        if (code!=null&&code.length()>0) {
//                                            DataOptEntity dataEntity1 = new DataOptEntity();
//                                            dataEntity1.setCode(code);
//                                            sendData2(order_CntrOrderMsg_CB, dataEntity1);
//                                        }
                                    } else if (operation.equals(CNTR_REFESH_DEVICE)) {//请求设备实时数据
                                        JSONObject data = object.getJSONObject("data");
                                        String refresh_id = data.getString("refresh_id");
                                        DBDeiveceData dbDeiveceData = null;
                                        try {
                                            dbDeiveceData = dbUtils.findFirst(Selector.from(DBDeiveceData.class).orderBy("id", true));
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
                                        if (dbDeiveceData != null) {
                                            List<DBShopData> dbShopDataList = new ArrayList<>();
//                                            Utils.getCntrDataList(cntrData, dbShopDataList);
//                                            dbDeiveceData.setCntr_ary(dbShopDataList);

                                            String longitude = SharePerfenceUtils.getInstance(context).getLongitude();
                                            String latitude = SharePerfenceUtils.getInstance(context).getAtitude();
                                            if (longitude == null)
                                                longitude = "";
                                            if (latitude == null)
                                                latitude = "";
                                            dbDeiveceData.setLongitude(longitude);
                                            dbDeiveceData.setLatitude(latitude);
                                            sendData(order_RecentData, dbDeiveceData);
                                        }
                                        if (refresh_id != null && refresh_id.length() > 0) {
                                            DataOptEntity dataEntity1 = new DataOptEntity();
                                            dataEntity1.setRefresh_id(refresh_id);
                                            sendData2(CNTR_REFESH_DEVICE_CB, dataEntity1);
                                        }
                                    } else if (operation.equals(ExamineCntr)) {
                                        JSONObject data = object.getJSONObject("data");
                                        String code = data.getString("code");
                                        testing_code = code;
                                        String device_id = data.getString("device_id");
                                        String temp_string = data.getString("container_array");
                                        List<String> temp = JSON.parseArray(temp_string, String.class);
                                        if (temp != null && temp.size() > 0 && device_id.equals(dataEntity.getDevice_id())) {
                                            List<Container_array> temp1 = new ArrayList<>();
                                            for (int i = 0; i < temp.size(); i++) {
                                                Container_array container_array = new Container_array();
                                                container_array.setNum(temp.get(i));
                                                container_array.setStatus("1");
                                                temp1.add(container_array);
                                            }
                                            timerTesting(temp1);
                                        }
                                    } else if (operation.equals(CRTR_OPT_CB)) {
//                                        dbUtils.findAll(Selector.from(DBOptEntity.class));
                                        try {
                                            dbUtils.deleteAll(DBOptEntity.class);
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else if (operation.equals(ErrorMotorCB))
                                    {
                                        JSONObject data = object.getJSONObject("data");
                                        String opt_id = data.getString("opt_id");
                                        if (opt_id!=null&&opt_id.length()>0)
                                        {
                                            try {
                                                dbUtils.delete(DBOptEntity.class, WhereBuilder.b("opt_id", "=", opt_id));
                                            } catch (DbException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private DBDrinksData oldDrinksData;//下发控制饮料粉料和水量
    private DBDrinksData newdbDrinksData;//需要判断的发送是否成功判断
    private int numDrinksData = 0;//计数
//    private List<Drinks_array> oldDrinksArray;//下发需要设置的饮料粉料和水量

//    private

    //    private void sendDevice()
//    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true)
//                {
//                    senHandler.sendEmptyMessage(Integer.parseInt(opt_type));
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }

    private void getPriceArray() {
        price_array = new ArrayList<>();
        try {
            price_array = dbUtils.findAll(Selector.from(Price_array.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (price_array != null && price_array.size() > 0)
            handlerPrice.sendEmptyMessage(123);
    }

    private void savePriceArray(List<Price_array> price_array) {
        this.price_array = price_array;
        handlerPrice.sendEmptyMessage(123);
        try {
            dbUtils.deleteAll(Price_array.class);
            dbUtils.saveAll(price_array);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    Handler handlerPrice = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            for (int i = 0; i < price_array.size(); i++) {
                switch (price_array.get(i).getOpt_type()) {
                    case "1":
                        textGood1.setText(price_array.get(i).getPrice());
                        break;
                    case "3":
                        textGood2.setText(price_array.get(i).getPrice());
                        break;
                    case "9":
                        textGood3.setText(price_array.get(i).getPrice());
                        break;
                    case "7":
                        textGood4.setText(price_array.get(i).getPrice());
                        break;
                }
            }
            return false;
        }
    });


    //有新的策略判断是否下载和请求数据
    private void ruleNew(String rule_id) {
        DBRuleData dbRuleData = null;
        try {
            dbRuleData = dbUtils.findFirst(Selector.from(DBRuleData.class).where("rule_id", "=", rule_id));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (dbRuleData == null)//表示有新的策略 请求数据
        {
            DataOptEntity dataOptEntity = new DataOptEntity();
            dataOptEntity.setDevice_id(dataEntity.getDevice_id());
            dataOptEntity.setRule_id(rule_id);
            sendData2(order_RuleData, dataOptEntity);
        } else {

        }
    }

    Handler imageHler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
//            image_dit == null &&

            //临时测试注释
//            if (open_door != null && open_door.equals("1"))
//                return false;
//            if (is_testing)
//                return false;
//            if (sensor != null && sensor.equals("1"))
//                return false;
//            if (Cntr_transaction_state != null && (Cntr_transaction_state.equals("02") || Cntr_transaction_state.equals("2")
//                    ||Cntr_transaction_state.equals("03") || Cntr_transaction_state.equals("3"))
//                    )
//                return false;
//            if (goods_error_time>0)
//                return false;

            if (dataEntity != null && dataEntity.getQrcode() != null && !image_qc.equals(dataEntity.getQrcode())) {
                if (!image_qc.equals(dataEntity.getQrcode())) {
                    Bitmap dit = DrawUtil.createImage(dataEntity.getQrcode());
                    if (dit != null) {
                        image_dit = dit;
                        if (dataEntity.getDevice_id() != null) {
                            String headImage = DrawUtil.saveHeadBitmap(dit, 100, dataEntity.getDevice_id() + "_" + System.currentTimeMillis());
                            if (headImage != null && headImage.length() > 0) {
                                DBImageData dbImageData = new DBImageData();
                                dbImageData.setQrcode(dataEntity.getQrcode());
                                dbImageData.setPath(headImage);
                                image_path = headImage;
                                image_qc = dataEntity.getQrcode();
                                try {
                                    dbUtils.delete(DBImageData.class, WhereBuilder.b("id", ">", -1));
                                    dbUtils.save(dbImageData);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    clearCache();
                } else {
                    image_dit = DrawUtil.scaleImage(image_path);
                }
            } else {
                if (image_dit == null)
                    image_dit = DrawUtil.scaleImage(image_path);
            }
//            scaleImage
            if (isShowQRCode) {
                if (mSocket != null && image_dit != null && isSuccess) {
                    //yanshi临时注释
                    imageCode.setImageBitmap(image_dit);
//                    imageCode.setImageResource(R.mipmap.erweima);
                    textHint.setText(getString(R.string.buy));
                    if (dataEntity != null && textNumber != null) {
                        textNumber.setText("机器编号：" + dataEntity.getDevice_id());
                    }
//                    textHint.setText("微信/支付宝扫描购买");
                } else {
                    L.i("imageHler=isShowQRCode"+isShowQRCode);
                    imageCode.setImageResource(R.drawable.device_bug);
                    textHint.setText(getString(R.string.network_error));
//                    textHint.setText("机器故障");
                }
            } else {
                imageCode.setImageResource(R.drawable.image_for);
                textHint.setText(getString(R.string.water_cycle));
            }
            return false;
        }
    });

    /**
     * 清除Cache内的全部内容
     */
    public void clearCache() {
        System.gc();
        System.runFinalization();
    }

    boolean newtWork = true;
    /**
     * ru
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
//            try {
//                dbUtils.dropTable(DBHourDeiveceData.class);//删除表格
//                dbUtils.dropTable(DBDeiveceData.class);//删除表格
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
            if (dataEntity.getIs_update().equals("1") && dataEntity.getDownload_url().length() > 0) {
//                if (newtWork) {
//                    newtWork = false;
//                    Log.e("dataEntity.getIs_update()=" + dataEntity.getIs_update(), "   dataEntity.getDownload_url=" + dataEntity.getDownload_url());
//                    File file = Utils.downLoadFile(context, dataEntity.getDownload_url());
//                    if (file.exists()) {
//                        Utils.openFile(context, file);
//                    }
                Intent intent = new Intent(context,
                        DownloadService.class);
                intent.putExtra(MyIntents.URL, dataEntity.getDownload_url());
                intent.putExtra(MyIntents.TYPE, MyIntents.Types.ADD);
                startService(intent);
//                }
            }
        }
    };

    Handler httpDownLoadHler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            new Thread(networkTask).start();
            return false;
        }
    });

    /**
     * 发送android_id
     */
//    public void sendNow(String operation) {
//        DBDeiveceData dataEntity = new DBDeiveceData();
//        dataEntity.setMachine_code(android_id);
//        sendTCPData(operation, dataEntity);
//    }
//
//    /**
//     * 发送回调
//     */
//    public void sendBC(String operation, String opt_id, String opt_type, String num) {
//
//        DBDeiveceData dataEntity = new DBDeiveceData();
//        dataEntity.setOpt_type(opt_type);
//        dataEntity.setOpt_id(opt_id);
//        if (num != null && num.length() > 0)
//            dataEntity.setNum(num);
//        sendTCPData(operation, dataEntity);
//    }
    public void sendNow(String operation) {
        DataOptEntity dataEntity = new DataOptEntity();
        dataEntity.setMachine_code(android_id);
        dataEntity.setMachine_code_2(android_id_2);
        dataEntity.setNetwork(StrNetwork);
        dataEntity.setCntr_type(HttpUrls.CNTR_TYPE);
        sendData2(operation, dataEntity);
    }

    /**
     * 发送回调
     */
    public void sendBC(String operation, String opt_id, String opt_type, String num) {

        DataOptEntity dataEntity1 = new DataOptEntity();
        dataEntity1.setOpt_type(opt_type);
        dataEntity1.setOpt_id(opt_id);
        if (dataEntity != null && dataEntity.getDevice_id() != null)
            dataEntity1.setDevice_id(dataEntity.getDevice_id());
        if (num != null && num.length() > 0)
            dataEntity1.setNum(num);
        sendData2(operation, dataEntity1);
    }

    /**
     * 向服务器发送数据
     */
    public boolean sendData2(String operation, DataOptEntity dataOptEntity) {

        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        //socket数据格式封装
        SendOptEntity entity = new SendOptEntity();
        entity.setData(dataOptEntity);
        entity.setApp_ver(setApp_ver);
        entity.setOperation(operation);
        String dataStr = JSON.toJSONString(entity) + "\n";
        L.i("sendData2-datastr:"+dataStr);
        if (!soc.isClosed() && !soc.isOutputShutdown()) {
            try {
                OutputStream out = soc.getOutputStream();
                out.write(dataStr.getBytes("utf-8"));
                sendTime = System.currentTimeMillis();////每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }


    /**
     * 向服务器发送数据
     */
    public boolean sendData(String operation, DBDeiveceData dbDeiveceData) {

        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        //socket数据格式封装
        SendSocketEntity entity = new SendSocketEntity();
        entity.setData(dbDeiveceData);
        entity.setApp_ver(setApp_ver);
        entity.setOperation(operation);
        String dataStr = JSON.toJSONString(entity) + "\n";
        Utils.LogE(dataStr);
        if (!soc.isClosed() && !soc.isOutputShutdown()) {
            try {
                OutputStream out = soc.getOutputStream();
                out.write(dataStr.getBytes("utf-8"));
                sendTime = System.currentTimeMillis();////每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

//    private String byteToString(byte[] tmp_byte) {
//        String tmp = "";
//        for (int i = 0; i < tmp_byte.length; i++) {
//            String hex = Integer.toHexString(tmp_byte[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            tmp += ' ';
//            tmp = tmp + hex;
//        }
//        return tmp;
//    }

    private WifiInfo wifiInfo = null;        //获得的Wifi信息
    private WifiManager wifiManager = null;    //Wifi管理器

    private int level;                        //信号强度值


    private Long saveDataTime = 3600l;//每小时保存和上传一次数据

    private void getSaveData() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                saveDataTime--;
//                Utils.LogE("saveDataTime=" + saveDataTime);
                if (saveDataTime < 0) {
                    getUploadData();
                    saveDataTime = 3600l;
                }
            }
        }, 1000, 1000);
    }

    private void getUploadData()//每一个小时上传一次数据
    {
//        dbHourDeiveceData.setTime(Utils.long2String(System.currentTimeMillis()/1000,"yyyy-M-d HH"));
        String updataTime = Utils.long2String(System.currentTimeMillis() / 1000, "yyyy-M-d HH");
        DBHourDeiveceData dbHourDeiveceData = null;
        DBDeiveceData dbDeiveceData = null;
        try {
            dbHourDeiveceData = dbUtils.findFirst(Selector.from(DBHourDeiveceData.class).where("time_hore", "=", updataTime));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (dbHourDeiveceData != null) {
            String json = JSON.toJSONString(dbHourDeiveceData);
            dbDeiveceData = JSON.parseObject(json, DBDeiveceData.class);

        } else {
            try {
                dbDeiveceData = dbUtils.findFirst(Selector.from(DBDeiveceData.class).orderBy("id", true));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        String longitude = SharePerfenceUtils.getInstance(context).getLongitude();
        String latitude = SharePerfenceUtils.getInstance(context).getAtitude();
        if (longitude == null)
            longitude = "";
        if (latitude == null)
            latitude = "";

        if (dbDeiveceData != null) {
            dbDeiveceData.setLongitude(longitude);
            dbDeiveceData.setLatitude(latitude);
            List<DBShopData> dbShopDataList = new ArrayList<>();
//            Utils.getCntrDataList(cntrData, dbShopDataList);
//            dbDeiveceData.setCntr_ary(dbShopDataList);
            if (device_id != null && device_id.length() > 0)
                dbDeiveceData.setDevice_id(device_id);
            sendData(order_UploadData, dbDeiveceData);
            try {
                dbUtils.delete(DBHourDeiveceData.class, WhereBuilder.b("device_id", "=", device_id));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    //开机时接到绑定数据，查找数据是否有历史小时数据
    private void UpdataLocHoreData() {
        DBDeiveceData dbDeiveceData22 = null;
        try {
            dbDeiveceData22 = dbUtils.findFirst(Selector.from(DBDeiveceData.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (dbDeiveceData22 != null) {
            if (dbDeiveceData22 != null) {
                String longitude = SharePerfenceUtils.getInstance(context).getLongitude();
                String latitude = SharePerfenceUtils.getInstance(context).getAtitude();
                if (longitude == null)
                    longitude = "";
                if (latitude == null)
                    latitude = "";

                dbDeiveceData22.setLongitude(longitude);
                dbDeiveceData22.setLatitude(latitude);
                List<DBShopData> dbShopDataList = new ArrayList<>();
//                Utils.getCntrDataList(cntrData, dbShopDataList);
//                dbDeiveceData22.setCntr_ary(dbShopDataList);
                sendData(order_RecentData, dbDeiveceData22);
            }
        }

        List<DBHourDeiveceData> listData = null;
        try {
            listData = dbUtils.findAll(Selector.from(DBHourDeiveceData.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (listData != null && listData.size() > 0) {
            for (int i = 0; i < listData.size(); i++) {
                String json = JSON.toJSONString(listData.get(i));
                Utils.LogE("1111111");
                DBDeiveceData dbDeiveceData = JSON.parseObject(json, DBDeiveceData.class);
                if (dbDeiveceData != null) {
                    Utils.LogE("222222");
                    List<DBShopData> dbShopDataList = new ArrayList<>();
//                    Utils.getCntrDataList(cntrData, dbShopDataList);
//                    dbDeiveceData.setCntr_ary(dbShopDataList);
                    sendData(order_UploadData, dbDeiveceData);
                }
            }
            try {
                dbUtils.delete(DBHourDeiveceData.class, WhereBuilder.b("id", ">", -1));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }


    private void WifiInfoControl() {
        // 获得WifiManager
        wifiManager = (WifiManager) Application.getInstance().getApplicationContext().getSystemService(WIFI_SERVICE);
        // 使用定时器,每隔5秒获得一次信号强度值
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                wifiInfo = wifiManager.getConnectionInfo();
                //获得信号强度值
                level = wifiInfo.getRssi();
//                if (level > -150) {
//                    if (mSocket == null) {
//                        Log.e("socketStat=", socketStat + "");
//                        new InitSocketThread().start();
//                    }
//                }

                if (mSocket != null) {
                    //根据获得的信号强度发送信息
                    if (level <= 0 && level >= -50) {
                        Message msg = new Message();
                        msg.what = 1;
                        handlerLL.sendMessage(msg);
                    } else if (level < -50 && level >= -70) {
                        Message msg = new Message();
                        msg.what = 2;
                        handlerLL.sendMessage(msg);
                    } else if (level < -70 && level >= -80) {
                        Message msg = new Message();
                        msg.what = 3;
                        handlerLL.sendMessage(msg);
                    } else if (level < -80 && level >= -100) {
                        Message msg = new Message();
                        msg.what = 4;
                        handlerLL.sendMessage(msg);
                    } else if (level < -100 && level >= -150) {
                        Message msg = new Message();
                        msg.what = 1;
                        handlerLL.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 1;
                        handlerLL.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = 6;
                    handlerLL.sendMessage(msg);
                }
            }
        }, 1000, 1000);
    }

    Timer timerSocket = new Timer();

    private void timerSocket() {
//        timerSocket.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        }, 2000, 2000);

        timerSocket.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("socketStat=", socketStat + "");
                if (!socketStat) {
                    new InitSocketThread().start();
                } else {
                    if (mSocket != null)
                        cleanSendTimerTask();
                }
            }
        }, 2000, 2000);
    }

    private void cleanSendTimerTask() {
        if (timerSocket != null) {
            timerSocket.cancel();
            timerSocket = null;
        }
    }

    Timer timer_testing;
    private boolean is_testing = false;
    private int item_testing = -1;
    private String testing_code = "";
    private String testing_status = "0";

    //检测货到马达
    private void timerTesting(List<Container_array> temp) {

        if (is_testing)
            return;
        timer_testing = new Timer();
        testing_array.clear();
        testing_array_error.clear();
        testing_array.addAll(temp);
        if (testing_array != null && testing_array.size() > 0) {

        } else {
            return;
        }
        item_testing = -1;
        is_testing = true;
        sensor = "1";
        timer_testing.schedule(new TimerTask() {
            @Override
            public void run() {
                item_testing++;
                Utils.LogE("timerTesting");
                if (item_testing < testing_array.size()) {
                    Sales_opt_type = testing_array.get(item_testing).getNum();
                    testing_status = "0";
                    serial_number++;
                    if(serial_number>200)
                    {
                        serial_number=0;
                    }
                    senHandler.sendEmptyMessage(201);
                }
//                if (item_testing == testing_array.size()) {
//                    senHandler.sendEmptyMessage(301);
//                }
                if (item_testing >= testing_array.size()) {
                    Utils.LogE("testing_array=" + JSON.toJSONString(testing_array));
                    cleantimerTesting();
                    item_testing = -1;
                    is_testing = false;

                    DataOptEntity dataOptEntity = new DataOptEntity();
                    dataOptEntity.setDevice_id(dataEntity.getDevice_id());
                    dataOptEntity.setCode(testing_code);
                    dataOptEntity.setContainer_array(testing_array);
                    if (sensor.equals("0"))
                        dataOptEntity.setDrop_status("1");
                    else
                        dataOptEntity.setDrop_status("2");
                    sendData2(ExamineCntrCB, dataOptEntity);

                }
            }
        }, 1000, 9000);

    }

    private void cleantimerTesting() {
        if (timer_testing != null) {
            timer_testing.cancel();
            timer_testing = null;
        }
    }

    // 使用Handler实现UI线程与Timer线程之间的信息传递,每5秒告诉UI线程获得wifiInto
    private Handler handlerLL = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
//            Log.e("cjjj=", "" + level);
            switch (message.what) {
                // 如果收到正确的消息就获取WifiInfo，改变图片并显示信号强度
                case 1:
                    wifi_image.setImageResource(R.drawable.single3);
                    break;
                case 2:
                    wifi_image.setImageResource(R.drawable.single2);
                    break;
                case 3:
                    wifi_image.setImageResource(R.drawable.single2);
                    break;
                case 4:
                    wifi_image.setImageResource(R.drawable.single1);
                    break;
                case 5:
                    wifi_image.setImageResource(R.drawable.single0);
                    break;
                default:
                    wifi_image.setImageResource(R.drawable.single);
                    break;
            }

            return false;
        }
    });


    //发送策略回调
    private void httpRuleCallback(String status) {
        if (rule_id != null) {
            DataOptEntity dataOptEntity = new DataOptEntity();
            dataOptEntity.setDevice_id(dataEntity.getDevice_id());
            dataOptEntity.setRule_id(rule_id);
            dataOptEntity.setStatus(status);
            sendData2(order_RuleCallback, dataOptEntity);
        }
    }


    //控制命令回调
    private void httpOrderMsgRuleCallback(String order_id, String status) {
        if (dataEntity != null && dataEntity.getDevice_id() != null) {
            DataOptEntity dataOptEntity = new DataOptEntity();
            dataOptEntity.setDevice_id(dataEntity.getDevice_id());
            dataOptEntity.setOrder_id(order_id);
            dataOptEntity.setStatus(status);
            sendData2(order_MsgCallback, dataOptEntity);
        }
    }

    //下载播放策略
    private Handler DownloadRulehandn = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (!isDownload)//没有下载的时候才下
                DownloadRule(context);
            return false;
        }
    });

    //判断是否播放广告
    private void getRule(Long ruleTime) {
        List<DBRuleData> dbRuleData = null;
        List<DBRuleData> tempRuleData = new ArrayList<>();
//        Long endTime=ruleTime;
//        86400
        try {
            dbRuleData = dbUtils.findAll(Selector.from(DBRuleData.class).where("start_date", "<", ruleTime)
                    .and("end_date", ">", RuleUtils.getTimeZero(ruleTime + "")).and(WhereBuilder.b("status", "=", "3").or("status", "=", "4"))
                    .and("rule_type", "=", "1"));

            if (dbRuleData == null)
                dbRuleData = dbUtils.findAll(Selector.from(DBRuleData.class).where("start_date", "<", ruleTime)
                        .and("end_date", ">", RuleUtils.getTimeZero(ruleTime + "")).and(WhereBuilder.b("status", "=", "3").or("status", "=", "4")));
//            .and("status", "=", "3")
            if (dbRuleData != null)
//                Utils.LogE("dbRuleData="+JSON.toJSONString(dbRuleData));
                for (int i = 0; i < dbRuleData.size(); i++) {
                    Long endTime = RuleUtils.getTime(dbRuleData.get(i).getEnd_date(), dbRuleData.get(i).getEnd_time());
                    if (endTime > ruleTime) {
                        tempRuleData.add(dbRuleData.get(i));
                        if (dbRuleData.get(i).getStatus().equals("3") && dbRuleData.get(i).getStatus_type().equals("0"))
                            httpRuleCallback("4");
                        if (dbRuleData.get(i).getRule_type().equals("1"))//判断如果是新的
                        {
//                            RuleUtils.deleteRule();
                        }
                    }
                }
        } catch (DbException e) {
            e.printStackTrace();
        }

        boolean isType = true;
        if (tempRuleData.size() > 0) {
            if (ruleInfos.size() == 0) {
                ruleInfos.addAll(tempRuleData);
            } else {
                if (ruleInfos.size() > 0)
                    for (int i = 0; i < ruleInfos.size(); i++) {
                        for (int j = 0; j < tempRuleData.size(); j++) {
                            if (ruleInfos.get(i).getRule_id().equals(tempRuleData.get(j).getRule_id())) {
                                isType = false;
                            }
                        }
                    }
                if (!isType) {
                    ruleInfos.clear();
                    ruleInfos.addAll(tempRuleData);
                } else {
                    ruleInfos.clear();
                    ruleInfos.addAll(tempRuleData);
                }
            }
        } else //默认
        {
            if (infos.size() > 0) {
                boolean isUpdate = false;
                for (int i = 0; i < infos.size(); i++) {
                    if (imageList.get(0).equals(infos.get(i).getPathUrl())) {
                        isUpdate = true;
                        break;
                    }
                }
                if (!isUpdate) {
                    infos.clear();
                    setDefault();
                    //初始化控件
                    initialize();
                }

            } else {
                infos.clear();
                setDefault();
                //初始化控件
                initialize();
            }
            return;
        }

        if (isType) {
            for (int i = 0; i < tempRuleData.size(); i++) {
                List<ADInfo> dbInfos = new ArrayList<>();
                tempRuleData.get(i).setResource_array(JSON.parseArray(tempRuleData.get(i).getResource_array_string(), String.class));
                try {
                    dbInfos = dbUtils.findAll(Selector.from(ADInfo.class)
                            .where("resource_id", "in", tempRuleData.get(i).getResource_array())
                            .and("is_success", "==", "1")
                            .orderBy("weight", false));
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (dbInfos != null && dbInfos.size() > 0) {
                    infos.clear();
                    Utils.LogE("dbInfos=" + JSON.toJSONString(dbInfos));
                    infos.addAll(dbInfos);
//                    ruleInfos.addAll(dbInfos);
                }
            }
            //初始化控件
            initialize();
        }
    }


    //设置默认广告策略
    private void setDefault() {
        for (int i = 0; i < imageList.size(); i++) {
            ADInfo adInfo = new ADInfo();
            adInfo.setPathUrl(imageList.get(i));
            adInfo.setIs_success("1");
            adInfo.setFile_type("2");
            adInfo.setRemain_time("67");
            infos.add(adInfo);
        }
    }

    private boolean isDownload = false;//是否在下载

    // 下载广告策略
    public void DownloadRule(final Context context) {
        isDownload = false;
        DBRuleData dbRuleData = null;
        List<ADInfo> adInfo = null;
        try {
            dbRuleData = dbUtils.findFirst(Selector.from(DBRuleData.class).where("status", "=", "2").and("rule_type", "=", "1"));
            if (dbRuleData != null) {
                dbRuleData.setResource_array(JSON.parseArray(dbRuleData.getResource_array_string(), String.class));
                adInfo = dbUtils.findAll(Selector.from(ADInfo.class).where("resource_id", "in", dbRuleData.getResource_array())
                        .and("is_success", "==", "-1"));
//                Utils.LogE("adInfo"+adInfo.size()+"   adInfo="+JSON.toJSONString(adInfo));
            }
            if (adInfo != null && adInfo.size() > 0) {
                httpGetFile(dbRuleData.getRule_id(), dbUtils, context);
            } else {
                try {
                    dbRuleData = dbUtils.findFirst(Selector.from(DBRuleData.class).where("rule_id", "=", rule_id));
                    if (dbRuleData != null) {
                        dbRuleData.setResource_array(JSON.parseArray(dbRuleData.getResource_array_string(), String.class));
                        List<ADInfo> temp = dbUtils.findAll(Selector.from(ADInfo.class)
                                .where("resource_id", "in", dbRuleData.getResource_array())
                                .and("is_success", "==", "1"));
                        if (temp != null && temp.size() > 0) {
                            dbRuleData.setStatus("3");
                            dbRuleData.setStatus_type("0");
                            dbUtils.update(dbRuleData, WhereBuilder.b("rule_id", "=", dbRuleData.getRule_id()), "status", "status_type");
                        } else {
                            dbRuleData.setStatus("0");
                            dbRuleData.setStatus_type("0");
                            dbUtils.update(dbRuleData, WhereBuilder.b("rule_id", "=", dbRuleData.getRule_id()), "status", "status_type");
                        }
                        httpRuleCallback("3");
                        ruleHandler.sendEmptyMessage(123);
                    }
//                List<DBRuleData> loadDbrule=null;
//                loadDbrule= db.findFirst(Selector.from(DBRuleData.class).where("rule_id", "!=", rule_id));

                } catch (DbException e) {
                    e.printStackTrace();
                }
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
    public void httpGetFile(final String rule_id, final DbUtils db, final Context context) {
        isDownload = false;
        if (!socketStat)//是否连接上网络
            return;

        ADInfo adInfo = null;
        try {
            adInfo = db.findFirst(Selector.from(ADInfo.class)
                    .where("is_success", "==", "-1")
                    .and("rule_id", "=", rule_id)
                    .orderBy("id", false));
//            .and("resource_id", "!=", "3")
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (adInfo == null)//没有需要下载时
        {
            try {
                DBRuleData dbRuleData = db.findFirst(Selector.from(DBRuleData.class).where("rule_id", "=", rule_id));

                if (dbRuleData != null) {
                    dbRuleData.setResource_array(JSON.parseArray(dbRuleData.getResource_array_string(), String.class));
                    List<ADInfo> temp = db.findAll(Selector.from(ADInfo.class)
                            .where("resource_id", "in", dbRuleData.getResource_array())
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
                    httpRuleCallback("3");
                    ruleHandler.sendEmptyMessage(123);
                }
//                List<DBRuleData> loadDbrule=null;
//                loadDbrule= db.findFirst(Selector.from(DBRuleData.class).where("rule_id", "!=", rule_id));

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
        isDownload = true;
        File file = new File(videoPath);
        if (!file.exists()) {
            final ADInfo finalAdInfo = adInfo;
            client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] data) {
                    Utils.LogE("onSuccess");
                    while (!RuleUtils.saveVedio(db, titlePath, videoPath, data, finalAdInfo)) {
                    }
                    httpGetFile(rule_id, db, context);
                }

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                      Throwable arg3) {
                    Utils.LogE("onFailure");
                    httpGetFile(rule_id, db, context);
                }
            });
        } else {
            adInfo.setPathUrl(videoPath);
            adInfo.setIs_success("1");
            try {
                db.update(adInfo, WhereBuilder.b("resource_id", "=", adInfo.getResource_id()), "pathUrl", "is_success");
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    //下载播放策略
    private Handler ruleHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            getRule(System.currentTimeMillis() / 1000);
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        if (isReg) {
            Utils.LogE("unregisterReceiver=onDestroy");
            unregisterReceiver(myReceiver);
            isReg = false;
        }
//        Toast.makeText(context, "unregisterReceiver=onDestroy", Toast.LENGTH_SHORT).show();
//        System.exit(0);
//        isConnect = false;
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
        System.exit(0);

        super.onDestroy();
    }

    private String StrNetwork = "-1";

    public class NetWorkBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                int NET_WORK_STATE = Utils.getNetworkState(context);
                if (text_3g != null) {
                    if (NET_WORK_STATE == Utils.NETWORN_2G) {
                        text_3g.setVisibility(View.VISIBLE);
                        wifi_image.setVisibility(View.GONE);
                        text_3g.setText("2G");
                        StrNetwork = "1";
                    } else if (NET_WORK_STATE == Utils.NETWORN_3G) {
                        text_3g.setVisibility(View.VISIBLE);
                        wifi_image.setVisibility(View.GONE);
                        text_3g.setText("3G");
                        StrNetwork = "1";
                    } else if (NET_WORK_STATE == Utils.NETWORN_4G) {
                        text_3g.setVisibility(View.VISIBLE);
                        wifi_image.setVisibility(View.GONE);
                        text_3g.setText("4G");
                        StrNetwork = "1";
                    } else if (NET_WORK_STATE == Utils.NETWORN_WIFI) {
                        text_3g.setVisibility(View.GONE);
                        wifi_image.setVisibility(View.VISIBLE);
                        StrNetwork = "2";
                    } else if (NET_WORK_STATE == Utils.NETWORN_NONE) {
                        text_3g.setVisibility(View.GONE);
                        wifi_image.setVisibility(View.GONE);
                        codeHan.sendEmptyMessage(123);
                        mSocket = null;
                    }
                }
                Log.e("NET_WORK_STATE", NET_WORK_STATE + "");
            }
        }
    }
}