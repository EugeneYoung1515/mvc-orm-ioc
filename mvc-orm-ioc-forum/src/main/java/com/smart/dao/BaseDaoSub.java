package com.smart.dao;

import com.smart.core.ioc.annotations.Autowired;
import com.smart.core.orm.BaseDao;

public class BaseDaoSub<T> extends BaseDao<T> {

    @Override
    @Autowired
    public void setBoardDao(BoardDao boardDao) {
        super.setBoardDao(boardDao);
    }

    @Override
    @Autowired
    public void setTopicDao(TopicDao topicDao) {
        super.setTopicDao(topicDao);
    }

    @Override
    @Autowired
    public void setUserDao(UserDao userDao) {
        super.setUserDao(userDao);
    }
}
