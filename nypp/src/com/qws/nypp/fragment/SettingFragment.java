package com.qws.nypp.fragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.MainActivity;
import com.qws.nypp.activity.settting.AddrHandActivity;
import com.qws.nypp.activity.settting.MyCarActivity;
import com.qws.nypp.activity.settting.MyCollectionActivity;
import com.qws.nypp.activity.settting.MyOrderActivity;
import com.qws.nypp.activity.settting.OpinionActivity;
import com.qws.nypp.activity.settting.QRCodeActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.adapter.CommAdapter.AdapterListener;
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
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 设置
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class SettingFragment extends BaseFragment implements AdapterListener {
	
	private TextView unpaidTv;//待付款
	private TextView nodeliveryTv;//待发货
	private TextView noreceivingTv;//待收货
	private TextView appraiseTv;  //未评价
	private TextView refundingTv;//退款中
	
	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_setting, null);
	}

	@Override
	protected void findViews() {
		unpaidTv = (TextView)findViewById(R.id.ic_set_unpaid_tv);
		nodeliveryTv = (TextView)findViewById(R.id.ic_set_nodelivery_tv);
		noreceivingTv = (TextView)findViewById(R.id.ic_set_noreceiving_tv);
		appraiseTv = (TextView)findViewById(R.id.ic_set_appraise_tv);
		refundingTv = (TextView)findViewById(R.id.ic_set_refunding_tv);
	}

	@Override
	protected void initData() {
		titleView.setTitle("设置");
		titleView.setRightBtn("个人中心", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		}, 0);
		titleView.setRightEnabled(false);
	}

	@Override
	protected void setListener() {
		findViewById(R.id.ic_set_my_order_ll).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentUtil.gotoActivity(context, MyOrderActivity.class);
			}
		});
	}
	
	@Override
	protected void getData() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.GET_ORDERS_AMOUNT);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("memberId", TApplication.getInstance().getMemberId());
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
                // 请求成功
                JSONObject result = response.get();// 响应结果
                if("200".equals(result.optString("status"))) {
                	JSONObject json = result.optJSONObject("data");
                	
                	unpaidTv.setText(json.optString("unpaidAmount"));
                	nodeliveryTv.setText(json.optString("noDeliveryAmount"));
                	noreceivingTv.setText(json.optString("noReceivingAmount"));
                	appraiseTv.setText(json.optString("noAppraiseAmount"));
                	refundingTv.setText(json.optString("refundingAmount"));
                } 
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, false);
	}

	@Override
	public void onItemClick(int position, View v) {
		switch (position) {
		case 0:// 我的订单
			IntentUtil.gotoActivity(context, MyOrderActivity.class);
			break;
		case 1:// 我的收货地址
			IntentUtil.gotoActivity(context, AddrHandActivity.class);
			break;
		case 2:// 我的收藏
			IntentUtil.gotoActivity(context, MyCollectionActivity.class);
			break;
		case 3:// 购物车
			IntentUtil.gotoActivity(context, MyCarActivity.class);
			break;
		case 4:// 意见反馈
			IntentUtil.gotoActivity(context, OpinionActivity.class);
			break;
		case 5:// 二维码
			IntentUtil.gotoActivity(context, QRCodeActivity.class);
			break;
		case 6:// 版本更新

			break;
		default:
			break;
		}
	}
}