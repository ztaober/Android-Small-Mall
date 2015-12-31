package com.base.wwmm.activity;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;

import com.base.wwmm.R;

public class MainActivity extends BaseFragmentActivity {
	/** fragment模块集合 */
	private List<Fragment> fragments = new ArrayList<Fragment>();
	/** 上次按退出的时间 */
	private long lastExitTime = 0;

	@Override
	protected int getContentViewId() {
		return R.layout.a_main;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}
}