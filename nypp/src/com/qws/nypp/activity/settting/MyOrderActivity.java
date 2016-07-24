package com.qws.nypp.activity.settting;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.adapter.CommAdapter.AdapterListener;
import com.qws.nypp.bean.CommonResult4List;
import com.qws.nypp.bean.OrderInforBean;
import com.qws.nypp.bean.OrderProBean;
import com.qws.nypp.bean.OrderSukBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.view.AutoSizeListView;
import com.qws.nypp.view.TabIndicator;
import com.qws.nypp.view.TabIndicator.OnTabChangeListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 我的订单
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class MyOrderActivity extends BaseActivity {
	
	private DisplayImageOptions options;
	/** 切换进度条 */
	private TabIndicator viewTab;
	private String[] items = new String[] { "全部", "待付款", "待发货", "待收货", "待评价"};
	private int orderState;
	private int page;
	private int rows;
	/** 上啦刷新下啦加载listview */
	private PullToRefreshListView prListView;
	private CommAdapter<OrderInforBean> orderInfoAdapter;
	private List<OrderInforBean> orderInforList = new ArrayList<OrderInforBean>();
	@Override
	protected int getContentViewId() {
		return R.layout.a_my_order;
	}

	@Override
	protected void findViews() {
		viewTab = (TabIndicator) findViewById(R.id.view_tab);
		prListView = (PullToRefreshListView) findViewById(R.id.order_refresh_listview);
	}

	@Override
	protected void initData() {
		titleView.setTitle("我的订单");
		viewTab.initData(items);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		viewTab.setTabListener(new OnTabChangeListener() {
			
			@Override
			public void OnTabChange(int position) {
				orderState = position;
				page = 1;
				rows = 8;
				orderInforList.clear();
				getOrderList();
			}
		});
		options = TApplication.getInstance().getAllOptionsNoAmi(R.drawable.bg_defualt_118);
		orderInfoAdapter = new CommAdapter<OrderInforBean>(context, orderInforList,R.layout.item_order_info, new AdapterListener() {

			@Override
			public void onItemClick(int position, View v) {
				
			}
		}) {
			
			@Override
			public void onGetView(int position, View convertView, final OrderInforBean data) {
				setText(convertView, R.id.item_order_info_logis,"总金额(含运费"+data.logisticsFees+"元)" );
				setText(convertView, R.id.item_order_info_pices, " ¥ "+(data.orderAmount + data.logisticsFees) );
				if(data.orderStatus == 1){
					setText(convertView, R.id.item_order_info_status, "待付款");
				} else {
					setText(convertView, R.id.item_order_info_status, "订单状态:"+data.orderStatus);
				}
				
				
				AutoSizeListView listView = (AutoSizeListView) convertView.findViewById(R.id.order_suk_listview);
				CommAdapter<OrderSukBean> adapter = new CommAdapter<OrderSukBean>(context,data.newSukList,R.layout.item_order_suk, new AdapterListener() {

					@Override
					public void onItemClick(int position, View v) {
						
					}
				}) {

					@Override
					public void onGetView(int position, View convertView, OrderSukBean sukData) {
						ImageLoader.getInstance().displayImage(sukData.image, (ImageView)convertView.findViewById(R.id.item_order_suk_pic), options);
						setText(convertView, R.id.item_order_suk_title_tv, sukData.title);
						setText(convertView, R.id.item_order_suk_color_size, "颜色:"+sukData.colour+";尺寸:"+sukData.size);
						setText(convertView, R.id.item_order_suk_price,sukData.preferentialPrice+"元");
						setText(convertView, R.id.item_order_suk_num, "x"+sukData.quantity);
					}
				};
				listView.setAdapter(adapter);
			}
		};
		
		prListView.setAdapter(orderInfoAdapter);
		
		prListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				page = 1;
				orderInforList.clear();
				getOrderList();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				page++;
				getOrderList();
			}
		});
	}

	@Override
	protected void getData() {
		orderState = 0;
		page = 1;
		rows = 8;
		getOrderList();
	}
	/**
	 * 根据会员编号及状态获取订单列表
	 * 
	 * @updateTime 2016-7-22 下午10:53:55
	 * @updateAuthor troy
	 * @updateInfo
	 */
	private void getOrderList(){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.ACQUIRE_ALL_ORDER);
		JSONObject postJson = null;
		try {
			postJson = new JSONObject();
			postJson.put("sign", TApplication.getInstance().getUserSign());
			postJson.put("memberId", TApplication.getInstance().getMemberId());
			postJson.put("page", page);
			postJson.put("rows", rows);
			postJson.put("orderState", orderState);
		} catch (Exception e) {
			return;
		}
		request.setRequestBody(postJson.toString());
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
                // 请求成功
                JSONObject result = response.get();// 响应结果
                if("200".equals(result.optString("status"))) {
                	CommonResult4List<OrderInforBean> orderInforBean = CommonResult4List.fromJson(result.toString(), OrderInforBean.class);
                	List<OrderInforBean> infoList = orderInforBean.getData();
                	
                	for(OrderInforBean inforBean : infoList){
                		for(OrderProBean proBean : inforBean.products){
                			String prodrctId = proBean.prodrctId;
                			String title = proBean.title;
                			for(OrderSukBean sukBean : proBean.sukList){
                				sukBean.prodrctId = prodrctId;
                				sukBean.title = title;
                				inforBean.newSukList.add(sukBean);
                			}
                		}
                	}
                	orderInforList.addAll(infoList);
                	orderInfoAdapter.refreshList(orderInforList);
                	prListView.onRefreshComplete();
                } 
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				prListView.onRefreshComplete();
			}
		}, false, true);
	}
}