package com.qws.nypp.bean;

import java.util.List;

/**
 * 商品详情
 * 
 * @Description
 * @author troy
 * @date 2016-4-19 下午4:25:31
 * @Copyright:
 */
public class GoodsDetailBean extends BaseBean {
	
	/**
	 * 轮播图
	 */
	public List<String> figure;
	/**
	 * 产品suk详情
	 */
	public List<SukBean> sukList;
	/**
	 * 最高评价
	 */
	public AppraiseBean appraise;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 成交笔数
	 */
	public int soldQuantity;
	/**
	 * 库存
	 */
	public String quantity;
	/**
	 * 起批数
	 */
	public int minimum;
	/**
	 * 价格
	 */
	public double preferentialPrice;
	/**
	 * 原价
	 */
	public double price;
	/**
	 * 快递费
	 */
	public double logistics;
	/**
	 * 发货地
	 */
	public String harbour;
	/**
	 * 评价数
	 */
	public int appraiseCount;
	/**
	 * 货号
	 */
	public String atrNo;

}
