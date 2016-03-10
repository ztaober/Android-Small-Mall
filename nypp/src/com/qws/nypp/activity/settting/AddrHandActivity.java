package com.qws.nypp.activity.settting;

import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;

/**
 * 管理收货地址
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class AddrHandActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_addr_hand;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("管理收货地址");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}