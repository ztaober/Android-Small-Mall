package com.base.wwmm.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

/**
 * 处理图片的工具类
 * 
 * @Description
 * @author 綦巍
 * @date 2015-4-22
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class BitmapUtil {
	public int screenW;
	public int screenH;
	public int navigationBarHeight;
	public int statusBarHeight;

	private BitmapUtil(Context context) {
		Resources resources = context.getResources();
		screenW = resources.getDisplayMetrics().widthPixels;
		screenH = resources.getDisplayMetrics().heightPixels;
		try {
			statusBarHeight = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
		} catch (NotFoundException e) {
			statusBarHeight = 0;
		}
		try {
			navigationBarHeight = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
		} catch (NotFoundException e) {
			navigationBarHeight = 0;
		}
	}

	public static void init(Context context) {
		synchronized (BitmapUtil.class) {
			if (util == null) {
				util = new BitmapUtil(context);
			}
		}
	}

	private static BitmapUtil util;

	public static BitmapUtil getInstance() {
		if (util == null) {
			throw new RuntimeException("BitmapUtil is not init!");
		}
		return util;
	}

	/**
	 * 根据图片和路径返回需要的最小图片
	 * 
	 * @createTime 2015-4-22,下午6:23:30
	 * @createAuthor 綦巍
	 */
	public Bitmap getMinByBitmap(Bitmap bitmap, ImageView imageView, String imgPath) {
		if (imageView == null || imageView.getWidth() == 0 || imageView.getHeight() == 0) {
			return getMinByBitmap(bitmap, screenW, screenH, imgPath);
		} else {
			return getMinByBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), imgPath);
		}
	}

	/**
	 * 根据图片和路径返回需要的最小图片
	 * 
	 * @createTime 2015-4-22,下午6:23:30
	 * @createAuthor 綦巍
	 */
	public Bitmap getMinByBitmap(Bitmap bitmap, int width, int height, String imgPath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = getProportion(bitmap.getWidth(), bitmap.getHeight(), width, height);
		if (options.inSampleSize == 1) {
			return bitmap;
		}
		return BitmapFactory.decodeFile(imgPath, options);
	}

	/**
	 * 根据路径返回需要的最小图片
	 * 
	 * @createTime 2015-4-22,下午6:23:30
	 * @createAuthor 綦巍
	 */
	public Bitmap getMinByPath(String path, ImageView imageView) {
		if (imageView == null || imageView.getWidth() == 0 || imageView.getHeight() == 0) {
			return getMinByPath(path, screenW, screenH);
		} else {
			return getMinByPath(path, imageView.getWidth(), imageView.getHeight());
		}
	}

	/**
	 * 根据路径返回需要的最小图片
	 * 
	 * @createTime 2015-4-22,下午6:23:30
	 * @createAuthor 綦巍
	 */
	public Bitmap getMinByPath(String path, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = getProportion(options.outWidth, options.outHeight, width, height);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * 计算当前需要的比例
	 * 
	 * @version 1.0
	 * @createTime 2015-4-27,下午1:46:31
	 * @updateTime 2015-4-27,下午1:46:31
	 * @createAuthor 綦巍
	 * @updateAuthor 綦巍
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param nowW 当前图片的宽
	 * @param nowH 当前图片的高
	 * @param needW 需要的宽
	 * @param needH 需要的高
	 * @return
	 */
	public int getProportion(int nowW, int nowH, int needW, int needH) {
		float proportionW = nowW / needW;
		float proportionH = nowH / needH;
		float proportion = proportionW > proportionH ? proportionH : proportionW;
		if ((int) (proportion + 0.5f) > 1) {
			return (int) (proportion + 0.5f);
		} else {
			return 1;
		}
	}

	/**
	 * 通过ImageView和path获取需要的图片的宽高
	 * 
	 * @version 1.0
	 * @param baseName
	 * @createTime 2015-4-27,下午2:17:55
	 * @updateTime 2015-4-27,下午2:17:55
	 * @createAuthor 綦巍
	 * @updateAuthor 綦巍
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param imageView
	 * @param path 图片路径
	 * @return
	 */
	public String getNeedWHByImageView(String baseName, ImageView imageView, String path) {
		return getNeedWHByImageView(baseName, imageView.getWidth(), imageView.getHeight(), path);
	}

	/**
	 * 通过宽高和path获取需要的图片的宽高
	 * 
	 * @version 1.0
	 * @param baseName
	 * @createTime 2015-4-27,下午2:17:55
	 * @updateTime 2015-4-27,下午2:17:55
	 * @createAuthor 綦巍
	 * @updateAuthor 綦巍
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param imageView
	 * @param path 图片路径
	 * @return
	 */
	public String getNeedWHByImageView(String baseName, int width, int height, String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = getProportion(options.outWidth, options.outHeight, width, height);
		BitmapFactory.decodeFile(path, options);
		return baseName + options.outWidth + "x" + options.outHeight;
	}

	/** 压缩图片到icon_max_size */
	public static Bitmap compressBitmap(Bitmap image) {
		int icon_max_size = 100 * 1024;
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		int size = bos.toByteArray().length;
		if (size > icon_max_size) {
			float scale = (float) icon_max_size / (float) size;
			// 长*宽，反求，计算大概比例。
			scale = (float) Math.sqrt(scale);
			Matrix matrix = new Matrix();
			matrix.reset();
			matrix.setScale(scale, scale);
			Bitmap bitmap = Bitmap.createBitmap(image, 0, 0, imageWidth, imageHeight, matrix, true);
			image.recycle();
			image = null;
			return bitmap;
		} else {
			return image;
		}
	}

	/** 压缩图片到指定径路,并返回径路 */
	public static String compressBitmapByPath(String imgPath, String savePath) {
		int icon_max_size = 600 * 1024;
		File file = new File(imgPath);
		if (file.exists()) {
			float scale = (float) file.length() / icon_max_size;
			int inSampleSize = (int) Math.sqrt(scale)+1;
			if (inSampleSize <= 1) {
				return imgPath;
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = inSampleSize > 0 ? inSampleSize : inSampleSize;
			Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
			if (setBitmapToPath(compressBitmap(bitmap), savePath)) {
				return savePath;
			}
		}
		return imgPath;
	}

	/** 保存图片到指定路径 */
	public static boolean setBitmapToPath(Bitmap bitmap, String savePath) {
		File f = new File(savePath);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			LogUtil.i("图片保存成功:" + f.length());
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}