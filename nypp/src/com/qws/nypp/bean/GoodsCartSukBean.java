package com.qws.nypp.bean;

/**
 * 购物车item
 * @author troy
 *
 */
public class GoodsCartSukBean extends BaseBean {
	/**
	 * 颜色规格
	 */
	public String colour;
	/**
	 * 数量
	 */
	public int quantity;
	/**
	 * 大小
	 */
	public String size;
	/**
	 * 折扣前价格
	 */
	public double price;
	/**
	 * 实际价格
	 */
	public double preferentialPrice;
	/**
	 * 详细编号
	 */
	public String detailId;
	/**
	 * suk编号
	 */
	public String sukId;
	//选中状态
	public boolean select;

	
}
