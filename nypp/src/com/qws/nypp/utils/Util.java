package com.qws.nypp.utils;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import android.view.View;

public class Util {
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View convertView, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			convertView.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

	/**
	 * 判断当前应用程序处于前台还是后台
	 * 
	 * @updateTime 2015-6-22,下午2:44:41
	 * @updateAuthor qw
	 * @param context
	 * @return
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
	
	public static String getTime(long time){
		return new SimpleDateFormat("yyyy.MM.dd").format(new Date(time));
	}
	
	private static long lastClickTime;
    
    public static boolean isFastDoubleClick() {
        long cur = System.currentTimeMillis();
        if (cur - lastClickTime < 300) {
            lastClickTime = cur;
        	return true;
        }
        lastClickTime = cur;
        return false;
    }

	/**
	 * 使用md5的算法进行加密
	 * 
	 * @updateTime 2015-6-22,下午2:44:29
	 * @updateAuthor qw
	 * @param plainText
	 * @return
	 */
	public static String md5(String plainText) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}
	
	public static String md5three(String plainText){
		return md5(md5(md5(plainText)));
	}
	
	/**
	 * 获取 移动终端设备id号
	 * 
	 * @param context
	 *            :上下文文本对象
	 * @return id 移动终端设备id号
	 */
	public static String getDevId(Context context) {
		String id = SpUtil.getSpUtil().getSPValue("devicesID", "");
		if (id.length() == 0) {
			try {
				id = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			} catch (Exception e) {
			}
			if (id == null)
				id = "";
		}
		if (id.length() == 0) {
			try {
				id = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
			} catch (Exception e) {
			}
			if (id == null)
				id = "";
		}
		if (id.length() == 0) {
			try {
				id = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
			} catch (Exception e) {
			}
			if (id == null)
				id = "";
		}
		if (id.length() == 0) {
			try {
				Class<?> c = Class.forName("android.os.SystemProperties");
				Method get = c.getMethod("get", String.class, String.class);
				id = (String) (get.invoke(c, "ro.serialno", "unknown"));
			} catch (Exception e) {
			}
		}
		if (id.length() == 0 || "0".equals(id)) {
			// 随机生成
			id = UUID.randomUUID().toString().replaceAll("-", "");
			SpUtil.getSpUtil().putSPValue("devicesID", id);
		}
		return id;
	}
}
