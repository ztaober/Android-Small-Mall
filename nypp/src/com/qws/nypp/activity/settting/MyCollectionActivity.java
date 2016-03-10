package com.qws.nypp.activity.settting;

import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;

/**
 * 我的收藏
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class MyCollectionActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_my_collection;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("我的收藏");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}