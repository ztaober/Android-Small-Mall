package com.qws.nypp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qws.nypp.R;

/**
 * 首页底部按钮,看情况也可以使用RadioButton
 * 
 * @Description
 * @author 綦巍
 * @date 2015-8-14
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class MyRadioView extends RelativeLayout {
	private TextView text;
	private ImageView img;
	private int img0Res;
	private int img1Res;
	public boolean isCheck = false;

	public MyRadioView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public MyRadioView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		View contentView = View.inflate(context, R.layout.view_my_radio, null);
		text = (TextView) contentView.findViewById(R.id.myRadio_txt);
		img = (ImageView) contentView.findViewById(R.id.myRadio_img);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyRadioView);
		text.setText(getText(a, R.styleable.MyRadioView_text));
		text.setTextColor(getResources().getColor(R.color.all_txt_32));
		text.setEnabled(false);
		img1Res = a.getResourceId(R.styleable.MyRadioView_img1, R.drawable.trans);
		img0Res = a.getResourceId(R.styleable.MyRadioView_img0, R.drawable.trans);
		img.setImageResource(img0Res);
		addView(contentView);
		a.recycle();
	}

	private String getText(TypedArray a, int index) {
		String txt = a.getString(index);
		if (txt == null) {
			return "";
		}
		return txt;
	}

	/**
	 * 设置状态
	 * 
	 * @updateTime 2015-6-27,下午10:42:37
	 * @updateAuthor qw
	 * @param check
	 * @return
	 */
	public void setCheck(boolean check) {
		if (isCheck == check) {
			return;
		}
		isCheck = check;
		text.setEnabled(check);
		img.setImageResource(isCheck ? img1Res : img0Res);
		if (check) {
			text.setTextColor(getResources().getColor(R.color.all_txt_32));
		} else {
			text.setTextColor(getResources().getColor(R.color.all_txt_32));
		}
	}

	/**
	 * 设置文本
	 */
	public void setText(String value) {
		text.setText(value);
	}

	/** 设置资源 */
	public void setImgRes(int img0Res, int img1Res) {
		this.img0Res = img0Res;
		this.img1Res = img1Res;
		img.setImageResource(isCheck ? img1Res : img0Res);
	}
}