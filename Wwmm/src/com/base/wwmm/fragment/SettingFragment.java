package com.base.wwmm.fragment;

import com.base.wwmm.R;

import android.view.View;

/**
 * 设置
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class SettingFragment extends BaseFragment {

	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_setting, null);
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("设置");
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}
}