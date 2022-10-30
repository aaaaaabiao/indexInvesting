package com.abiao.data.action.db;

import com.abiao.data.dao.IndexDailyDataDao;
import com.abiao.data.dao.StockIndicatorDao;
import com.abiao.data.model.IndexDailyData;
import com.abiao.data.model.StockIndicator;

import java.util.List;

public class IndexDailyDataDbOp implements DbOp<IndexDailyData> {

    private String stockCode;
    private IndexDailyDataDao indexDailyDataDao;

    public IndexDailyDataDbOp(String stockCode, IndexDailyDataDao indexDailyDataDao) {
        this.stockCode = stockCode;
        this.indexDailyDataDao = indexDailyDataDao;
    }

    @Override
    public List<IndexDailyData> select() {
        return indexDailyDataDao.selectByKey(stockCode);
    }

    @Override
    public void batchUpdate(List<IndexDailyData> update) {
        indexDailyDataDao.batchUpdate(update);
    }
}
