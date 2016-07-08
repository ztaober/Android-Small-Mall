package com.qws.nypp.view;

import com.qws.nypp.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingView extends RelativeLayout{

	private LinearLayout mBgLayout;
	private TextView mTipTxt;
	private Button mReloadBT;
	private ProgressBar mLoadingBar;
	
	public enum LoadingMode {
		LOADING, LOADING_SUCCESS, LOADING_FAILED
	}
	
	public LoadingView(Context context) {
		this(context, null);
	}
	
	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}

	private void init() {
		View view = View.inflate(getContext(), R.layout.view_loading_tips, null);
		
		addView(view);
		
		mBgLayout = (LinearLayout) view.findViewById(R.id.view_loading_bg_layout);
		mTipTxt = (TextView) view.findViewById(R.id.view_loading_tip_txt);
		mReloadBT = (Button) view.findViewById(R.id.view_loading_reload_bt);
		mLoadingBar = (ProgressBar) view.findViewById(R.id.view_loading_bar);
		
		setVisibility(View.GONE);
	}

	/**
	 * 设置网络失败时显示的提示文字
	 * @param message
	 */
	public void setTipMessage(String message) {
		mTipTxt.setText(message);
	}
	
	/**
	 * 给重新加载按钮设置点击事件
	 * @param l
	 */
	public void setReloadBtListenner(OnClickListener l) {
		mReloadBT.setOnClickListener(l);
	}
	
	/**
	 * 加载模式 
	 * LOADING	正在加载, 
	 * LOADING_SUCCESS	加载成功, 
	 * LOADING_FAILED	加载失败,
	 * @param mode
	 */
	public void setLoadingMode(LoadingMode mode) {
		if(LoadingMode.LOADING == mode) {
			// 正在加载
			setVisibility(View.VISIBLE);
			mBgLayout.setBackgroundColor(Color.WHITE);
			mBgLayout.setVisibility(View.VISIBLE);
			mLoadingBar.setVisibility(View.VISIBLE);
			mTipTxt.setVisibility(View.GONE);
			mReloadBT.setVisibility(View.GONE);
		} else if(LoadingMode.LOADING_SUCCESS == mode) {
			// 加载成功
			setVisibility(View.GONE);
			mBgLayout.setBackgroundColor(Color.TRANSPARENT);
			mBgLayout.setVisibility(View.GONE);
			mLoadingBar.setVisibility(View.GONE);
			mTipTxt.setVisibility(View.GONE);
			mReloadBT.setVisibility(View.GONE);
		} else if(LoadingMode.LOADING_FAILED == mode) {
			// 加载失败
			setVisibility(View.VISIBLE);
			mBgLayout.setBackgroundColor(Color.WHITE);
			mBgLayout.setVisibility(View.VISIBLE);
			mLoadingBar.setVisibility(View.GONE);
			mTipTxt.setVisibility(View.VISIBLE);
			mReloadBT.setVisibility(View.VISIBLE);
		}
	}
	
}
