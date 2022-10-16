package com.abiao.data.dao.mapper;


import com.abiao.data.model.IndexContent;
import com.abiao.data.model.StockIndicator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;


public class ListedCompanyMapperTest {

    String resource = "mybatis-config.xml";
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    InputStream inputStream;
    {
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = sqlSessionFactory.openSession();
    StockIndicatorsMapper stockIndicatorsMapper = sqlSession.getMapper(StockIndicatorsMapper.class);
    IndexContentMapper indexContentMapper = sqlSession.getMapper(IndexContentMapper.class);
    ListedCompanyMapper listedCompanyMapper = sqlSession.getMapper(ListedCompanyMapper.class);


    @Test
    public void selectStockIndicatorsByCode() {
        List<StockIndicator> stockIndicators = stockIndicatorsMapper.selectStockIndicatorsByCode("000001");
        for (StockIndicator stockIndicator : stockIndicators) {
            System.out.println(stockIndicator);
        }


    }


    @Test
    public void selectByStockCodesAndDate() throws IOException {

        List<String> stockCodes = Arrays.asList("000001", "000002", "00003");
        List<StockIndicator> stockIndicators = stockIndicatorsMapper.selectByStockCodesAndDate(stockCodes, "2022-08-05");
        for (StockIndicator stockIndicator : stockIndicators) {
            System.out.println(stockIndicator);
        }

    }


    /**
     * 获取所有股票代码
     * */
    @Test
    public void getAllStockCodeTest() {
        List<String> codes = listedCompanyMapper.selectStockCode();
        System.out.println(codes.size());
    }

}
