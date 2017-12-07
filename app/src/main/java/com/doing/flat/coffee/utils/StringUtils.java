package com.doing.flat.coffee.utils;

import android.net.Uri;
import android.util.Base64;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {

    public static boolean isNotEmpty(String text) {
        if (text == null || text.equals("")) {
            return false;
        }
        return true;
    }

    public static String decode64String(String s) {
        String string = "";
        try {
            string = new String(Base64.decode(s, Base64.DEFAULT));
        } catch (Exception e) {
//            Utils.LogE("String decode failed:" + s);
        }
        return string;
    }

    public static String encode64String(String s) {
        return Base64.encodeToString(s.getBytes(), Base64.NO_WRAP);
    }
    /**
     * 日期转字符串
     *
     * @param time       日期时间戳
     * @param outFormate
     * @return
     */
    public static String long2String(long time, String outFormate) {
        SimpleDateFormat formatter = new SimpleDateFormat(outFormate);
        Date curDate = new Date(time * 1000);
        String str = formatter.format(curDate);
        return str;
    }

    //字符 转时间戳
    public static Long getStringToTime(String user_time, String format) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Long.parseLong(re_time);
    }
    /**
     * 字符转换时间戳
     *
     * @param time
     * @param sFormat 格式
     * @return
     */
    public static String getLongTime(String time, String sFormat) {
        String re_time = null;
        SimpleDateFormat format = new SimpleDateFormat(sFormat);
        Date d;
        try {
            d = format.parse(time);
            long l = d.getTime() / 1000;
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * @param time 时间戳格式的字符串
     * @return
     */
    public static String getStringTime(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long lcc_time = Long.valueOf(time);
        return sdf.format(new Date(lcc_time * 1000L));
    }

    public static ArrayList<String> str2Array(String s) {
        if (s != null) {
            ArrayList<String> list = new ArrayList<String>();
            String[] t = s.substring(1, s.length() - 1).split(",");
            for (String str : t) {
                File file = new File(Uri.parse(str).getPath());
                if (file.exists()) {
//					list.add(Uri.fromFile(file).toString());
                    list.add(str.trim());
                }
            }
            return list;
        }
        return null;
    }

    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static ArrayList<String> keyStr2Array(String key) {
        ArrayList<String> list = new ArrayList<String>();
        if (key != null && key.length() != 0) {
//            if (!key.contains(",")) {
//                return list;
//            } else {
            String[] t = key.substring(1, key.length() - 1).split(",");
            for (String str : t) {
                if (str.length() != 0 && str != "") {
                    list.add(str.trim());
                }
            }
//            }
        }
        return list;
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String StringFilter(String str1) throws PatternSyntaxException {
        str1 = str1.replaceAll("，", ",").replaceAll("！", "!")
                .replaceAll("？", "?").replaceAll("：", ":")
                .replaceAll("（", "(").replaceAll("）", ")");//替换成英文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str1);
        return m.replaceAll("").trim();
    }


    /**
     * 转化全角
     *
     * @return
     */
    public static String toDBC(String source) {
        char[] c = source.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);

    }

    public static byte[] urlsafeEncodeBytes(byte[] src) {
        if (src.length % 3 == 0) {
            return Base64.encode(src, Base64.URL_SAFE | Base64.NO_WRAP);
        }

        byte[] b = Base64.encode(src, Base64.URL_SAFE | Base64.NO_WRAP);
        if (b.length % 4 == 0) {
            return b;
        }

        int pad = 4 - b.length % 4;
        byte[] b2 = new byte[b.length + pad];
        System.arraycopy(b, 0, b2, 0, b.length);
        b2[b.length] = '=';
        if (pad > 1) {
            b2[b.length + 1] = '=';
        }
        return b2;
    }
}
