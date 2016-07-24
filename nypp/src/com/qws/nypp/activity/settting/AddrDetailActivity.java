package com.qws.nypp.activity.settting;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.bean.AddressBean;
import com.qws.nypp.bean.CommonResult4List;
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
 * 收货地址详情
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class AddrDetailActivity extends BaseActivity {

	private AddressBean addrData;
	private View bottomRl;
	private TextView nameTv;
	private TextView phoneTv;
	private TextView zipCodeTv;
	private TextView cityTv;
	private TextView addrTv;
	@Override
	protected int getContentViewId() {
		return R.layout.a_addr_detail;
	}

	@Override
	protected void findViews() {
		bottomRl = findViewById(R.id.addr_detail_bottom_rl);
		nameTv = (TextView) findViewById(R.id.addr_detail_name);
		phoneTv = (TextView) findViewById(R.id.addr_detail_phone);
		zipCodeTv = (TextView) findViewById(R.id.addr_detail_zip);
		cityTv = (TextView) findViewById(R.id.addr_detail_city);
		addrTv = (TextView) findViewById(R.id.addr_detail_addr);
	}

	@Override
	protected void initData() {
		titleView.setTitle("收货地址");
		addrData = (AddressBean) getIntent().getSerializableExtra("addrData");
		bottomRl.setVisibility(addrData.defaultAddress ? View.GONE :View.VISIBLE);
		nameTv.setText(addrData.name);
		phoneTv.setText(addrData.mobile);
		zipCodeTv.setText(addrData.zipCode);
		cityTv.setText(addrData.province+" "+addrData.city+" "+addrData.district);
		addrTv.setText(addrData.address);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		titleView.setRightBtn("修改", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AddrEditActivity.class);
				intent.putExtra("addrData", addrData);
				startActivityForResult(intent, 2345);
			}
		});
		//设置为默认地址
		findViewById(R.id.addr_detail_bottom_tv).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateDefaultAddress();
			}
		});
		//删除此地址
		findViewById(R.id.addr_detail_delete).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteAddress();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2345 && resultCode == 5432) {
			AddrDetailActivity.this.finish();
		}
	}

	@Override
	protected void getData() {

	}
	/**
	 * 设置为默认地址
	 * 
	 * @updateTime 2016-7-19 下午5:59:44
	 * @updateAuthor troy
	 * @updateInfo
	 */
	private void updateDefaultAddress(){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.UPDATE_DEFAULT_ADDRESS);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("memberNo", TApplication.getInstance().getMemberId());
		postData.put("id", addrData.id);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				ToastUtil.show(result.optString("declare", "设置失败"));
				if("200".equals(result.optString("status"))) {
					AddrDetailActivity.this.finish();
					EventBus.getDefault().post("getAllAddress");
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}
	/**
	 * 删除收货地址
	 * 
	 * @updateTime 2016-7-19 下午6:43:44
	 * @updateAuthor troy
	 * @updateInfo
	 */
	private void deleteAddress(){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.DELETE_CONTACT_ADDRESS);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("memberNo", TApplication.getInstance().getMemberId());
		postData.put("id", addrData.id);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {
			
			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				ToastUtil.show(result.optString("declare", "删除失败"));
				if("200".equals(result.optString("status"))) {
					AddrDetailActivity.this.finish();
					EventBus.getDefault().post("getAllAddress");
				}
			}
			
			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}
	
}