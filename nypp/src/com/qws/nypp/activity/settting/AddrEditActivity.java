package com.qws.nypp.activity.settting;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.bean.AddressBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.ToastUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

/**
 * 添加&修改 收获地址
 * 
 * @Description
 * @author troy
 * @date 2016-7-19 下午2:03:12
 * @Copyright:
 */
public class AddrEditActivity extends BaseActivity {

	private AddressBean addrData;
	private EditText nameTv; //
	private EditText phoneTv; // 
	private EditText zipTv;	 //
	private EditText addrTv;	// 
	private TextView cityTv;	//
	private TextView submitTv;	// 
	private String provinceCode;
	private String cityCode;
	private String areaCode;
	private String name;
	private String phone;
	private String zipCode;
	private String addr;
	private boolean isChange;
	@Override
	protected int getContentViewId() {
		return R.layout.a_addr_edit;
	}

	@Override
	protected void findViews() {
		nameTv = (EditText) findViewById(R.id.addr_edit_name);
		phoneTv = (EditText) findViewById(R.id.addr_edit_phone);
		zipTv = (EditText) findViewById(R.id.addr_edit_zip);
		addrTv = (EditText) findViewById(R.id.addr_edit_address);
		cityTv = (TextView) findViewById(R.id.addr_edit_city);
		submitTv = (TextView) findViewById(R.id.addr_edit_submit);
	}

	@Override
	protected void initData() {
		addrData = (AddressBean) getIntent().getSerializableExtra("addrData");
		if(addrData!=null){
			isChange = true;
			nameTv.setText(addrData.name);
			phoneTv.setText(addrData.mobile);
			zipTv.setText(addrData.zipCode);
			cityTv.setText(addrData.province+" "+addrData.city+" "+addrData.district);
			cityTv.setTextColor(getResources().getColor(R.color.black));
			addrTv.setText(addrData.address);
			provinceCode = addrData.provinceId;
			cityCode = addrData.cityId;
			areaCode = addrData.districtId;
		}
		titleView.setTitle(isChange ? "修改地址" : "创建新地址");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 2333 && resultCode == 2333 && data!=null){
			String selData = data.getStringExtra("selData");
			try {
				JSONObject jsonObject = new JSONObject(selData);
				String province = jsonObject.optString("province");
				String city = jsonObject.optString("city");
				String area = jsonObject.optString("area");
				provinceCode = jsonObject.optString("provinceCode");
				cityCode = jsonObject.optString("cityCode");
				areaCode = jsonObject.optString("areaCode");
				
				cityTv.setText(province+" "+city+" "+area);
				cityTv.setTextColor(getResources().getColor(R.color.black));
			} catch (JSONException e) {
			}
		}
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		cityTv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_UP == event.getAction()){
					IntentUtil.gotoActivityForResult(context, CityChooserActivity.class,2333);
				}
				return false;
			}
		});
		submitTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				name = nameTv.getText().toString().trim();
				phone = phoneTv.getText().toString().trim();
				zipCode = zipTv.getText().toString().trim();
				String city = cityTv.getText().toString().trim();
				addr = addrTv.getText().toString().trim();
				
				if(name.length()<=0){
					ToastUtil.show("请填写收货人姓名");
					return;
				}
				if(phone.length()<=0){
					ToastUtil.show("请填写手机号码");
					return;
				}
				if(zipCode.length()<=0){
					ToastUtil.show("请填写邮政编码");
					return;
				}
				if("省，市，区".equals(city) && areaCode!=null && !"".equals(areaCode)){
					ToastUtil.show("请选择省市区");
					return;
				}
				if(addr.length()<=0){
					ToastUtil.show("请填写详细收货地址");
					return;
				}
				if(isChange){
					changeAddress();
				}else{
					addAddress();
				}
			}
		});
	}
	private void changeAddress() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.UPDATE_CONTACT_ADDRESS);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("memberNo", TApplication.getInstance().getMemberId());
		postData.put("id", addrData.id);
		postData.put("name", name);
		postData.put("mobile", phone);
		postData.put("zipCode", zipCode);
		postData.put("address", addr);
		postData.put("provinceId", provinceCode);
		postData.put("cityId", cityCode);
		postData.put("districtId", areaCode);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				ToastUtil.show(result.optString("declare", "修改失败"));
				if("200".equals(result.optString("status"))) {
					setResult(5432);
					AddrEditActivity.this.finish();
					EventBus.getDefault().post("getAllAddress");
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}

	protected void addAddress() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.ADD_CONTACT_ADDRESS);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("memberNo", TApplication.getInstance().getMemberId());
		postData.put("name", name);
		postData.put("mobile", phone);
		postData.put("zipCode", zipCode);
		postData.put("address", addr);
		postData.put("provinceId", provinceCode);
		postData.put("cityId", cityCode);
		postData.put("districtId", areaCode);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				ToastUtil.show(result.optString("declare", "添加失败"));
				if("200".equals(result.optString("status"))) {
					AddrEditActivity.this.finish();
					EventBus.getDefault().post("getAllAddress");
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}

	@Override
	protected void getData() {

	}
}