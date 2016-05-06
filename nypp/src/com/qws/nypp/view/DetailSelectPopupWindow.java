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

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 购买选项的PopupWindow
 */
public class DetailSelectPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
	private LayoutInflater mInflater;
	private GridView colorGv;
	private GridView sizeGv;
	private SkuAdapter skuColorAdapter;// 颜色适配器
	private SkuAdapter skuSizeAdapter;// 尺码适配器
	String color;//
	String size;//
	List<SukBean> mList;// sku数据
	List<SukTypeBean> mColorList;// 颜色列表
	List<SukTypeBean> mSizeList;// 尺码列表
	

    public void initPopupWindow(final Context context, GoodsBean currentGoods, View.OnClickListener listener) {
        if (this.context == null) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
            setContentView(View.inflate(context, R.layout.ppw_detail_select, null));
            TextView title = (TextView) getContentView().findViewById(R.id.ppw_detail_title);
            title.setText("geh");
            title.setOnClickListener(this);
            colorGv = (GridView) getContentView().findViewById(R.id.gv_color);
            sizeGv = (GridView) getContentView().findViewById(R.id.gv_size);
           
            initFlowLayoutData();
            initFlowlayoutView();
           
            
            getContentView().findViewById(R.id.commSelect_view_gap).setOnClickListener(listener);
            //设置SelectPicPopupWindow弹出窗体的宽
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            //设置SelectPicPopupWindow弹出窗体的高
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置SelectPicPopupWindow弹出窗体可点击
            setFocusable(true);
            //设置SelectPicPopupWindow弹出窗体动画效果
            setAnimationStyle(R.style.AnimBottom);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.trans50));
            //设置SelectPicPopupWindow弹出窗体的背景
            setBackgroundDrawable(dw);
        }
    }

//    private void initFlowLayoutData(GoodsDetailBean bean) {
    private void initFlowLayoutData() {
//    	mList = bean.sukList;
    	mList = new ArrayList<SukBean>();
    	SukBean item1 = new SukBean();
		item1.setQuantity(16);
		item1.setColour("红色");
		item1.setSize("M");
		mList.add(item1);
		SukBean item2 = new SukBean();
		item2.setQuantity(16);
		item2.setColour("白色");
		item2.setSize("L");
		mList.add(item2);
		SukBean item3 = new SukBean();
		item3.setQuantity(16);
		item3.setColour("黑色");
		item3.setSize("XL");
		mList.add(item3);
		SukBean item4 = new SukBean();
		item4.setQuantity(16);
		item4.setColour("红色");
		item4.setSize("XL");
		mList.add(item4);
		SukBean item5 = new SukBean();
		item5.setQuantity(16);
		item5.setColour("白色");
		item5.setSize("M");
		mList.add(item5);
		SukBean item6 = new SukBean();
		item6.setQuantity(16);
		item6.setColour("绿色");
		item6.setSize("L");
		mList.add(item6);
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
}
