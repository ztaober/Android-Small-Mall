package com.base.wwmm.activity.settting;

import com.base.wwmm.R;
import com.base.wwmm.activity.BaseActivity;

/**
 * 意见反馈
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class OpinionActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_opinion;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("意见反馈");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}