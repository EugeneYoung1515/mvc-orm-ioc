package com.smart.service;


import com.smart.core.ioc.annotations.Autowired;
import com.smart.core.ioc.annotations.Service;
import com.smart.dao.*;
import com.smart.domain.*;

import java.util.Date;
import java.util.List;

//h@Transactional
@Service
public class ForumServiceImpl implements ForumService{
	private TopicDao topicDao;
	private UserDao userDao;
	private BoardDao boardDao;
	private PostDao postDao;

	@Autowired
	public void setTopicDao(TopicDao topicDao) {
		this.topicDao = topicDao;
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setBoardDao(BoardDao boardDao) {
		this.boardDao = boardDao;
	}

	@Autowired
	public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}

	//要把显式更新都补上
	//从数据库拿出来再放回去 更新
	public void addTopic(Topic topic) throws Exception{//这个方法里就涉及到了board topic post user 四个对象
		Board board = (Board) boardDao.get(topic.getBoardId());
		board.setTopicNum(board.getTopicNum() + 1);
		boardDao.update(board);
		//topicDao.save(topic);
		Topic topic2 = topicDao.saveTopicReturnId(topic);
		
		topic.getMainPost().setTopic(topic2);//**** //****
		MainPost post = topic.getMainPost();
		post.setCreateTime(new Date());
		post.setUser(topic.getUser());//**** //**** [[[
		post.setPostTitle(topic.getTopicTitle());
		post.setBoardId(topic.getBoardId());
		postDao.save(topic.getMainPost()); //**** [[[

		User user = topic.getUser(); //**** [[[
		user.setCredit(user.getCredit() - 10);
		userDao.update(user);
	}
	
    
	/**
	 * 删除一个主题帖，用户积分减50，论坛版块主题帖数减1，删除
	 * 主题帖所有关联的帖子。
	 * @param topicId 要删除的主题帖ID
	 */
	public void removeTopic(int topicId) throws Exception {
		Topic topic = topicDao.get(topicId);

		// 将论坛版块的主题帖数减1
		Board board = boardDao.get(topic.getBoardId());
		board.setTopicNum(board.getTopicNum() - 1);
		boardDao.update(board);

		// 发表该主题帖用户扣除50积分
		User user = topic.getUser(); //****
		//User user = userDao.get(topic.getUserId());
		user.setCredit(user.getCredit() - 50);
		userDao.update(user);

		postDao.deleteTopicPosts(topicId);
		topicDao.remove(topic);
	}
	
	/**
	 * 添加一个回复帖子，用户积分加5分，主题帖子回复数加1并更新最后回复时间
	 * @param post
	 */
	public void addPost(Post post) throws Exception{

		postDao.save(post);
		
		User user = post.getUser(); //****
		user.setCredit(user.getCredit() - 5);
		userDao.update(user);
		
		Topic topic = topicDao.get(post.getTopic().getTopicId());//**** //****
		topic.setReplies(topic.getReplies() + 1);
		topic.setLastPost(new Date());
		topicDao.update(topic);
	}
	
	/**
	 * 删除一个回复的帖子，发表回复帖子的用户积分减20，主题帖的回复数减1
	 * @param postId
	 */
	public void removePost(int postId) throws Exception{
		Post post = postDao.get(postId);
		postDao.remove(post);
		
		Topic topic = topicDao.get(post.getTopic().getTopicId());//**** //****
		//Topic topic = topicDao.get(post.getTopicId());
		topic.setReplies(topic.getReplies() - 1);
		topicDao.update(topic);
		
		User user =post.getUser(); //****
		//User user = userDao.get(post.getUserId());
		user.setCredit(user.getCredit() - 20);
		userDao.update(user);

	}
	
	

	/**
	 * 创建一个新的论坛版块
	 * 
	 * @param board
	 */
	public void addBoard(Board board) throws Exception {
		boardDao.save(board);
	}
	
	/**
	 * 删除一个版块
	 * @param boardId
	 */
	public void removeBoard(int boardId) throws Exception{
		Board board = boardDao.get(boardId);
		boardDao.remove(board);
	}
	
