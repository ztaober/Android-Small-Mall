package com.base.wwmm.fragment;

import com.base.wwmm.R;

import android.view.View;

/**
 * 热区
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class HomeFragment extends BaseFragment {

	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_home, null);
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("热区");
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}
}