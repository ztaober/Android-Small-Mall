package com.qws.nypp.utils;

import android.util.Log;

public class LogUtil {
	/** 是否显示日志 */
	public static final boolean SHOW_LOG = true;

	public static void i(Object object) {
		if (SHOW_LOG) {
			Log.i("qw", object.toString());
		}
	}
}
