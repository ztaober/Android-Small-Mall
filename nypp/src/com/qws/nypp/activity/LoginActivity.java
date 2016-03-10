package com.qws.nypp.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qws.nypp.R;
import com.qws.nypp.utils.IntentUtil;

/**
 * 登录界面
 * 
 * @Description
 * @author
 * @date 2015-12-30
 */
public class LoginActivity extends BaseActivity {
	/** 头像 */
	private ImageView login_img_head;
	/** 昵称 */
	private TextView login_txt_nike;
	/** 帐号 */
	private EditText login_edit_uesrname;
	/** 密码 */
	private EditText login_edit_password;

	@Override
	protected int getContentViewId() {
		return R.layout.a_login;
	}

	@Override
	protected void findViews() {
		login_img_head = (ImageView) findViewById(R.id.login_img_head);
		login_txt_nike = (TextView) findViewById(R.id.login_txt_nike);
		login_edit_uesrname = (EditText) findViewById(R.id.login_edit_uesrname);
		login_edit_password = (EditText) findViewById(R.id.login_edit_password);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {
		findViewById(R.id.login_txt_login).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentUtil.gotoActivityAndFinish(context, MainActivity.class);
			}
		});
		findViewById(R.id.login_txt_prob).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentUtil.gotoActivity(context, LoginProbActivity.class);
			}
		});
	}

	@Override
	protected void getData() {

	}
}