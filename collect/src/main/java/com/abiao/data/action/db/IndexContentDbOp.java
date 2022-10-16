package com.abiao.data.action.db;

import com.abiao.data.dao.IndexContentDao;
import com.abiao.data.dao.ListedCompanyDao;
import com.abiao.data.model.IndexContent;
import com.abiao.data.model.ListedCompany;

import java.util.List;

public class IndexContentDbOp implements DbOp<IndexContent> {

    private String stockCode;

    public IndexContentDbOp(String stockCode) {
        this.stockCode = stockCode;
    }

    private IndexContentDao indexContentDao = new IndexContentDao();

    @Override
    public List<IndexContent> select() {
        return indexContentDao.selectByKey(stockCode);
    }

    @Override
    public void batchUpdate(List<IndexContent> update) {
        indexContentDao.batchUpdate(update);
    }
}
