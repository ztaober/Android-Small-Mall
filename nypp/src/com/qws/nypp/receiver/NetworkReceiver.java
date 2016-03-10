package com.qws.nypp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.qws.nypp.utils.NetUtil;

/**
 * 网络变化监听
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class NetworkReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			if (NetUtil.isNetworkAvailable(context)) {// 有网络
			} else {// 无网络
			}
			// ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			// NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
			// if (netInfo != null && netInfo.isAvailable()) {
			// if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {// WiFi网络
			//
			// } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {// 有线网络
			//
			// } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {// 3g网络
			//
			// } else {// 网络断开
			//
			// }
			// } else {// 没有网络
			//
			// }
		}
	}
}
