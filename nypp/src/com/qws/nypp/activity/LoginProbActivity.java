package com.qws.nypp.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.SpConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.SpUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.qws.nypp.view.dialog.FunctionDialog;
import com.qws.nypp.view.dialog.MenuCallback;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 登录帮助提示
 * 
 * @Description
 * @author
 * @date 2015-12-30
 */
public class LoginProbActivity extends BaseActivity {

	private TextView callTv;
	private TextView chatTv;
	String call = "";
	@Override
	protected int getContentViewId() {
		return R.layout.a_login_probl;
	}

	@Override
	protected void findViews() {
		callTv = (TextView) findViewById(R.id.login_help_call_tv);
		chatTv = (TextView) findViewById(R.id.login_help_chat_tv);
	}

	@Override
	protected void initData() {
		titleView.setTitle("帮助提示");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		
		callTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				FunctionDialog.show(LoginProbActivity.this, false,
//						"拨打客服电话", call, "", getString(android.R.string.ok),
//						"", new MenuCallback() {
//
//							@Override
//							public void onMenuResult(int menuType) {
//								if (menuType == R.id.right_bt) {
//									
//								}
//							}
//						});				
				FunctionDialog.show(LoginProbActivity.this, true,
						"拨打客服电话", call, getString(android.R.string.cancel),
						"", getString(android.R.string.ok), new MenuCallback() {

							@Override
							public void onMenuResult(int menuType) {
								if (menuType == R.id.right_bt) {
									IntentUtil.goCallPhone(context, call);
								}
							}
				});				
			}
		});
	}

	@Override
	protected void getData() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.CONTACT_US);
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
                // 请求成功
                JSONObject result = response.get();// 响应结果
                if("200".equals(result.optString("status"))) {
//                	JSONObject data = result.optJSONObject("data");
                	JSONArray data = result.optJSONArray("data");
                	String chat = null;
					try {
						chat = (String) data.get(0);
						call = (String) data.get(1);
					} catch (JSONException e) {
					}
                	
                	callTv.setText(call);
                	chatTv.setText(chat);
                } else {
                	ToastUtil.show(result.optString("declare", "未知错误"));
                }
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}
}