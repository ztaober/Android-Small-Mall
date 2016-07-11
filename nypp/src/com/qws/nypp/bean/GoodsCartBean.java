package com.qws.nypp.bean;

import java.util.List;

/**
 * 购物车item
 * @author troy
 *
 */
public class GoodsCartBean extends BaseBean {
	/**
	 * 图片
	 */
	public String image;
	/**
	 * 快递费
	 */
	public String logistics;
	/**
	 * 产品编号
	 */
	public String pid;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 购物车编号
	 */
	public String scId;
	/**
	 * 优惠内容
	 */
	public String couponTitle;
	/**
	 * 详细商品 list
	 */
	public List<GoodsCartSukBean> sukList;
	
	//所有的价格
	public String allPri;
	//所有的件数
	public int allQua;
	//选中状态
	public boolean select;
}
