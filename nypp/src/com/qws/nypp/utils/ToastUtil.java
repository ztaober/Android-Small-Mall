package com.qws.nypp.utils;

import com.qws.nypp.config.TApplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Toast操作工具类
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class ToastUtil {

	private static Toast toast;

	public static void showToast(Context context, Object text) {
		if (text == null) {
			return;
		}
		if (toast == null) {
			toast = Toast.makeText(context, text.toString(), Toast.LENGTH_SHORT);
		} else {
			toast.setText(text.toString());
		}
		toast.show();
	}
	
	public static void show(Object text) {
		if(text == null){
			return;
		}
		if (toast == null) {
			toast = Toast.makeText(TApplication.context, text.toString(), Toast.LENGTH_SHORT);
		} else {
			toast.setText(text.toString());
		}
		toast.show();
	}
}