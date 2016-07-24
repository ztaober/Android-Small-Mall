package com.qws.nypp.bean;

import java.util.List;

/**
 * 产品列表
 * 
 * @Description
 * @author troy
 * @date 2016-7-23 下午2:18:33
 * @Copyright:
 */
public class OrderProBean extends BaseBean {
	
	/**
	 * 	产品ID
	 */
	public String prodrctId;
	/**
	 * 	产品标题
	 */
	public String title;
	/**
	 * 	产品list
	 */
	public List<OrderSukBean> sukList;

}
