package com.qws.nypp.activity.home;

import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;

/**
 * 下单界面
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class PlaceOrderActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_place_order;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("下单界面");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}