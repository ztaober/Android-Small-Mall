package com.qws.nypp.view;

import com.qws.nypp.R;
import com.qws.nypp.utils.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 购买数量加减控件
 * 
 * @Description
 * @author troy
 * @date 2016-7-4 下午6:31:26
 * @Copyright:
 */
public class StockChangeView extends LinearLayout implements View.OnClickListener {

	private Context c;
	private ImageView reduceIv;
	private ImageView addIv;
	private TextView stockTv;
	private int num = 1;
	private int maxNum = 1;
	private String warn ="";
	public StockChangeView(Context context) {
		super(context);
		init(context);
	}
	
	public StockChangeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public StockChangeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		c = context;
		View view = View.inflate(context, R.layout.view_stock_change, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addView(view);
		reduceIv = (ImageView) findViewById(R.id.view_stock_reduce);
		addIv = (ImageView) findViewById(R.id.view_stock_add);
		findViewById(R.id.view_stock_reduce).setOnClickListener(this);
		findViewById(R.id.view_stock_add).setOnClickListener(this);
		stockTv = (TextView) findViewById(R.id.view_stock_text);
		stockTv.setText(num+"");
	}
	
	public void notifyNum(String warn, int maxNum){
		this.num = 1;
		this.maxNum = maxNum;
		this.warn = warn;
		stockTv.setText(num+"");
	}
	
	public void notifyNum(int num, int maxNum){
		this.num = num;
		this.maxNum = maxNum;
		stockTv.setText(num+"");
	}
	
	public void isChange(boolean canChange){
		reduceIv.setVisibility(canChange ? View.VISIBLE:View.INVISIBLE );
		addIv.setVisibility(canChange ? View.VISIBLE:View.INVISIBLE);
	}
	
	public int getCurrentNum(){
		return num;
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.view_stock_reduce:
			if(!"".equals(warn)){
				ToastUtil.show(warn);
				break;
			}
			num--;
			if(num<1){
				num++;
				break;
			}
			stockTv.setText(num+"");
			break;
		case R.id.view_stock_add:
			if(!"".equals(warn)){
				ToastUtil.show(warn);
				break;
			}
			num++;
			if(num>maxNum){
				num--;
				break;
			}
			stockTv.setText(num+"");
			break;
			
		default:
			break;
		}
	}
}
