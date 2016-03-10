package com.qws.nypp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 监听键盘变化的LinearLayout,为兼容小米某些手机 不要写在根布局
 * 
 * @Description
 * @author qw
 * @date 2015-9-22
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class KeyboardShowLinearLayout extends LinearLayout {
	/** 监听 */
	private OnKeyboardChangeLinear onKeyboardChangeLinear;
	/** 基础高度 */
	private int baseH = 0;
	/** 是否显示 */
	private boolean isShow = false;

	public KeyboardShowLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KeyboardShowLinearLayout(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 该方法同样可以使用
		// final int newSpec = MeasureSpec.getSize(heightMeasureSpec);
		// final int oldSpec = getMeasuredHeight();
		// if (oldSpec > newSpec) {
		// if (onKeyboardChangeLinear != null && isShow) {
		// onKeyboardChangeLinear.onKeyboardChange(false);
		// }
		// isShow = false;
		// } else {
		// if (onKeyboardChangeLinear != null && !isShow) {
		// onKeyboardChangeLinear.onKeyboardChange(true);
		// }
		// isShow = true;
		// }
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (baseH == 0) {// 未赋值之前 不回调
			baseH = h;
			return;
		} else if (h == baseH && baseH > oldh) {// 键盘收回了
			if (onKeyboardChangeLinear != null && isShow) {
				onKeyboardChangeLinear.onKeyboardChange(false);
			}
			isShow = false;
		} else if (oldh == baseH && h < baseH) {// 键盘显示了
			if (onKeyboardChangeLinear != null && !isShow) {
				onKeyboardChangeLinear.onKeyboardChange(true);
			}
			isShow = true;
		}
	}

	public void setOnKeyboardChangeLinear(OnKeyboardChangeLinear onKeyboardChangeLinear) {
		this.onKeyboardChangeLinear = onKeyboardChangeLinear;
	}

	public interface OnKeyboardChangeLinear {
		public void onKeyboardChange(boolean isShow);
	}
}