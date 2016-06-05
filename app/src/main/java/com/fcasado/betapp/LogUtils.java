package com.fcasado.betapp;

import android.util.Log;

/**
 * Created by fcasado on 6/5/16.
 */
public class LogUtils {
	public static void d(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		Log.d(tag, msg, tr);
	}

	public static void d(String tag, String msg, Throwable tr) {
		Log.d(tag, msg, tr);
	}
}
