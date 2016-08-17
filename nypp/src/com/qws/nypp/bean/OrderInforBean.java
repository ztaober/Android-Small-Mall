package com.qws.nypp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表
 * 
 * @Description
 * @author troy
 * @date 2016-7-23 下午2:09:20
 * @Copyright:
 */
public class OrderInforBean extends BaseBean {
	
	/**
	 * 支付状态
	 */
	public boolean payState;
	/**
	 * 物流费用
	 */
	public double logisticsFees;
	/**
	 * 订单号码
	 */
	public String orderNo;
	/**
	 * 订单编号
	 */
	public String orderId;
	/**
	 * 订单状态
	 */
	public int orderStatus;
	/**
	 * 订单金额
	 */
	public double orderAmount;
	public double discountAmount;
	
	/**
	 * 产品列表
	 */
	public List<OrderProBean> products;
	
	/**
	 * 	产品list
	 */
	public List<OrderSukBean> newSukList = new ArrayList<OrderSukBean>();
		
}
