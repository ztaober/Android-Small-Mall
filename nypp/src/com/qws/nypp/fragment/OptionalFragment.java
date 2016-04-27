package com.qws.nypp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qws.nypp.R;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.bean.CategoryBean;
import com.qws.nypp.bean.CommonResult4List;
import com.qws.nypp.bean.GoodsBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.view.TabIndicator;
import com.qws.nypp.view.TabIndicator.OnTabChangeListener;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

/**
 * 自选
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class OptionalFragment extends BaseFragment {
	
	/** 控件 */
	private HorizontalScrollView categoryHs;
	/** 控件 */
	private RadioGroup categoryRg;
	/** 控件 */
	private GridView categoryGv;
	/** 控件 */
	private LinearLayout categoryLl;
	/** 控件 */
	private PullToRefreshListView mPullRefreshListView;
	/** 适配器 */
	private CommAdapter<String> adapterGv;
	/** 适配器 */
	private CommAdapter<GoodsBean> goodsAdapter;
	/** 数据 */
	private ArrayList<CategoryBean> list = new ArrayList<CategoryBean>();
	/** 数据 */
	private ArrayList<String> typeList = new ArrayList<String>();
	/** 数据 */
	private ArrayList<GoodsBean> goodsList = new ArrayList<GoodsBean>();
	/** 当前页 */
	private int page = 1;
	/** 切换进度条 */
	private TabIndicator view_tab;

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
	}
	

	@Override
	protected void initData() {
		titleView.setTitle("自选");
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
		
		
		goodsAdapter = new CommAdapter<GoodsBean>(context, goodsList, R.layout.item_optional_list_goods) {
			
			@Override
			public void onGetView(int position, View convertView, GoodsBean data) {
				setText(convertView, R.id.item_optional_goods_tv, data.getTitle());
			}
		};
		
		
		mPullRefreshListView.setAdapter(goodsAdapter);
		
	}

	private int choseIndex;
	protected void openChoser(int position) {
		choseIndex = position;
		typeList = (ArrayList<String>) list.get(position).getCategroys();
		adapterGv.refreshList(typeList);
		categoryLl.setVisibility(View.VISIBLE);
	}
	protected void closeChoser() {
		categoryRg.clearCheck();
		categoryLl.setVisibility(View.GONE);
	}

	private void getList() {
		ArrayList<String> q1 = new ArrayList<String>();
		q1.add("套装");
		q1.add("长袖款");
		q1.add("内衣");
		q1.add("内裤");
		ArrayList<String> q2 = new ArrayList<String>();
		q2.add("花纹");
		q2.add("黑色");
		q2.add("红色");
		q2.add("黄色");
		q2.add("白色");
		q2.add("紫色");
		ArrayList<String> q3 = new ArrayList<String>();
		q3.add("M");
		q3.add("L");
		q3.add("XL");
		q3.add("XXL");
		list.clear();
		list.add(new CategoryBean("款型", q1));
		list.add(new CategoryBean("颜色", q2));
		list.add(new CategoryBean("尺码", q3));
		typeList = (ArrayList<String>) list.get(0).getCategroys();
	}

	@Override
	protected void setListener() {
		categoryGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtil.i(""+position);
				String category = typeList.get(position);
				list.get(choseIndex).setSelectedType(category);
				closeChoser();
			}
		});
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				page = 1;
				goodsList.clear();
				getData(page);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				page++;
				getData(page);
			}
		});
		
		view_tab.setTabListener(new OnTabChangeListener() {
			
			@Override
			public void OnTabChange(int position) {
				LogUtil.t("OnTabChange="+position);
			}
		});
	}

	@Override
	protected void getData() {
		getData(page);
	}
	
	private void getData(int i){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.OPT_PRODUCT_PATH);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("page", i+"");
		postData.put("rows", "4");
		postData.put("sign", "test");
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(0, request, new OnResponseListener<JSONObject>() {

			@Override
			public void onStart(int what) {
			}

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				 if (what == 0) {
	                // 请求成功
	                JSONObject result = response.get();// 响应结果
	                CommonResult4List<GoodsBean> dataList = CommonResult4List.fromJson(result.toString(), GoodsBean.class);
	                goodsList.addAll(dataList.getData());
//	                goodsApter.notifyDataSetChanged();
	                goodsAdapter.refreshList(goodsList);
	                mPullRefreshListView.onRefreshComplete();
	                LogUtil.t("goodsList="+result);
	                LogUtil.t("goodsList size="+list.size());
	             }
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				ToastUtil.show("请求失败");
				mPullRefreshListView.onRefreshComplete();
			}

			@Override
			public void onFinish(int what) {
				LogUtil.t("onFinish");
			}
		});
	}
}