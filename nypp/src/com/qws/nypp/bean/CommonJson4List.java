package com.qws.nypp.bean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;


public class CommonJson4List<T> extends BaseBean{

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
    private List<T> data;
    

//    public static CommonJson4List fromJson(String json, Class clazz) {
//        Gson gson = new Gson();
//        Type objectType = type(CommonJson4List.class, clazz);
//        return gson.fromJson(json, objectType);
//    }
    
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

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

    public static CommonJson4List fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(CommonJson4List.class, clazz);
        return gson.fromJson(json, objectType);
    }

    public String toJson(Class<T> clazz) {
        Gson gson = new Gson();
        Type objectType = type(CommonJson4List.class, clazz);
        return gson.toJson(this, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

}