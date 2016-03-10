package com.qws.nypp.fragment;

import com.qws.nypp.R;

import android.view.View;

/**
 * 自选
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class OptionalFragment extends BaseFragment {

	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_optional, null);
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("自选");
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}
}