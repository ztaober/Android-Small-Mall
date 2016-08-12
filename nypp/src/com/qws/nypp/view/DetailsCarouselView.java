package com.qws.nypp.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.home.GoodsDetailActivity;
import com.qws.nypp.activity.home.ImagePagerActivity;
import com.qws.nypp.bean.BannerBean;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.utils.DisplayUtil;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;

/**
 * 详情页轮播图
 * 
 * @Description
 * @author Troy
 * @date 2016-4-20 上午11:32:46
 * @Copyright:
 */
public class DetailsCarouselView extends RelativeLayout {
	private Context context;
	private List<String> adList;
	/** ViewPager */
	private ViewPager viewpager_ad;
	/** 点 */
	private LinearLayout ll_ad;
	/** 广告播放时间 */
	private final int adPlayerTime = 5000;
	/** 广告当前所在位置 */
	private int flag = 0;
	/** touch开始x坐标 */
	private int startX = 0;
	/** touch开始y坐标 */
	private int startY = 0;
	/** imageLoader默认配置 */
	public DisplayImageOptions options;

	public DetailsCarouselView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public DetailsCarouselView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DetailsCarouselView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		options = TApplication.getInstance().getAllOptions(R.drawable.bg_defualt_detail);
		View view = View.inflate(context, R.layout.view_ad_carousel, null);
		addView(view);
		viewpager_ad = (ViewPager) findViewById(R.id.viewpager_ad);
		ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
	}
	
	public void initPicList(List<String> picList){
		adList = picList;
		initAd();
	}

	private void initAd() {
		if (adList.size() == 0) {
			return;
		}
		ll_ad.removeAllViews();
		for (int i = 0; i < adList.size(); i++) {
			View point = new View(context);
			point.setBackgroundResource(R.drawable.selector_enabl_point);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 8), DisplayUtil.dip2px(context, 8));
			params.leftMargin = DisplayUtil.dip2px(context, 4);
			params.rightMargin = 0;
			point.setEnabled(i == 0);
			point.setLayoutParams(params);
			ll_ad.addView(point);
		}
		viewpager_ad.setAdapter(new MyAdapter());
		viewpager_ad.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				flag = position;
				for (int i = 0; i < ll_ad.getChildCount(); i++) {
					ll_ad.getChildAt(i).setEnabled(false);
				}
				ll_ad.getChildAt(position % ll_ad.getChildCount()).setEnabled(true);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		viewpager_ad.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getX();
					startY = (int) event.getY();
				case MotionEvent.ACTION_MOVE:

					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					if (Math.abs(event.getX() - startX) < 10 && Math.abs(event.getY() - startY) < 10) {
        				Intent intent = new Intent(context, ImagePagerActivity.class);
        				intent.putStringArrayListExtra("path", (ArrayList<String>) adList);
        				intent.putExtra("cur", flag);
        				context.startActivity(intent);
					}
					break;
				}
				return false;
			}
		});
	}

	private class MyAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return adList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View view, int position) {
			ViewPager viewPage = (ViewPager) view;
			View inflate = View.inflate(getContext(), R.layout.item_ad_carousel, null);
			ImageView ad_img_item = (ImageView) inflate.findViewById(R.id.ad_img_item);
			ImageLoader.getInstance().displayImage(adList.get(position), ad_img_item, options);
			viewPage.addView(inflate);
			return inflate;
		}

		@Override
		public void destroyItem(ViewGroup group, int position, Object object) {
			ViewPager viewPage = (ViewPager) group;
			viewPage.removeView((View) object);
		}
	}
}
