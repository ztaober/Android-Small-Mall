package com.base.wwmm.view.pullview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ExpandableListView;

/**
 * 下拉刷新列表控件,且支持分组 <br/>
 * Description: TODO <br/>
 * Author:jiangwenze <br/>
 * Date: 2015年7月3日 <br/>
 * 
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class PullToRefreshExpandableListView extends PullToRefreshAdapterViewBase<ExpandableListView> {

	/** 支持下拉刷新 */
	public static final int MODE_PULL_TO_DOWN = 0x01;
	/** 支持上拉拉刷新 */
	public static final int MODE_PULL_TO_UP = 0x02;
	/** 同时支持上拉和下拉 */
	public static final int MODE_BOTH_UP_AND_DOWN = 0x03;
	/** 移动Item */
	private final int WHAT_MOVE_ITEM_TO_LAST = 1;
	/** 当前列表模式 */
	public static int currentListMode = MODE_BOTH_UP_AND_DOWN;
	/** view大小更改监听 */
	private OnSizeChangeListener listener;

	public PullToRefreshExpandableListView(Context context) {
		super(context);
	}

	public PullToRefreshExpandableListView(Context context, int mode) {
		super(context, mode);
	}

	public PullToRefreshExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected final ExpandableListView createRefreshableView(Context context, AttributeSet attrs) {
		ExpandableListView lv = new ExpandableListView(context, attrs);
		// Use Generated ID (from res/values/ids.xml)
		lv.setId(android.R.id.list);
		return lv;
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return super.getContextMenuInfo();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (listener != null) {
			listener.onChange(w, h, oldw, oldh);
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * 设置size大小更改监听
	 * 
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-11-28,下午4:31:49 <br/>
	 * UpdateTime: 2013-11-28,下午4:31:49 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	public void setOnSizeChangListener(OnSizeChangeListener listener) {
		this.listener = listener;
	}

	/**
	 * view 大小更改监听事件
	 * 
	 * <br/>
	 * Description: TODO <br/>
	 * Author:CodeApe <br/>
	 * Version: 1.0 <br/>
	 * Date: 2013-11-28 <br/>
	 * 
	 * @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
	 * 
	 */
	public interface OnSizeChangeListener {

		/**
		 * View 大小更改监听事件
		 * 
		 * <br/>
		 * Version: 1.0 <br/>
		 * CreateTime: 2013-11-28,下午4:18:33 <br/>
		 * UpdateTime: 2013-11-28,下午4:18:33 <br/>
		 * CreateAuthor: CodeApe <br/>
		 * UpdateAuthor: CodeApe <br/>
		 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
		 * 
		 * @param w 新的宽度
		 * @param h 新的高度
		 * @param oldw 旧的宽度
		 * @param oldh 新的高度
		 */
		public void onChange(int w, int h, int oldw, int oldh);
	}

}
