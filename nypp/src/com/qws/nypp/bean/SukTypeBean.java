package com.qws.nypp.bean;

/**
 * 
 * Suk 商品类型选择 GridView适配器数据
 * @Description
 * @author Troy
 * @date 2016-5-4 上午9:33:16
 * @Copyright:
 */
public class SukTypeBean extends BaseBean {
	private String name;//
	private String states;//状态 3种  1 选中  2 未选中 3不可选
	

	public SukTypeBean() {
	}
	
	public SukTypeBean(String name, String states) {
		this.name = name;
		this.states = states;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

}
