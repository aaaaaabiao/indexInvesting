package com.abiao.data.dao;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.constant.Constant;
import com.abiao.data.dao.mapper.FollowIndexMapper;
import com.abiao.data.dao.mapper.IndexContentMapper;
import com.abiao.data.model.FollowIndex;
import com.abiao.data.model.IndexContent;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class FollowIndexDao implements CommonDao<FollowIndex>{
    SqlSession sqlSession = SqlSessionBuilder.build();
    FollowIndexMapper followIndexMapper = sqlSession.getMapper(FollowIndexMapper.class);


    @Override
    public List<FollowIndex> selectAll() {
        return followIndexMapper.selectFollowIndex();
    }

    @Override
    public List<FollowIndex> selectByKey(String id) {
        return null;
    }

    @Override
    public FollowIndex selectByUniqueKey(String id) {
        return null;
    }

    @Override
    public void batchUpdate(List<FollowIndex> updates) {

    }
}
