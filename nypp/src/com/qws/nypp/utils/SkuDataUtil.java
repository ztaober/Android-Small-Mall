package com.qws.nypp.utils;

import java.util.ArrayList;
import java.util.List;

import com.qws.nypp.bean.SukBean;
import com.qws.nypp.bean.SukTypeBean;

import android.text.TextUtils;
import android.util.Log;

/**
 * 数据处理类
 */
public class SkuDataUtil {
	
	
	/**
	 * 获取所有库存
	 * 
	 * @return
	 */
	public static int getAllStock(List<SukBean> mList) {
		int stock = 0;
		for (SukBean itme : mList) {
			stock += itme.getQuantity();
		}
		return stock;
	}

	/**
	 * 获取库存
	 * 
	 * @param colorStr
	 * @param sizeStr
	 * @return
	 */
	public static int getStockByColorAndSize(List<SukBean> mList,String colorStr, String sizeStr) {
		int stock = 0;
		for (SukBean itme : mList) {
			String color = itme.getColour();
			String size = itme.getSize();
			if (color.equals(colorStr) && size.equals(sizeStr)) {
				stock = itme.getQuantity();
				break;
			}
		}
		return stock;
	}


	/**
	 * 清空状态
	 */
	public static List<SukTypeBean> clearAdapterStates(List<SukTypeBean> mList) {
		int size = mList.size();
		for (int i = 0; i < size; i++) {
			SukTypeBean bean = mList.get(i);
			bean.setStates("1");
			mList.set(i, bean);
		}
		return mList;
	}
	
	
	/**
	 * 设置选中状态
	 * @param postion
	 */
	public static List<SukTypeBean> setAdapterStates(List<SukTypeBean> mList,String key) {
		int size = mList.size();
		for (int i = 0; i < size; i++) {
			SukTypeBean bean = mList.get(i);
			String str=bean.getName();
			if(str.equals(key)){
				bean.setStates("0");
			}else{
				bean.setStates("1");
			}
			mList.set(i, bean);
		}
		return mList;
	}
	
	/**
	 * 获取该颜色的所有库存
	 * 
	 * @param colorStr
	 * @return
	 */
	public static int getColorAllStock(List<SukBean> mList,String str) {
		int stock = 0;
		for (SukBean itme : mList) {
			String color = itme.getColour();
			if (color.equals(str)) {
				stock += itme.getQuantity();
			}
		}
		return stock;
	}
	
	/**
	 * 获取该尺码的所有库存
	 * 
	 * @param colorStr
	 * @return
	 */
	public static int getSizeAllStock(List<SukBean> mList,String sizeStr) {
		int stock = 0;
		for (SukBean itme : mList) {
			String size = itme.getSize();
			if (size.equals(sizeStr)) {
				stock += itme.getQuantity();
			}
		}
		return stock;
	}

	/**
	 * 更新适配器状态
	 * 
	 * @param states
	 */
	public static List<SukTypeBean> updateAdapterStates(List<SukTypeBean> mList,String states, int postion) {
		int size = mList.size();
		for (int i = 0; i < size; i++) {
			SukTypeBean bean = mList.get(i);
			if (i == postion) {
				bean.setStates(states);
			} else {
				if (!bean.getStates().equals("2")) {
					bean.setStates("1");
				}
			}
			mList.set(i, bean);
		}
		return mList;
	}

	/**
	 * 点击颜色后，获取该颜色对应的所有尺码列表
	 * 
	 * @param colorStr
	 */
	public static List<String> getSizeListByColor(List<SukBean> mList,String colorStr) {
		List<String> list = null;
		if (!TextUtils.isEmpty(colorStr)) {
			list = new ArrayList<String>();
			for (SukBean itme : mList) {
				String color = itme.getColour();
				String size = itme.getSize();
				if (color.equals(colorStr)) {
					list.add(size);
				}
			}
		}
		return list;
	}

	/**
	 * 点击尺码后，获取该尺码对应的所有颜色列表
	 * 
	 * @param colorStr
	 */
	public static List<String> getColorListBySize(List<SukBean> mList,String sizeStr) {
		List<String> list = null;
		list = new ArrayList<String>();
		for (SukBean itme : mList) {
			String color = itme.getColour();
			String size = itme.getSize();
			if (size.equals(sizeStr)) {
				list.add(color);
			}
		}
		return list;
	}
	/**
	 * @param mList 尺码列表/颜色列表
	 * @param list  该颜色对应的尺码列表/颜色列表
	 * @param key 之前选中的尺码/颜色
	 * @return
	 */
	public static List<SukTypeBean> setSizeOrColorListStates(List<SukTypeBean> mList,List<String> list, String key) {
		int size = mList.size();
		List<SukTypeBean> list2 = new ArrayList<SukTypeBean>();
		for (int i = 0; i < size; i++) {
			SukTypeBean bean = mList.get(i);
			String name = bean.getName();
			for (String str : list) {
				if (name.equals(str)) {
					bean.setStates("1");
					if (key.equals(str)) {
						bean.setStates("0");
					}
					break;
				} else {
					bean.setStates("2");
				}
			}
			list2.add(bean);
		}
		return list2;
	}

	

	

}
