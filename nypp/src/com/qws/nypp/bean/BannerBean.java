package com.qws.nypp.bean;

public class BannerBean extends BaseBean {
	
	private String bannerPicture;
	private String bannerUrl;
	
	public BannerBean(String bannerPicture, String bannerUrl) {
		super();
		this.bannerPicture = bannerPicture;
		this.bannerUrl = bannerUrl;
	}
	public String getBannerPicture() {
		return bannerPicture;
	}
	public void setBannerPicture(String bannerPicture) {
		this.bannerPicture = bannerPicture;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	
}
