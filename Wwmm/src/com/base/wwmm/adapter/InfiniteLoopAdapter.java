package com.base.wwmm.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 无限循环的Adapter
 * 
 * @Description
 * @author 綦巍
 * @date 2015-5-8
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class InfiniteLoopAdapter extends PagerAdapter {
	private ArrayList<View> list;

	public InfiniteLoopAdapter(ArrayList<View> list) {
		super();
		this.list = list;
	}

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
		if (list.size() == 0) {
			return null;
		}
		ViewPager viewPage = (ViewPager) view;
		View instant = list.get(position % list.size());
		if ((ViewGroup) instant.getParent() != null && list.size() < 4) {
			((ViewGroup) instant.getParent()).removeView(instant);
		}
		viewPage.addView(instant);
		return instant;
	}

	@Override
	public void destroyItem(ViewGroup group, int position, Object object) {
		if (list.size() > 3) {
			ViewPager viewPage = (ViewPager) group;
			viewPage.removeView(list.get(position % list.size()));
		}
	}
}
