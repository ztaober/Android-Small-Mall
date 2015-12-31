package com.base.wwmm.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.base.wwmm.R;

/**
 * 自定义文本框
 * 
 * @Description
 * @author 谷世勇/Geoff
 * @date 2015年6月23日
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class DeleteEditText extends EditText implements OnFocusChangeListener, TextWatcher {

	/** 删除按钮的引用 */
	private Drawable mClearDrawable;
	/** 控件是否有焦点 */
	private boolean hasFoucs;
	/** 文本监听 */
	private OnTextChangeListener onTextChangeListener;

	public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
		this.onTextChangeListener = onTextChangeListener;
	}

	private OnDeleteClickListener onDeleteClickListener;

	public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
		this.onDeleteClickListener = onDeleteClickListener;
	}

	public DeleteEditText(Context context) {
		this(context, null);
	}

	public DeleteEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public DeleteEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(R.drawable.selector_bg_edit_del);
		}

		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
		// 默认设置隐藏图标
		setClearIconVisible(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
	}

	/**
	 * 
	 * 设置点击事件
	 * 
	 * @version 1.0
	 * @createTime 2015年6月23日,下午3:58:17
	 * @updateTime 2015年6月23日,下午3:58:17
	 * @createAuthor 谷世勇/Geoff
	 * @updateAuthor 谷世勇/Geoff
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {

				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));

				if (touchable) {
					this.setText("");
					if (onDeleteClickListener != null) {
						onDeleteClickListener.onDeleteClick();
					}
				}
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 * 
	 * @version 1.0
	 * @createTime 2015年6月23日,下午3:59:20
	 * @updateTime 2015年6月23日,下午3:59:20
	 * @createAuthor 谷世勇/Geoff
	 * @updateAuthor 谷世勇/Geoff
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param v
	 * @param hasFocus
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	/**
	 * 
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @version 1.0
	 * @createTime 2015年6月23日,下午3:59:30
	 * @updateTime 2015年6月23日,下午3:59:30
	 * @createAuthor 谷世勇/Geoff
	 * @updateAuthor 谷世勇/Geoff
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 
	 * 当输入框里面内容发生变化的时候回调的方法
	 * 
	 * @version 1.0
	 * @createTime 2015年6月23日,下午3:59:39
	 * @updateTime 2015年6月23日,下午3:59:39
	 * @createAuthor 谷世勇/Geoff
	 * @updateAuthor 谷世勇/Geoff
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param s
	 * @param start
	 * @param count
	 * @param after
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFoucs) {
			setClearIconVisible(s.length() > 0);
		}

		if (onTextChangeListener != null) {
			onTextChangeListener.onTextChanged(s, start, count, after);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (onTextChangeListener != null) {
			onTextChangeListener.beforeChange(s, start, count, after);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (onTextChangeListener != null) {
			onTextChangeListener.afterChange(s);
		}
	}

	/**
	 * 
	 * 设置晃动动画
	 * 
	 * @version 1.0
	 * @createTime 2015年6月23日,下午3:59:47
	 * @updateTime 2015年6月23日,下午3:59:47
	 * @createAuthor 谷世勇/Geoff
	 * @updateAuthor 谷世勇/Geoff
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	/**
	 * 
	 * 晃动动画
	 * 
	 * @version 1.0
	 * @createTime 2015年6月23日,下午3:59:54
	 * @updateTime 2015年6月23日,下午3:59:54
	 * @createAuthor 谷世勇/Geoff
	 * @updateAuthor 谷世勇/Geoff
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * @param counts
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	/**
	 * 删除回调
	 * 
	 * @Description TODO
	 * @author 谷世勇/Geoff
	 * @date 2015年6月23日
	 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
	 */
	public interface OnDeleteClickListener {
		public void onDeleteClick();
	}

	/**
	 * 文本变化监听
	 * 
	 * @Description TODO
	 * @author 谷世勇/Geoff
	 * @date 2015年6月23日
	 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
	 */
	public interface OnTextChangeListener {
		public void beforeChange(CharSequence s, int start, int count, int after);

		public void afterChange(Editable s);

		public void onTextChanged(CharSequence s, int start, int count, int after);
	}
}
