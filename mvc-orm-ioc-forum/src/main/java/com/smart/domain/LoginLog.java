package com.smart.domain;

import com.smart.core.orm.annotation.Column;
import com.smart.core.orm.annotation.Id;
import com.smart.core.orm.annotation.ManyToOne;
import com.smart.core.orm.annotation.Table;

import java.util.Date;

@Table(name = "t_login_log")
public class LoginLog extends BaseDomain {
	@Id
	@Column(name = "login_log_id")
	private int loginLogId;
	
	@Column(name = "login_datetime")
	private Date loginDate;

	//@Column(name = "login_datetime")
	//private LocalDateTime loginDate;

	@ManyToOne
	@Column(name = "user_id")//表间关系
	//private int userId;
    private User user;

	@Column(name = "ip")
	private String ip;
	
	
	public int getLoginLogId() {
		return loginLogId;
	}
	public void setLoginLogId(int loginLogId) {
		this.loginLogId = loginLogId;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
		//this.loginDate=LocalDateTime.now();
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		//userId=user.getUserId();
		this.user = user;
	}
	/*
	public int getUserId() {
		return userId;
	}
	*/
}
