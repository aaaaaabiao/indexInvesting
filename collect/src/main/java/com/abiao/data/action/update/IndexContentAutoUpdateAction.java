package com.abiao.data.action.update;

import com.abiao.data.action.db.DbOp;
import com.abiao.data.action.db.IndexContentDbOp;
import com.abiao.data.collect.Collect;
import com.abiao.data.collect.IndexContentCollect;
import com.abiao.data.constant.Constant;
import com.abiao.data.dao.ListedCompanyDao;
import com.abiao.data.model.IndexContent;
import com.abiao.data.model.ListedCompany;
import com.abiao.data.util.CommonUtil;
import com.abiao.data.util.ThreadLocalUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 上市公司公司自动更新
 * */

@Slf4j
public class IndexContentAutoUpdateAction extends AutoUpdateAction<IndexContent> {

    private String commitPath;

    public IndexContentAutoUpdateAction(Collect<IndexContent> collect, DbOp<IndexContent> dbOp, String commitPath) {
        super(collect, dbOp);
        this.commitPath = commitPath;
    }

    @Override
    protected List<IndexContent> diff(List<IndexContent> collectData, List<IndexContent> dbData) {
        Map<String, IndexContent> collectDataMap = collectData.stream().collect(Collectors.toMap(new Function<IndexContent, String>() {
            @Override
            public String apply(IndexContent indexContent) {
                return indexContent.getStockCode() + indexContent.getIndexCode() + indexContent.getInDate();
            }
        }, Function.identity()));


        Map<String, IndexContent> dbDataMap = dbData.stream().collect(Collectors.toMap(new Function<IndexContent, String>() {
            @Override
            public String apply(IndexContent indexContent) {
                return indexContent.getStockCode() + indexContent.getIndexCode() + indexContent.getInDate();
            }
        }, Function.identity()));

        List<IndexContent> updateValue = diff(collectDataMap, dbDataMap);
        log.info("diff count:{}", updateValue.size());
        return updateValue;
    }


    public List<ListedCompany> fix() {
        ListedCompanyDao listedCompanyDao = new ListedCompanyDao();
        List<ListedCompany> listedCompanies = new ArrayList<>();
        String[] codes = new String[]{"689009"};
        for (String code : codes) {
            listedCompanies.add(listedCompanyDao.selectByUniqueKey(code));
        }
        return listedCompanies;
    }


    @Override
    protected void commit(List<IndexContent> commit) {
        String commitSerialization = JSON.toJSONString(commit);
        String commitFilePath = ThreadLocalUtil.commitDataPath.get().get(Constant.indexContent).get("data");
        try {
            FileUtils.write(new File(commitFilePath), commitSerialization + "\n", true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        String commitPath = args[0];
        ListedCompanyDao listedCompanyDao = new ListedCompanyDao();
        List<ListedCompany> listedCompanies = listedCompanyDao.selectAll();
        for (ListedCompany listedCompany : listedCompanies) {
            log.info("listedCompany:{}", listedCompany);
            String stockCode = listedCompany.getStockCode();
            Collect<IndexContent> indexContentCollect = new IndexContentCollect(stockCode);
            DbOp<IndexContent> indexContentDbOp = new IndexContentDbOp(stockCode);
            try {
                new IndexContentAutoUpdateAction(indexContentCollect, indexContentDbOp, commitPath).action();
            }catch (Exception e) {
                log.error("stock_code:{}, e:{}", stockCode ,e);
            }
        }
    }
}
