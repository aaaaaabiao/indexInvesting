package com.abiao.data.dao;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.constant.Constant;
import com.abiao.data.dao.mapper.IndexContentMapper;
import com.abiao.data.model.IndexContent;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class IndexContentDao implements CommonDao<IndexContent>{
    SqlSession sqlSession = SqlSessionBuilder.build();
    IndexContentMapper indexContentMapper = sqlSession.getMapper(IndexContentMapper.class);

    @Override
    public List<IndexContent> selectAll() {
        return null;
    }

    @Override
    public List<IndexContent> selectByKey(String id) {
        return indexContentMapper.selectIndexContentByCode(id);
    }

    @Override
    public IndexContent selectByUniqueKey(String id) {
        return null;
    }

    @Override
    public void batchUpdate(List<IndexContent> updates) {
        indexContentMapper.batchInsert(updates);
        sqlSession.commit();
    }

    public List<IndexContent> selectIndexContentByIndexCodeAndTradeDate(String indexCode, String tradeDate) {
        return indexContentMapper.selectIndexContentByIndexCodeAndTradeDate(indexCode, tradeDate);
    }



    public String selectIndexEarliestDate(String indexCode) {
        if ("000000".equals(indexCode)) return Constant.earliestDate;
        return indexContentMapper.selectIndexEarliestDateByIndexCode(indexCode);
    }
}
