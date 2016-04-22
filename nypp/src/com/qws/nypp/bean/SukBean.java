package com.qws.nypp.bean;

/**
 * 
 * 
 * @Description
 * @author Troy
 * @date 2016-4-20 上午9:35:06
 * @Copyright:
 */
public class SukBean extends BaseBean {

	/**
	 * 产品suk编号
	 */
	private String sukId;
	/**
	 * 产品suk价格
	 */
	private double money;
	/**
	 * 产品suk大小
	 */
	private String size;
	/**
	 * 产品suk数量
	 */
	private int quantity;
	/**
	 * 产品suk颜色
	 */
	private String colour;
	/**
	 * 产品suk图片
	 */
	private String image;
	
	public String getSukId() {
		return sukId;
	}
	public void setSukId(String sukId) {
		this.sukId = sukId;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

}
