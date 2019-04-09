package com.smart.service;


import com.smart.core.ioc.annotations.Autowired;
import com.smart.core.ioc.annotations.Service;
import com.smart.dao.LoginLogDao;
import com.smart.dao.UserDao;
import com.smart.core.orm.annotation.Transactional;
import com.smart.domain.LoginLog;
import com.smart.domain.User;
import com.smart.exception.UserExistException;

import java.util.Date;
import java.util.List;

//@Transactional
@Service
public class UserServiceImpl implements UserService{
	
	private UserDao userDao;
	private LoginLogDao loginLogDao;

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setLoginLogDao(LoginLogDao loginLogDao) {
		this.loginLogDao = loginLogDao;
	}

	/**
	 * 注册一个新用户,如果用户名已经存在此抛出UserExistException的异常
	 * @param user 
	 */
	public void register(User user) throws Exception{//UserExistException {
		User u = this.getUserByUserName(user.getUserName());
		if(u != null){
		    throw new UserExistException("用户名已经存在");
		}else{
		    user.setCredit(100);
            user.setUserType(1);
            userDao.save(user);
		}
	}
	
	/**
     * 更新用户
     * @param user 
     */


    public void update(User user) throws Exception{

    	userDao.update(user);
    }
	

	   /**
     * 根据用户名/密码查询 User对象
     * @param userName 用户名
     * @return User
     */

    public User getUserByUserName(String userName) throws Exception{
        return userDao.getUserByUserName(userName);
    }
	
	
	/**
	 * 根据userId加载User对象
	 * @param userId
	 * @return
	 */
	public User getUserById(int userId) throws Exception{
		return userDao.get(userId);
	}
	
	/**
	 * 将用户锁定，锁定的用户不能够登录
	 * @param userName 锁定目标用户的用户名
	 */
	public void lockUser(String userName) throws Exception{
		User user = userDao.getUserByUserName(userName);
		user.setLocked(1);
	    userDao.update(user);
	}
	
	/**
	 * 解除用户的锁定
	 * @param userName 解除锁定目标用户的用户名
	 */
	public void unlockUser(String userName) throws Exception{
		User user = userDao.getUserByUserName(userName);
		user.setLocked(0);
		userDao.update(user);
	}
	
	
	/**
	 * 根据用户名为条件，执行模糊查询操作 
	 * @param userName 查询用户名
	 * @return 所有用户名前导匹配的userName的用户
	 */
	public List<User> queryUserByUserName(String userName) throws Exception{
		return userDao.queryUserByUserName(userName);
	}
	
	/**
	 * 获取所有用户
	 * @return 所有用户
	 */
	public List<User> getAllUsers() throws Exception{
		return userDao.loadAll();
	}
	
	/**
	 * 登陆成功
	 * @param user
	 */

	@Transactional//这个事务注解放在这里没用 要放在接口中
	public void loginSuccess(User user) throws Exception{
		user.setCredit( 5 + user.getCredit());
		LoginLog loginLog = new LoginLog();
		loginLog.setUser(user);
		loginLog.setIp(user.getLastIp());
		loginLog.setLoginDate(new Date());
        userDao.update(user);
        loginLogDao.save(loginLog);//一天登录一次加5分比较好
	}	
	
}
