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
 * 退款
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class RefundActivity extends BaseActivity {
	
	private EditText refundEt;
	private String orderId;

	@Override
	protected int getContentViewId() {
		return R.layout.a_refund;
	}

	@Override
	protected void findViews() {
		refundEt = (EditText) findViewById(R.id.refund_et);
	}

	@Override
	protected void initData() {
		titleView.setTitle("退款");
		orderId = getIntent().getStringExtra("orderId");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		
		findViewById(R.id.refund_done).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String refundStr = refundEt.getText().toString().trim();
				if ("".equals(refundStr)) {
					ToastUtil.show("亲，请输入您的退款原因");
					return;
				}
				goRefound(refundStr);
			}
		});
	}
	
	private void goRefound(String str) {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.APPLY_REFUND);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("orderId", orderId);
		postData.put("reason", str);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request,new HttpListener<JSONObject>() {

					@Override
					public void onSucceed(int what,Response<JSONObject> response) {
						JSONObject result = response.get();// 响应结果
						if ("200".equals(result.optString("status"))) {
							EventBus.getDefault().post("getOrderDetaildata");
							RefundActivity.this.finish();
							ToastUtil.show("申请已提交");
						} else{
							ToastUtil.show(result.optString("declare", "未知错误"));
						}
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