package com.smart.domain;


import com.smart.core.orm.annotation.Column;
import com.smart.core.orm.annotation.Id;
import com.smart.core.orm.annotation.Table;

import java.util.HashSet;
import java.util.Set;

@Table(name = "t_board")
public class Board extends BaseDomain {

	@Id
	@Column(name = "board_id")
	private int boardId;

	@Column(name = "board_name")
	private String boardName;

	@Column(name = "board_desc")
	private String boardDesc;

	@Column(name = "topic_num")
	private int topicNum ;

	//这里可能少一个注解
	private Set<User> users = new HashSet<User>();//表上没这个  表间关系

	public int getTopicNum() {
		return topicNum;
	}

	public void setTopicNum(int topicNum) {
		this.topicNum = topicNum;
	}

	public String getBoardDesc() {
		return boardDesc;
	}

	public void setBoardDesc(String boardDesc) {
		this.boardDesc = boardDesc;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
