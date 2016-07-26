package com.qws.nypp.activity.settting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.activity.MainActivity;
import com.qws.nypp.activity.home.PayModeActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.adapter.CommAdapter.AdapterListener;
import com.qws.nypp.bean.AddressBean;
import com.qws.nypp.bean.CommonResult;
import com.qws.nypp.bean.OrderDetailBean;
import com.qws.nypp.bean.OrderDetailSukBean;
import com.qws.nypp.bean.OrderProductsDetailBean;
import com.qws.nypp.bean.OrderSukBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.SpConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.SpUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.view.AutoSizeListView;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

/**
 * 订单详情
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class OrderDetaiActivity extends BaseActivity {
	
	private DisplayImageOptions options;
	private String orderId;
	
	private TextView nameTv;
	private TextView phoneTv;
	private TextView addressTv;
	private TextView orderNoTv;
	private TextView salePriceTv;
	private TextView allPriceTv;
	private TextView logistTv;
	private TextView moneyTv;
	private TextView userMsgTv;
	
	
	private OrderDetailBean orderDetailBean;
	private double allPrice;
	private List<OrderDetailSukBean> detailsList = new ArrayList<OrderDetailSukBean>();
	private AutoSizeListView orderList;
	private CommAdapter<OrderDetailSukBean> adapter;
	
	private TextView cancleTv;
	private TextView payTv;
	private TextView deleteTv;
	
	@Override
	protected int getContentViewId() {
		return R.layout.a_order_detail;
	}

	@Override
	protected void findViews() {
		nameTv  = (TextView) findViewById(R.id.order_detail_name);
		phoneTv  = (TextView) findViewById(R.id.order_detail_phone);
		addressTv  = (TextView) findViewById(R.id.order_detail_address);
		orderNoTv  = (TextView) findViewById(R.id.order_detail_order_num);
		salePriceTv  = (TextView) findViewById(R.id.order_detail_sale_price);
		allPriceTv  = (TextView) findViewById(R.id.order_detail_all_price);
		logistTv  = (TextView) findViewById(R.id.order_detail_logist);
		moneyTv  = (TextView) findViewById(R.id.order_detail_money);
		userMsgTv  = (TextView) findViewById(R.id.order_detail_user_msg);
		orderList = (AutoSizeListView) findViewById(R.id.order_detail_listview);
		
		cancleTv  = (TextView) findViewById(R.id.order_detail_cancle);
		payTv  = (TextView) findViewById(R.id.order_detail_pay);
		deleteTv  = (TextView) findViewById(R.id.order_detail_delete_order);
	}

	@Override
	protected void initData() {
		titleView.setTitle("订单详情");
		orderId = getIntent().getStringExtra("orderId");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		
		options = TApplication.getInstance().getAllOptionsNoAmi(R.drawable.bg_defualt_118);
		adapter = new CommAdapter<OrderDetailSukBean>(context,detailsList,R.layout.item_order_suk, new AdapterListener() {

			@Override
			public void onItemClick(int position, View v) {
				
			}
		}) {

			@Override
			public void onGetView(int position, View convertView, OrderDetailSukBean data) {
				ImageLoader.getInstance().displayImage(data.image, (ImageView)convertView.findViewById(R.id.item_order_suk_pic), options);
				setText(convertView, R.id.item_order_suk_title_tv, data.title);
				setText(convertView, R.id.item_order_suk_color_size, "颜色:"+data.colour+";尺寸:"+data.size);
				setText(convertView, R.id.item_order_suk_price,data.preferentialPrice+"元");
				setText(convertView, R.id.item_order_suk_num, "x"+data.quantity);
			}
		};
		
		orderList.setAdapter(adapter);
		
		
		cancleTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancleOrder();
			}
		});
		payTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,PayModeActivity.class);
				intent.putExtra("orderId", orderId);//订单编号
				startActivity(intent);
			}
		});
		deleteTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeOrder();
			}
		});
	}

	@Override
	protected void getData() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.UNIQUE_ORDER);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("orderId", orderId);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				CommonResult<OrderDetailBean> commonResult = CommonResult.fromJson(result.toString(), OrderDetailBean.class);
				orderDetailBean = commonResult.getData();
				
				for(OrderProductsDetailBean orderProBean : orderDetailBean.ordersProducts){
					String title = orderProBean.title;
					for(OrderDetailSukBean orderDetailSukBean : orderProBean.details){
						orderDetailSukBean.title = title;
						allPrice = allPrice + (orderDetailSukBean.preferentialPrice * orderDetailSukBean.quantity);
						orderDetailBean.newDetails.add(orderDetailSukBean);
					}
				}
				detailsList = orderDetailBean.newDetails;
				adapter.refreshList(detailsList);
				showData();
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}

	protected void showData() {
		nameTv.setText(orderDetailBean.contactName);		
		phoneTv.setText(orderDetailBean.contactPhone);		
		addressTv.setText(orderDetailBean.provinceName + orderDetailBean.cityName + orderDetailBean.districtName + orderDetailBean.address);		
		orderNoTv.setText("订单编号:"+orderDetailBean.orderNo);
		salePriceTv.setText("¥ "+orderDetailBean.discountAmount);
		allPriceTv.setText("¥ "+allPrice);
		logistTv.setText("¥ "+orderDetailBean.logisticsFees);
		moneyTv.setText("¥ "+ (orderDetailBean.orderAmount + orderDetailBean.logisticsFees));
		userMsgTv.setText("买家留言:"+orderDetailBean.message);
		
		if(orderDetailBean.orderStatus == 1){ //待付款
			deleteTv.setVisibility(View.INVISIBLE);
			cancleTv.setVisibility(View.VISIBLE);
			payTv.setVisibility(View.VISIBLE);
		} else if(orderDetailBean.orderStatus == 0){//已取消订单
			deleteTv.setVisibility(View.VISIBLE);
			cancleTv.setVisibility(View.INVISIBLE);
			payTv.setVisibility(View.INVISIBLE);
		}
	}
	
	protected void cancleOrder() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.CANCLE_RODERS);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("orderId", orderId);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				 if("200".equals(result.optString("status"))) {
					 getData();
					 EventBus.getDefault().post("getMyOrderList");
                } 
				 ToastUtil.show(result.optString("declare", "未知错误"));
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);		
	}
	
	protected void removeOrder() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.REMOVE_ORDERS);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("orderId", orderId);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {
			
			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				 if("200".equals(result.optString("status"))) {
					 EventBus.getDefault().post("getMyOrderList");
					 OrderDetaiActivity.this.finish();
                 } 
				 ToastUtil.show(result.optString("declare", "未知错误"));
			}
			
			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);		
	}
}