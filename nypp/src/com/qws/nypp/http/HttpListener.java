package com.qws.nypp.http;

import com.yolanda.nohttp.Response;

/**
 * @author Troy;
 */
public interface HttpListener<T> {

    void onSucceed(int what, Response<T> response);

    void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis);

}
