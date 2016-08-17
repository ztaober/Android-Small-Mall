package com.qws.nypp.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.qws.nypp.http.CallServer;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;

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
        if (cur - lastClickTime < 500) {
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
	
	public static boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}
	
	/**
	 * 获取应用当前版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getSoftVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packinfo.versionName;
		} catch (NameNotFoundException e) {
			return "1.0";
		}
	}
	
	/**
	 * 是否安装微信
	 * 
	 * @updateTime 2016-8-4 上午10:42:20
	 * @updateAuthor troy
	 * @updateInfo 
	 * @param context
	 * @return
	 */
	public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
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
	
	/**
	 * 开启下载，以notification通知的形式显示，百分百形式
	 * 
	 * @param context
	 * @param url
	 */
	private int mId = 0;
	private NotificationManager mNotificationManager;
	private String mTitle, mMessage;
	public void startNotiUpdateTask(final Context context, final String url, final String title) {
		int id = mId++;
		// 初始化Notification对象
		mNotificationManager = (NotificationManager) context.getSystemService(
				Context.NOTIFICATION_SERVICE);
		mTitle = "开始下载";
		mMessage = "正在下载";
		// 调用下载
		startDownload(context, url, title, id);
	}

	private void startDownload(final Context context, String url, String title, final int id) {
		// 开始下载了，但是任务没有完成，代表正在下载，那么暂停下载。
        if (downloadRequest != null && downloadRequest.isStarted() && !downloadRequest.isFinished()) {
        	Toast.makeText(context, "当前已经存在该任务下载!", Toast.LENGTH_SHORT).show();
			return;
        }
		
		final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);//0306工程
		mBuilder.setSmallIcon(android.R.drawable.stat_sys_download).setContentTitle(title)
				.setWhen(System.currentTimeMillis()).setContentText(mMessage)
				.setOngoing(true).setTicker(title).setProgress(100, 0, false);
		mNotificationManager.notify(id, mBuilder.build());
		
		downloadRequest = NoHttp.createDownloadRequest(url, FilePathUtils.getDefaultFilePath(context), url.substring(url.lastIndexOf("/") + 1, url.length()), true, true);
		CallServer.getDownloadInstance().add(5555, downloadRequest, new DownloadListener() {
			private long lastClickTime;
			@Override
			public void onStart(int what, boolean isResume, long rangeSize,
					Headers responseHeaders, long allCount) {
			}
			
			@Override
			public void onProgress(int what, int progress, long fileCount) {
				long time = System.currentTimeMillis();
				long timeD = time - lastClickTime;
				if (0 < timeD && timeD < 500) {
					return; // 防止更新速度过快，而产生卡顿
				}
				lastClickTime = time;

				mBuilder.setProgress(100, progress, false);
				mBuilder.setContentText(" 已接收" + formatFileLen(fileCount));
				mNotificationManager.notify(id, mBuilder.build());
			}
			
			@Override
			public void onFinish(int what, String filePath) {
				mNotificationManager.cancel(id);
				openFile(context, new File(filePath));
			}
			
			@Override
			public void onDownloadError(int what, Exception exception) {
				mNotificationManager.cancel(id);
				Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onCancel(int what) {
				
			}
		});
	}
	
	/**
	 * 计算百分比
	 * 
	 * @param all
	 *            , pro
	 * @return
	 */
	public static String getSHCollagen(int all, int pro) {
		String str = "";
		if (all < 0 || pro < 0 || all < pro) {
			return str;
		}
		try {
			double proTemp = (double) pro * 100;
			double allTemp = all;
			BigDecimal bigPro = new BigDecimal(proTemp + "");
			BigDecimal bigAll = new BigDecimal(allTemp + "");
			BigDecimal proDou = bigPro.divide(bigAll, 2, BigDecimal.ROUND_HALF_UP);
			str = proDou.toString();
			if (str.indexOf(".") > 0) {
				str = str.substring(0, str.indexOf("."));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str + "%";
	}
	
	/**
	 * 格式化文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileLen(long size) {
		if (size >= 1024 * 1024 * 1024) {
			return String.format("%.1fG", (size * 1.0) / (1024 * 1024 * 1024));
		}
		if (size > 1024 * 1024) {
			return String.format("%.1fM", (size * 1.0) / (1024 * 1024));
		}
		if (size > 1024) {
			return String.format("%.1fK", (size * 1.0) / 1024);
		}
		return size + "B";
	}
	
	public static void openFile(Context ct, File file) {
		try {
			if (null == file || !file.exists()) {
				Toast.makeText(ct, "文件不存在！", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 设置intent的Action属性
			intent.setAction(Intent.ACTION_VIEW);
			// 获取文件file的MIME类型
			String type = getMIMEType(file);
			// 设置intent的data和Type属性。
			intent.setDataAndType(/* uri */Uri.fromFile(file), type);
			// 跳转
			ct.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(ct, "找不到可以打开该文件类型的应用\r\n文件存放于:\r\n" + file.getPath(), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	private static String getMIMEType(File file) {

		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if ("".equals(end)) {
			return type;
		}
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	private final static String[][] MIME_MapTable = {
			// {后缀名，MIME类型}
			{ ".3gp", "video/3gpp" }, { ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" }, { ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" }, { ".c", "text/plain" }, { ".class", "application/octet-stream" },
			{ ".conf", "text/plain" }, { ".cpp", "text/plain" }, { ".doc", "application/msword" },
			{ ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" }, { ".gif", "image/gif" }, { ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" }, { ".h", "text/plain" }, { ".htm", "text/html" }, { ".html", "text/html" },
			{ ".jar", "application/java-archive" }, { ".java", "text/plain" }, { ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" }, { ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" }, { ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" }, { ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" }, { ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" }, { ".mpc", "application/vnd.mpohun.certificate" }, { ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" }, { ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" }, { ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" }, { ".pdf", "application/pdf" },
			{ ".png", "image/png" }, { ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" },
			{ ".rtf", "application/rtf" }, { ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" }, { ".wav", "audio/x-wav" },
			{ ".wma", "audio/x-ms-wma" }, { ".wmv", "audio/x-ms-wmv" }, { ".wps", "application/vnd.ms-works" },
			{ ".xml", "text/plain" }, { ".z", "application/x-compress" }, { ".zip", "application/x-zip-compressed" },
			{ "", "*/*" } };
	private DownloadRequest downloadRequest;

}
