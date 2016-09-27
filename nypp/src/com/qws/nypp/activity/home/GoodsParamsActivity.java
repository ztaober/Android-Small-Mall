package com.qws.nypp.activity.home;

import android.widget.TextView;

import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.bean.GoodsDetailBean;

/**
 * 产品参数页面
 * 
 * @Description
 * @author troy
 * @date 2016-7-27 下午8:11:50
 * @Copyright:
 */
public class GoodsParamsActivity extends BaseActivity {
	
	private GoodsDetailBean goodsDetailBean;
	
	private TextView atrNoTv;

	@Override
	protected int getContentViewId() {
		return R.layout.a_detail_params;
	}

	@Override
	protected void findViews() {
		atrNoTv = (TextView) findViewById(R.id.detial_params_atrno);
	}

	@Override
	protected void initData() {
		titleView.setTitle("产品参数");
		
		goodsDetailBean = (GoodsDetailBean) getIntent().getSerializableExtra("goodsDetailBean");
		if(goodsDetailBean == null){
			return;
		}
		atrNoTv.setText(goodsDetailBean.atrNo);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}


	@Override
	protected void getData() {
	}

}
