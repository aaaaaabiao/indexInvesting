package com.abiao.data.dao;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.dao.mapper.IndexDailyDataMapper;
import com.abiao.data.model.IndexDailyData;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class IndexDailyDataDao implements CommonDao<IndexDailyData>{

    SqlSession sqlSession = SqlSessionBuilder.build();
    IndexDailyDataMapper indexDailyDataMapper = sqlSession.getMapper(IndexDailyDataMapper.class);

    @Override
    public List<IndexDailyData> selectByKey(String indexCode) {
        List<IndexDailyData> ret = indexDailyDataMapper.selectIndexDailyDataByIndexCode(indexCode);
        sqlSession.clearCache();
        return ret;

    }

    @Override
    public IndexDailyData selectByUniqueKey(String id) {
        return null;
    }

    @Override
    public List<IndexDailyData> selectAll() {
        return null;
    }

    @Override
    public void batchUpdate(List<IndexDailyData> updates) {
        indexDailyDataMapper.batchInsertIndexDailyData(updates);
        sqlSession.commit();
    }



}
