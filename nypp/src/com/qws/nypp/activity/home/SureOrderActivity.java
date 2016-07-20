package com.qws.nypp.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.activity.settting.AddrHandActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.bean.AddressBean;
import com.qws.nypp.bean.CommonResult;
import com.qws.nypp.bean.CommonResult4List;
import com.qws.nypp.bean.GoodsCartBean;
import com.qws.nypp.bean.GoodsCartSukBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.view.AutoSizeListView;
import com.qws.nypp.view.StockChangeView;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 确认订单界面
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class SureOrderActivity extends BaseActivity implements OnClickListener {

	public static final int SELECT_ADDR = 666;
	private List<GoodsCartBean> cartList = new ArrayList<GoodsCartBean>();
	private AutoSizeListView orderLv;
	private CommAdapter<GoodsCartBean> ordersAdapter;
	private CommAdapter<GoodsCartSukBean> ordersSukAdapter;
	private DisplayImageOptions options;
	
	private TextView defaultTv;
	private TextView nameTv;
	private TextView phoneTv;
	private TextView addressTv;
	
	private AddressBean addrData;
	
	@Override
	protected int getContentViewId() {
		return R.layout.a_sure_detail;
	}

	@Override
	protected void findViews() {
		orderLv = (AutoSizeListView)findViewById(R.id.sure_order_listview);
		defaultTv  = (TextView) findViewById(R.id.sure_order_default_tv);
		nameTv  = (TextView) findViewById(R.id.sure_order_name);
		phoneTv  = (TextView) findViewById(R.id.sure_order_phone);
		addressTv  = (TextView) findViewById(R.id.sure_order_address);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sure_order_address_rl:
			Bundle bundle = new Bundle();
			bundle.putBoolean("isSelect", true);
			IntentUtil.gotoActivityForResult(context, AddrHandActivity.class, bundle, SELECT_ADDR);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SELECT_ADDR && resultCode == SELECT_ADDR && data!=null){
			AddressBean resultData = (AddressBean) data.getSerializableExtra("addrData");
			if(resultData!=null){
				addrData = resultData;
				setAddr();
			}
		}
	}

	@Override
	protected void initData() {
		titleView.setTitle("确认订单");
		
		Bundle bundle = getIntent().getExtras();
		cartList = (List<GoodsCartBean>) bundle.getSerializable("orderList");
		options = TApplication.getInstance().getAllOptionsNoAmi(R.drawable.bg_defualt_118);
		ordersAdapter = new CommAdapter<GoodsCartBean>(context, cartList, R.layout.item_sure_order_cart) {
			
			@Override
			public void onGetView(int position, View convertView, final GoodsCartBean cartData) {
				
				ImageLoader.getInstance().displayImage(cartData.image, (ImageView)convertView.findViewById(R.id.cart_pic_img), options);
				setText(convertView, R.id.item_cart_title_tv, cartData.title);
				setText(convertView, R.id.item_order_quantity, ""+cartData.allQua);
				setText(convertView, R.id.item_order_price, "¥"+cartData.allPri);
				
				
				AutoSizeListView sukListView = (AutoSizeListView)convertView.findViewById(R.id.item_order_lv);
				ordersSukAdapter = new CommAdapter<GoodsCartSukBean>(context,cartData.sukList,R.layout.item_sure_order_suk_cart) {
					
					@Override
					public void onGetView(int position, View convertView, final GoodsCartSukBean data) {
						setText(convertView, R.id.cart_suk_color, "颜色:"+data.colour);
						setText(convertView, R.id.cart_suk_size, "尺码:"+data.size);
						setText(convertView, R.id.cart_suk_price, "单价:"+String.format("%.2f" ,data.preferentialPrice)+"/件");
						setText(convertView, R.id.cart_suk_money, "¥"+String.format("%.2f" ,data.preferentialPrice*data.quantity));
						StockChangeView stockCv = (StockChangeView) convertView.findViewById(R.id.cart_suk_change);
						stockCv.isChange(false);
						stockCv.notifyNum(data.quantity,data.detailId);
					}
				};
				sukListView.setAdapter(ordersSukAdapter);
			}
		};
		orderLv.setAdapter(ordersAdapter);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		findViewById(R.id.sure_order_address_rl).setOnClickListener(this);
	}

	@Override
	protected void getData() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.QUERY_DEFAULT_ADDRESS);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", "test");
		postData.put("memberNo", "59BA82FE3CD711E691F700163E022948");
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				CommonResult<AddressBean> addressBean = CommonResult.fromJson(result.toString(), AddressBean.class);
				addrData = addressBean.getData();
				defaultTv.setVisibility(View.GONE);
				setAddr();
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}

	protected void setAddr() {
		nameTv.setText("收货人："+addrData.name);
		phoneTv.setText(addrData.mobile);
		addressTv.setText("收货地址:"+addrData.province+addrData.city+addrData.district+addrData.address);
	}

}