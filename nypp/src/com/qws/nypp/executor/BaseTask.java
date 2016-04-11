package com.qws.nypp.executor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.qws.nypp.bean.ResponseBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.utils.HandlerUtil;
import com.qws.nypp.utils.ProcessDialogUtil;

/**
 * 基础事务可以实现单独的网络请求，带缓存的请求和带数据库缓存的请求可以继承此事务后作扩展。
 */
public abstract class BaseTask<T> implements Runnable {

	/** 请求缓存成功 */
	protected static final int REQUEST_CACHE = 110;
	/** 请求成功 */
	protected static final int REQUEST_SUCCESS = 100;
	/** 请求失败 */
	protected static final int REQUEST_FAIL = 120;

	/** 请求返回数据 */
	protected ResponseBean<T> result;

	/** 等待提示信息 */
	protected String processMsg = "";
	/** 窗口是否可取消 */
	protected boolean cancelable = true;

	/** 当前线程对象 */
	protected Thread currentThread;

	/**
	 * 无参构造函数
	 * 
	 */
	public BaseTask() {
	};

	/**
	 * @param context 上下文
	 * @param processMsg 如果内容不为空，则会显示提示框，否则不显示
	 * @param cancelable 提示框是否可以取消，默认可以取消
	 */
	public BaseTask(Context context, String processMsg, boolean cancelable) {
		this.processMsg = processMsg;
		this.cancelable = cancelable;
		if (!TextUtils.isEmpty(this.processMsg)) {
			ProcessDialogUtil.showDialog(context, processMsg, this.cancelable);
			ProcessDialogUtil.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					onDismissDo();
					interrupt();
				}
			});
		}
	};

	private void onDismissDo() {

	}

	/**
	 * 设置可以显示DiaLog
	 * 
	 * @param context 上下文
	 * @param processMsg 如果内容不为空，则会显示提示框，否则不显示
	 * @param cancelable 提示框是否可以取消，默认可以取消
	 * @return
	 */
	public BaseTask<T> setDialogShow(Context context, String processMsg, boolean cancelable) {
		this.processMsg = processMsg;
		this.cancelable = cancelable;
		if (!TextUtils.isEmpty(this.processMsg)) {
			ProcessDialogUtil.showDialog(context, processMsg, this.cancelable);
			ProcessDialogUtil.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					onDismissDo();
					interrupt();
				}
			});
		}
		return this;
	}

	/**
	 * 事务执行线程
	 * 
	 */
	@Override
	public void run() {
		currentThread = Thread.currentThread();
		// 读取网络数据
		result = sendRequest();
		if (result.getStatus().equals(ServerConfig.RESPONSE_STATUS_SUCCESS)) {
			HandlerUtil.sendMessage(mHandler, REQUEST_SUCCESS, result);
		} else {
			HandlerUtil.sendMessage(mHandler, REQUEST_FAIL, result);
		}

	}

	/**
	 * 发送请求
	 * 
	 * @return 请求返回数据
	 */
	public abstract ResponseBean<T> sendRequest();

	/**
	 * 请求成功回调
	 * 
	 * @param result 请求返回数据
	 */
	public abstract void onSuccess(ResponseBean<T> result);

	/**
	 * 请求失败回调
	 * 
	 * @param result 请求返回数据
	 */
	public abstract void onFail(ResponseBean<T> result);

	/**
	 * 终止线程
	 */
	public void interrupt() {
		if (null != currentThread) {
			currentThread.interrupt();
		}
	}

	/**
	 * 异步处理Handler对象
	 */
	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_SUCCESS: // 网络请求数据成功
				ProcessDialogUtil.dismissDialog();
				onSuccess((ResponseBean<T>) msg.obj);
				break;
			case REQUEST_FAIL: // 网络请求数据失败
				ProcessDialogUtil.dismissDialog();
				ResponseBean<T> rb = (ResponseBean<T>) msg.obj;
				if (rb.getInfo() == null) {
					rb.setInfo("请求失败");
				} else if (rb.getObject() == null) {
					rb.setInfo("请求失败");
				}
				onFail((ResponseBean<T>) msg.obj);
				break;
			}
		}

	};

}
