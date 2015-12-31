package com.base.wwmm.activity;

import android.view.View;
import android.view.View.OnClickListener;

import com.base.wwmm.R;
import com.base.wwmm.utils.IntentUtil;

/**
 * 登录界面
 * 
 * @Description
 * @author
 * @date 2015-12-30
 */
public class LoginActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_login;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {
		findViewById(R.id.login_btn_login).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentUtil.gotoActivityAndFinish(context, MainActivity.class);
			}
		});
	}

	@Override
	protected void getData() {

	}
}