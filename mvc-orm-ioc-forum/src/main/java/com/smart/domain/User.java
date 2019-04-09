package com.smart.domain;


import com.smart.core.orm.annotation.Column;
import com.smart.core.orm.annotation.Id;
import com.smart.core.orm.annotation.Table;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "t_user")
public class User extends BaseDomain {
    /**
     *锁定用户对应的状态值 
     */
    public static final int USER_LOCK = 1;
    /**
     * 用户解锁对应的状态值
     */
    public static final int USER_UNLOCK = 0;
    /**
     * 管理员类型
     */
    public static final int FORUM_ADMIN = 2;
    /**
     * 普通用户类型
     */
    public static final int NORMAL_USER = 1;
    
    @Id
	@Column(name = "user_id")
    private int userId;

    @Column(name = "user_name")
	private String userName;
    
    @Column(name = "user_type")
    private int userType = NORMAL_USER;

    @Column(name = "last_ip")
	private String lastIp;
	
	@Column(name = "last_visit")
	private Date lastVisit;

	//@Column(name = "last_visit")
	//private LocalDateTime lastVisit;

	@Column(name = "password")
	private String password;

	@Column(name = "locked")
	private int locked ;

	@Column(name="credit")
	private int credit;

	//这里可能少一个注解
   	private Set<Board> manBoards = new HashSet<Board>();//表关系

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getLocked() {
		return locked;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}
	
	public Set<Board> getManBoards()
    {
        return manBoards;
    }

    public void setManBoards(Set<Board> manBoards)
    {
        this.manBoards = manBoards;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

    public int getUserType()
    {
        return userType;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
		//this.lastVisit=LocalDateTime.now();
	}

}
