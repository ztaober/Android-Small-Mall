package com.qws.nypp.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.home.SureOrderActivity;
import com.qws.nypp.adapter.SkuAdapter;
import com.qws.nypp.adapter.SkuAdapter.onItemClickListener;
import com.qws.nypp.bean.GoodsCartBean;
import com.qws.nypp.bean.GoodsCartSukBean;
import com.qws.nypp.bean.GoodsDetailBean;
import com.qws.nypp.bean.SukBean;
import com.qws.nypp.bean.SukTypeBean;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.SkuDataUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * 购买选项的PopupWindow
 */
public class DetailSelectPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private GoodsDetailBean detailBean;
    private String productId;
    private int type;
	private int mCount;
	private GridView colorGv;
	private GridView sizeGv;
	private SkuAdapter skuColorAdapter;// 颜色适配器
	private SkuAdapter skuSizeAdapter;// 尺码适配器
	private String color;//
	private String size;//
	private String warn = "请选择尺码,颜色分类";
	private SukBean	currentSuk;
	private int stock;//
	List<SukBean> mList;// sku数据
	List<SukTypeBean> mColorList;// 颜色列表
	List<SukTypeBean> mSizeList;// 尺码列表
	private ImageView sukImg;
	private TextView moneyTv;
	private TextView stockTv;
	private DisplayImageOptions options;
	private StockChangeView changeView;
	private int addNum;
	
	//type 0购买 1收藏
    public void initPopupWindow(final Context context, GoodsDetailBean goodsDetailBean, int type, String productId) {
        this.context = context;
        this.detailBean = goodsDetailBean;
        this.productId = productId;
        this.type = type;
        options = TApplication.getInstance().getAllOptions(R.drawable.bg_defualt_180);
        setContentView(View.inflate(context, R.layout.ppw_detail_select, null));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(R.style.anim_popup_dir);
        setBackgroundDrawable(new BitmapDrawable());
        new CountDownTimer(175, 25) {
			@Override
			public void onTick(long millisUntilFinished) {
				mCount++;
				backgroundAlpha(context, (float) (1 - (0.1 * mCount)));
			}
			
			@Override
			public void onFinish() {
				
			}
		}.start();
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				backgroundAlpha(context, 1f);
			}
		});
		
		mList = detailBean.sukList;
        initView();
		initFlowLayoutData();
        initFlowlayoutView();
        
    }

    private void initView() {
    	colorGv = (GridView) getContentView().findViewById(R.id.gv_color);
        sizeGv = (GridView) getContentView().findViewById(R.id.gv_size);
       
        sukImg = (ImageView) getContentView().findViewById(R.id.ppw_detail_img);
        ImageLoader.getInstance().displayImage(detailBean.figure.get(0), sukImg, options);
        TextView titleTv = (TextView) getContentView().findViewById(R.id.ppw_detail_title);
        titleTv.setText(detailBean.title);
        moneyTv = (TextView) getContentView().findViewById(R.id.ppw_detail_money);
        moneyTv.setText("¥ "+detailBean.preferentialPrice);
        stockTv = (TextView) getContentView().findViewById(R.id.ppw_detail_quantity);
        stockTv.setText(SkuDataUtil.getAllStock(mList)+"件可售");
        
        changeView = (StockChangeView) getContentView().findViewById(R.id.ppw_detail_change);
        changeView.notifyNum(warn,1);
        
        getContentView().findViewById(R.id.ppw_detail_ok).setOnClickListener(this);
	}

	private void initFlowLayoutData() {
    	mColorList = new ArrayList<SukTypeBean>();
		mSizeList = new ArrayList<SukTypeBean>();
    	Set<String> colorSet = new HashSet<String>(); // color列表
    	Set<String> sizeSet = new HashSet<String>(); // size列表
    	for(int i=0; i<mList.size(); i++){
    		SukBean sukBean = mList.get(i);
    		String color = sukBean.getColour();
    		String size = sukBean.getSize();
    		colorSet.add(color);
    		sizeSet.add(size);
    	}
    	Iterator<String> colorIt = colorSet.iterator();
    	while(colorIt.hasNext()){
    		String color = colorIt.next();
    		SukTypeBean colorBean = new SukTypeBean(color,"1");
			mColorList.add(colorBean);
    	}
    	Iterator<String> sizeIt = sizeSet.iterator();
    	while(sizeIt.hasNext()){
    		String size = sizeIt.next();
    		SukTypeBean sizeBean = new SukTypeBean(size,"1");
    		mSizeList.add(sizeBean);
    	}
	}

	private void initFlowlayoutView() {
		skuColorAdapter = new SkuAdapter(mColorList, context);
		colorGv.setAdapter(skuColorAdapter);
		skuColorAdapter.setItemClickListener(new onItemClickListener() {

			@Override
			public void onItemClick(SukTypeBean bean, int position) {
				// TODO Auto-generated method stub
				color = bean.getName();
				switch (bean.getStates()) {
				case "0":
					// 清空尺码
					mSizeList=SkuDataUtil.clearAdapterStates(mSizeList);
					skuSizeAdapter.notifyDataSetChanged();
					// 清空颜色
					mColorList=SkuDataUtil.clearAdapterStates(mColorList);
					skuColorAdapter.notifyDataSetChanged();
					color = "";
					// 判断使用选中了尺码
					if (!TextUtils.isEmpty(size)) {
						// 选中尺码，计算库存
						stock =SkuDataUtil.getSizeAllStock(mList,size);
						if (stock > 0) {
							stockTv.setText(stock +"件可售");
						}
						warn = "请选择颜色分类";
						changeView.notifyNum(warn,stock);
						// 获取该尺码对应的颜色列表
						List<String> list = SkuDataUtil.getColorListBySize(mList,size);
						if (list != null && list.size() > 0) {
							// 更新颜色列表
							mColorList = SkuDataUtil.setSizeOrColorListStates(mColorList,list, color);
							skuColorAdapter.notifyDataSetChanged();
						}
						mSizeList=SkuDataUtil.setAdapterStates(mSizeList,size);
						skuSizeAdapter.notifyDataSetChanged();
					} else {
						// 所有库存
						stock = SkuDataUtil.getAllStock(mList);
						if (stock > 0) {
							stockTv.setText(stock + "件可售");
						}
						warn = "请选择尺码,颜色分类";
						changeView.notifyNum(warn,stock);
					}
					break;
				case "1":
					// 选中颜色
					mColorList=SkuDataUtil.updateAdapterStates(mColorList,"0", position);
					skuColorAdapter.notifyDataSetChanged();
					// 计算改颜色对应的尺码列表
					List<String> list = SkuDataUtil.getSizeListByColor(mList,color);
					if (!TextUtils.isEmpty(size)) {
						// 计算改颜色与尺码对应的库存
						stock = SkuDataUtil.getStockByColorAndSize(mList,color, size);
						currentSuk = SkuDataUtil.getMateEntity(mList, color, size);
						ImageLoader.getInstance().displayImage(currentSuk.getImage(), sukImg, options);
						moneyTv.setText("¥ "+currentSuk.getMoney());
//						tvSkuName.setText("规格:" + color + " " + size);
						warn = "";
						changeView.notifyNum(warn,stock);
						LogUtil.t("c规格:" + color + " " + size);
						if (stock > 0) {
							stockTv.setText(stock + "件可售");
						}
						if (list != null && list.size() > 0) {
							// 更新尺码列表
							mSizeList = SkuDataUtil.setSizeOrColorListStates(mSizeList,list, size);
							skuSizeAdapter.notifyDataSetChanged();
						}
					} else {
						// 根据颜色计算库存
						stock = SkuDataUtil.getColorAllStock(mList,color);
						String url = SkuDataUtil.getImgByColor(mList, color);
						ImageLoader.getInstance().displayImage(url, sukImg, options);
						if (stock > 0) {
							stockTv.setText(stock + "件可售");
						}
						warn = "请选择尺码";
						changeView.notifyNum(warn,stock);
						if (list != null && list.size() > 0) {
							// 更新尺码列表
							mSizeList = SkuDataUtil.setSizeOrColorListStates(mSizeList,list, "");
							skuSizeAdapter.notifyDataSetChanged();
						}
					}
					break;
				default:
					break;
				}
			}
		});

		skuSizeAdapter = new SkuAdapter(mSizeList, context);
		sizeGv.setAdapter(skuSizeAdapter);
		skuSizeAdapter.setItemClickListener(new onItemClickListener() {

			@Override
			public void onItemClick(SukTypeBean bean, int position) {
				size = bean.getName();
				switch (bean.getStates()) {
				case "0": //选中
					// 清空尺码
					mSizeList=SkuDataUtil.clearAdapterStates(mSizeList);
					skuSizeAdapter.notifyDataSetChanged();
					// 清空颜色
					mColorList=SkuDataUtil.clearAdapterStates(mColorList);
					skuColorAdapter.notifyDataSetChanged();
					size = "";
					if (!TextUtils.isEmpty(color)) {
						// 计算改颜色对应的所有库存
						stock = SkuDataUtil.getColorAllStock(mList,color);
						String url = SkuDataUtil.getImgByColor(mList, color);
						ImageLoader.getInstance().displayImage(url, sukImg, options);
						if (stock > 0) {
							stockTv.setText(stock + "件可售");
						}
						warn = "请选择尺码";
						changeView.notifyNum(warn,stock);
						// 计算改颜色对应的尺码列表
						List<String> list = SkuDataUtil.getSizeListByColor(mList,color);
						if (list != null && list.size() > 0) {
							// 更新尺码列表
							mSizeList = SkuDataUtil.setSizeOrColorListStates(mSizeList,list, size);
							skuSizeAdapter.notifyDataSetChanged();
						}
						mColorList=SkuDataUtil.setAdapterStates(mColorList,color);
						skuColorAdapter.notifyDataSetChanged();
					} else {
						// 获取所有库存
						stock = SkuDataUtil.getAllStock(mList);
						if (stock > 0) {
							stockTv.setText(stock + "件可售");
						}
						warn = "请选择尺码,颜色分类";
						changeView.notifyNum(warn,stock);
					}
					break;
				case "1":
					// 选中尺码
					mSizeList=SkuDataUtil.updateAdapterStates(mSizeList, "0", position);
					skuSizeAdapter.notifyDataSetChanged();
					// 获取该尺码对应的颜色列表
					List<String> list = SkuDataUtil.getColorListBySize(mList,size);
					if (!TextUtils.isEmpty(color)) {
						// 计算改颜色与尺码对应的库存
						stock = SkuDataUtil.getStockByColorAndSize(mList,color, size);
						currentSuk = SkuDataUtil.getMateEntity(mList, color, size);
						ImageLoader.getInstance().displayImage(currentSuk.getImage(), sukImg, options);
						moneyTv.setText("¥ "+currentSuk.getMoney());
//						tvSkuName.setText("规格:" + color + " " + size);
						warn = "";
						changeView.notifyNum(warn,stock);
						LogUtil.t("s规格:" + color + " " + size);
						if (stock > 0) {
							stockTv.setText(stock + "件可售");
						}
						if (list != null && list.size() > 0) {
							// 更新颜色列表
							mColorList = SkuDataUtil.setSizeOrColorListStates(mColorList,list, color);
							skuColorAdapter.notifyDataSetChanged();
						}
					} else {
						// 计算改尺码的所有库存
						stock = SkuDataUtil.getSizeAllStock(mList,size);
						if (stock > 0) {
							stockTv.setText(stock + "件可售");
						}
						warn = "请选择颜色分类";
						changeView.notifyNum(warn,stock);
						if (list != null && list.size() > 0) {
							mColorList =  SkuDataUtil.setSizeOrColorListStates(mColorList,list, "");
							skuColorAdapter.notifyDataSetChanged();
						}
					}
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ppw_detail_ok:
            	if(Util.isFastDoubleClick())
            		break;
            	if(!"".equals(warn)){
            		ToastUtil.show(warn);
            		break;
            	}
			addNum = changeView.getCurrentNum();
            	//type 未0购买 1为加入购物车
            	if(type == 0){
            		if(addNum < detailBean.minimum){
            			ToastUtil.show("您的起批数量不足，无法购买");
            		}else {
            			List<GoodsCartSukBean> selectCartList = new ArrayList<GoodsCartSukBean>();
            			GoodsCartSukBean goodsCartSukBean = new GoodsCartSukBean(currentSuk.getColour(), addNum, 
            					currentSuk.getSize(), detailBean.price, currentSuk.getMoney(), "", currentSuk.getSukId(), true);
            			selectCartList.add(goodsCartSukBean);
            			
            			List<GoodsCartBean> list = new ArrayList<GoodsCartBean>();
            			double allPri = currentSuk.getMoney()*addNum;
            			GoodsCartBean goodsCartBean = new GoodsCartBean(currentSuk.getImage(), detailBean.logistics, 
            					productId, detailBean.title, "", "", selectCartList, Double.toString(allPri) , addNum, true);
            			list.add(goodsCartBean);
            			
            			Bundle bundle = new Bundle();
            			bundle.putInt("quantity", currentSuk.getQuantity());
            			bundle.putInt("minimum", detailBean.minimum);
    					bundle.putSerializable("orderList", (Serializable)list);
    					IntentUtil.gotoActivity(context, SureOrderActivity.class, bundle);
    					this.dismiss();
            		}
            	}else{
            		//添加到进货单
            		insertCart();
            	}
                break;
            default:
                break;
        }
    }
	
	/**
	 * 加入进货单
	 */
	private void insertCart() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.PRODUCT_INSERT_CART);
		JSONObject postJson = null;
		try {
			postJson = new JSONObject();
			postJson.put("sign", TApplication.getInstance().getUserSign());
			postJson.put("memberId", TApplication.getInstance().getMemberId());
			postJson.put("productId", productId);
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("sukId", currentSuk.getSukId());
			object.put("quantity", addNum);
			array.put(object);
			postJson.put("sukList", array);
		} catch (Exception e) {
			return;
		}
		 LogUtil.t(postJson.toString());
		request.setRequestBody(postJson.toString());
		CallServer.getRequestInstance().add(context, 0, request, new HttpListener<JSONObject>() {

			@Override
			public void onSucceed(int what, Response<JSONObject> response) {
				JSONObject result = response.get();// 响应结果

				if("200".equals(result.optString("status"))) {
		            ToastUtil.show("成功加入进货单，快去看看吧~");
		            EventBus.getDefault().post("getOrderData");
				}else{
	                String resultStr = result.optString("declare", "添加进货单失败");
					ToastUtil.show(resultStr);
				}
               
			}

			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int responseCode, long networkMillis) {
				
			}
		}, false, true);
	}
	
	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(Context context, float bgAlpha) {
		WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		((Activity)context).getWindow().setAttributes(lp);
	}
}
