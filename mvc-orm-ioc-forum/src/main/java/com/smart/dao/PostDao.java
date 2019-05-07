package com.smart.dao;

import com.smart.core.ioc.annotations.Repository;
import com.smart.core.orm.BaseDao;
import com.smart.domain.Post;

@Repository
public class PostDao extends BaseDao<Post> {
    public Page getPagedPosts(int topicId, int pageNo, int pageSize) throws Exception {
        String sql = "SELECT * FROM t_post WHERE topic_id = ? ORDER BY create_time ASC";
        return pageQuery(sql,pageNo,pageSize,topicId);
    }

    public void deleteTopicPosts(int topicId) throws Exception{
        String sql = "DELETE FROM t_post WHERE topic_id=?";
        executeUpdate(sql,topicId);
    }
}
