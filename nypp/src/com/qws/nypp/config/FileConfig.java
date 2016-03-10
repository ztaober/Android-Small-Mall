package com.qws.nypp.config;

import android.os.Environment;

/**
 * 文件配置,私人文件夹需要初始化
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class FileConfig {

	// *************************** 应用使用的文件路径 *****************************//
	// 公用文件路径
	/** 应用根目录 */
	public static final String PATH_BASE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ProjectName/";
	/** 应用Log日志 */
	public static final String PATH_LOG = PATH_BASE + "Log/";
	/** 下载文件文件夹 */
	public static final String PATH_DOWNLOAD = PATH_BASE + "Download/";
	/** 拍照文件夹 */
	public static final String PATH_CAMERA = PATH_BASE + "Camera/";
	/** 应用基本缓存文件图片路径 */
	public static final String PATH_IMAGES = PATH_BASE + "Image/";
	/** 收藏的图片路径 */
	public static final String PATH_PHOTOS = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/baseproject/";
	/** 拍照缓存文件 */
	public static final String PATH_IMAGE_TEMP = PATH_CAMERA + "temp.jpg";
	/** 图片压缩缓存文件 */
	public static final String PATH_IMAGE_CUT = PATH_CAMERA + "cut.jpg";
	/** 图片裁剪缓存文件 */
	public static final String PATH_IMAGE_CLIP = PATH_CAMERA + "clip.jpg";

	// 用户私有文件路径
	/** 用户私有缓存文件夹 */
	public static String PATH_USER_FILE = "";
	/** 用户私有图片缓存文件夹 */
	public static String PATH_USER_IMAGE = "";
	/** 用户私有图片缩略图 缓存文件夹 */
	public static String PATH_USER_THUMBNAIL = "";
	/** 拍照上传名字 */
	public static String UPLOAD_FILE_NAME = "";
	/** 用户私有视频缓存文件夹 */
	public static String PATH_USER_VIDEO = "";
	/** 用户私有录音缓存文件夹 */
	public static String PATH_USER_AUDIO = "";
	/** 用户私收藏文件夹 */
	public static String PATH_USER_FAVORITES = "";
}
