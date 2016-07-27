package com.qws.nypp.utils;

import java.util.Stack;

import com.qws.nypp.activity.LoginActivity;
import com.qws.nypp.activity.MainActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序
 * 
 * @Description
 * @author troy
 * @date 2016-7-26 下午7:27:46
 * @Copyright:
 */
public class AppManager {

	private static Stack<Activity> activityStack;

	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束Activity
	 */
	public void finishAllActivity(boolean isContainLogin) {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			Activity activity = activityStack.get(i);
			if (null != activity) {
				if (activity instanceof LoginActivity && !isContainLogin) {
					continue;
				}
				activity.finish();
			}
		}
		activityStack.clear();
	}
	
	/**
	 * 回到桌面 Activity
	 */
	public void goHomeActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			Activity activity = activityStack.get(i);
			if (null != activity) {
				if (activity instanceof LoginActivity || activity instanceof MainActivity) {
					continue;
				}
				activity.finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * activity 启动
	 * 
	 * @param activity
	 * @param appIntent
	 */
	public void startActivity(Context activity, Intent appIntent) {
		appIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(appIntent);
		// activity.overridePendingTransition(android.R.anim.slide_in_left,
		// android.R.anim.slide_in_left);
	}

	/**
	 * * 退出应用程序
	 */
	public void AppExit(Context context, boolean isContainLogin) {
		try {
//			LoginUtil.isLogin = false;
//			// 退出消息通道
//			MsgSyncCenter.getInstance(context).logout(null, null);
			
			// 关闭工程相关页面
			finishAllActivity(isContainLogin);
			if (isContainLogin) {
				finishActivity(LoginActivity.class);
				ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
				activityMgr.restartPackage(context.getPackageName());
				// 退出程序
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		} catch (Exception e) {
		}
	}
}