package com.base.wwmm.activity.settting;

import com.base.wwmm.R;
import com.base.wwmm.activity.BaseActivity;

/**
 * 收货地址详情
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class AddrDetailActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_addr_detail;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("收货地址详情");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}