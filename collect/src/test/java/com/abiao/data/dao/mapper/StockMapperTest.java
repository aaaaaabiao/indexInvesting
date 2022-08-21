package com.abiao.data.dao.mapper;


import com.abiao.data.dao.mapper.StockIndicatorsMapper;
import com.abiao.data.model.StockIndicator;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class StockMapperTest {

    String resource = "mybatis-config.xml";
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
    StockIndicatorsMapper stockMapper = sqlSession.getMapper(StockIndicatorsMapper.class);
    IndexMapper indexMapper = sqlSession.getMapper(IndexMapper.class);


    @Test
    public void selectStockIndicatorsByCode() {
        List<StockIndicator> stockIndicators = stockMapper.selectStockIndicatorsByCode("000001");
        for (StockIndicator stockIndicator : stockIndicators) {
            System.out.println(stockIndicator);
        }


    }


    @Test
    public void selectByStockCodesAndDate() throws IOException {

        List<String> stockCodes = Arrays.asList("000001", "000002", "00003");
        List<StockIndicator> stockIndicators = stockMapper.selectByStockCodesAndDate(stockCodes, "2022-08-05");
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
}
