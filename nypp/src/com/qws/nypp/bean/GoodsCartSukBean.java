package com.qws.nypp.bean;

import com.google.gson.Gson;

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
	
	public GoodsCartSukBean(){};
	
	public GoodsCartSukBean(String colour, int quantity, String size,
			double price, double preferentialPrice, String detailId,
			String sukId, boolean select) {
		super();
		this.colour = colour;
		this.quantity = quantity;
		this.size = size;
		this.price = price;
		this.preferentialPrice = preferentialPrice;
		this.detailId = detailId;
		this.sukId = sukId;
		this.select = select;
	}

	public GoodsCartSukBean getCartSukBean(String json){
		Gson gson = new Gson();
		return gson.fromJson(json, GoodsCartSukBean.class);
	}
	
	public String clone() {
		return new Gson().toJson(this);
	}
	
}
