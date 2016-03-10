package com.qws.nypp.view.pullview;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;


/**
 * 滑动View
 * 
 * @Description
 * @author 綦巍
 * @date 2015-5-14
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class SlideLayoutView extends LinearLayout {
	/** 滑动工具 */
	private Scroller mScroller;
	/** 上一次的位子 */
	private float mLastMotionX;
	/** 需要滑动到的位子 */
	private int deltaX;
	/** 滑动的最大宽度 */
	private int back_width;
	/** 当前状态 false 关闭 true打开 */
	private boolean state = false;
	/** 记录第一次点击的位置 */
	private RectF downRect = new RectF();
	/** 是否是滑动了 */
	private boolean isMove = false;

	public SlideLayoutView(Context context) {
		this(context, null);
	}

	public SlideLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isMove = false;
			downRect.set(ev.getX() - 10, ev.getY() - 10, ev.getX() + 10, ev.getY() + 10);
			mLastMotionX = ev.getX();
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			if (eventClick != null) {
				eventClick.onEventDown();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (!downRect.contains(ev.getX(), ev.getY())) {
				isMove = true;
			}
			break;
		default:
			break;
		}
		return isMove;
	}

	@Override
	public void computeScroll() {
		// 会更新Scroller中的当前x,y位置
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();
		back_width = 0;
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			if (i > 0) {
				back_width = back_width + child.getMeasuredWidth();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			deltaX = (int) (mLastMotionX - x);
			mLastMotionX = x;
			int scrollx = getScrollX() + deltaX;
			if (scrollx > 0 && scrollx < back_width) {
				scrollBy(deltaX, 0);
			} else if (scrollx > back_width) {
				scrollTo(back_width, 0);
			} else if (scrollx < 0) {
				scrollTo(0, 0);
			}
			break;
		case MotionEvent.ACTION_UP:
			int scroll = getScrollX();
			if (scroll > back_width / 2) {
				scrollTo(back_width, 0);
				state = true;
			} else {
				scrollTo(0, 0);
				state = false;
			}
			if (eventClick != null) {
				eventClick.onEventUp();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			scrollIn();
			break;
		}
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int margeLeft = 0;
		int size = getChildCount();
		int hight = 0;
		for (int i = 0; i < size; i++) {
			View view = getChildAt(i);
			if (view.getVisibility() != View.GONE) {
				int childWidth = view.getMeasuredWidth();
				// 将内部子孩子横排排列
				view.layout(margeLeft, 0, margeLeft + childWidth, hight == 0 ? view.getMeasuredHeight() : hight);
				margeLeft += childWidth;
			}
			if (i == 0) {
				hight = view.getMeasuredHeight();
			}
		}
	}

	private void smoothScrollTo(int destX, int destY) {
		int scrollX = getScrollX();
		int delta = destX - scrollX;
		mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
		invalidate();
	}

	/**
	 * 关闭侧滑
	 */
	public void scrollIn() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
		this.smoothScrollTo(0, 0);
		state = false;
	}

	/**
	 * 设置状态
	 * 
	 * @param state true显示 false关闭
	 */
	public void setState(boolean state) {
		scrollTo(state ? back_width : 0, 0);
		this.state = state;
	}

	public interface EventClick {
		/** 当前控件获取点下事件 */
		public void onEventDown();

		/** 当前控件获取抬起事件 */
		public void onEventUp();
	}

	private EventClick eventClick;

	public void setEventClick(EventClick eventClick) {
		this.eventClick = eventClick;
	}

	/**
	 * @return true 打开,false 关闭
	 */
	public boolean getState() {
		return state;
	}

}
