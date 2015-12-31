package com.base.wwmm.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.base.wwmm.R;

/**
 * 
 * @Description
 * @author
 * @date 2015-12-31
 */
public class ViewPagerAdapter extends PagerAdapter {
	/** view集合 */
	private ArrayList<View> views;

	public ViewPagerAdapter(ArrayList<View> views) {
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(View view, int position) {
		ViewPager viewPage = (ViewPager) view;
		viewPage.addView(views.get(position));
		return views.get(position);
	}

	@Override
	public void destroyItem(ViewGroup group, int position, Object object) {
		ViewPager viewPage = (ViewPager) group;
		viewPage.removeView(views.get(position));
	}
}