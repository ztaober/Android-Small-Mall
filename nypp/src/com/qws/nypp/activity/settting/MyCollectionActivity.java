package com.qws.nypp.activity.settting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.activity.home.GoodsDetailActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.adapter.CommAdapter.AdapterListener;
import com.qws.nypp.bean.CollectionBean;
import com.qws.nypp.bean.CommonResult;
import com.qws.nypp.bean.CommonResult4List;
import com.qws.nypp.bean.OrderDetailBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.view.LoadingView.LoadingMode;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 我的收藏
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class MyCollectionActivity extends BaseActivity {
	
	private SwipeMenuListView collectLv;
	private CommAdapter<CollectionBean> adapter;
	private List<CollectionBean> list = new ArrayList<CollectionBean>();
	private DisplayImageOptions options;
	private TextView noDataTv;
	@Override
	protected int getContentViewId() {
		return R.layout.a_my_collection;
	}

	@Override
	protected void findViews() {
		collectLv = (SwipeMenuListView) findViewById(R.id.collection_listview);
		noDataTv = (TextView) findViewById(R.id.collect_no_data);
	}

	@Override
	protected void initData() {
		titleView.setTitle("我的收藏");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		options = TApplication.getInstance().getAllOptionsNoAmi(R.drawable.bg_defualt_118);
		adapter = new CommAdapter<CollectionBean>(context, list, R.layout.item_collect_list, new AdapterListener() {

			@Override
			public void onItemClick(int position, View v) {
				Bundle bundle = new Bundle();
				bundle.putString("productId", list.get(position).productId);
            	IntentUtil.gotoActivity(context, GoodsDetailActivity.class, bundle);
			}
		}) {

			@Override
			public void onGetView(int position, View convertView, CollectionBean data) {
				ImageLoader.getInstance().displayImage(data.images, (ImageView)convertView.findViewById(R.id.cart_pic_img), options);
				setText(convertView, R.id.item_cart_title_tv, data.title);
				setText(convertView, R.id.item_collect_price, "¥ "+data.minPrice+"-"+data.maxPrice);
			}
		};
		collectLv.setAdapter(adapter);
		
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				openItem.setBackground(R.color.red);
				// set item width
				openItem.setWidth(dp2px(80));
				// set item title
				openItem.setTitle("删除");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(getResources().getColor(R.color.white));
				// add to menu
				menu.addMenuItem(openItem);

			}
		};
		collectLv.setMenuCreator(creator);
		
		// set SwipeListener
		collectLv.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
			}

			@Override
			public void onSwipeEnd(int position) {
			}
		});
		
		
		collectLv.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		    @Override
		    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		    	   switch (index) {
                   case 0:
                	   deleteCollect(position);
                       break;
               }
               return false;

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

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
	

	@Override
	protected void getData() {
		mLoadingView.setLoadingMode(LoadingMode.LOADING);
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.GET_MY_COLLECTION);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("memberId", TApplication.getInstance().getMemberId());
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				 if("200".equals(result.optString("status"))) {
					 CommonResult4List<CollectionBean> result4List = CommonResult4List.fromJson(result.toString(), CollectionBean.class);
						list = result4List.getData();
						adapter.refreshList(list);
						if(list.size() == 0){
							noDataTv.setVisibility(View.VISIBLE);	
						}else{
							noDataTv.setVisibility(View.GONE);	
						}
						mLoadingView.setLoadingMode(LoadingMode.LOADING_SUCCESS);
				 }else{
					 ToastUtil.show(result.optString("declare", "未知错误"));
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
	
	protected void deleteCollect(final int position) {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.CANCLE_COLLECTION);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("colId", list.get(position).colId);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				 if("200".equals(result.optString("status"))) {
					 list.remove(position);
					adapter.refreshList(list);
					if(list.size() == 0){
						noDataTv.setVisibility(View.VISIBLE);	
					}else{
						noDataTv.setVisibility(View.GONE);	
					}
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