package com.smart.domain;


import com.smart.core.orm.annotation.Column;
import com.smart.core.orm.annotation.Id;
import com.smart.core.orm.annotation.ManyToOne;
import com.smart.core.orm.annotation.Table;

import java.util.Date;

@Table(name = "t_topic")
public class Topic extends BaseDomain {
	/**
	 * 精华主题帖子
	 */
	public static final int DIGEST_TOPIC = 1;
	/**
	 * 普通的主题帖子
	 */
	public static final int NOT_DIGEST_TOPIC = 0;

	@Id
	@Column(name = "topic_id")
	private int topicId;

	@Column(name = "topic_title")
	private String topicTitle;

	@ManyToOne
	@Column(name = "user_id")
	//private int userId;
	private User user;

	@Column(name = "board_id")
	private int boardId;

	//@Transient
	private MainPost mainPost = new MainPost();//表上没有这个字段 但是与表间关系有关

	@Column(name = "last_post")
	private Date lastPost = new Date();

	//@Column(name = "last_post")
	//private LocalDateTime lastPost = LocalDateTime.now();

	@Column(name = "create_time")
	private Date createTime = new Date();

	//@Column(name = "create_time")
	//private LocalDateTime createTime = LocalDateTime.now();

	@Column(name = "topic_views")
	private int views;

	@Column(name = "topic_replies")
	private int replies;

	@Column(name = "digest")
	private int digest = NOT_DIGEST_TOPIC;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
		//this.createTime=LocalDateTime.now();
	}

	public int getDigest() {
		return digest;
	}

	public void setDigest(int digest) {
		this.digest = digest;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public Date getLastPost() {
		return lastPost;
	}

	public void setLastPost(Date lastPost) {
		this.lastPost = lastPost;
		//this.lastPost=LocalDateTime.now();
	}

	public int getReplies() {
		return replies;
	}

	public void setReplies(int replies) {
		this.replies = replies;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		//userId=user.getUserId();
		this.user = user;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public MainPost getMainPost() {
		return mainPost;
	}

	public void setMainPost(MainPost mainPost) {
		this.mainPost = mainPost;
	}

	/*
	public int getUserId() {
		return userId;
	}
	*/
}
