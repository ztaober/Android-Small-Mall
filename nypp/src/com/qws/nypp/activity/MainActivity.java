package com.qws.nypp.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.fragment.BaseFragment;
import com.qws.nypp.fragment.HomeFragment;
import com.qws.nypp.fragment.OptionalFragment;
import com.qws.nypp.fragment.OrderFragment;
import com.qws.nypp.fragment.SettingFragment;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.AppManager;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.qws.nypp.view.MyRadioView;
import com.qws.nypp.view.dialog.FunctionDialog;
import com.qws.nypp.view.dialog.MenuCallback;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

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
		checkVersion();
	}
	
	@Override
	protected boolean useEventBus() {
		return true;
	}
	
	/** 切换页面 */
    public void onEventMainThread(String msg) {  
        if (msg != null) {
        	if("goHomeFrag".equals(msg)){
        		AppManager.getAppManager().goHomeActivity();//关掉所有页面到主页
        		radio_home.performClick();
        	}else if("goOrderFrag".equals(msg)){
        		AppManager.getAppManager().goHomeActivity();
        		radio_order.performClick();
        	}
        }
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
		beginTransaction.commitAllowingStateLoss();
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
//			if ((System.currentTimeMillis() - lastExitTime) > 2000) {
//				ToastUtil.showToast(this, "再按一次退出");
//				lastExitTime = System.currentTimeMillis();
//				return true;
//			}
			FunctionDialog.show(MainActivity.this, true,
					"是否退出登录", "", getString(android.R.string.cancel),
					"", getString(android.R.string.ok), new MenuCallback() {

						@Override
						public void onMenuResult(int menuType) {
							if (menuType == R.id.right_bt) {
								MainActivity.this.finish();
							}
						}
			});		
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	 
    /**
     * 版本检测
     * 
     * @updateTime 2016-8-17 上午11:31:11
     * @updateAuthor troy
     * @updateInfo 
     * @return
     */
    public void checkVersion(){
    	Request<JSONObject> request = new NyppJsonRequest(ServerConfig.GET_APK_PATH);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", Util.getDevId(context));
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 6666, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
                // 请求成功
                JSONObject result = response.get();// 响应结果
//              {"apkPath":"http:\/\/121.42.204.196:80\/\/upload\/20160817\/20160817112136304.apk","version":"1.1"}
                if("200".equals(result.optString("status"))) {
                	JSONObject data = result.optJSONObject("data");
                	String newVersion = data.optString("version");
                	String oldVersion = Util.getSoftVersion(context);
                	final String url = data.optString("apkPath");
                	Log.i("taotao", newVersion+"=="+oldVersion);
                	if(!newVersion.equalsIgnoreCase(oldVersion)){
                		FunctionDialog.show(MainActivity.this, true,
        						"温馨提示", "尊敬的用户,软件有新版本是否更新？", getString(android.R.string.cancel),
        						"", getString(android.R.string.ok), new MenuCallback() {

        							@Override
        							public void onMenuResult(int menuType) {
        								if (menuType == R.id.right_bt) {
        									Util util = new Util();
        									util.startNotiUpdateTask(context, url, url.substring(url.lastIndexOf("/") + 1, url.length()));
        								}
        							}
        				});
                	}
                }
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, false);
    }
	
}