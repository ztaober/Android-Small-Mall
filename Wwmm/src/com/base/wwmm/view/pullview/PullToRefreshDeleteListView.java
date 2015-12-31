package com.base.wwmm.view.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;

/**
 * 下拉刷新列表控件
 * 
 * @Description TODO
 * @author CodeApe
 * @version 1.0
 * @date 2013-12-4
 * @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 * 
 */
public class PullToRefreshDeleteListView extends PullToRefreshAdapterViewBase<DeleteListView> {
	
	public PullToRefreshDeleteListView(Context context) {
		super(context);
	}

	public PullToRefreshDeleteListView(Context context, int mode) {
		super(context, mode);
	}

	public PullToRefreshDeleteListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected final DeleteListView createRefreshableView(Context context, AttributeSet attrs) {
		DeleteListView lv = new DeleteListView(context, attrs);
		lv.setId(android.R.id.list);
		return lv;
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return super.getContextMenuInfo();
	}
}
