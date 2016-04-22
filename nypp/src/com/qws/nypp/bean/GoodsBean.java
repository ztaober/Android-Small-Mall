package com.qws.nypp.bean;

/**
 * 列表商品信息
 * 
 * @Description
 * @author Troy
 * @date 2016-4-19 下午4:27:04
 * @Copyright:
 */
public class GoodsBean extends BaseBean {
	/**
	 * 
	 */
	private String image;
	/**
	 * 库存数量
	 */
	private int quantity;
	/**
	 * 产品ID
	 */
	private String productId;
	/**
	 * 库存类型 整单 混批
	 */
	private String stockType;
	/**
	 * 原价
	 */
	private int price;
	/**
	 * 促销类型
	 */
	private String couponType;
	/**
	 * 提示文字
	 */
	private String hint;
	/**
	 * 组合形式
	 */
	private String combining;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 现价
	 */
	private int preferentialPrice;
	/**
	 * 销售数量
	 */
	private int soldQuantity;
	/**
	 * 促销标题
	 */
	private String couponTitle;
	
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getStockType() {
		return stockType;
	}
	public void setStockType(String stockType) {
		this.stockType = stockType;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getCombining() {
		return combining;
	}
	public void setCombining(String combining) {
		this.combining = combining;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPreferentialPrice() {
		return preferentialPrice;
	}
	public void setPreferentialPrice(int preferentialPrice) {
		this.preferentialPrice = preferentialPrice;
	}
	public int getSoldQuantity() {
		return soldQuantity;
	}
	public void setSoldQuantity(int soldQuantity) {
		this.soldQuantity = soldQuantity;
	}
	public String getCouponTitle() {
		return couponTitle;
	}
	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle;
	}
	
}
