package com.qws.nypp.activity.settting;

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
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.ToastUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

/**
 * 意见反馈
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class OpinionActivity extends BaseActivity {

	private EditText opinionEt;
	@Override
	protected int getContentViewId() {
		return R.layout.a_opinion;
	}

	@Override
	protected void findViews() {
		opinionEt = (EditText) findViewById(R.id.opinion_et);
	}

	@Override
	protected void initData() {
		titleView.setTitle("意见反馈");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		
		findViewById(R.id.opinion_done).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String opinionStr = opinionEt.getText().toString().trim();
				if ("".equals(opinionStr)) {
					ToastUtil.show("内容不能未空~");
					return;
				}
				goOpinion(opinionStr);
			}
		});
	}
	
	private void goOpinion(String str) {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.COMMENTS_SUBMIT);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("member",  TApplication.getInstance().getMemberId());
		postData.put("opinion", str);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request,new HttpListener<JSONObject>() {

					@Override
					public void onSucceed(int what,Response<JSONObject> response) {
						JSONObject result = response.get();// 响应结果
						if ("200".equals(result.optString("status"))) {
							OpinionActivity.this.finish();
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