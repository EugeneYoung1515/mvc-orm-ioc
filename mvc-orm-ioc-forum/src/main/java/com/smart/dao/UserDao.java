package com.smart.dao;

import com.smart.core.ioc.annotations.Repository;
import com.smart.core.orm.BaseDao;
import com.smart.domain.User;

import java.util.List;

@Repository
public class UserDao extends BaseDaoSub<User> {
    public User getUserByUserName(String userName) throws Exception{
        List<User> list = executeQuery("SELECT * FROM t_user WHERE user_name = ?",userName);
        if(list.size()!=0){
            return list.get(0);
        }
        return null;
    }

    public List<User> queryUserByUserName(String userName) throws Exception{
        return executeQuery("SELECT * FROM t_user WHERE user_name = ?",userName);
    }
}
