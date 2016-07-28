package com.qws.nypp.activity.settting.center;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.activity.MainActivity;
import com.qws.nypp.activity.settting.AddrDetailActivity;
import com.qws.nypp.activity.settting.AddrHandActivity;
import com.qws.nypp.config.SpConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.utils.AppManager;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.SpUtil;
import com.qws.nypp.view.dialog.FunctionDialog;
import com.qws.nypp.view.dialog.MenuCallback;

public class PersonalCenterActivity extends BaseActivity implements OnClickListener {
	
	private ImageView imgIv;
	private TextView nameTv;
	private DisplayImageOptions options;

	@Override
	protected int getContentViewId() {
		return R.layout.a_personal_center;
	}

	@Override
	protected void findViews() {
		imgIv = (ImageView) findViewById(R.id.personal_img_iv);
		nameTv = (TextView) findViewById(R.id.personal_name_tv);
	}

	@Override
	protected void initData() {
		titleView.setTitle("个人信息");
		options = TApplication.getInstance().getAllOptions(R.drawable.ic_defult_user_02);
	}
	
	@Override
	protected void onResume(boolean isBack) {
		super.onResume(isBack);
		
		String nickName = SpUtil.getSpUtil().getSPValue(SpConfig.NICK_NAME,"");
		String portraitUrl = SpUtil.getSpUtil().getSPValue(SpConfig.PORTRAIT_URL, "");
		nameTv.setText(nickName);
		ImageLoader.getInstance().displayImage(portraitUrl, imgIv, options);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		findViewById(R.id.personal_img_rl).setOnClickListener(this);
		findViewById(R.id.personal_name_rl).setOnClickListener(this);
		findViewById(R.id.personal_addr_rl).setOnClickListener(this);
		findViewById(R.id.personal_pwd_rl).setOnClickListener(this);
		findViewById(R.id.personal_logout_tv).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personal_img_rl:
			//TODO 照片
			break;
			
		case R.id.personal_name_rl:
			IntentUtil.gotoActivity(context, ChangeNameActivity.class);
			break;
			
		case R.id.personal_addr_rl:
			IntentUtil.gotoActivity(context, AddrHandActivity.class);
			break;
			
		case R.id.personal_pwd_rl:
			IntentUtil.gotoActivity(context, ChangePwdActivity.class);
			break;
			
		case R.id.personal_logout_tv:
			FunctionDialog.show(PersonalCenterActivity.this, true,
					"温馨提示", "确定要退出当前帐号", getString(android.R.string.cancel),
					"", getString(android.R.string.ok), new MenuCallback() {

						@Override
						public void onMenuResult(int menuType) {
							if (menuType == R.id.right_bt) {
								AppManager.getAppManager().AppExit(context, false);
							}
						}
			});	
			break;

		default:
			break;
		}
	}

	@Override
	protected void getData() {

	}

}
