package com.base.wwmm.view.pullview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.base.wwmm.utils.DisplayUtil;

/**
 * 进度条圈圈
 * 
 * @Description
 * @author
 * @date 2015-9-25
 * @Copyright: Copyright (c) 2015 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class CircleProgressBarView extends View {
	/** 最大角度比:一圈=1f */
	private float maxProgress = 1f;
	/** 30dp宽 使用工具转换 */
	private int width = 30;
	/** 30dp高 使用工具转换 */
	private int height = 30;
	/** 初始角度 */
	private int beginAngle = -180;
	/** 圆的起始XY 使用工具转换 */
	private int ovalBegin = 2;
	/** 圆的结束XY 使用工具转换 */
	private int ovalEnd = 28;
	/** 画笔宽度 使用工具转换 */
	private float strokeWidth = 1.5f;

	private int progress;
	private int max;
	private Paint paint = new Paint();
	private RectF oval;

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		if (progress > max * maxProgress) {
			progress = (int) (max * maxProgress);
		}
		this.progress = progress;
		postInvalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(MeasureSpec.makeMeasureSpec(cg(width), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(cg(height), MeasureSpec.AT_MOST));
	}

	public CircleProgressBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setAntiAlias(true);// 设置是否抗锯齿
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿
		paint.setStrokeWidth(cg(strokeWidth));// 设置画笔宽度
		paint.setStyle(Paint.Style.STROKE);// 设置中空的样式
		paint.setColor(0xffc8c8c8);// 设置画笔为灰色
		oval = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		oval.set(cg(ovalBegin), cg(ovalBegin), cg(ovalEnd), cg(ovalEnd));// 设置类似于左上角坐标（45,45），右下角坐标（155,155），这样也就保证了半径为55
		canvas.drawArc(oval, beginAngle, ((float) progress / max) * 360, false, paint);// 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
	}

	private int cg(float dp) {
		return DisplayUtil.dip2px(getContext(), dp);
	}
}
