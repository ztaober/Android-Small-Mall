package com.qws.nypp.activity.settting;

import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;

/**
 * 二维码
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class QRCodeActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_qrcode;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("二维码");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}