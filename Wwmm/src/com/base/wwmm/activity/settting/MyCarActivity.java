package com.base.wwmm.activity.settting;

import com.base.wwmm.R;
import com.base.wwmm.activity.BaseActivity;

/**
 * 购物车
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class MyCarActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_my_car;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("购物车");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}