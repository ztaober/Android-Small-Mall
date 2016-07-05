package com.qws.nypp.activity.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.bean.CommonResult;
import com.qws.nypp.bean.GoodsBean;
import com.qws.nypp.bean.GoodsDetailBean;
import com.qws.nypp.bean.SukBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.qws.nypp.view.DetailSelectPopupWindow;
import com.qws.nypp.view.DetailsCarouselView;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 商品详情
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class GoodsDetailActivity extends BaseActivity implements OnClickListener {

	public static final String TAG = "GoodsDetailActivity";
	private GoodsBean currentGoods;
	private GoodsDetailBean goodsDetailBean;
	
	private DetailsCarouselView carouselView;
	private TextView titleTv;		//商品名
	private TextView soldTv;		//成交数
	private TextView stockTv;		//库存
	private TextView miniTv;		//起批数
	private TextView prePriceTv;	//价格
	private TextView priceTv;		//折扣前价格
	private TextView logisticsTv;	//快递费
	private TextView harbourTv;		//地址
	private Button addCart;
	private DetailSelectPopupWindow selectPpw;
	@Override
	protected int getContentViewId() {
		return R.layout.a_goods_detail;
	}

	@Override
	protected void findViews() {
		titleTv = (TextView) findViewById(R.id.goods_detail_title);
		soldTv = (TextView) findViewById(R.id.goods_detail_sold_num);
		stockTv = (TextView) findViewById(R.id.goods_detail_stock_num);
		miniTv = (TextView) findViewById(R.id.goods_detail_mini_num);
		prePriceTv = (TextView) findViewById(R.id.goods_detail_pre_price);
		priceTv = (TextView) findViewById(R.id.goods_detail_price);
		logisticsTv = (TextView) findViewById(R.id.goods_detail_logistics);
		harbourTv = (TextView) findViewById(R.id.goods_detail_harbour);
	}

	@Override
	protected void initData() {
		titleView.setTitle("商品详情");
		currentGoods = (GoodsBean) getIntent().getSerializableExtra("bean");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		addCart = (Button) findViewById(R.id.detail_add_cart);
		carouselView = (DetailsCarouselView) findViewById(R.id.goods_detail_carousel);
		findViewById(R.id.detail_collection).setOnClickListener(this);
		findViewById(R.id.detail_add_cart).setOnClickListener(this);
		findViewById(R.id.detail_buy).setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.detail_collection:
			if (Util.isFastDoubleClick()) {
				return;
			}
			addCollection();
			break;
		case R.id.detail_add_cart:
			selectPpw = new DetailSelectPopupWindow();
			selectPpw.initPopupWindow(context, goodsDetailBean, 1, currentGoods.getProductId());
			selectPpw.showAtLocation(addCart, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,0);
			break;
		case R.id.detail_buy:
			selectPpw = new DetailSelectPopupWindow();
			selectPpw.initPopupWindow(context, goodsDetailBean, 0, currentGoods.getProductId());
			selectPpw.showAtLocation(addCart, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,0);
			break;

		default:
			break;
		}
	}

	private void addCollection() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.PRODUCT_ADD_COLLECTION);
		JSONObject postJson = null;
		try {
			postJson = new JSONObject();
			postJson.put("sign", "1");
			postJson.put("memberId", "59BA82FE3CD711E691F700163E022948");
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("productId", currentGoods.getProductId());
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

	@Override
	protected void getData() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.PRODUCT_DETAIL_PATH);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", "1");
		postData.put("productId", currentGoods.getProductId());
		LogUtil.t(new Gson().toJson(postData));
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();// 响应结果
                CommonResult<GoodsDetailBean> goodsDetial = CommonResult.fromJson(result.toString(), GoodsDetailBean.class);
                GoodsDetailBean data = goodsDetial.getData();
                goodsDetailBean = data;
                carouselView.initPicList(data.figure);
                
                titleTv.setText(data.title);
                soldTv.setText("成交"+data.soldQuantity+"笔");
                stockTv.setText("库存"+data.quantity+"件");
                miniTv.setText(data.minimum+"件起批");
                prePriceTv.setText("¥"+data.preferentialPrice);
                priceTv.setText("¥"+data.price);
                priceTv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
                logisticsTv.setText("快递"+data.logistics+"元");
                harbourTv.setText(data.harbour);
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}

}