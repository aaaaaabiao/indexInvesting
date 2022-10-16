package com.abiao.data.action.db;

import com.abiao.data.dao.ListedCompanyDao;
import com.abiao.data.model.ListedCompany;

import java.util.List;

public class ListedCompanyDbOp implements DbOp<ListedCompany> {



    private ListedCompanyDao listedCompanyDao = new ListedCompanyDao();

    @Override
    public List<ListedCompany> select() {
        return listedCompanyDao.selectAll();
    }

    @Override
    public void batchUpdate(List<ListedCompany> update) {
        listedCompanyDao.batchUpdate(update);
    }
}
