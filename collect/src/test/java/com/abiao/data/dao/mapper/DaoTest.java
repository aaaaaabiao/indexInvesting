package com.abiao.data.dao.mapper;

import com.abiao.data.action.db.DbOp;
import com.abiao.data.action.db.ListedCompanyDbOp;
import com.abiao.data.model.ListedCompany;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DaoTest {

    DbOp dbOp = new ListedCompanyDbOp();

    @Test
    public void listedCompanyDbOpTest() {
//        System.out.println(new ListedCompanyDbOp().select());

        ListedCompany company = new ListedCompany();

        company.setStockCode("1");
        company.setStockName("2");
        company.setIndustry("3");
        company.setInMarketDate("4");

        List<ListedCompany> listedCompanies = new ArrayList<>();
        listedCompanies.add(company);
        dbOp.batchUpdate(listedCompanies);
    }


}
