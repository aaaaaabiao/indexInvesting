package com.abiao.data.valuation;
import com.abiao.data.dao.IndexContentDao;
import com.abiao.data.dao.IndexValuationDao;
import com.abiao.data.dao.ListedCompanyDao;
import com.abiao.data.dao.StockIndicatorDao;
import com.abiao.data.model.IndexContent;
import com.abiao.data.model.IndexValuation;
import com.abiao.data.model.ListedCompany;
import com.abiao.data.model.StockIndicator;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class IndexValuationAction {


    @Parameter(names = "--commitMode", description = "Comma-separated list of group names to be run", required = true)
    public String commitMode;

    @Parameter(names = "--localOutput", description = "output file path when commitMode is local", required = false)
    public String localOutput;


    private String indexCode;

    private ListedCompanyDao listedCompanyDao;

    private StockIndicatorDao stockIndicatorDao;

    private IndexValuationDao indexValuationDao;

    private IndexContentDao indexContentDao;

    private ValuationComputer valuationComputer;


    public IndexValuationAction(String indexCode,
                                ListedCompanyDao listedCompanyDao,
                                StockIndicatorDao stockIndicatorDao,
                                IndexValuationDao indexValuationDao,
                                IndexContentDao indexContentDao,
                                ValuationComputer valuationComputer) {
        this.indexCode = indexCode;
        this.listedCompanyDao = listedCompanyDao;
        this.stockIndicatorDao = stockIndicatorDao;
        this.indexValuationDao = indexValuationDao;
        this.indexContentDao = indexContentDao;
        this.valuationComputer = valuationComputer;
    }



    private List<String> getStockCodes(String date) {
        if (indexCode.equals("000000")) {
            //全市场
            List<ListedCompany> listedCompanies = listedCompanyDao.selectByDate(date);
            List<String> codes = listedCompanies.stream().map(ListedCompany::getStockCode).collect(Collectors.toList());
            return codes;
        }else {
            return indexContentDao.selectIndexContentByIndexCodeAndTradeDate(indexCode, date)
                    .stream().map(IndexContent::getStockCode).collect(Collectors.toList());
        }
    }



    public void run() throws ParseException {
        //计算日期范围
        String currentTradeDateStr = indexValuationDao.selectLatestTradeDate(indexCode);
        String indexEarliestDateStr = indexContentDao.selectIndexEarliestDate(indexCode);
        String latestDateStr = stockIndicatorDao.selectLatestTradeDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTradeDate = df.parse(currentTradeDateStr);
        Date indexEarliestDate = df.parse(indexEarliestDateStr);
        currentTradeDate = currentTradeDate.before(indexEarliestDate) ? indexEarliestDate : currentTradeDate;
        Date latestTradeDate = df.parse(latestDateStr);
        latestTradeDate = DateUtils.addDays(latestTradeDate, 1);
        List<IndexValuation> indexValuations = new ArrayList<>();
        while (currentTradeDate.before(latestTradeDate)) {
            //计算估值
            String formatDate = df.format(currentTradeDate);
            List<String> indexContentCodes = getStockCodes(formatDate);
            List<StockIndicator> stockIndicators = stockIndicatorDao.selectByStockCodesAndDate(indexContentCodes, formatDate);
            if (stockIndicators.size() > 0) {
                IndexValuation indexValuation = valuationComputer.compute(stockIndicators);
                indexValuation.setIndexCode(indexCode);
                indexValuation.setTradeDate(formatDate);
                indexValuations.add(indexValuation);
                log.info("computer index valuation, indexCode:{}, date:{}, indexContentSize:{}, stockIndicatorSize:{}, valuation:{},",
                        indexCode, formatDate, indexContentCodes.size(), stockIndicators.size(), indexValuation);
            }

            currentTradeDate = DateUtils.addDays(currentTradeDate, 1);
        }
        commit(indexValuations);
    }


    protected void commit(List<IndexValuation> commit) {

        if ("local".equals(commitMode)) {
            String commitSerialization = JSON.toJSONString(commit);
            try {
                FileUtils.write(new File(localOutput), commitSerialization + "\n", true);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }else if ("db".equals(commitMode)) {
            indexValuationDao.batchUpdate(commit);
        }
    }

    public static void main(String[] args) throws ParseException {
        String[] indexCodes = new String[]{"000300", "000905", "000827", "399971", "000990", "000991", "000015", "000993"};
        ListedCompanyDao listedCompanyDao = new ListedCompanyDao();
        StockIndicatorDao stockIndicatorDao = new StockIndicatorDao();
        IndexValuationDao indexValuationDao = new IndexValuationDao();
        IndexContentDao indexContentDao = new IndexContentDao();
        ValuationComputer computer = new PEWEValuationComputer();


        for (String indexCode : indexCodes) {
            IndexValuationAction indexValuationAction = new IndexValuationAction(indexCode,
                    listedCompanyDao, stockIndicatorDao, indexValuationDao,indexContentDao, computer);
            JCommander.newBuilder()
                    .addObject(indexValuationAction)
                    .build()
                    .parse(args);
            indexValuationAction.run();
        }
    }
}
