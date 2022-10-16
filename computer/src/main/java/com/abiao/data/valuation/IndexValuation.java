package com.abiao.data.valuation;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.dao.IndexContentDao;
import com.abiao.data.dao.ListedCompanyDao;
import com.abiao.data.dao.StockIndicatorDao;
import com.abiao.data.dao.mapper.IndexContentMapper;
import com.abiao.data.dao.mapper.StockIndicatorsMapper;
import com.abiao.data.model.ListedCompany;
import com.abiao.data.model.StockIndicator;
import com.abiao.data.util.AverageUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.SqlSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class IndexValuation {

    ListedCompanyDao listedCompanyDao = new ListedCompanyDao();
    /**
     * 计算指数某天的估值(等全PE)
     * */
//    Double PEEqualWeight(List<String> codes, String date) {
//        StockIndicatorDao stockIndicatorDao = new StockIndicatorDao();
//        List<StockIndicator> stockIndicators = stockIndicatorDao.selectByStockCodesAndDate(codes, date);
//        if (stockIndicators.size() > 0) {
//            List<Double> peTTMList = stockIndicators.stream()
//                    .filter(item -> item.getPeTTM() != null)
//                    .map(StockIndicator::getPeTTM)
//                    .collect(Collectors.toList());
//            double harmonicMeanPETTM = AverageUtil.harmonicMean(peTTMList);
//            log.info("date:{}, stockNum:{}, peNum:{}, , pe:{}", date, codes.size(), peTTMList.size(),  harmonicMeanPETTM);
//            return harmonicMeanPETTM;
//        }else {
//            return null;
//        }
//    }



    Double equalWeight(List<String> codes, String date, String type) {
        StockIndicatorDao stockIndicatorDao = new StockIndicatorDao();
        List<StockIndicator> stockIndicators = stockIndicatorDao.selectByStockCodesAndDate(codes, date);
        if (stockIndicators.size() > 0) {
            List<Double> peTTMList = stockIndicators.stream()
                    .filter(item -> {
                        if ("pe".equals(type)) {
                            return item.getPe() != null;
                        }else if ("peTTM".equals(type)){
                            return item.getPeTTM() != null;
                        }else if ("pb".equals(type)) {
                            return item.getPb() != null;
                        }
                        return item.getPeTTM() != null;
                    })
                    .map(item -> {
                        if ("pe".equals(type)) {
                            return item.getPe();
                        }else if ("peTTM".equals(type)){
                            return item.getPeTTM();
                        }else if ("pb".equals(type)) {
                            return item.getPb();
                        }
                        return item.getPeTTM();
                    })
                    .collect(Collectors.toList());
            double harmonicMeanPETTM = AverageUtil.harmonicMean(peTTMList);
            log.info("date:{}, type:{}, stockNum:{}, valuationNum:{}, , value:{}", date, type, codes.size(), peTTMList.size(),  harmonicMeanPETTM);
            return harmonicMeanPETTM;
        }else {
            return null;
        }
    }






    List<String> marketAllCode(String date) {
        List<ListedCompany> listedCompanies = listedCompanyDao.selectByDate(date);
        return listedCompanies.stream().map(ListedCompany::getStockCode).collect(Collectors.toList());
    }

    public static void main(String[] args) throws ParseException, IOException {
        String type = "pb";
        //全市场PE
        List<String> ret = new ArrayList<>();
        IndexValuation indexValuation = new IndexValuation();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = df.parse("2003-01-02");
        Date endDate = df.parse("2022-09-30");
        Double prePE = 0.0;
        while (curDate.before(endDate)) {
            String formatDate = df.format(curDate);
            List<String> codes = indexValuation.marketAllCode(formatDate);
            Double pe = indexValuation.equalWeight(codes, df.format(curDate), type);
            if (pe == null) {
                pe = prePE;
            }
            ret.add(df.format(curDate) + "\t" + pe);
            prePE = pe;
            curDate = DateUtils.addDays(curDate, 1);

        }
        FileUtils.write(new File("/Users/hubiao/Desktop/valuation.csv"), String.join("\n", ret), Charset.defaultCharset());
    }
}
