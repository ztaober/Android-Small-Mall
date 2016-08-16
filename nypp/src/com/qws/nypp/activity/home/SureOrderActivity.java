package com.qws.nypp.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.activity.settting.AddrHandActivity;
import com.qws.nypp.activity.settting.OrderDetaiActivity;
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
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.view.AutoSizeListView;
import com.qws.nypp.view.StockChangeView;
import com.qws.nypp.view.StockChangeView.OnNumChangeListenner;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

/**
 * 确认订单界面
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class SureOrderActivity extends BaseActivity implements OnClickListener {

	public static final int SELECT_ADDR = 666;
	private PopupWindow mPopupWindow = null;
	private List<GoodsCartBean> cartList = new ArrayList<GoodsCartBean>();
	private int quantity; //库存 详情页过来的可以修改 order页面过来的不可修改
	private int minimum; //起批数 详情页过来的可以修改 order页面过来的不可修改
	private AutoSizeListView orderLv;
	private CommAdapter<GoodsCartBean> ordersAdapter;
	private CommAdapter<GoodsCartSukBean> ordersSukAdapter;
	private DisplayImageOptions options;
	private EditText msgEt;
	private TextView defaultTv;
	private TextView nameTv;
	private TextView phoneTv;
	private TextView addressTv;
	
	private TextView logisTv;
	private TextView priceTv;
	private TextView allPriceTv;
	
	private AddressBean addrData;
	private double logistics = 0; 
	private double allMoney =0;
	private double saleMoneyMax = 3000;
	
	
	@Override
	protected int getContentViewId() {
		return R.layout.a_sure_detail;
	}

	@Override
	protected void findViews() {
		orderLv = (AutoSizeListView)findViewById(R.id.sure_order_listview);
		msgEt  = (EditText) findViewById(R.id.sure_order_edit_msg);
		defaultTv  = (TextView) findViewById(R.id.sure_order_default_tv);
		nameTv  = (TextView) findViewById(R.id.sure_order_name);
		phoneTv  = (TextView) findViewById(R.id.sure_order_phone);
		addressTv  = (TextView) findViewById(R.id.sure_order_address);
		
		logisTv  = (TextView) findViewById(R.id.sure_detail_logistics);
		priceTv  = (TextView) findViewById(R.id.sure_detail_price);
		allPriceTv  = (TextView) findViewById(R.id.sure_detail_allmoney);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sure_order_address_rl:
			Bundle bundle = new Bundle();
			bundle.putBoolean("isSelect", true);
			IntentUtil.gotoActivityForResult(context, AddrHandActivity.class, bundle, SELECT_ADDR);
			break;
		case R.id.sure_detail_done:
			submitOrders();
			break;

		default:
			break;
		}
	}
	
	private void submitOrders(){
		if(addrData == null){
			ToastUtil.show("请您去选择收货地址");
			return;
		}
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.SUBMIT_ORDERS);
		JSONObject postJson = null;
		try {
			postJson = new JSONObject();
			postJson.put("sign", TApplication.getInstance().getUserSign());
			postJson.put("memberId", TApplication.getInstance().getMemberId());
			postJson.put("contactName", addrData.name); //姓名
			postJson.put("contactPhone", addrData.mobile);
			postJson.put("province", addrData.provinceId);
			postJson.put("city", addrData.cityId);
			postJson.put("district", addrData.districtId);
			postJson.put("address", addrData.address);
			postJson.put("message", msgEt.getText().toString());
			postJson.put("orderAmount", allMoney);
			postJson.put("logisticsFees", logistics);
			postJson.put("discountAmount", 0); //优惠金额
			
			JSONArray array = new JSONArray();
			for(GoodsCartBean cartBean : cartList){
				JSONObject object = new JSONObject();
				object.put("cartId", "");
				object.put("productId", cartBean.pid);
				JSONArray sukArray = new JSONArray();
				for(GoodsCartSukBean sukBean : cartBean.sukList){
					JSONObject sukobject = new JSONObject();
					sukobject.put("sukId", sukBean.sukId);
					sukobject.put("number", sukBean.quantity);
					sukobject.put("cartDetailId", sukBean.detailId);
					sukArray.put(sukobject);
				}
				object.put("sukList", sukArray);
				array.put(object);
			}
			
			postJson.put("productIds", array);
		} catch (Exception e) {
			return;
		}
		request.setRequestBody(postJson.toString());
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();// 响应结果
				if("200".equals(result.optString("status"))) {
					Intent intent = new Intent(context,PayModeActivity.class);
					intent.putExtra("orderId", result.optString("data"));//订单编号
					intent.putExtra("orderMoney", allMoney);//订单价格
					startActivityForResult(intent,SELECT_ADDR);
		            EventBus.getDefault().post("getOrderData");
				}else{
	                String resultStr = result.optString("declare", "定单提交失败");
					ToastUtil.show(resultStr);
				}
               
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SELECT_ADDR && resultCode == 4444 && data!=null){
			Intent intent = new Intent(context, OrderDetaiActivity.class);
			intent.putExtra("orderId", data.getStringExtra("orderId"));//订单编号
			startActivity(intent);
			this.finish();
		}
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
		quantity = bundle.getInt("quantity", 0);
		minimum = bundle.getInt("minimum", 0);
		
		setMoney();
		options = TApplication.getInstance().getAllOptionsNoAmi(R.drawable.bg_defualt_118);
		ordersAdapter = new CommAdapter<GoodsCartBean>(context, cartList, R.layout.item_sure_order_cart) {
			
			@Override
			public void onGetView(int position, View convertView, final GoodsCartBean cartData) {
				
				ImageLoader.getInstance().displayImage(cartData.image, (ImageView)convertView.findViewById(R.id.cart_pic_img), options);
				setText(convertView, R.id.item_order_position, "产品"+(position+1));
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
						if(quantity == 0){
							stockCv.isChange(false);
							stockCv.notifyNum(data.quantity,data.detailId);
						}else{
							stockCv.isChange(true);
							stockCv.notifyNum(data.quantity,minimum,quantity, new OnNumChangeListenner() {
								
								@Override
								public void changeNum(int num) {
									data.quantity = num;
									setMoney();
								}
							});
						}
						
					}
				};
				sukListView.setAdapter(ordersSukAdapter);
			}
		};
		orderLv.setAdapter(ordersAdapter);
		
		//解决ScrollView下嵌套ListView进页面不在顶部的问题
		nameTv.setFocusable(true);  
		nameTv.setFocusableInTouchMode(true);  
		nameTv.requestFocus();  
	}
	
	/**
	 * 规则1.如果所有货品的价格加起来>=3000元，就免邮   2. 如果有多件产品，拿最多的邮费作为最终统一的一次邮费
	 * 
	 * @updateTime 2016-7-29 上午11:16:13
	 * @updateAuthor troy
	 * @updateInfo
	 */
	private void setMoney() {
		double allPiece = 0;
		double logistics = 0;
		for(GoodsCartBean cartBean : cartList){
			if(logistics < cartBean.logistics){
				logistics = cartBean.logistics; // 如果有多件产品，拿最多的邮费作为最终统一的一次邮费
			}
			for(GoodsCartSukBean sukBean : cartBean.sukList){
				allPiece+= sukBean.preferentialPrice * sukBean.quantity;
			}
		}
		if(allPiece >= saleMoneyMax){ 
			logistics = 0;//如果所有货品的价格加起来>=3000元，就免邮
		}
		allMoney = logistics + allPiece;
		logisTv.setText("运费:¥"+logistics);
		priceTv.setText("货款总计:¥"+allPiece);
		allPriceTv.setText("合计:¥"+ allMoney);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		findViewById(R.id.sure_order_address_rl).setOnClickListener(this);
		findViewById(R.id.sure_detail_done).setOnClickListener(this);
		
		titleView.setRightImgNewBtn(R.drawable.ic_nav_more, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopupWindow.update();
				mPopupWindow.showAsDropDown(titleView,  Gravity.LEFT, Gravity.NO_GRAVITY);
			}
		});
	}

	@Override
	protected void getData() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.QUERY_DEFAULT_ADDRESS);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("memberNo", TApplication.getInstance().getMemberId());
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				
				JSONObject result = response.get();
				if("200".equals(result.optString("status"))) {
					CommonResult<AddressBean> addressBean = CommonResult.fromJson(result.toString(), AddressBean.class);
					addrData = addressBean.getData();
					if(addrData != null){
						setAddr();
					}
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}

	protected void setAddr() {
		defaultTv.setVisibility(View.GONE);
		nameTv.setText("收货人："+addrData.name);
		phoneTv.setText(addrData.mobile);
		addressTv.setText("收货地址:"+addrData.province+addrData.city+addrData.district+addrData.address);
	}

	/**
	 * 初始化Popuwindow[返回退出的popw]
	 */
	private void initPopuWindow() {
		final View v = this.getLayoutInflater().inflate(R.layout.popup_window_cancle, null);
		v.findViewById(R.id.popw_cancle_home).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post("goHomeFrag");
			}
		});
		v.findViewById(R.id.popw_cancle_cart).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post("goOrderFrag");
			}
		});
		
		mPopupWindow = new PopupWindow(v, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}
	
}