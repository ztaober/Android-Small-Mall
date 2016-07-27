package com.qws.nypp.activity.home;

import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;

/**
 * 产品参数页面
 * 
 * @Description
 * @author troy
 * @date 2016-7-27 下午8:11:50
 * @Copyright:
 */
public class GoodsParamsActivity extends BaseActivity {


	@Override
	protected int getContentViewId() {
		return R.layout.a_detail_params;
	}

	@Override
	protected void findViews() {
	}

	@Override
	protected void initData() {
		titleView.setTitle("产品参数");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}


	@Override
	protected void getData() {
	}

}
