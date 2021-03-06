package com.qws.nypp.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.SpConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.SpUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.qws.nypp.view.dialog.FunctionDialog;
import com.qws.nypp.view.dialog.MenuCallback;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

/**
 * 登录界面
 * 
 * @Description
 * @author
 * @date 2015-12-30
 */
public class LoginActivity extends BaseActivity {
	/** 头像 */
	private ImageView headImg;
	private DisplayImageOptions options;
	/** 昵称 */
	private TextView login_txt_nike;
	/** 帐号 */
	private EditText login_edit_uesrname;
	/** 密码 */
	private EditText login_edit_password;
	private String mUserName;
	private String mPassword;

	@Override
	protected int getContentViewId() {
		return R.layout.a_login;
	}

	@Override
	protected void findViews() {
		headImg = (ImageView) findViewById(R.id.login_img_head);
		login_txt_nike = (TextView) findViewById(R.id.login_txt_nike);
		login_edit_uesrname = (EditText) findViewById(R.id.login_edit_uesrname);
		login_edit_password = (EditText) findViewById(R.id.login_edit_password);
	}

	@Override
	protected void initData() {
		login_edit_uesrname.setText(SpUtil.getSpUtil().getSPValue(SpConfig.USER_NAME, ""));
		login_edit_password.setText(SpUtil.getSpUtil().getSPValue(SpConfig.USER_PWD, ""));
		
		options = TApplication.getInstance().getAllOptions(R.drawable.ic_default_user);
		
	}
	
	@Override
	protected void onResume(boolean isBack) {
		super.onResume(isBack);
		login_txt_nike.setText(SpUtil.getSpUtil().getSPValue(SpConfig.NICK_NAME, ""));
		ImageLoader.getInstance().displayImage(SpUtil.getSpUtil().getSPValue(SpConfig.PORTRAIT_URL, ""), headImg, options);
	}

	@Override
	protected void setListener() {
		findViewById(R.id.login_txt_login).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Login();
			}
		});
		findViewById(R.id.login_txt_prob).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentUtil.gotoActivity(context, LoginProbActivity.class);
			}
		});
	}
	
	private void Login() {
		mUserName = login_edit_uesrname.getText().toString().trim();
		mPassword = login_edit_password.getText().toString().trim();
		if ("".equals(mUserName) || "".equals(mPassword)) {
			ToastUtil.showToast(context, "用户名或密码不能为空");
			return;
		}
		
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.LOGIN_PATH);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", Util.getDevId(context));
		postData.put("account", mUserName);
		postData.put("password", Util.md5three(mPassword));
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
                // 请求成功
                JSONObject result = response.get();// 响应结果
                if("200".equals(result.optString("status"))) {
                	JSONObject data = result.optJSONObject("data");
                	if( null != data){
                		SpUtil.getSpUtil().putSPValue(SpConfig.USER_NAME, mUserName);
                		SpUtil.getSpUtil().putSPValue(SpConfig.USER_PWD, mPassword);
                		SpUtil.getSpUtil().putSPValue(SpConfig.NICK_NAME, data.optString("nickname", ""));
                		SpUtil.getSpUtil().putSPValue(SpConfig.PORTRAIT_URL, data.optString("portrait", ""));
                		SpUtil.getSpUtil().putSPValue(SpConfig.USER_SIGN, data.optString("sign", ""));
                		SpUtil.getSpUtil().putSPValue(SpConfig.MEMBER_NAME, data.optString("account", ""));
                		
                		TApplication.getInstance().setUserSign(data.optString("sign", ""));
                		TApplication.getInstance().setMemberId(data.optString("memberId", ""));
        				IntentUtil.gotoActivity(context, MainActivity.class);
                	}
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

	@Override
	protected void getData() {
	}
	
	@Override
	protected boolean useEventBus() {
		return true;
	}
	
	/** 挤下线弹框 清除密码 */
    public void onEventMainThread(String msg) {  
        if (msg != null && "LogonInvalid".equals(msg)) {
        	FunctionDialog.show(LoginActivity.this, false,
					"您的帐号在其他设备登录,注意帐号安全,可电话咨询客服人员", "", "",
					getString(android.R.string.ok), "", new MenuCallback() {

						@Override
						public void onMenuResult(int menuType) {
						}
			});	
        	login_edit_password.setText("");
        	SpUtil.getSpUtil().putSPValue(SpConfig.USER_PWD, "");
        }
    }  

}