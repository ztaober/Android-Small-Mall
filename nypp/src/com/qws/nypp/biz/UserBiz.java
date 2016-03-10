package com.qws.nypp.biz;

import java.util.HashMap;

import com.google.gson.reflect.TypeToken;
import com.qws.nypp.bean.ResponseBean;
import com.qws.nypp.bean.UserBean;
import com.qws.nypp.config.ServerConfig;

/**
 * 用户相关的业务
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class UserBiz extends BaseBiz {
	private String LOGIN = "login";
	private String REGIST = "regist";

	public ResponseBean<UserBean> regist(String username, String password) {
		HashMap<String, String> map = getPostHeadMap(REGIST);
		map.put("username", username);
		map.put("password", password);
		return sendPost(ServerConfig.BASE_PATH, 20000, map, new TypeToken<ResponseBean<UserBean>>() {
		});
	}

	public ResponseBean<UserBean> login(String username, String password) {
		HashMap<String, String> map = getPostHeadMap(LOGIN);
		map.put("username", username);
		map.put("password", password);
		return sendPost(ServerConfig.BASE_PATH, 20000, map, new TypeToken<ResponseBean<UserBean>>() {
		});
	}
}
