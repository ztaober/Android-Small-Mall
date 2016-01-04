package com.base.wwmm.activity.home;

import com.base.wwmm.R;
import com.base.wwmm.activity.BaseActivity;

/**
 * 确认订单界面
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class SureOrderActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_sure_detail;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("确认订单");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}