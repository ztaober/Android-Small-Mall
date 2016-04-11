package com.qws.nypp.view.pullview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView>{

	public PullToRefreshRecyclerView(Context context) {
		super(context);
	}
	
	public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshRecyclerView(Context context, int mode) {
		super(context, mode);
	}

	@Override
	protected RecyclerView createRefreshableView(Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isReadyForPullDown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isReadyForPullUp() {
		// TODO Auto-generated method stub
		return false;
	}

}
