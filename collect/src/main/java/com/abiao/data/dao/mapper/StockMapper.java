package com.abiao.data.dao.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface StockMapper {


     String indexContent = "index_content";

    /**
     * 获取A股所有股票代码
     * */
    @Select("select stock_code from company_info")
    List<String> getAllStockCode();
}
