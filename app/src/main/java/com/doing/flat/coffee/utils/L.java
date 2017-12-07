package com.doing.flat.coffee.utils;

import android.util.Log;

/**
 * log util
 * 
 * just set isLog=false if you want to close all the log printed by this util
 * 
 * @author cym
 */
public class L {

	private static final boolean isLog = true;
	private static final String tag = "mmmm";

	public static void d(String msg) {
		if (isLog) {
			Log.d(tag, msg);
		}
	}

	public static void i(String msg) {
		if (isLog) {
			Log.i(tag, msg);
		}
	}

	public static void e(String msg) {
		if (isLog) {
			Log.e(tag, msg);
		}
	}

	public static void v(String msg) {
		if (isLog) {
			Log.v(tag, msg);
		}
	}

	public static void w(String msg) {
		if (isLog) {
			Log.w(tag, msg);
		}
	}

	// byte数组转16进制字符
	public static String getHexString(byte[] b) throws Exception {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			result += ",";
		}
		return result;
	}
}
