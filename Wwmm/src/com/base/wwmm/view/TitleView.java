package com.base.wwmm.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.wwmm.R;
import com.base.wwmm.utils.DisplayUtil;

/**
 * 标题栏控件.
 */
public class TitleView extends RelativeLayout {
	/** 上下文环境 */
	private Context context;
	/** 父视图 */
	private View view_Parent;
	/** 左边按钮 */
	private View view_LeftBtn;
	/** 右边按钮 */
	private View view_RightBtn;
	/** 右边按钮 (右边开始算，第二个按钮) */
	private View view_RightBtn_New;

	/** 左边按钮图标 */
	private ImageView img_Left;
	/** 右边按钮图标 */
	private ImageView img_Right;
	/** 右边按钮图标(右边开始算，第二个按钮) */
	private ImageView img_right_new;
	/** 中间图片 */
	private ImageView img_title;

	/** 左边按钮文本 */
	private TextView txt_Left;
	/** 右边按钮文本 */
	private TextView txt_Right;
	/** 标题 */
	private TextView txt_Title;
	/** 标题2 */
	private TextView txt_Title2;

	public TitleView(Context context) {
		super(context);
		init(context);
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 初始化.
	 */
	@SuppressLint("InflateParams")
	private void init(Context context) {
		this.context = context;
		view_Parent = LayoutInflater.from(this.context).inflate(R.layout.view_title, null);
		this.addView(view_Parent);
		view_LeftBtn = view_Parent.findViewById(R.id.view_left);
		view_RightBtn = view_Parent.findViewById(R.id.view_right);
		view_RightBtn_New = view_Parent.findViewById(R.id.view_right_new);

		img_Left = (ImageView) view_Parent.findViewById(R.id.img_left);
		img_Right = (ImageView) view_Parent.findViewById(R.id.img_right);
		img_right_new = (ImageView) view_Parent.findViewById(R.id.img_right_new);
		img_title = (ImageView) view_Parent.findViewById(R.id.img_title);

		txt_Left = (TextView) view_Parent.findViewById(R.id.txt_left);
		txt_Right = (TextView) view_Parent.findViewById(R.id.txt_right);
		txt_Title = (TextView) view_Parent.findViewById(R.id.txt_title);
		txt_Title2 = (TextView) view_Parent.findViewById(R.id.txt_title2);
		view_Parent.setBackgroundColor(context.getResources().getColor(R.color.comm_color));
	}

	/**
	 * 设置标题
	 * 
	 * @param resId 文本资源Id
	 */
	public void setTitle(int resId) {
		txt_Title.setVisibility(View.VISIBLE);
		txt_Title.setText(context.getString(resId));
	}

	/**
	 * 设置标题
	 * 
	 * @param title 标题文本内容
	 */
	public void setTitle(CharSequence title) {
		txt_Title.setVisibility(View.VISIBLE);
		txt_Title.setText(title);
	}

	/**
	 * 设置标题监听事件
	 * 
	 * @param listener
	 */
	public void setOnTitleListener(OnClickListener listener) {
		Drawable drawable = getResources().getDrawable(R.drawable.img_title_arrow_down);
		txt_Title.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
		txt_Title.setCompoundDrawablePadding(DisplayUtil.dip2px(context, 5));
		txt_Title.setOnClickListener(listener);
	}

	/**
	 * 设置标题点击事件的图标
	 * 
	 * @param resId
	 */
	public void setOnTitleRightImage(int resId) {
		Drawable drawable = getResources().getDrawable(resId);
		txt_Title.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
		txt_Title.setCompoundDrawablePadding(DisplayUtil.dip2px(context, 5));
	}

	/**
	 * 设置左边图片按钮
	 * 
	 * @param resId 图片资源id
	 */
	public void setLeftImgBtn(int resId, OnClickListener listener) {
		img_Left.setVisibility(View.VISIBLE);
		img_Left.setImageResource(resId);
		view_LeftBtn.setOnClickListener(listener);
	}

	/**
	 * 设置左边按钮
	 * 
	 * @param text 按钮文本内容
	 * @param listener 按钮监听事件
	 */
	public void setLeftBtn(String text, OnClickListener listener) {
		txt_Left.setVisibility(View.VISIBLE);
		txt_Left.setText(text);
		view_LeftBtn.setOnClickListener(listener);
	}

	/**
	 * 设置左边带箭头按钮
	 * 
	 * @param text 按妞文本内容
	 * @param listener 按钮监听事件
	 */
	public void setLeftArrowBtn(String text, OnClickListener listener) {
		img_Left.setVisibility(View.VISIBLE);
		txt_Left.setVisibility(View.VISIBLE);
		txt_Left.setText(text);
		view_LeftBtn.setOnClickListener(listener);
	}

	/**
	 * 设置右边图片按钮
	 * 
	 * @param resId 图片资源id
	 */
	public void setRightImgBtn(int resId, OnClickListener listener) {
		img_Right.setVisibility(View.VISIBLE);
		img_Right.setImageResource(resId);
		view_RightBtn.setOnClickListener(listener);
	}

	/**
	 * 设置右边图片按钮
	 * 
	 * @param resId 图片资源id
	 */
	public void setRightImgBtn(int resId) {
		img_Right.setVisibility(View.VISIBLE);
		img_Right.setImageResource(resId);
	}

	/**
	 * 设置右边图片按钮
	 * 
	 * @param resId 图片资源id
	 */
	public void setRightImgBtn(int resId, OnLongClickListener listener) {
		img_Right.setVisibility(View.VISIBLE);
		img_Right.setImageResource(resId);
		view_RightBtn.setOnLongClickListener(listener);
	}

	/**
	 * 设置右边按钮
	 * 
	 * @param text 按钮文本内容
	 * @param listener 按钮监听事件
	 */
	public void setRightBtn(String text, OnClickListener listener) {
		txt_Right.setVisibility(View.VISIBLE);
		txt_Right.setText(text);
		view_RightBtn.setOnClickListener(listener);
	}

	/**
	 * 设置右边点击事件
	 * 
	 * @param listener
	 */
	public void setRightBtn(OnClickListener listener) {
		view_RightBtn.setOnClickListener(listener);
	}

	/**
	 * 设置右边按钮(带图片)
	 * 
	 * @param text 按钮文本内容
	 * @param listener 按钮监听事件
	 * @param drawable 图片资源
	 * @param direction 方向(默认左边) 1==left，2==top, 3==right, 4==bottom
	 */
	public void setRightBtn(String text, Drawable drawable, int direction) {
		txt_Right.setVisibility(View.VISIBLE);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		if (direction == 2) {
			txt_Right.setCompoundDrawables(null, drawable, null, null); // 设置左图标
		} else if (direction == 3) {
			txt_Right.setCompoundDrawables(null, null, drawable, null); // 设置左图标
		} else if (direction == 4) {
			txt_Right.setCompoundDrawables(null, null, null, drawable); // 设置左图标
		} else {
			txt_Right.setCompoundDrawables(drawable, null, null, null); // 设置左图标
		}
		txt_Right.setGravity(Gravity.CENTER_VERTICAL);
		txt_Right.setText(text);
	}

	/**
	 * 是否禁用右边试图
	 * 
	 * @param enabled
	 */
	public void setRightEnabled(boolean enabled) {
		txt_Right.setEnabled(enabled);
	}

	/**
	 * 设置右边的背景
	 */
	public void setRightTextColor(int resid) {
		txt_Right.setTextColor(resid);
	}

	/**
	 * 设置右边带箭头按钮
	 * 
	 * @param text 按妞文本内容
	 * @param listener 按钮监听事件
	 */
	public void setRightArrowBtn(String text, OnClickListener listener) {
		img_Right.setVisibility(View.VISIBLE);
		txt_Right.setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = -DisplayUtil.dip2px(context, 15);
		img_Right.setLayoutParams(params);
		txt_Right.setText(text);
		view_RightBtn.setOnClickListener(listener);
	}

	public ImageView getImg_Right() {
		return img_Right;
	}

	/**
	 * 设置返回按钮
	 */
	public void setBackBtn() {
		img_Left.setVisibility(View.VISIBLE);
		view_LeftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showInput(false);
				((Activity) context).finish();
				((Activity) context).overridePendingTransition(R.anim.exit_enter, R.anim.exit_exit);
			}
		});
	}

	/**
	 * 设置右边按钮文本内容
	 * 
	 * @param text 按钮文本内容
	 */
	public void setRightBtnText(String text) {
		view_RightBtn.setVisibility(View.VISIBLE);
		txt_Right.setText(text);
	}

	/**
	 * 设置返回按钮
	 * 
	 * @param listener 返回按钮监听事件
	 */
	public void setBackBtn(OnClickListener listener) {
		img_Left.setVisibility(View.VISIBLE);
		view_LeftBtn.setOnClickListener(listener);

	}

	/**
	 * 设置返回按钮
	 * 
	 * @param text 返回按钮文本
	 */
	public void setBackBtn(String text) {
		txt_Left.setVisibility(View.VISIBLE);
		txt_Left.setText(text);
		view_LeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showInput(false);
				((Activity) context).finish();
				((Activity) context).overridePendingTransition(R.anim.exit_enter, R.anim.exit_enter);
			}
		});
	}

	/**
	 * 设置返回按钮
	 * 
	 * @param text 返回按钮文本
	 * @param listener 返回按钮事件
	 */
	public void setBackBtn(String text, OnClickListener listener) {
		img_Left.setVisibility(View.VISIBLE);
		txt_Left.setVisibility(View.VISIBLE);
		txt_Left.setText(text);
		view_LeftBtn.setOnClickListener(listener);
		((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
	}

	/**
	 * 设置右边按钮是否可有
	 * 
	 * @param enable true 可用 false 不可用
	 */
	public void setRightBtnEnable(boolean enable) {
		view_RightBtn.setEnabled(enable);
	}

	/**
	 * 设置右边按钮显示状态
	 * 
	 * @param visibility View.GONE, View.VISIBLE
	 */
	public void setRightBtnVisibility(int visibility) {
		view_RightBtn.setVisibility(visibility);
	}

	/**
	 * 是否关闭键盘
	 * 
	 * @param true 显示， false 关闭键盘
	 */
	public void showInput(boolean show) {
		try {
			if (show) {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInputFromInputMethod(((Activity) context).getCurrentFocus().getApplicationWindowToken(), 0);
			} else {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getApplicationWindowToken(), 0);
			}
		} catch (NullPointerException e1) {

		} catch (Exception e) {
		}
	}

	/**
	 * 显示副标题
	 * 
	 * @param title 标题内容
	 */
	public void setSecondTitle(String title) {
		txt_Title2.setVisibility(View.VISIBLE);
		txt_Title2.setText(title);
	}

	/**
	 * 设置字体颜色
	 * 
	 * @param color
	 */
	public void setTxtColor(int color) {
		view_Parent.setBackgroundColor(color);

		txt_Right.setTextColor(color);
		txt_Title.setTextColor(color);
	}

	/**
	 * 设置背景颜色
	 * 
	 * @param color
	 */
	public void setBgColor(int color) {
		view_Parent.setBackgroundColor(color);
	}

	/**
	 * 设置右边按钮图标(右边开始算，第二个按钮)
	 * 
	 * @param resId 图片资源id
	 * @param listener 按钮点击事件
	 */
	public void setRightImgNewBtn(int resId, OnClickListener listener) {
		img_right_new.setVisibility(View.VISIBLE);
		img_right_new.setImageResource(resId);
		view_RightBtn_New.setOnClickListener(listener);
	}

	/**
	 * 获取左边按钮
	 * 
	 * @return
	 */
	public TextView getLeftBtnView() {
		return txt_Left;
	}

	/**
	 * 获取右边按钮
	 * 
	 * @return
	 */
	public TextView getRightBtnView() {
		return txt_Right;
	}

	/** 设置中间图片资源 */
	public void setCenterImageRes(int res) {
		img_title.setVisibility(View.VISIBLE);
		img_title.setImageResource(res);
	}

	public void setCenterImageListener(OnClickListener listener) {
		img_title.setOnClickListener(listener);
	}
}
