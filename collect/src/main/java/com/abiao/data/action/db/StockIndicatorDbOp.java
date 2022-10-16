package com.abiao.data.action.db;

import com.abiao.data.dao.StockIndicatorDao;
import com.abiao.data.model.StockIndicator;

import java.util.List;

public class StockIndicatorDbOp implements DbOp<StockIndicator> {

    private String stockCode;
    private StockIndicatorDao stockIndicatorDao;

    public StockIndicatorDbOp(String stockCode, StockIndicatorDao stockIndicatorDao) {
        this.stockCode = stockCode;
        this.stockIndicatorDao = stockIndicatorDao;
    }

    @Override
    public List<StockIndicator> select() {
        return stockIndicatorDao.selectByKey(stockCode);
    }

    @Override
    public void batchUpdate(List<StockIndicator> update) {
        stockIndicatorDao.batchUpdate(update);
    }
}
