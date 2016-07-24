package com.qws.nypp.view;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qws.nypp.R;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.ToastUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

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
	private String warn = "";
	private String detailId = "";
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
	//品种选择使用
	public void notifyNum(String warn, int maxNum){
		this.num = 1;
		this.maxNum = maxNum;
		this.warn = warn;
		stockTv.setText(num+"");
	}
	//进货单使用
	public void notifyNum(int num, String detailId){
		this.num = num;
		this.maxNum = 10000;
		this.detailId = detailId;
		stockTv.setText(num+"");
	}

	public interface OnNumChangeListenner{
		void changeNum(int num);
	}
	private OnNumChangeListenner listner;
	//确认订单使用
	public void notifyNum(int num, int maxNum , OnNumChangeListenner listner){
		this.num = num;
		this.maxNum = maxNum;
		this.listner = listner;
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
			if(!"".equals(detailId)){
				if(num!=1){
					updateCartDetail(num -1);
				}
				break;	
			}
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
			if(listner!=null){
				listner.changeNum(num);
			}
			break;
		case R.id.view_stock_add:
			if(!"".equals(detailId)){
				updateCartDetail(num +1);
				break;	
			}
			if(!"".equals(warn)){
				ToastUtil.show(warn);
				break;
			}
			num++;
			if(num>maxNum){
				num--;
				ToastUtil.show("超过库存");
				break;
			}
			stockTv.setText(num+"");
			if(listner!=null){
				listner.changeNum(num);
			}
			break;
			
		default:
			break;
		}
	}
	
	private void updateCartDetail(final int changeNum){
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.PRODUCT_UPDATE_CART);
		JSONObject postJson = null;
		try {
			postJson = new JSONObject();
			postJson.put("sign", TApplication.getInstance().getUserSign());
			postJson.put("memberId", TApplication.getInstance().getMemberId());
			postJson.put("detailId", detailId);//购物车详细编号
			postJson.put("quantity", changeNum);//数量
		} catch (Exception e) {
			return;
		}
		request.setRequestBody(postJson.toString());
		CallServer.getRequestInstance().add(c, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();// 响应结果
				if("200".equals(result.optString("status"))) {
	                num = changeNum;
	                stockTv.setText(num+"");
				}else{
					String resultStr = result.optString("declare", "修改失败");
	                ToastUtil.show(resultStr);
				}
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, false);
	}
}
