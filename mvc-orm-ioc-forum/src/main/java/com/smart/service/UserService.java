package com.smart.service;

import com.smart.domain.User;
import java.util.List;

//@Transactional
public interface UserService {

	public void register(User user) throws Exception;
	public void update(User user) throws Exception;
	public User getUserByUserName(String userName) throws Exception;
	public User getUserById(int userId) throws Exception;
	public void lockUser(String userName) throws Exception;
	public void unlockUser(String userName) throws Exception;
	public List<User> queryUserByUserName(String userName) throws Exception;
	public List<User> getAllUsers() throws Exception;
	//@Transactional
	public void loginSuccess(User user) throws Exception;
	
}
