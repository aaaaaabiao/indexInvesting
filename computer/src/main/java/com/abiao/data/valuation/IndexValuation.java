package com.abiao.data.valuation;

import com.abiao.data.connect.SqlSessionBuilder;
import com.abiao.data.dao.mapper.IndexMapper;
import com.abiao.data.dao.mapper.StockIndicatorsMapper;
import com.abiao.data.model.StockIndicator;
import com.abiao.data.util.AverageUtil;
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
    private SqlSession sqlSession = new SqlSessionBuilder().build();

    /**
     * 计算指数某天的估值
     * */
    Double PEEqualWeight(String indexCode, String date) {
        IndexMapper indexMapper = sqlSession.getMapper(IndexMapper.class);
        StockIndicatorsMapper stockIndicatorsMapper = sqlSession.getMapper(StockIndicatorsMapper.class);
        List<String> codes = indexMapper.selectContentByIndexCodeAndDate(indexCode, date);
        List<StockIndicator> stockIndicators = stockIndicatorsMapper.selectByStockCodesAndDate(codes, date);
        if (stockIndicators.size() > 0) {
            List<Double> peTTMList = stockIndicators.stream()
                    .filter(item -> item.getPeTTM() != null)
                    .map(StockIndicator::getPeTTM)
                    .collect(Collectors.toList());
            List<String> peNullStockCode = stockIndicators.stream()
                    .filter(item -> item.getPeTTM() == null)
                    .map(StockIndicator::getStockCode)
                    .collect(Collectors.toList());


            double harmonicMeanPETTM = AverageUtil.harmonicMean(peTTMList);
            log.info("indexCode:{}, stockNum:{}, peNum:{}, date:{}, pe:{}", indexCode, codes.size(), peTTMList.size(), date, harmonicMeanPETTM);
//            System.out.println(String.format("date:%s, pe:%s, indexCode:%s, stockNum:%s, peNum:%s, peNull:%s", date, harmonicMeanPETTM, indexCode, codes.size(), peTTMList.size(), peNullStockCode));
            return harmonicMeanPETTM;
        }else {
            return null;
        }

    }


    public static void main(String[] args) throws ParseException, IOException {
        List<String> ret = new ArrayList<>();
        String indexCode = "sh000300";
        IndexValuation indexValuation = new IndexValuation();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = df.parse("2005-04-08");
        Date endDate = df.parse("2022-06-30");
        Double prePE = 0.0;
        while (curDate.before(endDate)) {
            Double pe = indexValuation.PEEqualWeight(indexCode, df.format(curDate));
            if (pe == null) {
                pe = prePE;
            }
            ret.add(df.format(curDate) + "\t" + pe);
            prePE = pe;
            curDate = DateUtils.addDays(curDate, 1);

        }
//        log.info("ret:{}", ret);
        FileUtils.write(new File("/Users/hubiao/Desktop/valuation.csv"), String.join("\n", ret), Charset.defaultCharset());
        System.out.println();
    }
}
