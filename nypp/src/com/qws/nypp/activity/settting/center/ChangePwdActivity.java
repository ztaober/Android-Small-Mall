package com.qws.nypp.activity.settting.center;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.SpConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.SpUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

public class ChangePwdActivity extends BaseActivity {

	private EditText oldpwdEt;
	private EditText newpwdEt;
	private EditText renewpwdEt;

	@Override
	protected int getContentViewId() {
		return R.layout.a_change_pwd;
	}

	@Override
	protected void findViews() {
		oldpwdEt = (EditText) findViewById(R.id.change_pwd_oldpwd);
		newpwdEt = (EditText) findViewById(R.id.change_pwd_newpwd);
		renewpwdEt = (EditText) findViewById(R.id.change_pwd_renewpwd);
	}

	@Override
	protected void initData() {
		titleView.setTitle("修改登录密码");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		
		findViewById(R.id.change_pwd_done).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String oldPwd = oldpwdEt.getText().toString().trim();
				String newPwd = newpwdEt.getText().toString().trim();
				String renewPwd = renewpwdEt.getText().toString().trim();
				if("".equals(oldPwd)){
					ToastUtil.show("请输入您的旧密码");
					return;
				}
				if("".equals(newPwd)){
					ToastUtil.show("请输入您的新密码");
					return;
				}
				if("".equals(renewPwd)){
					ToastUtil.show("请输入确认密码");
					return;
				}
				if(oldPwd.length() < 6 || oldPwd.length() > 16 || newPwd.length() < 6 || newPwd.length() > 16 || renewPwd.length() < 6 || renewPwd.length() > 16){
					ToastUtil.show("您输入的密码小余六位数");
				}
				oldPwd  = Util.md5three(oldPwd);
				newPwd  = Util.md5three(newPwd);
				renewPwd  = Util.md5three(renewPwd);
				if(!newPwd.equals(renewPwd)){
					ToastUtil.show("两次输入的密码不同,请检查");
					return;
				}
				changePwd(oldPwd,newPwd);
			}
		});
	}
	
	private void changePwd(String oldpwd ,String newpwd) {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.UPDATE_PWD_BY_ID);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("id", TApplication.getInstance().getMemberId());
		postData.put("oldPwd", oldpwd);
		postData.put("newPwd", newpwd);
		postData.put("repNewPwd", newpwd);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request,new HttpListener<JSONObject>() {

				@Override
				public void onSucceed(int what,Response<JSONObject> response) {
					JSONObject result = response.get();// 响应结果
					if ("200".equals(result.optString("status"))) {
						ChangePwdActivity.this.finish();
					} 
					ToastUtil.show(result.optString("declare", "未知错误"));
				}

				@Override
				public void onFailed(int what, String url, Object tag,Exception exception, int responseCode,
						long networkMillis) {

				}
		}, false, true);
	}

	@Override
	protected void getData() {

	}

}
