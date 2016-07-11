package com.qws.nypp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.extras.recyclerview.PullToRefreshRecyclerView;
import com.handmark.pulltorefresh.library.wraprecyclerview.WrapRecyclerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.home.GoodsDetailActivity;
import com.qws.nypp.bean.CommonResult4List;
import com.qws.nypp.bean.GoodsBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.DisplayUtil;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.view.AdCarouselView;
import com.qws.nypp.view.LoadingView.LoadingMode;
import com.qws.nypp.view.pullview.SpacesItemDecoration;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 热区
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class HomeFragment extends BaseFragment {
	/** 控件 */
	private PullToRefreshRecyclerView mPullRefreshRecyclerView;
	private RecyclerView mRecyclerView;
	/** 适配器 */
	private RecyclerViewAdapter mAdapter;
	/** 数据 */
	private ArrayList<GoodsBean> list = new ArrayList<GoodsBean>();
	/** 当前页 */
	private int page = 1;
	/** 广告View */
	private AdCarouselView adCarouselView;
	
	static final int MENU_SET_MODE = 0;
	
	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_home, null);
	}

	@Override
	protected void findViews() {
		
		mPullRefreshRecyclerView = (PullToRefreshRecyclerView) this.findViewById(R.id.pull_refresh_recycler);
		mPullRefreshRecyclerView.setHasPullUpFriction(true); // 设置没有上拉阻力
		// 上下拉的mRecyclerView
		mRecyclerView = mPullRefreshRecyclerView.getRefreshableView();
		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		//设置item之间的间隔
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(DisplayUtil.dip2px(context, 5)));
	}
	
	@Override
	protected void initData() {
		titleView.setTitle("热区");
		
		mAdapter = new RecyclerViewAdapter();
		mRecyclerView.setAdapter(mAdapter);
		adCarouselView = new AdCarouselView(context);
		mPullRefreshRecyclerView.getRefreshableView().addHeaderView(adCarouselView);
		
		// 重新加载按钮事件
		mLoadingView.setReloadBtListenner(new OnClickListener() {
			@Override
			public void onClick(View v) {
				page = 1;
				list.clear();
				getData(page);
				// 加载模式
				mLoadingView.setLoadingMode(LoadingMode.LOADING);
			}
		});
	}

	@Override
	protected void setListener() {
		mPullRefreshRecyclerView.setOnRefreshListener(new OnRefreshListener2<WrapRecyclerView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<WrapRecyclerView> refreshView) {
				page = 1;
				list.clear();
				getData(page);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<WrapRecyclerView> refreshView) {
				page++;
				getData(page);
			}
		});
		
	}

	@Override
	protected void getData() {
		list.clear();
		getData(1);
		// 加载模式
		mLoadingView.setLoadingMode(LoadingMode.LOADING);
	}
	
	private void getData(int i){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.HOT_PRODUCT_PATH);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("page", i+"");
		postData.put("rows", "4");
		postData.put("sign", "test");
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();// 响应结果
                CommonResult4List<GoodsBean> goodsList = CommonResult4List.fromJson(result.toString(), GoodsBean.class);
                list.addAll(goodsList.getData());
                mAdapter.notifyDataSetChanged();
                mPullRefreshRecyclerView.onRefreshComplete();
                
                mLoadingView.setLoadingMode(LoadingMode.LOADING_SUCCESS);
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				mPullRefreshRecyclerView.onRefreshComplete();
				
				mLoadingView.setLoadingMode(LoadingMode.LOADING_FAILED);
			}
		}, false, false);
	}
	
	class RecyclerViewAdapter extends Adapter<ViewHolder> {
		
		/** imageLoader默认配置 */
		DisplayImageOptions options = TApplication.getInstance().getAllOptions(R.drawable.bg_defualt_list);

		@Override
		public int getItemCount() {
			return list.size();
		}

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
            		context).inflate(R.layout.item_home_recycle_goods, parent,
                    false));
            return holder;
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			GoodsBean goodsBean = list.get(position);
			ImageLoader.getInstance().displayImage(goodsBean.getImage(), ((MyViewHolder)holder).iv, options);
			((MyViewHolder)holder).tv.setText(goodsBean.getTitle());
			((MyViewHolder)holder).soldTv.setText("成交"+goodsBean.getSoldQuantity()+"笔");
			((MyViewHolder)holder).stockTv.setText("混批");
			((MyViewHolder)holder).newPriceTv.setText("¥"+goodsBean.getPreferentialPrice());
			((MyViewHolder)holder).oldPriceTv.setText("¥"+goodsBean.getPrice());
			((MyViewHolder)holder).oldPriceTv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
		}
		
		

        class MyViewHolder extends ViewHolder {
        	ImageView iv;
            TextView tv;
            TextView soldTv;
            TextView stockTv;
            TextView newPriceTv;
            TextView oldPriceTv;
            public MyViewHolder(View view) {
                super(view);
                itemView.setOnClickListener(new OnClickListener() {  
                    @Override  
                    public void onClick(View v) {  
                    	Bundle bundle = new Bundle();
						bundle.putSerializable("bean", list.get(getPosition()-1));
                    	IntentUtil.gotoActivity(context, GoodsDetailActivity.class, bundle);
                    }  
                });  
                iv = (ImageView) view.findViewById(R.id.item_home_goods_img);
                tv = (TextView) view.findViewById(R.id.item_home_goods_tv);
                soldTv = (TextView) view.findViewById(R.id.sold_tv);
                stockTv = (TextView) view.findViewById(R.id.stock_tv);
                newPriceTv = (TextView) view.findViewById(R.id.new_price_tv);
                oldPriceTv = (TextView) view.findViewById(R.id.old_price_tv);
            }
        }
		
	}
}