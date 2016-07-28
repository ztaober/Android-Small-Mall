package com.qws.nypp.activity.settting;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.bean.OrderDetailSukBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.SpConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.SpUtil;
import com.qws.nypp.utils.ToastUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

/**
 * 评价页面
 * 
 * @Description
 * @author troy
 * @date 2016-7-28 上午11:46:37
 * @Copyright:
 */
public class AppraiseActivity extends BaseActivity {
	
	private ImageView appraiseIv;
	private RatingBar ratingBar;
	private EditText appraiseEt;
	private OrderDetailSukBean sukBean;
	private String orderId;
	private int startNum = 0;

	@Override
	protected int getContentViewId() {
		return R.layout.a_appraise;
	}

	@Override
	protected void findViews() {
		appraiseIv = (ImageView) findViewById(R.id.appraise_iv);
		appraiseEt = (EditText) findViewById(R.id.appraise_et);
		ratingBar = (RatingBar) findViewById(R.id.appraise_rb);
	}

	@Override
	protected void initData() {
		titleView.setTitle("评价");
		sukBean = (OrderDetailSukBean) getIntent().getSerializableExtra("sukBean");
		orderId =getIntent().getStringExtra("orderId");
		
		ImageLoader.getInstance().displayImage(sukBean.image, appraiseIv,TApplication.getInstance().getAllOptionsNoAmi(R.drawable.bg_defualt_118));
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				startNum =  (int)(rating+0.5);
			}
		});
		
		findViewById(R.id.appraise_done).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String appraiseStr = appraiseEt.getText().toString().trim();
				if ("".equals(appraiseStr)) {
					ToastUtil.show("请输入评价内容");
					return;
				}
				goAppraise(appraiseStr);
			}
		});
	}
	
	private void goAppraise(String str) {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.APPRAISE_ORDERS);
		JSONObject postJson = null;
		try {
			postJson = new JSONObject();
			postJson.put("sign", TApplication.getInstance().getUserSign());
			postJson.put("memberId", TApplication.getInstance().getMemberId());
			postJson.put("ordersId", orderId);
			postJson.put("memberName", SpUtil.getSpUtil().getSPValue(SpConfig.MEMBER_NAME, ""));
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("detailId", sukBean.detailId);
			object.put("content", str);
			object.put("level", startNum);
			object.put("productId", sukBean.commodity);
			object.put("quantity", sukBean.quantity);
			object.put("size", sukBean.size);
			object.put("color", sukBean.colour);
			array.put(object);
			postJson.put("list", array);
		} catch (Exception e) {
			return;
		}
		request.setRequestBody(postJson.toString());
		CallServer.getRequestInstance().add(context, 0, request,new HttpListener<JSONObject>() {

					@Override
					public void onSucceed(int what,Response<JSONObject> response) {
						JSONObject result = response.get();// 响应结果
						if ("200".equals(result.optString("status"))) {
							EventBus.getDefault().post("getOrderDetaildata");
							AppraiseActivity.this.finish();
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