package com.base.wwmm.fragment;

import java.util.Arrays;

import android.view.View;
import android.widget.ListView;

import com.base.wwmm.R;
import com.base.wwmm.activity.settting.AddrHandActivity;
import com.base.wwmm.activity.settting.MyCarActivity;
import com.base.wwmm.activity.settting.MyCollectionActivity;
import com.base.wwmm.activity.settting.MyOrderActivity;
import com.base.wwmm.activity.settting.OpinionActivity;
import com.base.wwmm.activity.settting.QRCodeActivity;
import com.base.wwmm.adapter.CommAdapter;
import com.base.wwmm.adapter.CommAdapter.AdapterListener;
import com.base.wwmm.utils.IntentUtil;

/**
 * 设置
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class SettingFragment extends BaseFragment implements AdapterListener {
	private ListView listView;
	private CommAdapter<String> adapter;
	private String[] items = new String[] { "我的订单", "我的收货地址", "我的收藏", "购物车", "意见反馈", "二维码", "版本更新" };

	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_setting, null);
	}

	@Override
	protected void findViews() {
		listView = findViewById(R.id.setting_listview);
	}

	@Override
	protected void initData() {
		titleView.setTitle("设置");
		adapter = new CommAdapter<String>(context, Arrays.asList(items), R.layout.item_setting, this) {

			@Override
			public void onGetView(int position, View convertView, String data) {
				setText(convertView, R.id.setting_txt_name, data);
			}
		};
		listView.setAdapter(adapter);
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}

	@Override
	public void onItemClick(int position, View v) {
		switch (position) {
		case 0:// 我的订单
			IntentUtil.gotoActivity(context, MyOrderActivity.class);
			break;
		case 1:// 我的收货地址
			IntentUtil.gotoActivity(context, AddrHandActivity.class);
			break;
		case 2:// 我的收藏
			IntentUtil.gotoActivity(context, MyCollectionActivity.class);
			break;
		case 3:// 购物车
			IntentUtil.gotoActivity(context, MyCarActivity.class);
			break;
		case 4:// 意见反馈
			IntentUtil.gotoActivity(context, OpinionActivity.class);
			break;
		case 5:// 二维码
			IntentUtil.gotoActivity(context, QRCodeActivity.class);
			break;
		case 6:// 版本更新

			break;
		default:
			break;
		}
	}
}