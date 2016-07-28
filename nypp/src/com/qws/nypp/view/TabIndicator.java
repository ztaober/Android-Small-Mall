package com.qws.nypp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.qws.nypp.R;
import com.qws.nypp.utils.DisplayUtil;

/**
 * Tab栏目
 * 
 * @Description 使用initData(String[] tabArgs)
 * @author Troy
 * @date 2016-4-27 下午2:29:14
 * @Copyright:
 */
public class TabIndicator extends RelativeLayout {
	
	private Context c;
	/** tab文字容器 */
	private RadioGroup tabTextGroup;
	/** tab底部图片容器 */
	private LinearLayout tabImgLinear;
	/** 监听 */
	private OnTabChangeListener listener;
	/** 选中角标 */
	private int currentCheck = 0;
	/** 动画操作的img */
	private ImageView sliderImg;
	private int LineSize = 30;

	public TabIndicator(Context context) {
		super(context);
		init(context);
	}

	public TabIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TabIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	/** TabIndicator监听器 */
	public interface OnTabChangeListener{
		public abstract void OnTabChange(int position);
	}
	
	/** 监听TabIndicator */
	public void setTabListener(OnTabChangeListener listener){
		this.listener = listener;
	}

	private void init(Context context) {
		c = context;
		View view = View.inflate(context, R.layout.view_tab_indicator, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		addView(view);
		tabTextGroup = (RadioGroup) findViewById(R.id.tab_text_group);
		tabImgLinear = (LinearLayout) findViewById(R.id.tab_img_ll);
	}
	
	public void setLineSize(int LineSize){
		this.LineSize = LineSize;
	}
	
	public void initData(String[] tabArgs) {
		if (tabArgs.length == 0) {
			return;
		}
		tabTextGroup.removeAllViews();
		tabImgLinear.removeAllViews();
		
		for (int i = 0; i < tabArgs.length; i++) {
			RadioButton textBtn = new RadioButton(c);
			textBtn.setButtonDrawable(android.R.color.transparent);
			textBtn.setText(tabArgs[i]);
			textBtn.setTextSize(14);
			textBtn.setId(i);
			int padding = DisplayUtil.dip2px(c, 10);
			textBtn.setPadding(padding, padding, padding, padding);
			if(i != 0){
				textBtn.setTextColor(getResources().getColor(R.color.opt_gray));
			} else {
				textBtn.setTextColor(getResources().getColor(R.color.comm_color));
			}
			textBtn.setGravity(Gravity.CENTER);  
			RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
			tabTextGroup.addView(textBtn, i, params);
		}
		
		for (int i = 0; i < tabArgs.length; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(c, 2), 1.0f);
			if(i == 0){
				sliderImg = new ImageView(c);
				sliderImg.setPadding(DisplayUtil.dip2px(c, LineSize), 0, DisplayUtil.dip2px(c, LineSize), 0);
				sliderImg.setImageResource(R.color.comm_color);
				tabImgLinear.addView(sliderImg, i, params);
			} else {
				ImageView img = new ImageView(c);
				img.setPadding(DisplayUtil.dip2px(c, LineSize), 0, DisplayUtil.dip2px(c, LineSize), 0);
				tabImgLinear.addView(img, i, params);
			}
		}
		
		setListener();
	}
	

	private void setListener() {
		tabTextGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				/** 触发回调 */
				listener.OnTabChange(checkedId);
				
				int fromX = currentCheck * sliderImg.getWidth();
				int toX = checkedId * sliderImg.getWidth();
				currentCheck = checkedId;
				translateSlider(fromX,toX);
				
				int childCount = group.getChildCount();
				for(int i=0; i<childCount; i++){
					RadioButton btn = (RadioButton)tabTextGroup.getChildAt(i);
					if(i == checkedId){
						btn.setTextColor(getResources().getColor(R.color.comm_color));
					} else {
						btn.setTextColor(getResources().getColor(R.color.opt_gray));
					}
				}
			}
		});
		
	}

	private void translateSlider(int fromXDelta, int toXDelta) {
		TranslateAnimation animation = new TranslateAnimation(fromXDelta, toXDelta, 0, 0);
		animation.setDuration(200);
		animation.setFillAfter(true);
		sliderImg.startAnimation(animation);
	}
	

}
