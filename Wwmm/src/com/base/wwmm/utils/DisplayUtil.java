package com.base.wwmm.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

public class DisplayUtil {

	/**
	 * 获取通知栏高度
	 * 
	 * @updateTime 2015-6-22,下午2:40:49
	 * @updateAuthor qw
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * dp转px
	 * 
	 * @updateTime 2015-6-22,下午2:40:04
	 * @updateAuthor
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转dp
	 * 
	 * @updateTime 2015-6-22,下午2:41:09
	 * @updateAuthor qw
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @updateTime 2015-6-22,下午2:41:48
	 * @updateAuthor qw
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @updateTime 2015-6-22,下午2:41:48
	 * @updateAuthor qw
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 将View的宽变成屏幕的宽,高按比例给
	 * 
	 * @version 1.0
	 * @createTime 2015-4-30,下午5:28:01
	 * @updateTime 2015-4-30,下午5:28:01
	 * @createAuthor 綦巍
	 * @updateAuthor 綦巍
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param context
	 * @param view
	 * @param l
	 */
	public static void setViewWH(Context context, View view, float l) {
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		int height = (int) (screenWidth * l);
		view.setLayoutParams(new LayoutParams(screenWidth, height));
	}
}
