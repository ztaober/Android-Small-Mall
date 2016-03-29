package com.qws.nypp.bean;

import java.util.List;

public class CategoryBean extends BaseBean {
	//款式名称
	private String goodsType;
	//款式类别集合
	private List<String> categroys;
	//被选中的款式
	private String selectedType = "";
	//点击状态
	private boolean selected;
	
	public CategoryBean() {
		super();
	}

	public CategoryBean(String goodsType, List<String> categroys) {
		super();
		this.goodsType = goodsType;
		this.categroys = categroys;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public List<String> getCategroys() {
		return categroys;
	}

	public void setCategroys(List<String> categroys) {
		this.categroys = categroys;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
