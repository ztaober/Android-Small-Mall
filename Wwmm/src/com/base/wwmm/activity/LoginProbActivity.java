package com.base.wwmm.activity;

import com.base.wwmm.R;

/**
 * 登录帮助提示
 * 
 * @Description
 * @author
 * @date 2015-12-30
 */
public class LoginProbActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_login_probl;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("帮助提示");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}