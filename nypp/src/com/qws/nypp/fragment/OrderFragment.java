package com.qws.nypp.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.home.SureOrderActivity;
import com.qws.nypp.activity.settting.MyOrderActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.adapter.CommAdapter.AdapterListener;
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
import com.qws.nypp.view.LoadingView.LoadingMode;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

public class OrderFragment extends BaseFragment implements AdapterListener {
	
	private List<GoodsCartBean> cartList = new ArrayList<GoodsCartBean>();
	private View noOrder;
	private ListView orderLv;
	private CheckBox orderCb;
	private TextView orderMoneyTv;
	private TextView orderManyTv;
	private TextView collectTv;
	private TextView doneTv;
	private CommAdapter<GoodsCartBean> ordersAdapter;
	private CommAdapter<GoodsCartSukBean> ordersSukAdapter;
	private DisplayImageOptions options;
	private boolean placeOrder;
	private boolean collectOrder;
	private List<String> selectCart = new ArrayList<String>();
	private List<String> selectCartPid = new ArrayList<String>();

	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_order, null);
	}

	@Override
	protected void findViews() {
		noOrder = findViewById(R.id.no_order);
		orderLv = (ListView)findViewById(R.id.order_listview);
		orderCb = (CheckBox)findViewById(R.id.order_checkbox);
		orderMoneyTv = (TextView)findViewById(R.id.order_money);
		orderManyTv = (TextView)findViewById(R.id.order_many);
		collectTv = (TextView)findViewById(R.id.order_collect);
		doneTv = (TextView)findViewById(R.id.order_done);
	}

	@Override
	protected void initData() {
		titleView.setTitle("进货单");
		EventBus.getDefault().register(this); 
		options = TApplication.getInstance().getAllOptionsNoAmi(R.drawable.bg_defualt_118);
		ordersAdapter = new CommAdapter<GoodsCartBean>(context, cartList, R.layout.item_order_cart , this) {
			
			@Override
			public void onGetView(int position, View convertView, final GoodsCartBean cartData) {
				
				final ImageView select = (ImageView)convertView.findViewById(R.id.cart_select_img);
				select.setImageResource(cartData.select ? R.drawable.ic_select : R.drawable.ic_unselect);
				select.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(cartData.select){
							cartData.select = false;
							for(GoodsCartSukBean sukBean : cartData.sukList){
								sukBean.select = false;
							}
							notifyOrderData();
							ordersAdapter.notifyDataSetChanged();
						}else{
							cartData.select = true;
							for(GoodsCartSukBean sukBean : cartData.sukList){
								sukBean.select = true;
							}
							notifyOrderData();
							ordersAdapter.notifyDataSetChanged();
						}
						select.setImageResource(cartData.select ? R.drawable.ic_select : R.drawable.ic_unselect);
					}
				});
				ImageLoader.getInstance().displayImage(cartData.image, (ImageView)convertView.findViewById(R.id.cart_pic_img), options);
				setText(convertView, R.id.item_cart_title_tv, cartData.title);
				setText(convertView, R.id.item_order_quantity, ""+cartData.allQua);
				setText(convertView, R.id.item_order_price, "¥"+cartData.allPri);
				
				
				AutoSizeListView sukListView = (AutoSizeListView)convertView.findViewById(R.id.item_order_lv);
				ordersSukAdapter = new CommAdapter<GoodsCartSukBean>(context,cartData.sukList,R.layout.item_order_suk_cart) {
					
					@Override
					public void onGetView(int position, View convertView, final GoodsCartSukBean data) {
						final ImageView select = (ImageView)convertView.findViewById(R.id.cart_suk_select_img);
						select.setImageResource(data.select ? R.drawable.ic_select : R.drawable.ic_unselect);
						select.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								if(data.select){
									data.select = false;
									cartData.select = false;
									notifyOrderData();
									ordersAdapter.notifyDataSetChanged();
								}else{
									data.select = true;
									boolean flag = true;
									for(GoodsCartSukBean sukbean : cartData.sukList){
										flag = flag && sukbean.select;
										if(!flag)
											break;
									}
									if(flag){
										cartData.select = true;
									}
									notifyOrderData();
									ordersAdapter.notifyDataSetChanged();
								}
								select.setImageResource(data.select ? R.drawable.ic_select : R.drawable.ic_unselect);
							}
						});
						setText(convertView, R.id.cart_suk_color, "颜色:"+data.colour);
						setText(convertView, R.id.cart_suk_size, "尺码:"+data.size);
						setText(convertView, R.id.cart_suk_price, "单价:"+String.format("%.2f" ,data.price)+"/件");
						setText(convertView, R.id.cart_suk_money, "¥"+String.format("%.2f" ,data.price*data.quantity));
						StockChangeView stockCv = (StockChangeView) convertView.findViewById(R.id.cart_suk_change);
						stockCv.isChange(collectOrder);
						stockCv.notifyNum(data.quantity,data.detailId);
					}
				};
				sukListView.setAdapter(ordersSukAdapter);
			}
		};
		orderLv.setAdapter(ordersAdapter);
		
	}
	
	@Override
	public void onItemClick(int position, View v) {
		
	}
	
	private void notifyOrderData(){
		int type = 0;
		int piece = 0;
		double total = 0;
		if( selectCart!=null ){
			selectCart.clear();
		}
		if( selectCartPid!=null ){
			selectCartPid.clear();
		}
		for(GoodsCartBean cartBean : cartList){
			for(GoodsCartSukBean sukBean : cartBean.sukList){
				if(sukBean.select){
					selectCart.add(sukBean.detailId);
					selectCartPid.add(cartBean.pid);
					type++;
					piece += sukBean.quantity;
					total += sukBean.price * sukBean.quantity;
				}
			}
		}
		if(!collectOrder){
			orderMoneyTv.setText("总计：¥"+String.format("%.2f" ,total));
			orderManyTv.setText(type+"种"+piece+"件,不含运费");
			if(piece >= 10){
				doneTv.setBackgroundResource(R.color.order_done_red);
				placeOrder = true;
			} else {
				doneTv.setBackgroundResource(R.color.order_done_gray);
				placeOrder = false;
			}
		}
	}

	@Override
	protected void setListener() {
		// 重新加载按钮事件
		mLoadingView.setReloadBtListenner(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getOrderData();
			}
		});
		orderCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				for(GoodsCartBean cartBean : cartList){
					cartBean.select = isChecked;
					for(GoodsCartSukBean sukBean : cartBean.sukList){
						sukBean.select = isChecked;
					}
				}
				notifyOrderData();
				ordersAdapter.notifyDataSetChanged();
			}
		});
		
		doneTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(placeOrder && !collectOrder){
					ToastUtil.show("GO下单");
					Bundle bundle = new Bundle();
					bundle.putSerializable("orderList", (Serializable)cartList);
					IntentUtil.gotoActivity(context, SureOrderActivity.class, bundle);
				} else if(collectOrder){
					if(selectCart.size() <= 0){
						ToastUtil.show("请选择删除的产品");
					}else{
						removeCart();
					}
				}
			}
		});
		
		collectTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectCartPid.size() <= 0){
					ToastUtil.show("请选择收藏的产品");
				}else{
					addCollection();
				}
			}
		});
		
		titleView.setRightBtn("编辑", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!collectOrder){
					titleView.setRightBtnText("完成");
					collectTv.setVisibility(View.VISIBLE);
					doneTv.setText("删除");
					collectOrder = true;
				}else{
					titleView.setRightBtnText("编辑");
					collectTv.setVisibility(View.GONE);
					doneTv.setText("结算");
					collectOrder = false;
				}
				if(orderCb.isChecked()){
					orderCb.setChecked(false);
				}else{
					for(GoodsCartBean cartBean : cartList){
						cartBean.select = false;
						for(GoodsCartSukBean sukBean : cartBean.sukList){
							sukBean.select = false;
						}
					}
					notifyOrderData();
					ordersAdapter.notifyDataSetChanged();
				}
				if(!collectOrder){
					getOrderData();
				}
			}
		}, 0);
	}

	@Override
	protected void getData() {
		getOrderData();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		 EventBus.getDefault().unregister(this); 
	}
	
	/** 添加进货单之后收到这个事件 */
    public void onEventMainThread(String msg) {  
        if (msg != null && "getOrderData".equals(msg)) {
        	getOrderData();
        }
    }  
	
	private void getOrderData(){
		mLoadingView.setLoadingMode(LoadingMode.LOADING);
		NyppJsonRequest request = new NyppJsonRequest(ServerConfig.PRODUCT_GET_CART);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", "1");
		postData.put("memberId", "59BA82FE3CD711E691F700163E022948");
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				CommonResult4List<GoodsCartBean> result4List = CommonResult4List.fromJson(result.toString(), GoodsCartBean.class);
				List<GoodsCartBean> allCartList = result4List.getData();
				
				if(cartList != null){
					cartList.clear();
				}
				
				for(GoodsCartBean cartBean : allCartList){
					List<GoodsCartSukBean> sukList = cartBean.sukList;
					if (sukList.size() > 0) {
						int allQua = 0;
						double allPri = 0;
						for(GoodsCartSukBean sukBean : sukList){
							sukBean.select = false;
							allQua = sukBean.quantity +allQua;
							double pow = sukBean.quantity * sukBean.price;
							allPri = pow + allPri;
						}
						cartBean.allQua = allQua;
						cartBean.allPri = String.format("%.2f" ,allPri);
						cartBean.select = false;
						cartList.add(cartBean);
					}
				}
				notifyOrderData();
				ordersAdapter.notifyDataSetChanged();
				if(cartList.size() == 0){
					noOrder.setVisibility(View.GONE);
				}else{
					noOrder.setVisibility(View.INVISIBLE);
				}
				mLoadingView.setLoadingMode(LoadingMode.LOADING_SUCCESS);
			}

			@Override
			public void onFailed(int what, String url, Object tag, Exception exception, int responseCode,
					long networkMillis) {
				mLoadingView.setLoadingMode(LoadingMode.LOADING_FAILED);
			}
		}, false, false);
	}
	
	private void addCollection(){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.PRODUCT_ADD_COLLECTION);
		JSONObject postJson = null;
		try {
			postJson = new JSONObject();
			postJson.put("sign", "1");
			postJson.put("memberId", "59BA82FE3CD711E691F700163E022948");
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			for(String pId : selectCartPid){
				object.put("productId", pId);
			}
			array.put(object);
			postJson.put("productIds", array);
		} catch (Exception e) {
			return;
		}
		request.setRequestBody(postJson.toString());
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();// 响应结果
                String resultStr = result.optString("declare", "收藏失败");
                ToastUtil.show(resultStr);
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}
	
	private void removeCart(){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.PRODUCT_REMOVE_CART);
		JSONObject postJson = null;
		try {
			postJson = new JSONObject();
			postJson.put("sign", "1");
			postJson.put("memberId", "59BA82FE3CD711E691F700163E022948");
			JSONArray array = new JSONArray();
			for(String cart :selectCart){
				array.put(cart);//购物车产品子项编号
			}
			postJson.put("detailId", array);
		} catch (Exception e) {
			return;
		}
		request.setRequestBody(postJson.toString());
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();// 响应结果
                String resultStr = result.optString("declare", "删除失败");
                ToastUtil.show(resultStr);
                getOrderData();
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}

}
