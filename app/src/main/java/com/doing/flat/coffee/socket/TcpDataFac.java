package com.doing.flat.coffee.socket;

import java.util.Calendar;

/**
 * Created by cym on 2017/11/30.
 */

public class TcpDataFac {

    private byte tpdu[] = {(byte) 0x60, (byte) 0x03, (byte) 0x13, 00, 00};

    /*
    签到相关数据
     */
    private String sign_MsgLength = "0091";
    private String sign_MsgHead = "0800";//签到
    private byte sign_Bitmap[] = {(byte) 0xA2, 0x20, 00, 01, 00, (byte) 0xC0, 0x08, 00, 00, 00, 00, 00, 00, 00, 00, 00};
    private String sign_ProcCode = "000000";
    private String sign_TranDtTm = "0212145642";
    private String sign_AcqSsn = "000339";
    private String sign_AcqInst_Length = "08";
    private String sign_AcqInst = "81785800";
    private String sign_TermCode = "20292020";
    private String sign_MercCode = "123456789012316";
    private String sign_CtrlInfo = "2600000000000000";

    /*
    充值缴费数据
     */
    private String expend_MsgLength = "0432";
    private String expend_MsgHead = "0200";//缴费
    private byte expend_MsgBitMap[] = {(byte) 0xF2, (byte) 0x38, (byte) 0x46, (byte) 0xC1, (byte) 0xA0, (byte) 0xC1, (byte) 0x9A, 0x10, 00, 00, 00, 00, 00, 00, 00, 0x01};
    private String expend_MsgPAN = "166225000000000261";//带长度2位
    private String expend_MsgProcCode = "430350";
    private String expend_MsgTranAmt = "000000000001";
    private String expend_MsgTranDtTm = "0902191312";
    private String expend_MsgAcqSsn = "000009";
    private String expend_MsgLTime = "191312";
    private String expend_MsgLDate = "0902";
    private String expend_MsgMercType = "8901";
    private String expend_MsgEntrMode = "051";
    private String expend_MsgCardSNum = "002";
    private String expend_MsgCondMode = "00";
    private String expend_MsgPinCode = "06";
    private String expend_MsgAcqInst = "0881785800";
    private String expend_MsgForwInst = "0881785800";
    private String expend_MsgTrck2Dat = "296225000000000261=301020100000";
    private String expend_MsgTermCode = "20292086";
    private String expend_MsgMercCode = "123456789012316";
    private byte expend_MsgAddiData[] = {(byte) 0x30, (byte) 0x34, (byte) 0x31, (byte) 0x1F, (byte) 0x21, (byte) 0x03, (byte) 0x03,
            (byte) 0x50, (byte) 0x2F, (byte) 0x01, (byte) 0x12, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x31, (byte) 0x32, (byte) 0x33,
            (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33,
            (byte) 0x31, (byte) 0x36, (byte) 0x2F, (byte) 0x05, (byte) 0x0C, (byte) 0x32, (byte) 0x30, (byte) 0x31, (byte) 0x34, (byte) 0x31,
            (byte) 0x30, (byte) 0x32, (byte) 0x37, (byte) 0x31, (byte) 0x35, (byte) 0x34, (byte) 0x35};
    private String expend_MsgTranCurr = "156";
    private byte expend_MsgPinData[] = {(byte) 0xB4, (byte) 0xA1, (byte) 0xF9, (byte) 0x41, (byte) 0x28, (byte) 0xF0, 00, (byte) 0x62};
    private String expend_MsgCtrlInfo = "2600000000000000";
    private byte expend_MsgICCData[] = {(byte) 0x31, (byte) 0x36, (byte) 0x34, (byte) 0x9F, (byte) 0x26, (byte) 0x08, (byte) 0x69, (byte) 0xF7, (byte) 0xE5, (byte) 0xA8,
            (byte) 0x46, (byte) 0x54, (byte) 0xA6, (byte) 0xBC, (byte) 0x9F, (byte) 0x27, (byte) 0x01, (byte) 0x80, (byte) 0x9F, (byte) 0x10, (byte) 0x13, (byte) 0x07, (byte) 0x00,
            (byte) 0x01, (byte) 0x03, (byte) 0xA0, (byte) 0xA0, (byte) 0x00, (byte) 0x01, (byte) 0x0A, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x96, (byte) 0x78,
            (byte) 0x94, (byte) 0xEA, (byte) 0x52, (byte) 0xC0, (byte) 0x9F, (byte) 0x37, (byte) 0x04, (byte) 0x05, (byte) 0x0C, (byte) 0x08, (byte) 0x00, (byte) 0x9F, (byte) 0x36,
            (byte) 0x02, (byte) 0x02, (byte) 0xE2, (byte) 0x95, (byte) 0x05, (byte) 0x80, (byte) 0x00, (byte) 0x04, (byte) 0x08, (byte) 0x00, (byte) 0x9A, (byte) 0x03, (byte) 0x16,
            (byte) 0x09, (byte) 0x02, (byte) 0x9C, (byte) 0x01, (byte) 0x00, (byte) 0x9F, (byte) 0x02, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x01, (byte) 0x5F, (byte) 0x2A, (byte) 0x02, (byte) 0x01, (byte) 0x56, (byte) 0x82, (byte) 0x02, (byte) 0x7C, (byte) 0x00, (byte) 0x9F, (byte) 0x1A, (byte) 0x02,
            (byte) 0x01, (byte) 0x56, (byte) 0x9F, (byte) 0x03, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x9F, (byte) 0x33,
            (byte) 0x03, (byte) 0x60, (byte) 0x40, (byte) 0x00, (byte) 0x9F, (byte) 0x34, (byte) 0x03, (byte) 0x42, (byte) 0x03, (byte) 0x00, (byte) 0x9F, (byte) 0x35, (byte) 0x01,
            (byte) 0x24, (byte) 0x9F, (byte) 0x1E, (byte) 0x08, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x9F,
            (byte) 0x09, (byte) 0x02, (byte) 0x00, (byte) 0x20, (byte) 0x9F, (byte) 0x41, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x9F, (byte) 0x63,
            (byte) 0x10, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x84, (byte) 0x08, (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x01,
            (byte) 0x06, (byte) 0x30, (byte) 0x31, (byte) 0x33};
    private String expend_MsgReveCode = "2200000100051";
    private String expend_MsgMAC = "51BAADEA";

    public static byte sign_Test[] = {0x30, 0x30, 0x39, 0x31,
            0x30, 0x38, 0x30, 0x30,
            (byte) 0xA2, 0x20, 00, 01, 00, (byte) 0xC0, 0x08, 00, 00, 00, 00, 00, 00, 00, 00, 00,
            0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
            0x30, 0x32, 0x31, 0x32, 0x31, 0x34, 0x35, 0x36, 0x34, 0x32,
            0x30, 0x30, 0x30, 0x33, 0x33, 0x39,
            0x30, 0x38,
            0x38, 0x31, 0x37, 0x38, 0x35, 0x38, 0x30, 0x30,
            0x32, 0x30, 0x32, 0x39, 0x32, 0x30, 0x32, 0x30,
            0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x31, 0x36,
            0x32, 0x36, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30};

    public static byte Expend_test[] = {};


    private static TcpDataFac instance;

    public static TcpDataFac getInstance() {

        if (null == instance) {
            instance = new TcpDataFac();
        }
        return instance;
    }


    //签到数据封装
    public byte[] getSignData() {
        byte sData[];
        sData = formbyteData(sign_MsgLength.getBytes(), sign_MsgHead.getBytes());
        sData = formbyteData(sData, sign_Bitmap);
        sData = formbyteData(sData, sign_ProcCode.getBytes());
        sData = formbyteData(sData, getCurTime());
        sData = formbyteData(sData, sign_AcqSsn.getBytes());
        sData = formbyteData(sData, sign_AcqInst_Length.getBytes());
        sData = formbyteData(sData, sign_AcqInst.getBytes());
        sData = formbyteData(sData, sign_TermCode.getBytes());
        sData = formbyteData(sData, sign_MercCode.getBytes());
        sData = formbyteData(sData, sign_CtrlInfo.getBytes());
        return sData;
    }

    //消费数据封装
    public byte[] getExpendData() {
        byte sData[];
        sData = formbyteData(expend_MsgLength.getBytes(), expend_MsgHead.getBytes());
        sData = formbyteData(sData, expend_MsgBitMap);
        sData = formbyteData(sData, expend_MsgPAN.getBytes());
        sData = formbyteData(sData, expend_MsgProcCode.getBytes());
        sData = formbyteData(sData, expend_MsgTranAmt.getBytes());
        sData = formbyteData(sData, expend_MsgTranDtTm.getBytes());
        sData = formbyteData(sData, expend_MsgAcqSsn.getBytes());
        sData = formbyteData(sData, expend_MsgLTime.getBytes());
        sData = formbyteData(sData, expend_MsgLDate.getBytes());
        sData = formbyteData(sData, expend_MsgMercType.getBytes());
        sData = formbyteData(sData, expend_MsgEntrMode.getBytes());
        sData = formbyteData(sData, expend_MsgCardSNum.getBytes());
        sData = formbyteData(sData, expend_MsgCondMode.getBytes());
        sData = formbyteData(sData, expend_MsgPinCode.getBytes());
        sData = formbyteData(sData, expend_MsgAcqInst.getBytes());
        sData = formbyteData(sData, expend_MsgForwInst.getBytes());
        sData = formbyteData(sData, expend_MsgTrck2Dat.getBytes());
        sData = formbyteData(sData, expend_MsgTermCode.getBytes());
        sData = formbyteData(sData, expend_MsgMercCode.getBytes());
        sData = formbyteData(sData, expend_MsgAddiData);
        sData = formbyteData(sData, expend_MsgTranCurr.getBytes());
        sData = formbyteData(sData, expend_MsgPinData);
        sData = formbyteData(sData, expend_MsgCtrlInfo.getBytes());
        sData = formbyteData(sData, expend_MsgICCData);
        sData = formbyteData(sData, expend_MsgReveCode.getBytes());
        sData = formbyteData(sData, expend_MsgMAC.getBytes());
        return sData;
    }

    private byte[] formbyteData(byte[] parData, byte[] childData) {
        if (null == parData || null == childData || childData.length == 0) {
            return parData;
        }
        byte[] formResult = new byte[parData.length + childData.length];
        System.arraycopy(parData, 0, formResult, 0, parData.length);
        System.arraycopy(childData, 0, formResult, parData.length, childData.length);
        return formResult;
    }

    private byte[] getCurTime() {
        StringBuffer result = new StringBuffer();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int mHour = mCalendar.get(Calendar.HOUR);
        int mMinutes = mCalendar.get(Calendar.MINUTE);
        int second = mCalendar.get(Calendar.SECOND);
        result.append(formTimeLength(month + ""));
        result.append(formTimeLength(day + ""));
        result.append(formTimeLength(mHour + ""));
        result.append(formTimeLength(mMinutes + ""));
        result.append(formTimeLength(second + ""));
        return result.toString().getBytes();
    }

    //格式化时间，防止位数不对
    private String formTimeLength(String str) {
        String result;
        result = str;
        if (str.length() < 2) {
            result = "0" + str;
        }
        return result;
    }
}
