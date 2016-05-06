package com.qws.nypp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.qws.nypp.R;
import com.qws.nypp.fragment.BaseFragment;
import com.qws.nypp.fragment.HomeFragment;
import com.qws.nypp.fragment.OptionalFragment;
import com.qws.nypp.fragment.OrderFragment;
import com.qws.nypp.fragment.SettingFragment;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.view.MyRadioView;

public class MainActivity extends BaseFragmentActivity implements OnClickListener {
	/** Fragment管理 */
	private FragmentManager manager;
	/** 菜单切换出的Fragment集合 */
	private BaseFragment[] fragments;
	/** 上次按退出的时间 */
	private long lastExitTime = 0;
	/** 热区 */
	private MyRadioView radio_home;
	/** 自选 */
	private MyRadioView radio_optional;
	/** 进货单 */
	private MyRadioView radio_order;
	/** 设置 */
	private MyRadioView radio_setting;
	/** 当前选择项 */
	private MyRadioView radio_now;
	/** 第几个视图 */
	private int currentPage = 0;

	@Override
	protected int getContentViewId() {
		return R.layout.a_main;
	}

	@Override
	protected void findViews() {
		radio_home = (MyRadioView) findViewById(R.id.main_radio_home);
		radio_optional = (MyRadioView) findViewById(R.id.main_radio_optional);
		radio_order = (MyRadioView) findViewById(R.id.main_radio_order);
		radio_setting = (MyRadioView) findViewById(R.id.main_radio_setting);
	}

	@Override
	protected void initData() {
		manager = getSupportFragmentManager();// 获取Fragment管理
		fragments = new BaseFragment[4];
		//避免应用重载导致界面重叠的问题
		for(int i = 0; i < fragments.length; i++){
			Fragment fragment = manager.findFragmentByTag("tag"+i);
			if(fragment != null && fragment instanceof BaseFragment){
				fragments[i] = (BaseFragment)fragment;
			}
		}
		chooseFragment(0);
		radio_now = radio_home;
		radio_now.setCheck(true);
	}

	@Override
	protected void setListener() {
		radio_home.setOnClickListener(this);
		radio_optional.setOnClickListener(this);
		radio_order.setOnClickListener(this);
		radio_setting.setOnClickListener(this);
	}

	@Override
	protected void getData() {

	}

	public void chooseFragment(int position) {
		FragmentTransaction beginTransaction = manager.beginTransaction();
		if (fragments[position] == null) {
			switch (position) {
			case 0:
				fragments[position] = new HomeFragment();
				break;
			case 1:
				fragments[position] = new OptionalFragment();
				break;
			case 2:
				fragments[position] = new OrderFragment();
				break;
			case 3:
				fragments[position] = new SettingFragment();
				break;
			default:
				break;
			}
			beginTransaction.setCustomAnimations(R.anim.alpha_enter, 0);
			beginTransaction.add(R.id.content_frame, fragments[position], "tag"+position);
		}
		beginTransaction.show(fragments[position]);
		for (int i = 0; i < fragments.length; i++) {
			if (fragments[i] != null && position != i) {
				beginTransaction.hide(fragments[i]);
			}
		}
		beginTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		int position = 0;
		switch (v.getId()) {
		case R.id.main_radio_home:
			position++;
		case R.id.main_radio_optional:
			position++;
		case R.id.main_radio_order:
			position++;
		case R.id.main_radio_setting:
			position++;
			MyRadioView myRadioView = (MyRadioView) v;
			if (!myRadioView.isCheck) {
				radio_now.setCheck(false);
				radio_now = myRadioView;
				radio_now.setCheck(true);
				chooseFragment(fragments.length - position);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - lastExitTime) > 2000) {
				ToastUtil.showToast(this, "再按一次退出");
				lastExitTime = System.currentTimeMillis();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}