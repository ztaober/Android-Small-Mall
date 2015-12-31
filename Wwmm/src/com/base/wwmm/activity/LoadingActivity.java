package com.base.wwmm.activity;

import com.base.wwmm.R;
import com.base.wwmm.utils.IntentUtil;
import com.base.wwmm.utils.SpUtil;

/**
 * 等待页
 * 
 * @Description
 * @author
 * @date 2015-12-30
 */
public class LoadingActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_loading;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		if (this.isTaskRoot()) {
			boolean firstInto = SpUtil.getSpUtil().getSPValue(SpUtil.FIRST_INTO, true);
			if (firstInto) {
				IntentUtil.gotoActivityAndFinish(context, SplashActivity.class);
			} else {
				getLoginInfo();
			}
		} else {
			finish();
		}
	}

	/**
	 * 自动登录操作
	 * 
	 * @updateTime 2015-12-31,下午6:06:17
	 * @updateAuthor
	 */
	private void getLoginInfo() {
		IntentUtil.gotoActivityAndFinish(context, LoginActivity.class);
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}
}