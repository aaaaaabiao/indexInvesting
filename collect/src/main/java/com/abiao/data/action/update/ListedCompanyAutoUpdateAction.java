package com.abiao.data.action.update;

import com.abiao.data.action.db.ListedCompanyDbOp;
import com.abiao.data.collect.Collect;
import com.abiao.data.collect.ListedCompanyCollect;
import com.abiao.data.action.db.DbOp;
import com.abiao.data.model.ListedCompany;


import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 上市公司公司自动更新
 * */
public class ListedCompanyAutoUpdateAction extends AutoUpdateAction<ListedCompany> {

    private String commitPath;


    public ListedCompanyAutoUpdateAction(Collect<ListedCompany> collect, DbOp<ListedCompany> dbOp, String commitPath) {
        super(collect, dbOp);
        this.commitPath = commitPath;
    }

    @Override
    protected List<ListedCompany> diff(List<ListedCompany> collectData, List<ListedCompany> dbData) {
        Map<String, ListedCompany> m1 = collectData.stream().collect(Collectors.toMap(ListedCompany::getStockCode, Function.identity()));
        Map<String, ListedCompany> m2 = dbData.stream().collect(Collectors.toMap(ListedCompany::getStockCode, Function.identity()));
        return diff(m1, m2);
    }

    @Override
    protected String commitPath() {
        return commitPath;
    }


    public static void main(String[] args) throws IOException {
        String commitPath = args[0];

        DbOp<ListedCompany> dbOp = new ListedCompanyDbOp();
        Collect<ListedCompany> companyCollect = new ListedCompanyCollect();

        new ListedCompanyAutoUpdateAction(companyCollect, dbOp, commitPath).action();
    }
}
