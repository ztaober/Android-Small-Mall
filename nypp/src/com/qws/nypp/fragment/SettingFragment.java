package com.qws.nypp.fragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.MainActivity;
import com.qws.nypp.activity.settting.AddrHandActivity;
import com.qws.nypp.activity.settting.MyContactActivity;
import com.qws.nypp.activity.settting.MyCollectionActivity;
import com.qws.nypp.activity.settting.MyOrderActivity;
import com.qws.nypp.activity.settting.OpinionActivity;
import com.qws.nypp.activity.settting.QRCodeActivity;
import com.qws.nypp.activity.settting.center.PersonalCenterActivity;
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
import com.qws.nypp.view.dialog.FunctionDialog;
import com.qws.nypp.view.dialog.MenuCallback;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 设置
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class SettingFragment extends BaseFragment implements OnClickListener {
	
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
		titleView.setTitle("我");
		titleView.setRightBtn("个人中心", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentUtil.gotoActivity(context, PersonalCenterActivity.class);
			}
		}, 0);
		titleView.setRightEnabled(false);
	}

	@Override
	protected void setListener() {
		findViewById(R.id.ic_set_my_order_ll).setOnClickListener(this);
		findViewById(R.id.f_settinf_collect_rl).setOnClickListener(this);
		findViewById(R.id.f_settinf_opinion_rl).setOnClickListener(this);
		findViewById(R.id.f_settinf_qrcode_rl).setOnClickListener(this);
		findViewById(R.id.f_settinf_contact_rl).setOnClickListener(this);
		findViewById(R.id.f_settinf_version_rl).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ic_set_my_order_ll: 	//我的订单
			IntentUtil.gotoActivity(context, MyOrderActivity.class);
			break;
		case R.id.f_settinf_collect_rl: //我的收藏
			IntentUtil.gotoActivity(context, MyCollectionActivity.class);
			break;
		case R.id.f_settinf_opinion_rl: //意见反馈
			IntentUtil.gotoActivity(context, OpinionActivity.class);
			break;
		case R.id.f_settinf_qrcode_rl:  //二维码
			IntentUtil.gotoActivity(context, QRCodeActivity.class);
			break;
		case R.id.f_settinf_contact_rl: //服务咨询
			IntentUtil.gotoActivity(context, MyContactActivity.class);
			break;
		case R.id.f_settinf_version_rl: //版本检测
//			IntentUtil.gotoActivity(context, MyContactActivity.class);
			checkVersion();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void getData() {
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getNum();
	}
	
	private void getNum() {
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
	
    /**
     * 版本检测
     * 
     * @updateTime 2016-8-17 上午11:31:11
     * @updateAuthor troy
     * @updateInfo 
     * @return
     */
    public void checkVersion(){
    	Request<JSONObject> request = new NyppJsonRequest(ServerConfig.GET_APK_PATH);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", Util.getDevId(context));
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 6666, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
                // 请求成功
                JSONObject result = response.get();// 响应结果
                if("200".equals(result.optString("status"))) {
                	JSONObject data = result.optJSONObject("data");
                	String newVersion = data.optString("version");
                	String oldVersion = Util.getSoftVersion(context);
                	final String url = data.optString("apkPath");
                	if(!newVersion.equalsIgnoreCase(oldVersion)){
                		FunctionDialog.show(context, true,
        						"温馨提示", "尊敬的用户,软件有新版本是否更新？", getString(android.R.string.cancel),
        						"", getString(android.R.string.ok), new MenuCallback() {

        							@Override
        							public void onMenuResult(int menuType) {
        								if (menuType == R.id.right_bt) {
        									Util util = new Util();
        									util.startNotiUpdateTask(context, url, url.substring(url.lastIndexOf("/") + 1, url.length()));
        								}
        							}
        				});
                	}else{
                		ToastUtil.show("您的版本已经是最新!");
                	}
                }
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
    }

}