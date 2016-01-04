package com.base.wwmm.activity.home;

import com.base.wwmm.R;
import com.base.wwmm.activity.BaseActivity;

/**
 * 商品详情
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class GoodsDetailActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_goods_detail;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("商品详情");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}
}