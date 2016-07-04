package com.qws.nypp.view;

import com.qws.nypp.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	private TextView stockTv;
	private int num = 1;
	private int maxNum = 1;
	
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
		findViewById(R.id.view_stock_reduce).setOnClickListener(this);
		findViewById(R.id.view_stock_add).setOnClickListener(this);
		stockTv = (TextView) findViewById(R.id.view_stock_text);
		stockTv.setText(num+"");
	}
	
	private void notifyNum(int maxNum){
		this.num = 1;
		this.maxNum = maxNum;
		stockTv.setText(num+"");
	}
	
	private OnStockChangeListener listner;
	
	public interface OnStockChangeListener{
		void onStockChange(int num);
	}
	
	public void setListner(OnStockChangeListener listener){
		this.listner = listener;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.view_stock_reduce:
			num--;
			stockTv.setText(num+"");
			break;
		case R.id.view_stock_add:
			num++;
			stockTv.setText(num+"");
			break;
			
		default:
			listner.onStockChange(num);	
			break;
		}
	}
}
