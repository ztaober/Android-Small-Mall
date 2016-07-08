package com.qws.nypp.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.qws.nypp.R;
import com.qws.nypp.bean.CommonResult4List;
import com.qws.nypp.bean.GoodsCartBean;
import com.qws.nypp.bean.GoodsCartSukBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.yolanda.nohttp.Response;

import android.view.View;
import android.widget.ListView;

public class OrderFragment extends BaseFragment {
	
	private List<GoodsCartBean> cartList;
	private View noOrder;
	private ListView orderLv;

	@Override
	protected View getViews() {
		return View.inflate(context, R.layout.f_order, null);
	}

	@Override
	protected void findViews() {
		noOrder = findViewById(R.id.no_order);
		orderLv = findViewById(R.id.order_listview);
	}

	@Override
	protected void initData() {
		titleView.setTitle("进货单");
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {
		NyppJsonRequest request = new NyppJsonRequest(ServerConfig.PRODUCT_GET_CART);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", "1");
		postData.put("memberId", "59BA82FE3CD711E691F700163E022948");
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();
				CommonResult4List<GoodsCartBean> result4List = CommonResult4List.fromJson(result.toString(), GoodsCartBean.class);
				List<GoodsCartBean> allCartList = result4List.getData();
				
				if(cartList != null){
					cartList.clear();
				}
				
				for(GoodsCartBean cartBean : allCartList){
					List<GoodsCartSukBean> sukList = cartBean.sukList;
					if (sukList.size() > 0) 
						cartList.add(cartBean);
				}
				if(cartList.size() == 0){
					noOrder.setVisibility(View.GONE);
				}else{
					noOrder.setVisibility(View.INVISIBLE);
				}
				
			}

			@Override
			public void onFailed(int what, String url, Object tag, Exception exception, int responseCode,
					long networkMillis) {
				
			}
		}, false, true);
		
	}

}
