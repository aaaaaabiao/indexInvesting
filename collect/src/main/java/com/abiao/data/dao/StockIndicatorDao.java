package com.abiao.data.dao;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.dao.mapper.StockIndicatorsMapper;
import com.abiao.data.model.StockIndicator;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class StockIndicatorDao implements CommonDao<StockIndicator>{

    SqlSession sqlSession = SqlSessionBuilder.build();
    StockIndicatorsMapper stockIndicatorMapper = sqlSession.getMapper(StockIndicatorsMapper.class);

    @Override
    public List<StockIndicator> selectByKey(String stockCode) {
        List<StockIndicator> ret = stockIndicatorMapper.selectStockIndicatorsByCode(stockCode);
        sqlSession.clearCache();
        return ret;

    }

    @Override
    public StockIndicator selectByUniqueKey(String id) {
        return null;
    }

    @Override
    public List<StockIndicator> selectAll() {
        return null;
    }


    public List<StockIndicator> selectByStockCodesAndDate(List<String> stockCodes, String date) {

        List<StockIndicator> ret = stockIndicatorMapper.selectByStockCodesAndDate(stockCodes, date);
        sqlSession.clearCache();
        return ret;
    }


    public String selectLatestTradeDate() {
        return stockIndicatorMapper.selectLatestTradeDate();
    }

    @Override
    public void batchUpdate(List<StockIndicator> updates) {
        stockIndicatorMapper.batchInsertStockIndicator(updates);
        sqlSession.commit();
    }



}
