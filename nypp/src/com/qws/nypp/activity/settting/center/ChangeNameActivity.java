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
import com.qws.nypp.activity.settting.RefundActivity;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.SpConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.SpUtil;
import com.qws.nypp.utils.ToastUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

public class ChangeNameActivity extends BaseActivity {

	private EditText nameEt;
	@Override
	protected int getContentViewId() {
		return R.layout.a_change_name;
	}

	@Override
	protected void findViews() {
		nameEt = (EditText) findViewById(R.id.change_name_et);
	}

	@Override
	protected void initData() {
		titleView.setTitle("修改名字");
		String nickName = SpUtil.getSpUtil().getSPValue(SpConfig.NICK_NAME,"");
		nameEt.setText(nickName);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		titleView.setRightBtn("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = nameEt.getText().toString().trim();
				if("".equals(name)){
					ToastUtil.show("名字不能未空!");
					return;
				}
				changeName(name);
			}
		}, 0);
	}
	
	private void changeName(final String name) {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.UPDATE_MEMBER_BY_ID);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("id", TApplication.getInstance().getMemberId());
		postData.put("nickname", name);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request,new HttpListener<JSONObject>() {

				@Override
				public void onSucceed(int what,Response<JSONObject> response) {
					JSONObject result = response.get();// 响应结果
					if ("200".equals(result.optString("status"))) {
						SpUtil.getSpUtil().putSPValue(SpConfig.NICK_NAME, name);
						ChangeNameActivity.this.finish();
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
