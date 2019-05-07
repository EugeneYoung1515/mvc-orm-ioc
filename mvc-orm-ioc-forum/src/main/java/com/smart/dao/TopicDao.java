package com.smart.dao;

import com.smart.core.ioc.annotations.Repository;
import com.smart.core.orm.BaseDao;
import com.smart.domain.Topic;

@Repository
public class TopicDao extends BaseDao<Topic> {

    public Page getPagedTopics(int boardId,int pageNo,int pageSize) throws Exception{
        String sql = "SELECT * FROM t_topic WHERE board_id = ? ORDER BY last_post DESC";
        return pageQuery(sql,pageNo,pageSize,boardId);
    }

    public Page queryTopicByTitle(String title, int pageNo, int pageSize) throws Exception{
        String sql = "SELECT * t_topic WHERE topic_title = ? ORDER BY last_post DESC";
        return pageQuery(sql,pageNo,pageSize,title);
    }

    public Topic saveTopicReturnId(Topic topic) throws Exception{
        int id = save(topic);
        //String id = "SELECT * FROM t_topic ORDER BY topic_id DESC";
        //List<Topic> topics = executeQuery(id);
        //return topics.get(0);

        return get(id);
    }
}
