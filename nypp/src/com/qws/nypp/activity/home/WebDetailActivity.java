package com.qws.nypp.activity.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

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

/**
 * 图文详情
 * 
 * @Description
 * @author troy
 * @date 2016-7-27 下午8:13:04
 * @Copyright:
 */
public class WebDetailActivity extends BaseActivity {

	private WebView mWebView;
	private String productId;

	@Override
	protected int getContentViewId() {
		return R.layout.a_web_detail;
	}

	@Override
	protected void findViews() {
		mWebView = (WebView) findViewById(R.id.webview);
		initWebView();
	}

	@Override
	protected void initData() {
		titleView.setTitle("图文详情");
		productId = getIntent().getStringExtra("productId");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	private void initWebView() {
		WebSettings webSettings = mWebView.getSettings();// 
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSettings.setUseWideViewPort(true);// 设定支持viewport
		webSettings.setLoadWithOverviewMode(true);
//		webSettings.setBuiltInZoomControls(true);
//		webSettings.setSupportZoom(true);// 设定支持缩放
	}

	@Override
	protected void getData() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.PRODUCT_DETAIL_PRE_PATH);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("productId", productId);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request,new HttpListener<JSONObject>() {

					@Override
					public void onSucceed(int what,Response<JSONObject> response) {
						// 请求成功
						JSONObject result = response.get();// 响应结果
						if ("200".equals(result.optString("status"))) {
							String data = result.optString("data");
							if (null != data) {
								mWebView.loadData(data,"text/html; charset=utf-8", null);
							}
						} else {
							ToastUtil.show(result.optString("declare", "未知错误"));
						}
					}

					@Override
					public void onFailed(int what, String url, Object tag,Exception exception, int responseCode,
							long networkMillis) {

					}
				}, false, true);
	}

}
