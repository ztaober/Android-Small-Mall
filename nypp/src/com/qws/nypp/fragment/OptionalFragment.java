package com.qws.nypp.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qws.nypp.R;
import com.qws.nypp.activity.home.GoodsDetailActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.adapter.CommAdapter.AdapterListener;
import com.qws.nypp.bean.CategoryBean;
import com.qws.nypp.bean.CommonResult4List;
import com.qws.nypp.bean.GoodsBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.fragment.HomeFragment.RecyclerViewAdapter.MyViewHolder;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.view.TabIndicator;
import com.qws.nypp.view.LoadingView.LoadingMode;
import com.qws.nypp.view.TabIndicator.OnTabChangeListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 自选
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class OptionalFragment extends BaseFragment implements AdapterListener {
	
	/** 控件 */
	private HorizontalScrollView categoryHs;
	/** 控件 */
	private RadioGroup categoryRg;
	/** 控件 */
	private GridView categoryGv;
	/** 控件 */
	private LinearLayout categoryLl;
	/** 控件 */
	private TextView categoryHintTv;
	/** 控件 */
	private PullToRefreshListView mPullRefreshListView;
	/** 适配器 */
	private CommAdapter<String> adapterGv;
	/** 适配器 */
	private CommAdapter<GoodsBean> goodsAdapter;
	/** 数据 */
	private ArrayList<CategoryBean> list = new ArrayList<CategoryBean>();
	/** 数据 */
	private List<String> typeList;
	/** 数据 */
	private ArrayList<GoodsBean> goodsList = new ArrayList<GoodsBean>();
	/** 当前页 */
	private int page = 1;
	/** 切换进度条 */
	private TabIndicator view_tab;
	//排序 0综合，1价格，2销量
	private String order = "0";;
	//款型
	private String type = "";
	//颜色
	private String color = "";
	//尺码
	private String size = "";
	
	

	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_optional, null);
	}

	@Override
	protected void findViews() {
		categoryHs = (HorizontalScrollView)findViewById(R.id.category_hs);
		categoryRg = (RadioGroup)findViewById(R.id.categoryRg);
		categoryGv = (GridView)findViewById(R.id.categroy_gv);
		categoryLl = (LinearLayout)findViewById(R.id.category_ll);
		categoryLl.setVisibility(View.GONE);
		categoryLl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeChoser();
			}
		});
		
		view_tab = findViewById(R.id.view_tab);
		view_tab.initData(new String[] {"综合","价格","销量"});
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_listview);
		categoryHintTv = (TextView)findViewById(R.id.hint_tv);
	}
	

	@Override
	protected void initData() {
		titleView.setTitle("自选");
		options = TApplication.getInstance().getAllOptions(R.drawable.bg_defualt_180);
		getList();
		
		for(int i=0; i<categoryRg.getChildCount(); i++){
			RadioButton button = (RadioButton)categoryRg.getChildAt(i);
			button.setText(list.get(i).getGoodsType());
			button.setId(i);
			button.setBackgroundResource(R.drawable.category_label_unselect_n);
		}
		categoryRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == -1){
					int childCount = group.getChildCount();
					for(int i=0; i<childCount; i++){
						RadioButton button = (RadioButton)categoryRg.getChildAt(i);
						String selectedType = list.get(i).getSelectedType();
						if(!selectedType.equals("")){
							button.setText(selectedType);
							button.setBackgroundResource(R.drawable.category_sub_label_selected);
						}else{
							button.setBackgroundResource(R.drawable.category_label_unselect_n);
						}
					}
					categoryHs.setBackgroundResource(R.drawable.category_line_unselect);
				}else{
					int childCount = group.getChildCount();
					RadioButton clickButton = (RadioButton)group.getChildAt(checkedId);
					if(clickButton!=null){
						if(clickButton.isChecked()){
							String selectedType = list.get(checkedId).getSelectedType();
							if(!selectedType.equals("")){
								list.get(checkedId).setSelectedType("");
								clickButton.setText(list.get(checkedId).getGoodsType());
								clickButton.setBackgroundResource(R.drawable.category_label_unselect_n);
								closeChoser();
								switch (checkedId) {
								case 0:
									type = "";
									break;
								case 1:
									color = "";
									break;
								case 2:
									size = "";
									break;
								default:
									break;
								}
								page = 1;
								goodsList.clear();
								refreshData();
							}else{
								for(int i=0; i<childCount; i++){
									RadioButton button = (RadioButton)categoryRg.getChildAt(i);
									String selectedT = list.get(i).getSelectedType();
									if(!selectedT.equals("")){
										button.setBackgroundResource(R.drawable.category_sub_label_selected_s);
									}else{
										button.setBackgroundResource(R.drawable.category_label_unselect_s);
									}
								}
								clickButton.setBackgroundResource(R.drawable.category_label_selected);
								categoryHs.setBackgroundResource(R.drawable.category_line_selected);
								openChoser(checkedId);
							}
						}else{
							//clearCheck会调用一次上次点过的 然后才是-1  so 不做处理
						}
					}
				}
			}
		});
		
		
		
		adapterGv = new CommAdapter<String>(context, typeList, R.layout.item_category_text) {
			

			@Override
			public void onGetView(int position, View convertView, String data) {
				setText(convertView, R.id.optional_txt_type, data);
			}
		};
		
		categoryGv.setAdapter(adapterGv);
		
		
		goodsAdapter = new CommAdapter<GoodsBean>(context, goodsList, R.layout.item_optional_list_goods , this) {
			
			@Override
			public void onGetView(int position, View convertView, GoodsBean data) {
				ImageLoader.getInstance().displayImage(data.getImage(), (ImageView)convertView.findViewById(R.id.item_optional_goods_img), options);
				setText(convertView, R.id.item_optional_goods_tv, data.getTitle());
				setText(convertView, R.id.sold_tv, "成交"+data.getSoldQuantity()+"笔");
				setText(convertView, R.id.stock_tv, "混批");
				setText(convertView, R.id.new_price_tv, "¥ "+data.getPreferentialPrice());
				setText(convertView, R.id.old_price_tv, "¥ "+data.getPrice());
				((TextView)convertView.findViewById(R.id.old_price_tv)).getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
			}
		};
		
		
		mPullRefreshListView.setAdapter(goodsAdapter);
		
	}
	
	@Override
	public void onItemClick(int position, View v) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("productId", goodsList.get(position).getProductId());
    	IntentUtil.gotoActivity(context, GoodsDetailActivity.class, bundle);
	}

	private int choseIndex;
	private DisplayImageOptions options;
	protected void openChoser(int position) {
		choseIndex = position;
		typeList = (List<String>) list.get(position).getCategroys();
		adapterGv.refreshList(typeList);
		categoryLl.setVisibility(View.VISIBLE);
	}
	protected void closeChoser() {
		categoryRg.clearCheck();
		categoryLl.setVisibility(View.GONE);
	}

	private void getList() {
		list.clear();
		list.add(new CategoryBean("款型", Arrays.asList(new String[]{"文胸套装","长袖款","单品内衣","单品内裤"})));
		list.add(new CategoryBean("颜色", Arrays.asList(new String[]{"花纹","黑色","红色","黄色","白色","紫色","绿色","蓝色"})));
		list.add(new CategoryBean("尺码", Arrays.asList(new String[]{"M","L","XL","XXL"})));
		typeList = (List<String>) list.get(0).getCategroys();
	}

	@Override
	protected void setListener() {
		// 重新加载按钮事件
		mLoadingView.setReloadBtListenner(new OnClickListener() {
			@Override
			public void onClick(View v) {
				page = 1;
				goodsList.clear();
				refreshData();
				// 加载模式
				mLoadingView.setLoadingMode(LoadingMode.LOADING);
			}
		});
				
		categoryGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtil.i(""+position);
				String category = typeList.get(position);
				list.get(choseIndex).setSelectedType(category);
				switch (choseIndex) {
				case 0:
					position++;
					type = "0"+position;
					break;
				case 1:
					color = category;
					break;
				case 2:
					size = category;
					break;

				default:
					break;
				}
				page = 1;
				goodsList.clear();
				refreshData();
				closeChoser();
			}
		});
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				page = 1;
				goodsList.clear();
				refreshData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				page++;
				refreshData();
			}
		});
		
		view_tab.setTabListener(new OnTabChangeListener() {
			
			@Override
			public void OnTabChange(int position) {
				order = position+"";
				page = 1;
				goodsList.clear();
				refreshData();
			}
		});
	}

	@Override
	protected void getData() {
		refreshData();
		// 加载模式
		mLoadingView.setLoadingMode(LoadingMode.LOADING);
	}
	
	private void refreshData(){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.OPT_PRODUCT_LIST);
		JSONObject postJson = null;
		try {
			postJson = new JSONObject();
			postJson.put("page", page+"");
			postJson.put("rows", "5");
			postJson.put("order", order);
			postJson.put("sign", "test");
			postJson.put("rows", "5");
			if(!"".equals(type)){
				postJson.put("type", type);
			}
			if(!"".equals(color)){
				JSONArray array = new JSONArray();
				array.put(color);
				postJson.put("color", array);
			}
			if(!"".equals(size)){
				JSONArray array = new JSONArray();
				array.put(size);
				postJson.put("braSize", array);
			}
		} catch (Exception e) {
			return;
		}
		LogUtil.t(postJson.toString());
		request.setRequestBody(postJson.toString());
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				 JSONObject result = response.get();// 响应结果
                if("200".equals(result.optString("status"))) {
                    CommonResult4List<GoodsBean> dataList = CommonResult4List.fromJson(result.toString(), GoodsBean.class);
                    List<GoodsBean> data = dataList.getData();
                    goodsList.addAll(data);
                    goodsAdapter.refreshList(goodsList);
                    mPullRefreshListView.onRefreshComplete();
                    if(goodsList.size() < 1){
                    	categoryHintTv.setVisibility(View.VISIBLE);
                    } else {
                    	categoryHintTv.setVisibility(View.INVISIBLE);
                    }
                    
                    mLoadingView.setLoadingMode(LoadingMode.LOADING_SUCCESS);
                }
			}

			@Override
			public void onFailed(int what, String url, Object tag, Exception exception, int responseCode,
					long networkMillis) {
				mPullRefreshListView.onRefreshComplete();	
				mLoadingView.setLoadingMode(LoadingMode.LOADING_FAILED);
			}
		}, false, false);
	}

}