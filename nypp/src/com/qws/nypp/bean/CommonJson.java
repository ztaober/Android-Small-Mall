package com.qws.nypp.bean;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CommonJson<T> extends BaseBean {

	/**
	 * 状态码
	 */
	private String status;
	/**
	 * 说明
	 */
	private String declare;
	/**
	 * 时间戳
	 */
	private String timestamp;
	/**
	 * 数据集合
	 */
	private T data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeclare() {
		return declare;
	}

	public void setDeclare(String declare) {
		this.declare = declare;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public static CommonJson fromJson(String json, Class clazz) {  
        Gson gson = new Gson();  
        Type objectType = new TypeToken<CommonJson>() {}.getType();  
        return gson.fromJson(json, objectType);  
    }  

//	public static CommonJson fromJson(String json, Class clazz) {
//		Gson gson = new Gson();
//		Type objectType = type(CommonJson.class, clazz);
//		return gson.fromJson(json, objectType);
//	}

	public String toJson(Class<T> clazz) {
		Gson gson = new Gson();
//		Type objectType = type(CommonJson.class, clazz);
		Type objectType = new TypeToken<CommonJson>() {}.getType();  
		return gson.toJson(this, objectType);
	}

//	static ParameterizedType type(final Class raw, final Type... args) {
//		return new ParameterizedType() {
//			public Type getRawType() {
//				return raw;
//			}
//
//			public Type[] getActualTypeArguments() {
//				return args;
//			}
//
//			public Type getOwnerType() {
//				return null;
//			}
//		};
//	}

}
