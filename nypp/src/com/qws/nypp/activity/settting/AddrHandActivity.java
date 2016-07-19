package com.qws.nypp.activity.settting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.activity.home.SureOrderActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.adapter.CommAdapter.AdapterListener;
import com.qws.nypp.bean.AddressBean;
import com.qws.nypp.bean.CommonResult;
import com.qws.nypp.bean.CommonResult4List;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

/**
 * 选择&管理 收货地址
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class AddrHandActivity extends BaseActivity implements AdapterListener {

	private boolean isSelect;
	private TextView bottomTv;
	private ListView addrList;
	private CommAdapter<AddressBean> adapter;
	private List<AddressBean> data = new ArrayList<AddressBean>();

	@Override
	protected int getContentViewId() {
		return R.layout.a_addr_hand;
	}

	@Override
	protected void findViews() {
		bottomTv = (TextView) findViewById(R.id.addr_hand_bottom_tv);
		addrList = (ListView) findViewById(R.id.addr_hand_listview);
	}

	@Override
	protected void initData() {
		EventBus.getDefault().register(this);
		// isSelect  true为选择 false为管理
		Bundle bundle = getIntent().getExtras();
		if(bundle == null){
			isSelect = false;
		}else{
			isSelect = bundle.getBoolean("isSelect", true);
		}
		titleView.setTitle(isSelect ? "选择收货地址" : "管理收货地址");
		bottomTv.setText(isSelect ? "管理收货地址" : "添加新地址");
		
		adapter = new CommAdapter<AddressBean>(context, data, R.layout.item_addr_hand_list, this) {

			@Override
			public void onGetView(int position, View convertView, AddressBean data) {
				setText(convertView, R.id.item_addr_name, data.name);
				setText(convertView, R.id.item_addr_phone, data.mobile);
				setText(convertView, R.id.item_addr_address, data.province+data.city+data.district+data.address);
				convertView.findViewById(R.id.item_addr_default).setVisibility(data.defaultAddress ? View.VISIBLE : View.GONE);
			}
		};
		addrList.setAdapter(adapter);
	}
	
	@Override
	public void onItemClick(int position, View v) {
		if(isSelect){
			Intent intent = new Intent();
			intent.putExtra("addrData", data.get(position));
			setResult(SureOrderActivity.SELECT_ADDR, intent);
			finish();
		}else{
			Intent intent = new Intent(context,AddrDetailActivity.class);
			intent.putExtra("addrData", data.get(position));
			startActivity(intent);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		bottomTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isSelect){
					//跳转管理收获地址页面
					IntentUtil.gotoActivity(context, AddrHandActivity.class);
				}else{
					//添加新地址页面
					IntentUtil.gotoActivity(context, AddrEditActivity.class);
				}
			}
		});
	}

	@Override
	protected void getData() {
		getAllAddress();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		 EventBus.getDefault().unregister(this); 
	}
	
	/** 添加进货单之后收到这个事件 */
    public void onEventMainThread(String msg) {  
        if (msg != null && "getAllAddress".equals(msg)) {
        	getAllAddress();
        }
    }  
    
	private void getAllAddress(){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.QUERY_ALL_ADDRESS);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", "test");
		postData.put("memberNo", "59BA82FE3CD711E691F700163E022948");
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				CommonResult4List<AddressBean> addressBean = CommonResult4List.fromJson(result.toString(), AddressBean.class);
				LogUtil.t(result.toString());
				data = addressBean.getData();
				adapter.refreshList(data);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}

}