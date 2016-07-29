package com.qws.nypp.view;

import com.qws.nypp.R;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 弹出选择头像
 * 
 * @Description
 * @author troy
 * @date 2016-7-29 下午1:39:49
 * @Copyright:
 */
public class SelectPicPopupWindow extends PopupWindow implements OnClickListener {

	private int mCount;
	
	public SelectPicPopupWindow(final Activity context, OnClickListener itemsOnClick) {
		super(context);

		final View popupView = View.inflate(context, R.layout.view_choose_head_image_layout, null);
		popupView.findViewById(R.id.pop_choose_carme_txt).setOnClickListener(itemsOnClick);
		popupView.findViewById(R.id.pop_choose_library_txt).setOnClickListener(itemsOnClick);
		// 取消按钮
		popupView.findViewById(R.id.pop_choose_cancel_txt).setOnClickListener(this);

		// 设置SelectPicPopupWindow的View
		this.setContentView(popupView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.anim_popup_dir);
		setBackgroundDrawable(new BitmapDrawable());

		new CountDownTimer(175, 25) {
			@Override
			public void onTick(long millisUntilFinished) {
				mCount++;
				backgroundAlpha(context, (float) (1 - (0.1 * mCount)));
			}
			
			@Override
			public void onFinish() {
				
			}
		}.start();

		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				backgroundAlpha(context, 1f);
			}
		});

	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(Activity context, float bgAlpha) {
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		context.getWindow().setAttributes(lp);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_choose_cancel_txt:
			// 销毁弹出框
			dismiss();
			break;

		}
	}

}