package com.base.wwmm.utils;

import android.os.Handler;
import android.os.Message;

/**
 * handler封装工具类
 * 
 * @Description TODO
 * @author CodeApe
 * @version 1.0
 * @date 2014年12月30日
 * @Copyright: Copyright (c) 2014 Shenzhen Utoow Technology Co., Ltd. All rights
 *             reserved.
 * 
 */
public class HandlerUtil {

	/**
	 * 发送消息
	 * 
	 * @version 1.0
	 * @createTime 2014年12月30日,下午11:59:09
	 * @updateTime 2014年12月30日,下午11:59:09
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param handler
	 *            handler对象
	 * @param what
	 *            消息类型
	 */
	public static void sendMessage(Handler handler, int what) {
		Message message = handler.obtainMessage();
		message.what = what;
		handler.sendMessage(message);
	}

	/**
	 * 发送消息
	 * 
	 * @version 1.0
	 * @createTime 2014年12月30日,下午11:59:09
	 * @updateTime 2014年12月30日,下午11:59:09
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param handler
	 *            handler对象
	 * @param what
	 *            消息类型
	 * @param object
	 *            消息附带的对象.
	 */
	public static void sendMessage(Handler handler, int what, Object object) {
		Message message = handler.obtainMessage();
		message.what = what;
		message.obj = object;
		handler.sendMessage(message);
	}

	/**
	 * 发送消息
	 * 
	 * @version 1.0
	 * @createTime 2014年2月21日 下午3:27:38
	 * @updateTime 2014年2月21日 下午3:27:38
	 * @createAuthor liuyue
	 * @updateAuthor liuyue
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param handler
	 * @param what
	 * @param arg1
	 * @param object
	 */

	/**
	 * 发送消息
	 * 
	 * @version 1.0
	 * @createTime 2014年12月30日,下午11:59:09
	 * @updateTime 2014年12月30日,下午11:59:09
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param handler
	 *            handler对象
	 * @param what
	 *            消息类型
	 * @param arg1
	 *            消息附带的信息1
	 * @param object
	 *            消息附带的对象.
	 */
	public static void sendMessage(Handler handler, int what, int arg1, Object object) {
		Message message = handler.obtainMessage();
		message.what = what;
		message.obj = object;
		message.arg1 = arg1;
		handler.sendMessage(message);
	}

	/**
	 * 发送消息
	 * 
	 * @version 1.0
	 * @createTime 2014年12月30日,下午11:59:09
	 * @updateTime 2014年12月30日,下午11:59:09
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param handler
	 *            handler对象
	 * @param what
	 *            消息类型
	 * @param arg1
	 *            消息附带的信息1
	 * @param arg2
	 *            消息附带的信息2
	 */
	public static void sendMessage(Handler handler, int what, int arg1, int arg2) {
		Message message = handler.obtainMessage();
		message.what = what;
		message.arg1 = arg1;
		message.arg2 = arg2;
		handler.sendMessage(message);
	}

	/**
	 * 发送消息
	 * 
	 * @version 1.0
	 * @createTime 2014年12月30日,下午11:59:09
	 * @updateTime 2014年12月30日,下午11:59:09
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param handler
	 *            handler对象
	 * @param what
	 *            消息类型
	 * @param arg1
	 *            消息附带的信息1
	 * @param arg2
	 *            消息附带的信息2
	 * @param object
	 *            消息附带的对象.
	 */
	public static void sendMessage(Handler handler, int what, int arg1, int arg2, Object object) {
		Message message = handler.obtainMessage();
		message.what = what;
		message.arg1 = arg1;
		message.arg2 = arg2;
		message.obj = object;
		handler.sendMessage(message);
	}


}
