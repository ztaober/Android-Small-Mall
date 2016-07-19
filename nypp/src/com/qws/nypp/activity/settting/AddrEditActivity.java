package com.qws.nypp.activity.settting;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;

/**
 * 添加新地址
 * 
 * @Description
 * @author troy
 * @date 2016-7-19 下午2:03:12
 * @Copyright:
 */
public class AddrEditActivity extends BaseActivity {

	private EditText nameTv; //请填写收货人姓名
	private EditText phoneTv; // 请填写手机号码
	private EditText zipTv;	 //请填写邮政编码
	private EditText cityTv;	//请选择省市区
	private EditText addrTv;	// 请填写详细收货地址
	private TextView submitTv;	// 请填写详细收货地址
	
	@Override
	protected int getContentViewId() {
		return R.layout.a_addr_edit;
	}

	@Override
	protected void findViews() {
		nameTv = (EditText) findViewById(R.id.addr_edit_name);
		phoneTv = (EditText) findViewById(R.id.addr_edit_phone);
		zipTv = (EditText) findViewById(R.id.addr_edit_zip);
		cityTv = (EditText) findViewById(R.id.addr_edit_city);
		addrTv = (EditText) findViewById(R.id.addr_edit_address);
		submitTv = (TextView) findViewById(R.id.addr_edit_submit);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {
		cityTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		submitTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = nameTv.getText().toString().trim();
				String phone = phoneTv.getText().toString().trim();
				String zipCode = zipTv.getText().toString().trim();
				String addr = addrTv.getText().toString().trim();
			}
		});
	}

	@Override
	protected void getData() {

	}
}