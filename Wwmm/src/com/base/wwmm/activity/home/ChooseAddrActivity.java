package com.base.wwmm.activity.home;

import com.base.wwmm.R;
import com.base.wwmm.activity.BaseActivity;

/**
 * 选择收货地址
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class ChooseAddrActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_choose_addr;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("选择收货地址");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}