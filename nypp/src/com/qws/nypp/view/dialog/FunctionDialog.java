package com.qws.nypp.view.dialog;

import com.qws.nypp.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class FunctionDialog extends Dialog implements
		android.view.View.OnClickListener {

	private MenuCallback lisenter;

	public FunctionDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.function_dialog);
		getWindow().setGravity(Gravity.CENTER);
		initView();
	}

	private void initView() {
		findViewById(R.id.title).setOnClickListener(this);
		findViewById(R.id.content).setOnClickListener(this);
		findViewById(R.id.line).setOnClickListener(this);
		findViewById(R.id.line2).setOnClickListener(this);
		findViewById(R.id.right_bt).setOnClickListener(this);
		findViewById(R.id.left_bt).setOnClickListener(this);
		findViewById(R.id.center_bt).setOnClickListener(this);
		findViewById(R.id.all_re).setOnClickListener(this);
	}

	private void setMenuCallback(MenuCallback callback) {
		this.lisenter = callback;
	}

	private void showStyle(boolean type,String title, String content,String leftbtStr,String centerbtStr, String rightbtStr) {
		
		if(type == false){
		  findViewById(R.id.left_and_right).setVisibility(View.GONE);	
		  findViewById(R.id.center_bt).setVisibility(View.VISIBLE);
		}else if(type == true){
		  findViewById(R.id.left_and_right).setVisibility(View.VISIBLE);	
		  findViewById(R.id.center_bt).setVisibility(View.GONE);
		}
		((TextView)findViewById(R.id.title)).setText(title);
		if(!"".equals(content)){
			((TextView)findViewById(R.id.content)).setText(content);
		}else{
			((TextView)findViewById(R.id.content)).setVisibility(View.GONE);
		}
		
		((Button)findViewById(R.id.left_bt)).setText(leftbtStr);
		((Button)findViewById(R.id.center_bt)).setText(centerbtStr);
		((Button)findViewById(R.id.right_bt)).setText(rightbtStr);
	}

	@SuppressLint("NewApi")
	public static void show(Context context, boolean type,
			String title, String content,String leftbtStr, String centerbtStr,String rightbtStr,
			 MenuCallback callback) {
		FunctionDialog dialog = new FunctionDialog(context,
				R.style.dialog_style);
		dialog.setMenuCallback(callback);
		dialog.showStyle(type, title, content, leftbtStr, centerbtStr,rightbtStr);
		dialog.show();
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = (int) (size.x*0.8);
		int height = size.y;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = width;
		lp.height = height;
		dialog.getWindow().setAttributes(lp);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.title:
			break;
		case R.id.content:
			break;
		case R.id.line:
			break;
		case R.id.line2:
			break;
		case R.id.all_re:
			lisenter.onMenuResult(id);
			dismiss();
			break;
		default:
			dismiss();
			lisenter.onMenuResult(id);
			break;
		}
	}

}
