package com.qws.nypp.activity.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.activity.MainActivity;
import com.qws.nypp.activity.settting.OrderDetaiActivity;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.AppManager;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.qws.nypp.view.dialog.FunctionDialog;
import com.qws.nypp.view.dialog.MenuCallback;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

/**
 * 选择支付方式
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class PayModeActivity extends BaseActivity {
	
	private String orderId;

	@Override
	protected int getContentViewId() {
		return R.layout.a_pay_mode;
	}

	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		titleView.setTitle("选择支付方式");
		orderId = getIntent().getStringExtra("orderId");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		
		findViewById(R.id.pay_mode_wx_rl).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createWXPay();
			}
		});
		findViewById(R.id.pay_mode_alipay_rl).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				createAlipay();
			}
		});
	}

	private void createWXPay() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.CREATE_WX_ORDER);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("orderId", orderId);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				// 请求成功
                JSONObject result = response.get();// 响应结果
                if("200".equals(result.optString("status"))) {
                	weChatPay(result.optJSONObject("data"));
                }else{
                	ToastUtil.show(result.optString("declare", "未知错误"));
                }
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
			
		},false,true);

	}
	
	@Override
	protected boolean useEventBus() {
		return true;
	}
	
	/** 添加进货单之后收到这个事件 */
    public void onEventMainThread(String msg) {  
        if (msg != null) {
        	if("pay_success".equals(msg)){
        		FunctionDialog.show(PayModeActivity.this, true,
    					"支付成功", "", "回首页",
    					"", "查看详情", new MenuCallback() {

    						@Override
    						public void onMenuResult(int menuType) {
    							if (menuType == R.id.right_bt) {
    								Intent intent = new Intent(context, OrderDetaiActivity.class);
    				    			intent.putExtra("orderId", orderId);//订单编号
    				    			startActivity(intent);
    				    			AppManager.getAppManager().finishActivity(SureOrderActivity.class);
    				    			AppManager.getAppManager().finishActivity(PayModeActivity.class);
    							}
    							if (menuType == R.id.left_bt) {
    								EventBus.getDefault().post("goHomeFrag");
    							}
    						}
    			});	
        	}else if("pay_fail".equals(msg)){
        		ToastUtil.show("支付失败，请重试");
        		Intent intent = new Intent();
    			intent.putExtra("orderId", orderId);
        		setResult(4444,intent);
        	}
        	
        }
    }  

	@Override
	protected void getData() {

	}
	
	private void weChatPay(JSONObject json){
		IWXAPI api = WXAPIFactory.createWXAPI(this, ServerConfig.APP_ID);
		api.registerApp(ServerConfig.APP_ID);
		PayReq req = null;
		try {
			req = new PayReq();
			req.appId			= json.getString("appId");
			req.partnerId		= json.getString("partnerid");
			req.prepayId		= json.getString("prepayid");
			req.nonceStr		= json.getString("nonceStr");
			req.timeStamp		= json.getString("timeStamp");
			req.packageValue	= json.getString("package");
			req.sign			= json.getString("paySign");
		} catch (JSONException e) {
			ToastUtil.show("数据异常");
//			e.printStackTrace();
		}
		api.sendReq(req);
	}
}