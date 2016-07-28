package com.qws.nypp.activity.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.bean.CommonResult;
import com.qws.nypp.bean.GoodsBean;
import com.qws.nypp.bean.GoodsDetailBean;
import com.qws.nypp.bean.SukBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.qws.nypp.view.DetailSelectPopupWindow;
import com.qws.nypp.view.DetailsCarouselView;
import com.qws.nypp.view.LoadingView;
import com.qws.nypp.view.LoadingView.LoadingMode;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import de.greenrobot.event.EventBus;

/**
 * 商品详情
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class GoodsDetailActivity extends BaseActivity implements OnClickListener {

	public static final String TAG = "GoodsDetailActivity";
	private String productId;
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
	private TextView appraiseNumTv;		//评价数目
	
	private TextView noAppraiseTv; //没有评价
	private LinearLayout appraiseLl; //有评价
	private RatingBar ratingBar; //评价人姓名
	private TextView appraiseNameTv; //评价人姓名
	private TextView appraiseContentTv; //评价内容
	private TextView appraiseTimeTv; //评价时间
	private TextView appraiseSizeTv; //评价大小
	private TextView appraiseColorTv; //评价颜色
	private Button addCart;
	private DetailSelectPopupWindow selectPpw;
	private PopupWindow mPopupWindow = null;
	
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
		appraiseNumTv = (TextView) findViewById(R.id.goods_detail_appraise);
		noAppraiseTv = (TextView) findViewById(R.id.goods_detail_no_appraise);
		appraiseLl = (LinearLayout) findViewById(R.id.goods_detail_appraise_ll);
		ratingBar = (RatingBar) findViewById(R.id.goods_detail_ratingbar);
		appraiseNameTv = (TextView) findViewById(R.id.goods_detail_appraise_name);
		appraiseContentTv = (TextView) findViewById(R.id.goods_detail_appraise_content);
		appraiseTimeTv = (TextView) findViewById(R.id.goods_detail_appraise_time);
		appraiseSizeTv = (TextView) findViewById(R.id.goods_detail_appraise_size);
		appraiseColorTv = (TextView) findViewById(R.id.goods_detail_appraise_color);
		initPopuWindow();
	}

	@Override
	protected void initData() {
		titleView.setTitle("商品详情");
		productId = getIntent().getStringExtra("productId");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		addCart = (Button) findViewById(R.id.detail_add_cart);
		carouselView = (DetailsCarouselView) findViewById(R.id.goods_detail_carousel);
		findViewById(R.id.detail_collection).setOnClickListener(this);
		findViewById(R.id.detail_add_cart).setOnClickListener(this);
		findViewById(R.id.detail_buy).setOnClickListener(this);
		findViewById(R.id.goods_detail_all_appraise).setOnClickListener(this);
		findViewById(R.id.goods_detail_pro_detail).setOnClickListener(this);
		findViewById(R.id.goods_detail_pro_param).setOnClickListener(this);
		
//		titleView.setRightBtn("呵呵还", new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mPopupWindow.update();
//				mPopupWindow.showAsDropDown(titleView,  Gravity.LEFT, Gravity.NO_GRAVITY);
//			}
//		}, R.drawable.ic_nav_more);
//		titleView.setRightImgNewBtn(R.drawable.ic_nav_more, new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mPopupWindow.update();
//				mPopupWindow.showAsDropDown(titleView,  Gravity.LEFT, Gravity.NO_GRAVITY);
//			}
//		});
		
		// 重新加载按钮事件
		mLoadingView.setReloadBtListenner(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getData();
			}
		});
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
			selectPpw.initPopupWindow(context, goodsDetailBean, 1, productId);
			selectPpw.showAtLocation(addCart, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,0);
			break;
		case R.id.detail_buy:
			selectPpw = new DetailSelectPopupWindow();
			selectPpw.initPopupWindow(context, goodsDetailBean, 0, productId);
			selectPpw.showAtLocation(addCart, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,0);
			break;
		case R.id.goods_detail_all_appraise://查看所有评价
			Intent it1 = new Intent(context,GoodsAppraiseActivity.class);
			it1.putExtra("productId", productId);
			startActivity(it1);
			break;
		case R.id.goods_detail_pro_detail://商品详情
			Intent intent = new Intent(context,WebDetailActivity.class);
			intent.putExtra("productId", productId);
			startActivity(intent);
			break;
		case R.id.goods_detail_pro_param://产品参数
			Intent it = new Intent(context,GoodsParamsActivity.class);
			it.putExtra("goodsDetailBean", goodsDetailBean);
			startActivity(it);
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
			postJson.put("sign", TApplication.getInstance().getUserSign());
			postJson.put("memberId", TApplication.getInstance().getMemberId());
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("productId", productId);
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
		// 加载模式
		mLoadingView.setLoadingMode(LoadingMode.LOADING);
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.PRODUCT_DETAIL_PATH);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("productId", productId);
		LogUtil.t(new Gson().toJson(postData));
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();// 响应结果
				if("200".equals(result.optString("status"))) {
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
	                appraiseNumTv.setText("宝贝评价（"+data.appraiseCount+"）");
	                if(data.appraiseCount == 0){
	                	noAppraiseTv.setVisibility(View.VISIBLE);
	                	appraiseLl.setVisibility(View.GONE);
	                }else{
	                	noAppraiseTv.setVisibility(View.GONE);
	                	appraiseLl.setVisibility(View.VISIBLE);
	                	appraiseNameTv.setText(data.appraise.memberName);
	                	appraiseContentTv.setText(data.appraise.appraiseContent);
	                	appraiseTimeTv.setText(Util.getTime(data.appraise.appraiseDate));
	                	appraiseSizeTv.setText("尺码:"+data.appraise.appraiseSize);
	                	appraiseColorTv.setText("颜色:"+data.appraise.appraiseColor);
	                	ratingBar.setRating(data.appraise.appraiseLevel);
	                }
	                mLoadingView.setLoadingMode(LoadingMode.LOADING_SUCCESS);
				}else{
					mLoadingView.setLoadingMode(LoadingMode.LOADING_FAILED);
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
				mLoadingView.setLoadingMode(LoadingMode.LOADING_FAILED);
				
			}
		}, false, false);
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