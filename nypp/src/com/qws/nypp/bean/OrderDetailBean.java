package com.qws.nypp.bean;

import java.util.ArrayList;
import java.util.List;


public class OrderDetailBean extends BaseBean {
	
	/**
	 * 支付状态
	 */
	public boolean payState;
	/**
	 * 订单状态
	 */
	public int orderStatus;
	/**
	 * 订单号码
	 */
	public String orderNo;
	/**
	 * 订单编号
	 */
	public String orderId;
	/**
	 * 物流费用
	 */
	public double logisticsFees;
	/**
	 * 订单金额
	 */
	public double orderAmount;
	/**
	 * 优惠金额
	 */
	public double discountAmount;
	/**
	 * 姓名
	 */
	public String contactName;
	/**
	 * 电话
	 */
	public String contactPhone;
	/**
	 * 省
	 */
	public String provinceName;
	/**
	 * 市
	 */
	public String cityName;
	/**
	 * 区
	 */
	public String districtName;
	/**
	 * 详细地址
	 */
	public String address;
	/**
	 * 买家留言
	 */
	public String message;
	/**
	 * 商品列表
	 */
	public List<OrderProductsDetailBean> ordersProducts;
	/**
	 * 详情集合
	 */
	public List<OrderDetailSukBean> newDetails = new ArrayList<OrderDetailSukBean>();

}
