package com.qws.nypp.fragment;

import com.qws.nypp.R;

import android.view.View;

public class OrderFragment extends BaseFragment {

	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_order, null);
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("进货单");
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}

}
