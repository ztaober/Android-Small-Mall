package com.qws.nypp.fragment;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

import com.qws.nypp.R;
import com.qws.nypp.activity.home.GoodsDetailActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.bean.GoodsBean;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.view.AdCarouselView;
import com.qws.nypp.view.pullview.PullToRefreshListView;
import com.qws.nypp.view.pullview.PullToRefreshBase.OnRefreshListener;

/**
 * 热区
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class HomeFragment extends BaseFragment {
	/** 控件 */
	private PullToRefreshListView listview;
	/** 适配器 */
	private CommAdapter<GoodsBean> adapter;
	/** 数据 */
	private ArrayList<GoodsBean> list = new ArrayList<GoodsBean>();
	/** 当前页 */
	private int page = 1;
	/** 广告View */
	private AdCarouselView adCarouselView;

	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_home, null);
	}

	@Override
	protected void findViews() {
		listview = findViewById(R.id.home_listview);
	}

	@Override
	protected void initData() {
		titleView.setTitle("热区");
		list.add(new GoodsBean("商品1"));
		list.add(new GoodsBean("商品2"));
		list.add(new GoodsBean("商品3"));
		list.add(new GoodsBean("商品4"));
		list.add(new GoodsBean("商品5"));
		list.add(new GoodsBean("商品6"));
		adapter = new CommAdapter<GoodsBean>(context, list, R.layout.item_home_goods) {
			@Override
			public void onGetView(int position, View convertView, GoodsBean data) {
				setText(convertView, R.id.homeGoods_txt_name, data.goodsName);
				setText(convertView, R.id.homeGoods_txt_name2, data.goodsName);
				setOnClickListener(convertView, R.id.homeGoods_txt_name, new OnClickListener() {
					@Override
					public void onClick(View v) {
						IntentUtil.gotoActivity(context, GoodsDetailActivity.class);
					}
				});
				setOnClickListener(convertView, R.id.homeGoods_txt_name2, new OnClickListener() {
					@Override
					public void onClick(View v) {
						IntentUtil.gotoActivity(context, GoodsDetailActivity.class);
					}
				});
			}
		};
		adCarouselView = new AdCarouselView(context);
		listview.getRefreshableView().addHeaderView(adCarouselView);
		listview.getRefreshableView().setAdapter(adapter);
	}

	@Override
	protected void setListener() {
		listview.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onPullToDownRefresh() {
				page = 1;
				getRefresh(true, false);
			}

			@Override
			public void onPullToUpRefresh() {
				page++;
				getRefresh(false, false);
			}
		});
	}

	@Override
	protected void getData() {

	}

	private void getRefresh(final boolean isRefresh, final boolean needDialog) {
		if (isRefresh) {
			page = 1;
		}
		listview.onRefreshComplete();
	}
}