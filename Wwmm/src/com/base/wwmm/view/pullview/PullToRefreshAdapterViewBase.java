package com.base.wwmm.view.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public abstract class PullToRefreshAdapterViewBase<T extends AbsListView> extends PullToRefreshBase<T> implements OnScrollListener {

	private int lastSavedFirstVisibleItem = -1;
	/** 列表滑动监听 */
	private OnScrollListener onScrollListener;
	/** 监听列表最后一个item显示 */
	private OnLastItemVisibleListener onLastItemVisibleListener;

	/** 下拉出后的空view */
	private FrameLayout refreshableViewHolder;

	public PullToRefreshAdapterViewBase(Context context) {
		super(context);
		refreshableView.setOnScrollListener(this);
	}

	public PullToRefreshAdapterViewBase(Context context, int mode) {
		super(context, mode);
		refreshableView.setOnScrollListener(this);
	}

	public PullToRefreshAdapterViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		refreshableView.setOnScrollListener(this);
	}

	/**
	 * scroll事件监听
	 *
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-4,上午9:52:19 <br/>
	 * UpdateTime: 2013-12-4,上午9:52:19 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 *
	 * @param view
	 * @param firstVisibleItem
	 * @param visibleItemCount
	 * @param totalItemCount
	 */
	public final void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

		if (null != onLastItemVisibleListener) {// 监听器不为空，
			// detect if last item is visible
			if (visibleItemCount > 0 && (firstVisibleItem + visibleItemCount == totalItemCount)) {
				// only process first event
				if (firstVisibleItem != lastSavedFirstVisibleItem) {
					lastSavedFirstVisibleItem = firstVisibleItem;
					onLastItemVisibleListener.onLastItemVisible();
				}
			}
		}

		// 如果滑动监听器不为空,则继续往下传递
		if (null != onScrollListener) {
			onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public final void onScrollStateChanged(final AbsListView view, final int scrollState) {
		if (null != onScrollListener) {
			onScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	/**
	 * Sets the Empty View to be used by the Adapter View.
	 * 
	 * We need it handle it ourselves so that we can Pull-to-Refresh when the Empty View is shown.
	 * 
	 * Please note, you do <strong>not</strong> usually need to call this method yourself. Calling setEmptyView on the AdapterView will automatically
	 * call this method and set everything up. This includes when the Android Framework automatically sets the Empty View based on it's ID.
	 * 
	 * @param newEmptyView - Empty View to be used
	 */
	public final void setEmptyView(View newEmptyView) {
		// If we already have an Empty View, remove it
		// if (null != emptyView) {
		// refreshableViewHolder.removeView(emptyView);
		// }
		//
		// if (null != newEmptyView) {
		// ViewParent newEmptyViewParent = newEmptyView.getParent();
		// if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
		// ((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
		// }
		//
		// this.refreshableViewHolder.addView(newEmptyView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
		// }
		//
		// if (refreshableView instanceof EmptyViewMethodAccessor) {
		// ((EmptyViewMethodAccessor) refreshableView).setEmptyViewInternal(newEmptyView);
		// } else {
		// this.refreshableView.setEmptyView(newEmptyView);
		// }
	}

	/**
	 * 设置列表最后一个item显示监听
	 *
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-4,上午9:58:31 <br/>
	 * UpdateTime: 2013-12-4,上午9:58:31 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 *
	 * @param listener
	 */
	public final void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
		onLastItemVisibleListener = listener;
	}

	/**
	 * 设置列表滑动监听
	 *
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-4,上午9:58:51 <br/>
	 * UpdateTime: 2013-12-4,上午9:58:51 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 *
	 * @param listener
	 */
	public final void setOnScrollListener(OnScrollListener listener) {
		onScrollListener = listener;
	}

	/**
	 * 添加刷新View
	 *
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-4,上午9:59:15 <br/>
	 * UpdateTime: 2013-12-4,上午9:59:15 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 *
	 * @param context
	 * @param refreshableView
	 */
	protected void addRefreshableView(Context context, T refreshableView) {
		refreshableViewHolder = new FrameLayout(context);
		refreshableViewHolder.addView(refreshableView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		addView(refreshableViewHolder, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));
	};

	/**
	 * 是否准备好下拉（列表已经滑动到最顶端）
	 *
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-4,上午10:01:16 <br/>
	 * UpdateTime: 2013-12-4,上午10:01:16 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 *
	 * @return true列表已经在顶端
	 */
	public boolean isReadyForPullDown() {
		return isFirstItemVisible();
	}

	/**
	 * 是否准备好上拉（列表已经滑动到最底端）
	 *
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-4,上午10:01:16 <br/>
	 * UpdateTime: 2013-12-4,上午10:01:16 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 *
	 * @return true列表已经在最底端
	 */
	public boolean isReadyForPullUp() {
		return isLastItemVisible();
	}

	/**
	 * 列表是否已经滑动到最顶端
	 *
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-4,上午10:03:16 <br/>
	 * UpdateTime: 2013-12-4,上午10:03:16 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 *
	 * @return
	 */
	private boolean isFirstItemVisible() {
		if (this.refreshableView.getCount() == 0) {
			return true;
		} else if (refreshableView.getFirstVisiblePosition() == 0) {

			final View firstVisibleChild = refreshableView.getChildAt(0);

			if (firstVisibleChild != null) {
				return firstVisibleChild.getTop() >= refreshableView.getTop();
			}
		}

		return false;
	}

	/**
	 * 列表最后一个item是否显示
	 *
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-4,上午10:14:41 <br/>
	 * UpdateTime: 2013-12-4,上午10:14:41 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 *
	 * @return
	 */
	private boolean isLastItemVisible() {
		final int count = this.refreshableView.getCount();
		final int lastVisiblePosition = refreshableView.getLastVisiblePosition();

		if (count == 0) {
			return true;
		} else if (lastVisiblePosition == count - 1) {

			final int childIndex = lastVisiblePosition - refreshableView.getFirstVisiblePosition();
			final View lastVisibleChild = refreshableView.getChildAt(childIndex);

			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= refreshableView.getBottom();
			}
		}
		return false;
	}
}
