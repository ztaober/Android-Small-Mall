package com.qws.nypp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qws.nypp.R;
import com.qws.nypp.view.LoadingView;
import com.qws.nypp.view.TitleView;

import de.greenrobot.event.EventBus;

/**
 * 基类Fragment
 * 
 * @Description
 * @author qw
 * @date 2015-12-30
 */
public abstract class BaseFragment extends Fragment {
	/** Standard activity result: operation canceled. */
	protected final int RESULT_CANCELED = 0;
	/** Standard activity result: operation succeeded. */
	protected final int RESULT_OK = -1;
	/** Start of user-defined activity results. */
	protected final int RESULT_FIRST_USER = 1;
	protected Context context;
	/** 父视图 */
	protected View view_Parent;
	/** 标题 */
	protected TitleView titleView;
	/** 加载框View */
	protected LoadingView mLoadingView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		view_Parent = getViews();
		titleView = findViewById(R.id.view_title);
		mLoadingView = findViewById(R.id.network_error);
		findViews();
		initData();
		setListener();
		getData();
		if (useEventBus()) {
			EventBus.getDefault().register(this);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T findViewById(int resId) {
		return (T) view_Parent.findViewById(resId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if ((ViewGroup) view_Parent.getParent() != null) {
			((ViewGroup) view_Parent.getParent()).removeView(view_Parent);
		}
		return view_Parent;
	}

	/** 是否需要注册EventBus */
	protected boolean useEventBus() {
		return false;
	}

	/**
	 * 获取布局
	 * 
	 * @updateTime 2015-6-22,下午3:50:19
	 * @updateAuthor qw
	 * @return
	 */
	protected abstract View getViews();

	/** 查询View对象 */
	protected abstract void findViews();

	/** 初始化数据,设置数据 */
	protected abstract void initData();

	/** 设置监听 */
	protected abstract void setListener();

	/** 获取网络数据 */
	protected abstract void getData();

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (useEventBus()) {
			EventBus.getDefault().unregister(this);
		}
	}

}