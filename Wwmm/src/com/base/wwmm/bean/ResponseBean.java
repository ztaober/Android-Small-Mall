package com.base.wwmm.bean;

/**
 * 网络请求返回状态属性
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class ResponseBean<T> {
	/** 返回状态码 */
	private String status;
	/** 返回消息 */
	private String info;
	/** 返回数据 */
	private T object;

	public ResponseBean() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public static <T> ResponseBean<T> getSuccessBean() {
		ResponseBean<T> bean = new ResponseBean<T>();
		bean.status = "10000";
		bean.info = "请求成功";
		return bean;
	}

	public static <T> ResponseBean<T> getErrorBean() {
		ResponseBean<T> bean = new ResponseBean<T>();
		bean.status = "9999";
		bean.info = "请求失败";
		return bean;
	}
}
