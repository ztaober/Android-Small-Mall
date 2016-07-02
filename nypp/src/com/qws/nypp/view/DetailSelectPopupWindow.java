package com.qws.nypp.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qws.nypp.R;
import com.qws.nypp.adapter.SkuAdapter;
import com.qws.nypp.adapter.SkuAdapter.onItemClickListener;
import com.qws.nypp.bean.GoodsBean;
import com.qws.nypp.bean.GoodsDetailBean;
import com.qws.nypp.bean.SukBean;
import com.qws.nypp.bean.SukTypeBean;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.SkuDataUtil;
import com.qws.nypp.view.flowlayout.FlowLayout;
import com.qws.nypp.view.flowlayout.TagAdapter;
import com.qws.nypp.view.flowlayout.TagFlowLayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

/**
 * 购买选项的PopupWindow
 */
public class DetailSelectPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
	private LayoutInflater mInflater;
	private int mCount;
	private GridView colorGv;
	private GridView sizeGv;
	private SkuAdapter skuColorAdapter;// 颜色适配器
	private SkuAdapter skuSizeAdapter;// 尺码适配器
	String color;//
	String size;//
	List<SukBean> mList;// sku数据
	List<SukTypeBean> mColorList;// 颜色列表
	List<SukTypeBean> mSizeList;// 尺码列表
	private ImageView sukImg;
	private TextView titleTv;
	

    public void initPopupWindow(final Context context, List<SukBean> sukList, View.OnClickListener listener) {
        if (this.context == null) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
            setContentView(View.inflate(context, R.layout.ppw_detail_select, null));
            TextView title = (TextView) getContentView().findViewById(R.id.ppw_detail_title);
            title.setText("芯片啊皮夹克哦啊符号额安慰发飞啊飞阿文发恶发疯吉");
            title.setOnClickListener(this);
            colorGv = (GridView) getContentView().findViewById(R.id.gv_color);
            sizeGv = (GridView) getContentView().findViewById(R.id.gv_size);
           
            sukImg = (ImageView) getContentView().findViewById(R.id.ppw_detail_img);
            titleTv = (TextView) getContentView().findViewById(R.id.ppw_detail_title);
            titleTv.setText("");
            
//            getContentView().findViewById(R.id.commSelect_view_gap).setOnClickListener(listener);
            
            
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
    		
    		initFlowLayoutData(sukList);
            initFlowlayoutView();
        }
        
    }

    private void initFlowLayoutData(List<SukBean> sukList) {
    	mList = sukList;
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
    		SukTypeBean colorBean = new SukTypeBean();
			colorBean.setName(color);
			colorBean.setStates("1");
			mColorList.add(colorBean);
    	}
    	Iterator<String> sizeIt = sizeSet.iterator();
    	while(sizeIt.hasNext()){
    		String size = sizeIt.next();
    		SukTypeBean sizeBean = new SukTypeBean();
    		sizeBean.setName(size);
    		sizeBean.setStates("1");
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
//						stock =SkuDataUtil.getSizeAllStock(mList,size);
//						if (stock > 0) {
//							tvSkuStock.setText("库存：" + stock + "");
//						}
//						tvSkuName.setText("请选择尺码");
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
//						stock = SkuDataUtil.getAllStock(mList);
//						if (stock > 0) {
//							tvSkuStock.setText("库存：" + stock + "");
//						}
//						tvSkuName.setText("请选择尺码,颜色分类");
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
//						stock = SkuDataUtil.getStockByColorAndSize(mList,color, size);
//						tvSkuName.setText("规格:" + color + " " + size);
						LogUtil.t("c规格:" + color + " " + size);
//						if (stock > 0) {
//							tvSkuStock.setText("库存：" + stock + "");
//						}
						if (list != null && list.size() > 0) {
							// 更新尺码列表
							mSizeList = SkuDataUtil.setSizeOrColorListStates(mSizeList,list, size);
							skuSizeAdapter.notifyDataSetChanged();
						}
					} else {
						// 根据颜色计算库存
//						stock = SkuDataUtil.getColorAllStock(mList,color);
//						if (stock > 0) {
//							tvSkuStock.setText("库存：" + stock + "");
//						}
//						tvSkuName.setText("请选择尺码");
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
				// TODO Auto-generated method stub
				size = bean.getName();
				switch (bean.getStates()) {
				case "0":
					// 清空尺码
					mSizeList=SkuDataUtil.clearAdapterStates(mSizeList);
					skuSizeAdapter.notifyDataSetChanged();
					// 清空颜色
					mColorList=SkuDataUtil.clearAdapterStates(mColorList);
					skuColorAdapter.notifyDataSetChanged();
					size = "";
					if (!TextUtils.isEmpty(color)) {
						// 计算改颜色对应的所有库存
//						stock = SkuDataUtil.getColorAllStock(mList,color);
//						if (stock > 0) {
//							tvSkuStock.setText("库存：" + stock + "");
//						}
//						tvSkuName.setText("请选择尺码");
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
//						stock = SkuDataUtil.getAllStock(mList);
//						if (stock > 0) {
//							tvSkuStock.setText("库存：" + stock + "");
//						}
//						tvSkuName.setText("请选择尺码,颜色分类");
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
//						stock = SkuDataUtil.getStockByColorAndSize(mList,color, size);
//						tvSkuName.setText("规格:" + color + " " + size);
						LogUtil.t("s规格:" + color + " " + size);
//						if (stock > 0) {
//							tvSkuStock.setText("库存：" + stock + "");
//						}
						if (list != null && list.size() > 0) {
							// 更新颜色列表
							mColorList = SkuDataUtil.setSizeOrColorListStates(mColorList,list, color);
							skuColorAdapter.notifyDataSetChanged();
						}
					} else {
						// 计算改尺码的所有库存
//						stock = SkuDataUtil.getSizeAllStock(mList,size);
//						if (stock > 0) {
//							tvSkuStock.setText("库存：" + stock + "");
//						}
//						tvSkuName.setText("请选择颜色分类");
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
//            case R.id.commSelect_txt_select1:
//                break;
//            case R.id.commSelect_txt_select2:
//                break;
//            case R.id.commSelect_txt_select3:
//                dismiss();
//                break;
//            case R.id.moreSelect_txt_cancel://取消
//                dismiss();
//                break;
        case R.id.ppw_detail_title:
        	break;
            default:
                break;
        }
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
