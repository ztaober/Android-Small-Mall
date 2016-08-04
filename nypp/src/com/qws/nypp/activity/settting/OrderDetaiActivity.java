package com.qws.nypp.activity.settting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.activity.LoginProbActivity;
import com.qws.nypp.activity.MainActivity;
import com.qws.nypp.activity.home.GoodsDetailActivity;
import com.qws.nypp.activity.home.PayModeActivity;
import com.qws.nypp.activity.home.WebDetailActivity;
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
import com.qws.nypp.view.LoadingView.LoadingMode;
import com.qws.nypp.view.dialog.FunctionDialog;
import com.qws.nypp.view.dialog.MenuCallback;
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
	private TextView callTv;
	
	private OrderDetailBean orderDetailBean;
	private double allPrice;
	private List<OrderDetailSukBean> detailsList = new ArrayList<OrderDetailSukBean>();
	private AutoSizeListView orderList;
	private CommAdapter<OrderDetailSukBean> adapter;
	
	private TextView cancleTv;
	private TextView payTv;
	private TextView handlingTv;
	
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
		callTv  = (TextView) findViewById(R.id.order_detail_call);
		orderList = (AutoSizeListView) findViewById(R.id.order_detail_listview);
		
		cancleTv  = (TextView) findViewById(R.id.order_detail_cancle);
		payTv  = (TextView) findViewById(R.id.order_detail_pay);
		handlingTv  = (TextView) findViewById(R.id.order_detail_handling);
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
				Bundle bundle = new Bundle();
				bundle.putString("productId", detailsList.get(position).commodity);
            	IntentUtil.gotoActivity(context, GoodsDetailActivity.class, bundle);
			}
		}) {

			@Override
			public void onGetView(int position, View convertView, final OrderDetailSukBean data) {
				ImageLoader.getInstance().displayImage(data.image, (ImageView)convertView.findViewById(R.id.item_order_suk_pic), options);
				setText(convertView, R.id.item_order_suk_title_tv, data.title);
				setText(convertView, R.id.item_order_suk_color_size, "颜色:"+data.colour+";尺寸:"+data.size);
				setText(convertView, R.id.item_order_suk_price,data.preferentialPrice+"元");
				setText(convertView, R.id.item_order_suk_num, "x"+data.quantity);
				TextView appraiseTv = (TextView) convertView.findViewById(R.id.item_order_appraise);
				if(data.appraise){
					appraiseTv.setVisibility(View.VISIBLE);
					appraiseTv.setText("已评价");
					appraiseTv.setBackgroundDrawable(null);
				}else if(orderDetailBean.orderStatus == 4){
					appraiseTv.setVisibility(View.VISIBLE);
					appraiseTv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,AppraiseActivity.class);
							intent.putExtra("orderId", orderId);
							intent.putExtra("sukBean", data);
							startActivity(intent);
						}
					});
				}
			
			}
		};
		
		orderList.setAdapter(adapter);
		
		
		callTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String call = SpUtil.getSpUtil().getSPValue(SpConfig.CONTACT_CALL, "");
				FunctionDialog.show(OrderDetaiActivity.this, true,
						"拨打卖家电话", call, getString(android.R.string.cancel),
						"", getString(android.R.string.ok), new MenuCallback() {

							@Override
							public void onMenuResult(int menuType) {
								if (menuType == R.id.right_bt) {
									IntentUtil.goCallPhone(context, call);
								}
							}
				});		
			}
		});
		
		// 重新加载按钮事件
		mLoadingView.setReloadBtListenner(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getData();
			}
		});
	}
	
	@Override
	protected boolean useEventBus() {
		return true;
	}
	
	/** 退款之后收到这个事件 */
    public void onEventMainThread(String msg) {  
        if (msg != null && "getOrderDetaildata".equals(msg)) {
        	getData();
        }
    }  

	@Override
	protected void getData() {
		mLoadingView.setLoadingMode(LoadingMode.LOADING);
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.UNIQUE_ORDER);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("orderId", orderId);
		request.setRequestBody(new Gson().toJson(postData));
		Log.i("taotao", "go~"+new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 2333, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				Log.i("taotao", "data=="+result.toString());
				if("200".equals(result.optString("status"))) {
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
					mLoadingView.setLoadingMode(LoadingMode.LOADING_SUCCESS);
				}else{
					ToastUtil.show(result.optString("declare", "未知错误"));
					mLoadingView.setLoadingMode(LoadingMode.LOADING_FAILED);
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				Log.i("taotao", "data==ff");
				mLoadingView.setLoadingMode(LoadingMode.LOADING_FAILED);
			}
		}, false, false);
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
		
		switch (orderDetailBean.orderStatus) {
		case 0:  //已取消订单
			cancleTv.setVisibility(View.INVISIBLE);
			payTv.setBackgroundResource(R.color.opt_gray);
			payTv.setText("删除订单");
			payTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					handleOrder(ServerConfig.REMOVE_ORDERS);
				}
			});
			break;
		case 1:	//待付款订单
			cancleTv.setText("取消订单");
			payTv.setText("付款");
			cancleTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					handleOrder(ServerConfig.CANCLE_RODERS);
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
			break;
		case 2:		//待发货订单
			cancleTv.setText("提醒卖家发货");
			payTv.setText("退款");
			cancleTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ToastUtil.show("已提醒卖家发货");
				}
			});
			payTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//退款页面
					Intent intent = new Intent(context,RefundActivity.class);
					intent.putExtra("orderId", orderId);
					startActivity(intent);
				}
			});
			break;
		case 3:	//待收货
			cancleTv.setText("确认收货");
			payTv.setText("退款");
			cancleTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					handleOrder(ServerConfig.RECEIVE_ORDERS);
				}
			});
			payTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//退款页面
					Intent intent = new Intent(context,RefundActivity.class);
					intent.putExtra("orderId", orderId);
					startActivity(intent);
				}
			});
			break;
		case 4: //交易成功(未评价)
			cancleTv.setVisibility(View.INVISIBLE);
			payTv.setBackgroundResource(R.color.opt_gray);
			payTv.setText("删除订单");
			payTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					handleOrder(ServerConfig.REMOVE_ORDERS);
				}
			});
			break;
		case 5: //交易成功(已评价)
			cancleTv.setVisibility(View.INVISIBLE);
			payTv.setBackgroundResource(R.color.opt_gray);
			payTv.setText("删除订单");
			payTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					handleOrder(ServerConfig.REMOVE_ORDERS);
				}
			});
			break;
		case 6:		//退款处理中
			cancleTv.setVisibility(View.INVISIBLE);
			payTv.setVisibility(View.INVISIBLE);
			handlingTv.setVisibility(View.VISIBLE);
			break;
		case 7: 	//退款成功
			cancleTv.setVisibility(View.INVISIBLE);
			payTv.setBackgroundResource(R.color.opt_gray);
			payTv.setText("删除订单");
			payTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					handleOrder(ServerConfig.REMOVE_ORDERS);
				}
			});
		case 8:		//退款被拒
			cancleTv.setVisibility(View.INVISIBLE);
			payTv.setBackgroundResource(R.color.opt_gray);
			payTv.setText("删除订单");
			payTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					handleOrder(ServerConfig.REMOVE_ORDERS);
				}
			});
			break;

		default:
			break;
		}
	}
	
	protected void handleOrder(final String url) {
		Request<JSONObject> request = new NyppJsonRequest(url);
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
					 if(ServerConfig.REMOVE_ORDERS.equals(url)){
						 OrderDetaiActivity.this.finish();
						 return;
					 }
					 getData();
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