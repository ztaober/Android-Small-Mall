package com.qws.nypp.activity.home;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
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
import com.qws.nypp.utils.alipay.PayResult;
import com.qws.nypp.utils.alipay.SignUtils;
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
	private double orderMoney;
	private int resultNum = 4444;

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
		orderMoney = getIntent().getDoubleExtra("orderMoney", 0);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		
		findViewById(R.id.pay_mode_wx_rl).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Util.isWeixinAvilible(context)){
					createWXPay();
				}else{
					ToastUtil.show("未找到微信，请安装微信后再使用");
				}
			}
		});
		findViewById(R.id.pay_mode_alipay_rl).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createAlipay();
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
    				    			resultNum = 0;
    				    			AppManager.getAppManager().finishActivity(SureOrderActivity.class);
    				    			AppManager.getAppManager().finishActivity(PayModeActivity.class);
    							}
    							if (menuType == R.id.left_bt) {
    								resultNum = 0;
    								EventBus.getDefault().post("goHomeFrag");
    							}
    						}
    			});	
        	}else if("pay_fail".equals(msg)){
//        		ToastUtil.show("支付失败，请重试");
        		resultNum = 4444;
        	}
        	
        }
    }  
    
   @Override
	public void finish() {
	    Intent intent = new Intent();
		intent.putExtra("orderId", orderId);
		setResult(resultNum,intent); //支付页面后生成订单详情页面
		super.finish();
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
	
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	
	// 商户PID
	public static final String PARTNER = "2088221384342722";
	// 商户收款账号
	public static final String SELLER = "hulang915@live.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOr4uTfz8sV6jdrWJLCRsW4JwGK/IeL+872ASgH89V7hwY9Pq/v/vAF+zcWqsaYDdGoaeI8kD7d2gOtPH83L1/joyWn+jlSZ29Sf5YWtzRn/p/B86gY7TjDz4LW/UB0F0iVvReA4XMrBgSB+GxdLF99XOCZ3uyvHqeoWvePEl5Y7AgMBAAECgYEAqiJwEkiLx1uGrSaTKFpFy7Q53gYg5jjOia8DZ08e55W1TixVDkFMgxSqr+55dQhZWZ5THunwi1m93SPMlnl7pXaQUE2cTSbYQvz10Ml3jlXcRyg6kCiaAW6d+f7pmh6vYoffGtOfrwc8Kj4qQvBXl2NYvUTqVGx8Fhsnvu/fi2kCQQD+6cmsLjEpY6pkoAimdJ/vDDHMHfUSCOVyZU0OsbWDSgJkPz8IMEWKNfrPnb91JP4DPu9+jdWLsXLsyE3QXh7tAkEA6/kr4W0vMwELBBVlH20bjmrfiXGI3EOmO3zRrzD5zgpNPsvl4FrbuOReOQwYO6jgKF3V374jHTC/Ee5/GSY8xwJBANLDJbWws4EPIJD2KHDIOHwDTyD/G+N/a4Y774xYrkUQ6g1Mpab1kmmF3AvQ173ZihPQ819lkcxgOS9BJeVEDLkCQB/nr7wqBBC0WmXx8ps+KFSoNJMjy6pkZyxx4vP5/jySfgE4llswTbcBAZBbB0fBvJUYots+nQbMWXk2Mu8WkW8CQHJxFWpw9nMdLNAj4RLsqofZS9nEXOFPMWxRExY+TIOtFSCPrn/SZi87U78NL6S7Y5drcJFm5joJUlyxP7wNhIY=";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "";
	private static final int SDK_PAY_FLAG = 1;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					EventBus.getDefault().post("pay_success");
//					Toast.makeText(PayModeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					EventBus.getDefault().post("pay_fail");
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
//						Toast.makeText(PayModeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//						Toast.makeText(PayModeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			default:
				break;
			}
		};
	};
	
	private void createAlipay(){
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();
			return;
		}
		String orderInfo = getOrderInfo("茜维斯内衣", "品牌内衣", ""+orderMoney);

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayModeActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
//		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://121.42.204.196:80/malls/app/recvAliPay" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}
	
	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	
}