package com.qws.nypp.activity.home;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.adapter.CommAdapter;
import com.qws.nypp.bean.GoodsCartBean;
import com.qws.nypp.bean.GoodsCartSukBean;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.view.AutoSizeListView;
import com.qws.nypp.view.StockChangeView;

/**
 * 确认订单界面
 * 
 * @Description
 * @author
 * @date 2016-1-4
 */
public class SureOrderActivity extends BaseActivity {

	private List<GoodsCartBean> cartList = new ArrayList<GoodsCartBean>();
	private AutoSizeListView orderLv;
	private CommAdapter<GoodsCartBean> ordersAdapter;
	private CommAdapter<GoodsCartSukBean> ordersSukAdapter;
	private DisplayImageOptions options;
	
	@Override
	protected int getContentViewId() {
		return R.layout.a_sure_detail;
	}

	@Override
	protected void findViews() {
		orderLv = (AutoSizeListView)findViewById(R.id.sure_order_listview);
	}

	@Override
	protected void initData() {
		titleView.setTitle("确认订单");
		
		Bundle bundle = getIntent().getExtras();
		cartList = (List<GoodsCartBean>) bundle.getSerializable("orderList");
		
		options = TApplication.getInstance().getAllOptionsNoAmi(R.drawable.bg_defualt_118);
		ordersAdapter = new CommAdapter<GoodsCartBean>(context, cartList, R.layout.item_sure_order_cart) {
			
			@Override
			public void onGetView(int position, View convertView, final GoodsCartBean cartData) {
				
				ImageLoader.getInstance().displayImage(cartData.image, (ImageView)convertView.findViewById(R.id.cart_pic_img), options);
				setText(convertView, R.id.item_cart_title_tv, cartData.title);
				setText(convertView, R.id.item_order_quantity, ""+cartData.allQua);
				setText(convertView, R.id.item_order_price, "¥"+cartData.allPri);
				
				
				AutoSizeListView sukListView = (AutoSizeListView)convertView.findViewById(R.id.item_order_lv);
				ordersSukAdapter = new CommAdapter<GoodsCartSukBean>(context,cartData.sukList,R.layout.item_sure_order_suk_cart) {
					
					@Override
					public void onGetView(int position, View convertView, final GoodsCartSukBean data) {
						setText(convertView, R.id.cart_suk_color, "颜色:"+data.colour);
						setText(convertView, R.id.cart_suk_size, "尺码:"+data.size);
						setText(convertView, R.id.cart_suk_price, "单价:"+String.format("%.2f" ,data.price)+"/件");
						setText(convertView, R.id.cart_suk_money, "¥"+String.format("%.2f" ,data.price*data.quantity));
						StockChangeView stockCv = (StockChangeView) convertView.findViewById(R.id.cart_suk_change);
						stockCv.isChange(false);
						stockCv.notifyNum(data.quantity,data.detailId);
					}
				};
				sukListView.setAdapter(ordersSukAdapter);
			}
		};
		orderLv.setAdapter(ordersAdapter);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
	}

	@Override
	protected void getData() {

	}

}