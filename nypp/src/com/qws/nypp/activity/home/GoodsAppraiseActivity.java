package com.qws.nypp.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.bean.AppraiseBean;
import com.qws.nypp.bean.CommonResult4List;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 评价页面
 * 
 * @Description
 * @author troy
 * @date 2016-7-27 下午8:15:50
 * @Copyright:
 */
public class GoodsAppraiseActivity extends BaseActivity {
	
	private String productId;
	private ListView listView;
	private CommAdapter<AppraiseBean> adapter;
	private List<AppraiseBean> data = new ArrayList<AppraiseBean>();
	
	@Override
	protected int getContentViewId() {
		return R.layout.a_detail_appraise;
	}

	@Override
	protected void findViews() {
		listView = (ListView) findViewById(R.id.appraise_listview);
	}

	@Override
	protected void initData() {
		titleView.setTitle("评价");
		productId = getIntent().getStringExtra("productId");
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		
		adapter = new CommAdapter<AppraiseBean>(context, data, R.layout.item_appraise_detail) {
			
			@Override
			public void onGetView(int position, View convertView, AppraiseBean data) {
				setText(convertView, R.id.goods_detail_appraise_name, data.member_name);
				setText(convertView, R.id.goods_detail_appraise_content, data.content);
				setText(convertView, R.id.goods_detail_appraise_time, Util.getTime(data.create_date));
				setText(convertView, R.id.goods_detail_appraise_size, "尺码:"+data.size);
				setText(convertView, R.id.goods_detail_appraise_color, "颜色:"+data.color);
				
				RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.goods_detail_ratingbar);
				ratingBar.setRating(data.level);
			}
		};
		listView.setAdapter(adapter);
	}
	
	@Override
	protected void getData() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.ACQUIRE_ALL_APPRAISE_BY_PROID);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("productId", productId);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();// 响应结果
                if("200".equals(result.optString("status"))) {
                	CommonResult4List<AppraiseBean> result4List = CommonResult4List.fromJson(result.toString(), AppraiseBean.class);
                	data = result4List.getData();
                	if(data.size() != 0){
                		adapter.refreshList(data);
                	}
                }else{
                	ToastUtil.show(result.optString("declare", "未知错误"));
                }
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}

}
