package com.base.wwmm.activity.home;

import com.base.wwmm.R;
import com.base.wwmm.activity.BaseActivity;

/**
 * 选择支付方式
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class PayModeActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_pay_mode;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("选择支付方式");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}