package com.qws.nypp.bean;

/**
 * 用户信息
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class UserBean extends BaseBean {
	private String balance;
	private String nickname;
	private String sign;
	private String portrait;
	private String account;
	
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
}
