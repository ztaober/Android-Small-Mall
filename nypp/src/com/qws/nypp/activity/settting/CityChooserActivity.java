package com.qws.nypp.activity.settting;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;

/**
 * 省市区选择
 * 
 * @Description
 * @author troy
 * @date 2016-7-19 下午2:19:27
 * @Copyright:
 */
public class CityChooserActivity extends BaseActivity implements
	OnWheelChangedListener, OnClickListener{
	
	/**
	 * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	 */
	private JSONArray mJsonAry;
	/**
	 * 省的WheelView控件
	 */
	private WheelView mProvince;
	/**
	 * 市的WheelView控件
	 */
	private WheelView mCity;
	/**
	 * 区的WheelView控件
	 */
	private WheelView mArea;

	/**
	 * 所有省
	 */
	private String[] mProvinceDatas;
	private String[] mProvinceCodeDatas;
	/**
	 * key - 省 value - 市s
	 */
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mCitisDatasCodeMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区s
	 */
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mAreaDatasCodeMap = new HashMap<String, String[]>();

	/**
	 * 当前省的名称
	 */
	private String mCurrentProviceName;
	private String mCurrentProviceCode;
	/**
	 * 当前市的名称
	 */
	private String mCurrentCityName;
	private String mCurrentCityCode;
	/**
	 * 当前区的名称
	 */
	private String mCurrentAreaName = "";
	private String mCurrentAreaCode = "";

	@Override
	protected int getContentViewId() {
		overridePendingTransition(R.anim.push_bottom_in, 0);
		return R.layout.as_activity_citys_chooser;
	}

	@Override
	protected void findViews() {
		initJsonData();

		mProvince = (WheelView) findViewById(R.id.id_province);
		mCity = (WheelView) findViewById(R.id.id_city);
		mArea = (WheelView) findViewById(R.id.id_area);
		
		findViewById(R.id.city_rl).setOnClickListener(this);
		findViewById(R.id.city_choose).setOnClickListener(this);

		initDatas();

		mProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
		// 添加change事件
		mProvince.addChangingListener(this);
		// 添加change事件
		mCity.addChangingListener(this);
		// 添加change事件
		mArea.addChangingListener(this);

		mProvince.setVisibleItems(7);
		mCity.setVisibleItems(7);
		mArea.setVisibleItems(7);
		updateCities();
		updateAreas();
		
//		mProvince.setCurrentItem(AccessInstance.getInstance(this).mProvinceIndex);
//		mCity.setCurrentItem(AccessInstance.getInstance(this).mCityIndex);
//		mArea.setCurrentItem(AccessInstance.getInstance(this).mAreaIndex);
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.push_bottom_out);
		
//		//保存选择的地址
//		AccessInstance.getInstance(this).mProvinceIndex = mProvince.getCurrentItem();
//		AccessInstance.getInstance(this).mCityIndex = mCity.getCurrentItem();
//		AccessInstance.getInstance(this).mAreaIndex = mArea.getCurrentItem();
	}

	
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		mCurrentCityCode = mCitisDatasCodeMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mAreaDatasMap.get(mCurrentCityName);

		if (areas.length == 0) {
			areas = new String[] { "" };
		}
		mArea.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mArea.setCurrentItem(0);

		if (mAreaDatasMap.get(mCurrentCityName) != null
				&& mAreaDatasMap.get(mCurrentCityName).length != 0) {
			mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[0];
			mCurrentAreaCode = mAreaDatasCodeMap.get(mCurrentCityName)[0];
		} else {
			mCurrentAreaName = "";
			mCurrentAreaCode = "";
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		mCurrentProviceCode = mProvinceCodeDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities.length == 0) {
			cities = new String[] { "" };
		}
		mCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}
	

	/**
	 * 解析整个Json对象，完成后释放Json对象的内存
	 */
	private void initDatas() {
		try {
			mProvinceDatas = new String[mJsonAry.length()];
			mProvinceCodeDatas = new String[mJsonAry.length()];
			for (int i = 0; i < mJsonAry.length(); i++) {
				JSONObject jsonP = mJsonAry.getJSONObject(i);// 每个省的json对象
				String province = jsonP.getString("name");// 省名字
				String privinceCode = jsonP.getString("adminCode");

				mProvinceDatas[i] = province;
				mProvinceCodeDatas[i] = privinceCode;

				JSONArray jsonCs = null;
				try {
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("cities");
				} catch (Exception e1) {
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				String[] mCitiesCodeDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++) {
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("name");// 市名字
					String cityCode = jsonCity.getString("adminCode");
					mCitiesDatas[j] = city;
					mCitiesCodeDatas[j] = cityCode;
					JSONArray jsonAreas = null;
					try {
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("districts");
					} catch (Exception e) {
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
					String[] mAreasCodeDatas = new String[jsonAreas.length()];// 当前市的所有区CODE
					for (int k = 0; k < jsonAreas.length(); k++) {
						String area = jsonAreas.getJSONObject(k).getString(
								"name");// 区域的名称
						String areaCode = jsonAreas.getJSONObject(k).getString(
								"adminCode");
						mAreasDatas[k] = area;
						mAreasCodeDatas[k] = areaCode;
					}
					mAreaDatasMap.put(city, mAreasDatas);
					mAreaDatasCodeMap.put(city, mAreasCodeDatas);
				}

				mCitisDatasMap.put(province, mCitiesDatas);
				mCitisDatasCodeMap.put(province, mCitiesCodeDatas);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		mJsonAry = null;
	}

	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象 天津暂无
	 */
	private void initJsonData() {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = getAssets().open("allcity.json");
			byte[] buf = new byte[is.available()];
			int len = -1;

			while ((len = is.read(buf)) != -1) {
				sb.append(new String(buf, 0, len));
			}

			mJsonAry = new JSONArray(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * change事件的处理
	 */
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mProvince) {
			updateCities();
		} else if (wheel == mCity) {
			updateAreas();
		} else if (wheel == mArea) {
			if(mAreaDatasMap.get(mCurrentCityName).length !=0 && mAreaDatasCodeMap.get(mCurrentCityName).length !=0){
				mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
				mCurrentAreaCode = mAreaDatasCodeMap.get(mCurrentCityName)[newValue];
			}
		}

	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.city_rl) {
			CityChooserActivity.this.finish();
		} else if(v.getId() == R.id.city_choose) {
				JSONObject json = new JSONObject();
				try {
					json.put("province", mCurrentProviceName);
					json.put("city", mCurrentCityName);
					json.put("area", mCurrentAreaName);
					json.put("provinceCode", mCurrentProviceCode);
					json.put("cityCode", mCurrentCityCode);
					json.put("areaCode", mCurrentAreaCode);
				} catch (JSONException e) {
				}
				setResult(2333, new Intent().putExtra("selData", json.toString()));
				CityChooserActivity.this.finish();
		}
		
	}

}
