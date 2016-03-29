package com.qws.nypp.view;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qws.nypp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.activity.home.GoodsDetailActivity;
import com.qws.nypp.bean.InformationBean;
import com.qws.nypp.utils.DisplayUtil;
import com.qws.nypp.utils.IntentUtil;

/**
 * 
 * @Description
 * @author qw
 * @date 2016-1-4
 */
public class AdCarouselView extends RelativeLayout {
	private ArrayList<InformationBean> adList;
	/** ViewPager */
	private ViewPager viewpager_ad;
	/** 点 */
	private LinearLayout ll_ad;
	/** 切换广告线程 */
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (isShown()) {
				flag++;
				viewpager_ad.setCurrentItem(flag);
			}
			handler.removeCallbacks(runnable);
			handler.postDelayed(runnable, adPlayerTime);
		}
	};
	/** 广告播放时间 */
	private final int adPlayerTime = 5000;
	/** 广告当前所在位置 */
	private int flag;
	/** touch开始x坐标 */
	private int startX = 0;
	/** touch开始y坐标 */
	private int startY = 0;
	private Handler handler = new Handler();
	/** 是否是轮播图被按下 */
	private boolean isDown = false;
	/** 是否获取了网络数据 */
	private boolean isGetNet = false;
	/** imageLoader默认配置 */
	public DisplayImageOptions options;

	public AdCarouselView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public AdCarouselView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AdCarouselView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.build();
		View view = View.inflate(context, R.layout.view_ad_carousel, null);
		DisplayUtil.setViewWH(context, view, 256f / 640f);
		addView(view);
		viewpager_ad = (ViewPager) findViewById(R.id.viewpager_ad);
		ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
		queryInformations();
	}

	private void queryInformations() {
		if (!isGetNet) {
			isGetNet = true;
			adList = new ArrayList<InformationBean>();
			InformationBean object = new InformationBean();
			object.url = "http://staticlive.douyutv.com/upload/signs/201512311129148111.jpg";
			adList.add(object);
			object = new InformationBean();
			object.url = "http://staticlive.douyutv.com/upload/signs/201512311333458232.jpg";
			adList.add(object);
			object = new InformationBean();
			object.url = "http://staticlive.douyutv.com/upload/signs/201512311129148111.jpg";
			adList.add(object);
			object = new InformationBean();
			object.url = "http://staticlive.douyutv.com/upload/signs/201512311333458232.jpg";
			adList.add(object);
			initAd(getContext());
		}
	}

	/** 重新开始 */
	public void resetRun() {
		if (isDown) {
			isDown = false;
			handler.removeCallbacks(runnable);
			handler.postDelayed(runnable, adPlayerTime);
		}
	}

	private void initAd(Context context) {
		if (adList.size() == 0) {
			return;
		}
		handler.removeCallbacks(runnable);
		ll_ad.removeAllViews();
		for (int i = 0; i < adList.size(); i++) {
			View point = new View(context);
			point.setBackgroundResource(R.drawable.selector_enabl_point);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 10), DisplayUtil.dip2px(context, 10));
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
		flag = 1000 - (1000 % adList.size());
		viewpager_ad.setCurrentItem(flag);
		viewpager_ad.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getX();
					startY = (int) event.getY();
					handler.removeCallbacks(runnable);
					isDown = true;
				case MotionEvent.ACTION_MOVE:

					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					handler.postDelayed(runnable, adPlayerTime);
					if (Math.abs(event.getX() - startX) < 10 && Math.abs(event.getY() - startY) < 10) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("bean", adList.get(flag % adList.size()));
						IntentUtil.gotoActivity(getContext(), GoodsDetailActivity.class, bundle);
					}
					break;
				}
				return false;
			}
		});
		handler.postDelayed(runnable, adPlayerTime);
	}

	private class MyAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View view, int position) {
			ViewPager viewPage = (ViewPager) view;
			View inflate = View.inflate(getContext(), R.layout.item_ad_carousel, null);
			DisplayUtil.setViewWH(getContext(), inflate, 256f / 640f);
			ImageView ad_img_item = (ImageView) inflate.findViewById(R.id.ad_img_item);
			ImageLoader.getInstance().displayImage(adList.get(position % adList.size()).url, ad_img_item, options);
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
