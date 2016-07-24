package com.qws.nypp.bean;

import java.util.List;

import com.google.gson.Gson;

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
	public double logistics;
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
	
	public GoodsCartBean(){};
	public GoodsCartBean(String image, double logistics, String pid,
			String title, String scId, String couponTitle,
			List<GoodsCartSukBean> sukList, String allPri, int allQua,
			boolean select) {
		super();
		this.image = image;
		this.logistics = logistics;
		this.pid = pid;
		this.title = title;
		this.scId = scId;
		this.couponTitle = couponTitle;
		this.sukList = sukList;
		this.allPri = allPri;
		this.allQua = allQua;
		this.select = select;
	}

	public GoodsCartBean getCartBean(String json){
		Gson gson = new Gson();
		return gson.fromJson(json, GoodsCartBean.class);
	}
	
	public String clone() {
		return new Gson().toJson(this);
	}
}
