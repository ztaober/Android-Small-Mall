package com.qws.nypp.activity.settting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

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
	
	private ListView collectLv;
	private CommAdapter<CollectionBean> adapter;
	private List<CollectionBean> list = new ArrayList<CollectionBean>();
	private DisplayImageOptions options;
	@Override
	protected int getContentViewId() {
		return R.layout.a_my_collection;
	}

	@Override
	protected void findViews() {
		collectLv = (ListView) findViewById(R.id.collection_listview);
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
	}

	@Override
	protected void getData() {
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
				 }
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}
}