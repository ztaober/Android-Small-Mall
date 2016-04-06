package com.qws.nypp.config;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.qws.nypp.database.DataBaseManage;
import com.yolanda.nohttp.NoHttp;

/**
 * 应用入口
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class TApplication extends Application implements UncaughtExceptionHandler {
	/** 全局上下文，可用于文本、图片、sp数据的资源加载，不可用于视图级别的创建和展示 */
	public static Context context;
	/** 屏幕的宽 */
	public static int screenWidth = 0;
	/** 屏幕的高 */
	public static int screenHight = 0;
	/** 全局异常异步处理对象 */
	private static UncaughtExceptionHandler defaultUncaught;

	@Override
	public void onCreate() {
		super.onCreate();
		// 实例化全局调用的上下文
		context = getApplicationContext();
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHight = getResources().getDisplayMetrics().heightPixels;
		DataBaseManage.createPulibicDataBase();
		// defaultUncaught = Thread.getDefaultUncaughtExceptionHandler();
		// Thread.setDefaultUncaughtExceptionHandler(this);
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
		NoHttp.init(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

	}
}