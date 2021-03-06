package com.qws.nypp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.qws.nypp.R;
import com.qws.nypp.utils.AppManager;
import com.qws.nypp.utils.SystemBarTintManager;
import com.qws.nypp.utils.Util;
import com.qws.nypp.view.LoadingView;
import com.qws.nypp.view.TitleView;

import de.greenrobot.event.EventBus;

/**
 * @Description Acitivity基类
 * @author qw
 * @date 2015-6-22
 */
public abstract class BaseActivity extends Activity {
	protected Context context = BaseActivity.this;
	/** 获取当前类 */
	private Class<? extends Activity> c = this.getClass();
	/** 当前Activity的类名,用于判断当前是哪个Activity在最前面 */
	public static String clazzName;
	/** 用于判断应用是否退出到后台 */
	public static boolean isBack = false;
	/** 4.4版本以上的沉浸式 */
	protected SystemBarTintManager mTintManager;
	/** 标题 */
	protected TitleView titleView;
	/** 加载框View */
	protected LoadingView mLoadingView;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(context, getContentViewId(), null);
		// 布局内容会从actionbar以下开始
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			view.setFitsSystemWindows(true);
		}
		setContentView(view);
		titleView = (TitleView) findViewById(R.id.view_title);
		mLoadingView = (LoadingView) findViewById(R.id.network_error);
		findViews();
		initData();
		setListener();
		getData();
		if (useEventBus()) {
			EventBus.getDefault().register(this);
		}
		mTintManager = new SystemBarTintManager(this);
		setStatusBarState();
		
		AppManager.getAppManager().addActivity(this);
	}

	/** 是否需要注册EventBus */
	protected boolean useEventBus() {
		return false;
	}

	/** 获取布局id */
	protected abstract int getContentViewId();

	/** 查询View对象 */
	protected abstract void findViews();

	/** 初始化数据,设置数据 */
	protected abstract void initData();

	/** 设置监听 */
	protected abstract void setListener();

	/** 获取网络数据 */
	protected abstract void getData();

	@Override
	protected final void onResume() {
		super.onResume();
		onResume(isBack);
		clazzName = c.getName();
		isBack = false;
	}

	/**
	 * onResume执行后执行
	 * 
	 * @updateTime 2015-6-22,下午3:04:17
	 * @updateAuthor qw
	 * @param isBack 是否是从应用后台进入
	 */
	protected void onResume(boolean isBack) {
	}

	@Override
	protected final void onStop() {
		super.onStop();
		if (Util.isApplicationBroughtToBackground(context)) {
			isBack = true;
		}
		onStop(isBack);
	}

	/**
	 * onStop执行后执行
	 * 
	 * @updateTime 2015-6-22,下午3:05:21
	 * @updateAuthor qw
	 * @param isBack 是否是退出到后台
	 */
	protected void onStop(boolean isBack) {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
		if (useEventBus()) {
			EventBus.getDefault().unregister(this);
		}
	}

	@Override
	public void finish() {
		finishNoAnim();
		overridePendingTransition(R.anim.exit_enter, R.anim.exit_exit);
	}

	public void finishNoAnim() {
		closeKeyboard();
		super.finish();
		overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
	}

	public void closeKeyboard() {
		try {
			if (getCurrentFocus() != null && getCurrentFocus().getApplicationWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
			}
		} catch (Exception e) {
		}
	}

	private void setStatusBarState() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			mTintManager = new SystemBarTintManager(this);
			mTintManager.setStatusBarTintEnabled(true);
			// 使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
			mTintManager.setStatusBarTintResource(R.color.comm_color);
			// 设置状态栏的文字颜色
			mTintManager.setStatusBarDarkMode(false, this);
		}
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
}
