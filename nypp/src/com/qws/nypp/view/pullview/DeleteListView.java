package com.qws.nypp.view.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * 侧滑Item的ListView
 */
public class DeleteListView extends ListView {
	private float minDis = 10;
	// 记住上次X触摸屏的位置
	private float mLastMotionX;
	// 记住上次Y触摸屏的位置
	private float mLastMotionY;
	public boolean isLock = false;

	public DeleteListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DeleteListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DeleteListView(Context context) {
		super(context);
	}

	/**
	 * 如果一个ViewGroup的onInterceptTouchEvent()方法返回true，说明Touch事件被截获， 子View不再接收到Touch事件，而是转向本ViewGroup的
	 * onTouchEvent()方法处理。从Down开始，之后的Move，Up都会直接在onTouchEvent()方法中处理。 先前还在处理touch event的child view将会接收到一个 ACTION_CANCEL。
	 * 如果onInterceptTouchEvent()返回false，则事件会交给child view处理。
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isIntercept(ev)) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

	// /** 为了兼容listView中有editText,只能如此 TODO */ 加了这个listView高度不能自适应
	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.EXACTLY);
	// super.onMeasure(widthMeasureSpec, expandSpec);
	// }

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		boolean dte = super.dispatchTouchEvent(event);
		// if (MotionEvent.ACTION_UP == event.getAction() && !dte) {
		// int position = pointToPosition((int) event.getX(), (int) event.getY());
		// View view = getChildAt(position);
		// super.performItemClick(view, position, view.getId());
		// }
		return dte;
	}

	@Override
	// 处理点击事件，如果是手势的事件则不作点击事件 普通View
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	// 处理点击事件，如果是手势的事件则不作点击事件 ListView
	public boolean performItemClick(View view, int position, long id) {
		return super.performItemClick(view, position, id);
	}

	/**
	 * 检测是ListView滑动还是item滑动 isLock 一旦判读是item滑动，则在up之前都是返回false
	 */
	private boolean isIntercept(MotionEvent ev) {
		float x = ev.getX();
		float y = ev.getY();
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isLock) {
				float deltaX = Math.abs(mLastMotionX - x);
				float deltay = Math.abs(mLastMotionY - y);
				mLastMotionX = x;
				mLastMotionY = y;
				if (deltaX > deltay && deltaX > minDis) {
					isLock = true;
					return false;
				}
			} else {
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
			isLock = false;
			break;
		case MotionEvent.ACTION_CANCEL:
			isLock = false;
			break;
		}
		return true;
	}
}
