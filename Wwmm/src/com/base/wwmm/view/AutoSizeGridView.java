package com.base.wwmm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 自动计算列表高度的gridview控件
 * 
 * @Description 将gridview用于Scrollview、Listview嵌套使用时候可以使用该控件，避免显示不全和事件冲突
 * @author CodeApe
 * @version 1.0
 * @date 2014年12月31日
 * @Copyright: Copyright (c) 2014 Shenzhen Utoow Technology Co., Ltd. All rights reserved.
 * 
 */
public class AutoSizeGridView extends GridView {
	private OnClickListener listener;
	private boolean isClick = false;

	public AutoSizeGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AutoSizeGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoSizeGridView(Context context) {
		super(context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		listener = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (listener == null) {
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isClick = false;
			if (pointToPosition((int) event.getX(), (int) event.getY()) == INVALID_POSITION) {
				isClick = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			if (pointToPosition((int) event.getX(), (int) event.getY()) == INVALID_POSITION && isClick) {
				isClick = false;
				listener.onClick(this);
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
