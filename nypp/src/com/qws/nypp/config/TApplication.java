package com.qws.nypp.config;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.qws.nypp.R;
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
	
	private static TApplication mInstance;
	/** 全局上下文，可用于文本、图片、sp数据的资源加载，不可用于视图级别的创建和展示 */
	public static Context context;
	/** 屏幕的宽 */
	public static int screenWidth = 0;
	/** 屏幕的高 */
	public static int screenHight = 0;
	/** 全局异常异步处理对象 */
	private static UncaughtExceptionHandler defaultUncaught;
	
	public static TApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// 实例化全局调用的上下文
		context = getApplicationContext();
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHight = getResources().getDisplayMetrics().heightPixels;
		DataBaseManage.createPulibicDataBase();
		 defaultUncaught = Thread.getDefaultUncaughtExceptionHandler();
		 Thread.setDefaultUncaughtExceptionHandler(this);
		initImageLoader();
		NoHttp.init(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("Exception", "", ex);
		defaultUncaught.uncaughtException(thread, ex);
	}
	
	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCache(new LruMemoryCache(20 * 1024 * 1024)).memoryCacheSize(20 * 1024 * 1024)
				.diskCacheSize(30 * 1024 * 1024).diskCacheFileCount(100).build();
//		ImageLoader.getInstance().init(config);
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
	}
	
	// 默认的
	public DisplayImageOptions getOptions() {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.pic_default_no)
				.showImageForEmptyUri(R.drawable.pic_default_no).showImageOnFail(R.drawable.pic_default_no)
				.cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.RGB_565) // default
				// .displayer(new FadeInBitmapDisplayer(300)) // 设置这个，刷新图片时不会闪
				.displayer(new SimpleBitmapDisplayer()).build();
	}
	
	// 自定义默认的
	public DisplayImageOptions getOptions(int resouceId) {
		return new DisplayImageOptions.Builder().showImageOnLoading(0)
				.showImageForEmptyUri(resouceId).showImageOnFail(resouceId)
				.cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.RGB_565) // default
				// .displayer(new FadeInBitmapDisplayer(300)) // 设置这个，刷新图片时不会闪
				.displayer(new SimpleBitmapDisplayer())
				.displayer(new FadeInBitmapDisplayer(500))
				.build();
	}
	
	// 自定义默认的
	public DisplayImageOptions getAllOptions(int resouceId) {
		return new DisplayImageOptions.Builder().showImageOnLoading(resouceId)
				.showImageForEmptyUri(resouceId).showImageOnFail(resouceId)
				.cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.RGB_565) // default
				// .displayer(new FadeInBitmapDisplayer(300)) // 设置这个，刷新图片时不会闪
				.displayer(new SimpleBitmapDisplayer())
				.displayer(new FadeInBitmapDisplayer(500))
				.build();
	}

}