package com.abiao.data.dao;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.dao.mapper.IndexContentMapper;
import com.abiao.data.dao.mapper.ListedCompanyMapper;
import com.abiao.data.model.IndexContent;
import com.abiao.data.model.ListedCompany;
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
}