	/**
	 * 将帖子置为精华主题帖
	 * @param topicId 操作的目标主题帖ID
	 * //@param digest 精华级别 可选的值为1，2，3
	 */
	public void makeDigestTopic(int topicId) throws Exception{
		Topic topic = topicDao.get(topicId);
		topic.setDigest(Topic.DIGEST_TOPIC);
		topicDao.update(topic);

		User user = topic.getUser();//****
		//User user = userDao.get(topic.getUserId());
		user.setCredit(user.getCredit() + 100);
		userDao.update(user);
	}
	
    /**
     * 获取所有的论坛版块
     * @return
     */
    public List<Board> getAllBoards() throws Exception{
        return boardDao.loadAll();
    }	
	
	/**
	 * 获取论坛版块某一页主题帖，以最后回复时间降序排列
	 * @param boardId
	 * @return
	 */
    public Page getPagedTopics(int boardId, int pageNo, int pageSize) throws Exception{
		return topicDao.getPagedTopics(boardId,pageNo,pageSize);
    }
    
    /**
     * 获取同主题每一页帖子，以最后回复时间降序排列
     * @param topicId
     * @return
     */
    public Page getPagedPosts(int topicId,int pageNo,int pageSize) throws Exception{
        return postDao.getPagedPosts(topicId,pageNo,pageSize);
    }    
    

	/**
	 * 查找出所有包括标题包含title的主题帖
	 * 
	 * @param title
	 *            标题查询条件
	 * @return 标题包含title的主题帖
	 */
	public Page queryTopicByTitle(String title,int pageNo,int pageSize) throws Exception{
		return topicDao.queryTopicByTitle(title,pageNo,pageSize);
	}
	
	/**
	 * 根据boardId获取Board对象
	 * 
	 * @param boardId
	 */
	public Board getBoardById(int boardId) throws Exception {
		return boardDao.get(boardId);
	}

	/**
	 * 根据topicId获取Topic对象
	 * @param topicId
	 * @return Topic
	 */
	public Topic getTopicByTopicId(int topicId) throws Exception {
		return topicDao.get(topicId);
	}
	
	/**
	 * 获取回复帖子的对象
	 * @param postId
	 * @return 回复帖子的对象
	 */
	public Post getPostByPostId(int postId) throws Exception{
		return postDao.get(postId);
	}
    
	/**
	 * 将用户设为论坛版块的管理员
	 * @param boardId  论坛版块ID
	 * @param userName 设为论坛管理的用户名
	 */
	public void addBoardManager(int boardId,String userName) throws Exception{
	   	User user = userDao.getUserByUserName(userName);
	   	if(user == null){
	   		throw new RuntimeException("用户名为"+userName+"的用户不存在。");//运行期异常
	   	}else{
            Board board = boardDao.get(boardId);
            user.getManBoards().add(board);//**** ]]]
            userDao.update(user);
	   	}
	}
	
	/**
	 * 更改主题帖
	 * @param topic
	 */


	public void updateTopic(Topic topic) throws Exception{
		topicDao.update(topic);
	}
	
	/**
	 * 更改回复帖子的内容
	 * @param post
	 */


	public void updatePost(Post post) throws Exception{
		postDao.update(post);
	}
	
}
//模式 取出来更新 再放回去
//按对象 增删改查
//按id 增删改查
//主键 外键 与其他表关联的列
//有查询或者增删改 不是按照主键或者关联列来的

//自己的orm框架不彻底
//从数据拿出来更新后 好要再放回数据库(更新)
//new一个对象A，set方法赋值相关对象B 还要再给A的一个实例变量赋值(B的主键值) 保存到表中 A对应的一条记录保存了b的主键值 还要再保存B对应的记录
//从数据库拿到一个对象A 要拿到这个对象的相关对象B 还要根据A的一个实例变量(B的主键值)从数据库拿到B

//有一个情况似乎处理不了
//就是在web层 拿到一个对象的相关对象
//或者说视图层要显示一个对象的相关对象
//这的遇到了 显示一个话题的发表人
//就要topic.user.userName
//补救措施
//所有从数据库拿到的对象A 再从数据库中把相关对象B都补上 给A对象赋实例变量B

//mybatis情况应该相近
//不过处理应改不会这么麻烦

//想一个业务层的方法
//会涉及到多少个model对象 按这样去想
//哪些对象是已经有的 哪些对象是新的
