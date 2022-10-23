package com.abiao.data.dao;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.constant.Constant;
import com.abiao.data.dao.mapper.IndexValuationMapper;
import com.abiao.data.dao.mapper.StockIndicatorsMapper;
import com.abiao.data.model.IndexValuation;
import com.abiao.data.model.StockIndicator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class IndexValuationDao implements CommonDao<IndexValuation>{

    SqlSession sqlSession = SqlSessionBuilder.build();
    IndexValuationMapper indexValuationMapper = sqlSession.getMapper(IndexValuationMapper.class);


    @Override
    public List<IndexValuation> selectAll() {
        return null;
    }

    @Override
    public List<IndexValuation> selectByKey(String id) {
        return null;
    }

    @Override
    public IndexValuation selectByUniqueKey(String id) {
        return null;
    }



    @Override
    public void batchUpdate(List<IndexValuation> updates) {
        indexValuationMapper.batchInsertOrUpdate(updates);
        sqlSession.commit();
    }

    /**
     * 获取最新的交易日期
     * */
    public String selectLatestTradeDate(String indexCode) {
        String latest = indexValuationMapper.selectLatestTradeDateByIndexCode(indexCode);
        if (StringUtils.isBlank(latest)) return Constant.earliestDate;
        return indexValuationMapper.selectLatestTradeDateByIndexCode(indexCode);
    }
}
