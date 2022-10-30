package com.abiao.data.action.update;

import com.abiao.data.action.db.StockIndicatorDbOp;
import com.abiao.data.collect.Collect;
import com.abiao.data.collect.StockIndicatorCollect;
import com.abiao.data.action.db.DbOp;
import com.abiao.data.collect.spider.XueQiuKinePageProcess;
import com.abiao.data.dao.ListedCompanyDao;
import com.abiao.data.dao.StockIndicatorDao;
import com.abiao.data.model.ListedCompany;
import com.abiao.data.model.StockIndicator;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import us.codecraft.webmagic.Spider;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 股票指标实时更新
 * */
@Slf4j
public class StockIndicatorAutoUpdateAction extends AutoUpdateAction<StockIndicator> {

    private String commitFilePath;


    public StockIndicatorAutoUpdateAction(Collect<StockIndicator> collect, DbOp<StockIndicator> dbOp, String commitFilePath) {
        super(collect, dbOp);
        this.commitFilePath = commitFilePath;
    }

    public StockIndicatorAutoUpdateAction(Collect<StockIndicator> collect, DbOp<StockIndicator> dbOp) {
        super(collect, dbOp);
    }


    @Override
    protected List<StockIndicator> diff(List<StockIndicator> collectData, List<StockIndicator> dbData) {
        Map<String, StockIndicator> m1 = collectData.stream().collect(Collectors.toMap(StockIndicator::getTradeDate, Function.identity()));
        Map<String, StockIndicator> m2 = dbData.stream().collect(Collectors.toMap(StockIndicator::getTradeDate, Function.identity()));
        List<StockIndicator> updateValue = diff(m1, m2);
        log.info("diff count:{}", updateValue.size());
        return updateValue;
    }

    @Override
    protected String commitPath() {
        return commitFilePath;
    }


    public static void main(String[] args) {
        String commitPath = args[0];
        ListedCompanyDao listedCompanyDao = new ListedCompanyDao();
        StockIndicatorDao stockIndicatorDao = new StockIndicatorDao();
        List<ListedCompany> listedCompanies = listedCompanyDao.selectAllListed();
        Spider spider = Spider.create(new XueQiuKinePageProcess());
        int count = 1;
        log.info("start update stock_indicator info, listedCompaniesSize:{}", listedCompanies.size());
        for (ListedCompany listedCompany : listedCompanies) {
            log.info("count:{}, listedCompany:{}", count++, listedCompany);
            String stockCode = listedCompany.getStockCode();
            Collect<StockIndicator> stockIndicatorCollect = new StockIndicatorCollect(stockCode, spider);
            DbOp<StockIndicator> stockIndicatorDbOp = new StockIndicatorDbOp(stockCode, stockIndicatorDao);
            try {
                new StockIndicatorAutoUpdateAction(stockIndicatorCollect, stockIndicatorDbOp, commitPath).action();
            }catch (Exception e) {
                log.error("stock_code:{}, e:{}", stockCode ,e);
            }
        }
        spider.close();
    }
}
