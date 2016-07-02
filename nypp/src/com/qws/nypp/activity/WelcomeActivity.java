package com.qws.nypp.activity;

import com.qws.nypp.R;
import com.qws.nypp.utils.IntentUtil;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;

public class WelcomeActivity extends Activity {
	
	private CountDownTimer mCountDownTimer = new CountDownTimer(500, 500) {
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			IntentUtil.gotoActivityAndFinish(WelcomeActivity.this, LoginActivity.class);
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 设置默认键盘不弹出
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 设置设备为竖屏模式
		setContentView(R.layout.a_loading);
		mCountDownTimer.start();
	}
	
	@Override
	protected void onDestroy() {
		try {
			mCountDownTimer.cancel();
			super.onDestroy();
	
		} catch (Exception e) {
		}
	}

}
