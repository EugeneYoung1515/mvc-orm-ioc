package com.smart.service;


import com.smart.dao.*;
import com.smart.domain.*;

import java.util.List;

//h@Transactional
public interface ForumService {

	public void addTopic(Topic topic) throws Exception;

	public void removeTopic(int topicId) throws Exception;

	public void addPost(Post post) throws Exception;

	public void removePost(int postId) throws Exception;

	public void addBoard(Board board) throws Exception;

	public void removeBoard(int boardId) throws Exception;

	public void makeDigestTopic(int topicId) throws Exception;

	public List<Board> getAllBoards() throws Exception;

	public Page getPagedTopics(int boardId, int pageNo, int pageSize) throws Exception;

	public Page getPagedPosts(int topicId, int pageNo, int pageSize) throws Exception;

	public Page queryTopicByTitle(String title, int pageNo, int pageSize) throws Exception;

	public Board getBoardById(int boardId) throws Exception;

	public Topic getTopicByTopicId(int topicId) throws Exception;

	public Post getPostByPostId(int postId) throws Exception;

	public void addBoardManager(int boardId, String userName) throws Exception;

	public void updateTopic(Topic topic) throws Exception;

	public void updatePost(Post post) throws Exception;
}