package com.qws.nypp.http;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;

import com.qws.nypp.utils.AppManager;
import com.qws.nypp.utils.ProcessDialogUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.view.dialog.WaitDialog;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yolanda.nohttp.error.ClientError;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;

import de.greenrobot.event.EventBus;

public class HttpResponseListener<T> implements OnResponseListener<T> {

    /**
     * Dialog.
     */
//    private WaitDialog mWaitDialog;
	private Context context;
	private boolean canCancel;
    private Request<?> mRequest;

    /**
     * 结果回调.
     */
    private HttpListener<T> callback;

    /**
     * 是否显示dialog.
     */
    private boolean isLoading;

    /**
     * @param context      context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListener(Context context, Request<?> request, HttpListener<T> httpCallback, boolean canCancel, boolean isLoading) {
        this.mRequest = request;
        this.context = context;
        this.canCancel = canCancel;
//        if (context != null && isLoading) {
//            mWaitDialog = new WaitDialog(context);
//            mWaitDialog.setCancelable(canCancel);
//            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    mRequest.cancel(true);
//                }
//            });
//        }
        this.callback = httpCallback;
        this.isLoading = isLoading;
    }

    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
//        if (isLoading && mWaitDialog != null && !mWaitDialog.isShowing())
//            mWaitDialog.show();
        if (context != null && isLoading) {
  			ProcessDialogUtil.showDialog(context, "加载中", canCancel);
  			ProcessDialogUtil.setOnDismissListener(new OnDismissListener() {

  				@Override
  				public void onDismiss(DialogInterface dialog) {
  					 mRequest.cancel(true);
  				}
  			});
      }
    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
//        if (isLoading && mWaitDialog != null && mWaitDialog.isShowing())
//            mWaitDialog.dismiss();
    	ProcessDialogUtil.dismissDialog();
    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        if (callback != null){
        	try{
        		JSONObject json = (JSONObject) response.get();
//        		挤下线功能
            	if("204".equals(json.optString("status"))) {
            		AppManager.getAppManager().AppExit(context, false);
            		EventBus.getDefault().post("LogonInvalid");
            		return;
            	}
        	}catch(Exception e){
        	}
        	callback.onSucceed(what, response);
        }
           
    }

    /**
     * 失败回调.
     */
    @Override
    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        if (exception instanceof ClientError) {// 客户端错误
            if (responseCode == 400) {//服务器未能理解请求。
                ToastUtil.show("错误的请求，服务器表示不能理解。");
            } else if (responseCode == 403) {// 请求的页面被禁止
            	ToastUtil.show("错误的请求，服务器表示不愿意。");
            } else if (responseCode == 404) {// 服务器无法找到请求的页面
            	ToastUtil.show("错误的请求，服务器表示找不到。");
            } else {// 400-417都是客户端错误，开发者可以自己去查询噢
            	ToastUtil.show("错误的请求，服务器表示伤不起。");
            }
        } else if (exception instanceof ServerError) {// 服务器错误
            if (500 == responseCode) {
            	ToastUtil.show("服务器遇到不可预知的情况。");
            } else if (501 == responseCode) {
            	ToastUtil.show("服务器不支持的请求。");
            } else if (502 == responseCode) {
            	ToastUtil.show("服务器从上游服务器收到一个无效的响应。");
            } else if (503 == responseCode) {
            	ToastUtil.show("服务器临时过载或当机。");
            } else if (504 == responseCode) {
            	ToastUtil.show("网关超时。");
            } else if (505 == responseCode) {
            	ToastUtil.show("服务器不支持请求中指明的HTTP协议版本。");
            } else {
            	ToastUtil.show("服务器脑子有问题。");
            }
        } else if (exception instanceof NetworkError) {// 网络不好
        	ToastUtil.show("请检查网络。");
        } else if (exception instanceof TimeoutError) {// 请求超时
        	ToastUtil.show("请求超时，网络不好或者服务器不稳定。");
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
        	ToastUtil.show("未发现指定服务器。");
        } else if (exception instanceof URLError) {// URL是错的
        	ToastUtil.show("URL错误。");
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
        	ToastUtil.show("没有发现缓存。");
        } else {
        	ToastUtil.show("未知错误。");
        }
        Logger.e("错误：" + exception.getMessage());
        if (callback != null)
            callback.onFailed(what, url, tag, exception, responseCode, networkMillis);
    }

}
