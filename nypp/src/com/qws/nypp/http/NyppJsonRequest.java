package com.qws.nypp.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.JsonObjectRequest;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RestRequest;
import com.yolanda.nohttp.StringRequest;

/**
 * 定制的请求 setRequestBody json请求 json返回
 * 
 * @author Troy
 *
 */
public class NyppJsonRequest extends RestRequest<JSONObject> {
	
	public static final String ContentType = "application/json";

	public NyppJsonRequest(String url) {
		super(url, RequestMethod.POST);
	}
	/**
	 * 请求类型
	 * 需要配合setRequestBody传json请求
	 */
	@Override
    public String getContentType() {
        return ContentType;
    }

	@Override
	public JSONObject parseResponse(String url, Headers responseHeaders,
			byte[] responseBody) {
		String jsonStr = StringRequest.parseResponseString(url, responseHeaders, responseBody);
        try {
            return new JSONObject(jsonStr);
        } catch (Exception e) {
            try {
                return new JSONObject("{}");
            } catch (JSONException exception) {
            }
        }
        return null;
	}

	@Override
	public String getAccept() {
		// 告诉服务器你接受什么类型的数据, 会添加到请求头的Accept中
        return JsonObjectRequest.ACCEPT;
	}
	
}
