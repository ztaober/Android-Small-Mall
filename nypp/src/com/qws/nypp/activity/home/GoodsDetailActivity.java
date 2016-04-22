package com.qws.nypp.activity.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.bean.CommonResult;
import com.qws.nypp.bean.GoodsDetailBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.LogUtil;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 商品详情
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class GoodsDetailActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.a_goods_detail;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("商品详情");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.PRODUCT_DETAIL_PATH);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", "1");
		postData.put("productId", "bb7a2a215ed64623a3d3b8191b23d09c");
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(0, request, new OnResponseListener<JSONObject>() {

			@Override
			public void onStart(int what) {
			}

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				 if (what == 0) {
	                // 请求成功
	                JSONObject result = response.get();// 响应结果
	                CommonResult<GoodsDetailBean> goodsDetial = CommonResult.fromJson(result.toString(), GoodsDetailBean.class);
	                LogUtil.t("PRODUCT_DETAIL_PATH="+result);
	             }
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
			}

			@Override
			public void onFinish(int what) {
			}
		});
	}
}