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


public class StockMapperTest {

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
    IndexMapper indexMapper = sqlSession.getMapper(IndexMapper.class);
    StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);


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


    @Test
    public void selectContentByIndexCodeAndDateTest() {
        String indexCode = "sh000300";
        String date = "2022-08-01";
        List<String> codes = indexMapper.selectContentByIndexCodeAndDate(indexCode, date);
        System.out.println(codes);
    }


    /**
     * 获取所有股票代码
     * */
    @Test
    public void getAllStockCodeTest() {
        List<String> codes = stockMapper.getAllStockCode();
        System.out.println(codes.size());
    }


    /**
     * 插入指数成分信息
     * */
    @Test
    public void insertIndexContent() throws ParseException {
        IndexContent indexContent = new IndexContent("000300", "沪深300", "688599", "2021-12-13", null);
        IndexContent indexContent1 = new IndexContent("000300", "沪深300", "688599", "2021-12-13", null);
        indexMapper.insertIndexContext(indexContent);
        indexMapper.insertIndexContext(indexContent1);
        sqlSession.commit();
    }
}
