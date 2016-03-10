package com.qws.nypp.biz;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qws.nypp.bean.ResponseBean;
import com.qws.nypp.utils.NetUtil;

/**
 * 业务基类
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class BaseBiz {
	private static String SERVER_METHOD_KEY = "method";

	/**
	 * 获取请求方法参数集合
	 * 
	 * @updateTime 2015-6-22,下午10:45:59
	 * @updateAuthor qw
	 * @param method
	 * @return
	 */
	protected HashMap<String, String> getPostHeadMap(String method) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("key", "1");
		map.put(SERVER_METHOD_KEY, method);
		return map;
	}

	protected <T> ResponseBean<T> sendPost(String path, int timeout, HashMap<String, String> attribute, TypeToken<ResponseBean<T>> type) {
		ResponseBean<T> bean = ResponseBean.getErrorBean();
		try {
			String body = new NetUtil().sendPost(path, timeout, attribute);
			bean = new Gson().fromJson(body, type.getType());
		} catch (IOException e) {
			e.printStackTrace();
			bean.setInfo("连接异常");
		} catch (TimeoutException e) {
			e.printStackTrace();
			bean.setInfo("连接超时");
		} catch (Exception e) {
			e.printStackTrace();
			bean.setInfo("数据异常");
		}
		return bean;
	}
}
