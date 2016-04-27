package com.qws.nypp.fragment;

import java.util.Arrays;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.qws.nypp.R;
import com.qws.nypp.activity.settting.AddrHandActivity;
import com.qws.nypp.activity.settting.MyCarActivity;
import com.qws.nypp.activity.settting.MyCollectionActivity;
import com.qws.nypp.activity.settting.MyOrderActivity;
import com.qws.nypp.activity.settting.OpinionActivity;
import com.qws.nypp.activity.settting.QRCodeActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.adapter.CommAdapter.AdapterListener;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;

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
		titleView.setRightBtn("个人中心", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		}, 0);
		titleView.setRightEnabled(false);
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