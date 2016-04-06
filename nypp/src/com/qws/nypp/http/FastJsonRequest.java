package com.qws.nypp.http;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.JsonObjectRequest;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RestRequest;
import com.yolanda.nohttp.StringRequest;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * <p>自定义请求对象.</p>
 * Created in Feb 1, 2016 8:53:17 AM.
 *
 * @author YOLANDA;
 */
public class FastJsonRequest extends RestRequest<JSONObject> {

    public FastJsonRequest(String url) {
        super(url);
    }

    public FastJsonRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    public JSONObject parseResponse(String url, Headers responseHeaders, byte[] responseBody) {
        String result = StringRequest.parseResponseString(url, responseHeaders, responseBody);
        JSONObject jsonObject = null;
        try {
//            jsonObject = JSON.parseObject(result);
            jsonObject = new JSONObject(result);
        } catch (Throwable e) {
            // 这里默认的错误可以定义为你们自己的协议
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("error", -1);
//            map.put("url", url());
//            map.put("data", "");
//            map.put("method", getRequestMethod().toString());
//            jsonObject = (JSONObject) JSON.toJSON(map);
        }
        return jsonObject;
    }

    @Override
    public String getAccept() {
        // 告诉服务器你接受什么类型的数据, 会添加到请求头的Accept中
        return JsonObjectRequest.ACCEPT;
    }

}
