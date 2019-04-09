package com.smart.domain;


import com.smart.core.orm.annotation.Column;
import com.smart.core.orm.annotation.Id;
import com.smart.core.orm.annotation.ManyToOne;
import com.smart.core.orm.annotation.Table;

import java.util.Date;

@Table(name = "t_post")
public class Post extends BaseDomain {
	//看MainPost 实例变量比表少了Post_type

	@Column(name = "post_type")
	private int postType=TYPE1;

	public static final int TYPE1 = 1;

	public static final int TYPE2 = 2;
	
	@Id
	@Column(name = "post_id")
	private int postId;

	@Column(name = "post_title")
	private String postTitle;

	@Column(name = "post_text")
	private String postText;

	@Column(name = "board_id")
	private int boardId;

	@Column(name = "create_time")
	private Date createTime;

	//@Column(name = "create_time")
	//private LocalDateTime createTime;

	/*
	//这里可能少一个注解
	@Column(name = "user_id")//表相关的
	//注意类型对不上
	private User user;
	*/

	@ManyToOne
	@Column(name = "user_id")
	//private int userId;
	private User user;

	/*
	//这里可能少一个注解
	@Column(name = "topic_id")//表相关的
	//类型对不上
 	private Topic topic;
	//jdbc怎么实现 用列名到对象的映射？加上sql的表间关系 联结语法?
	*/

	@ManyToOne
	@Column(name = "topic_id")
	//private int topicId;
	private Topic topic;

	public void setPostType(int postType) {
		this.postType = postType;
	}

	public int getPostType() {
		return postType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
		//this.createTime=LocalDateTime.now();
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
		//topicId=topic.getTopicId();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		//userId = user.getUserId();
	}
/*
	public int getTopicId() {
		return topicId;
	}

	public int getUserId() {
		return userId;
	}
	*/
}
