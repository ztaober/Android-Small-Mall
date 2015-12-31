package com.base.wwmm.view.pullview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.base.wwmm.R;

/**
 * 加载视图
 * 
 * <br/>
 * Description: TODO <br/>
 * Author:CodeApe <br/>
 * Version: 1.0 <br/>
 * Date: 2013-12-4 <br/>
 * 
 * @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 * 
 */

public class LoadingLayout extends FrameLayout {
	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;
	/** 刷新圆圈 */
	private final CircleProgressBarView circleProgressBarView;
	/** 加载等待框 */
	private final View head_progressBar;
	/** 加载提示信息 */
	private final TextView headerText;

	private String pullLabel;
	private String refreshingLabel;
	private String releaseLabel;
	private RotateAnimation ra;

	public LoadingLayout(Context context, final int mode, String releaseLabel, String pullLabel, String refreshingLabel) {
		super(context);
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, this);
		headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
		circleProgressBarView = (CircleProgressBarView) header.findViewById(R.id.circleProgress);
		head_progressBar = header.findViewById(R.id.head_progressBar);
		this.releaseLabel = releaseLabel;
		this.pullLabel = pullLabel;
		this.refreshingLabel = refreshingLabel;
		ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(1000);
		LinearInterpolator lin = new LinearInterpolator();
		ra.setInterpolator(lin);
		ra.setRepeatCount(Animation.INFINITE);
	}

	public void reset() {
		headerText.setText(pullLabel);
		circleProgressBarView.setVisibility(View.VISIBLE);
		head_progressBar.setVisibility(View.GONE);
		head_progressBar.clearAnimation();
	}

	public void releaseToRefresh() {
		headerText.setText(releaseLabel);
	}

	public void setPullLabel(String pullLabel) {
		this.pullLabel = pullLabel;
	}

	/**
	 * 显示刷新中的界面
	 * 
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-28,下午1:40:00 <br/>
	 * UpdateTime: 2013-12-28,下午1:40:00 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param refreshingLabel
	 */
	public void refreshing() {
		headerText.setText(refreshingLabel);
		circleProgressBarView.setVisibility(View.INVISIBLE);
		head_progressBar.setVisibility(View.VISIBLE);
		head_progressBar.startAnimation(ra);
	}

	/**
	 * 设置刷新提示文字
	 * 
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-28,下午1:41:12 <br/>
	 * UpdateTime: 2013-12-28,下午1:41:12 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param refreshingLabel
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		this.refreshingLabel = refreshingLabel;
	}

	/**
	 * 显示松开刷新的文字
	 * 
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-28,下午1:41:29 <br/>
	 * UpdateTime: 2013-12-28,下午1:41:29 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param releaseLabel
	 */
	public void setReleaseLabel(String releaseLabel) {
		this.releaseLabel = releaseLabel;
	}

	/**
	 * 显示推动刷新的文本
	 * 
	 * <br/>
	 * Version: 1.0 <br/>
	 * CreateTime: 2013-12-28,下午1:41:59 <br/>
	 * UpdateTime: 2013-12-28,下午1:41:59 <br/>
	 * CreateAuthor: CodeApe <br/>
	 * UpdateAuthor: CodeApe <br/>
	 * UpdateInfo: (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	public void pullToRefresh() {
		headerText.setText(pullLabel);
		// headerImage.clearAnimation();
		// headerImage.startAnimation(resetRotateAnimation);
	}

	public void setTextColor(int color) {
		headerText.setTextColor(color);
	}

	public void setMax(int max) {
		circleProgressBarView.setMax(max);
	}

	public void setProgress(int progress) {
		circleProgressBarView.setProgress(progress);
	}
}
