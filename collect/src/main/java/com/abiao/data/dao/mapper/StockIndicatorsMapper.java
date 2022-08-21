package com.abiao.data.dao.mapper;

import com.abiao.data.model.StockIndicator;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.sql.Date;
import java.util.List;

public interface StockIndicatorsMapper {

    @Select("select * from stock_info where stock_code=#{stockCode}")
    List<StockIndicator> selectStockIndicatorsByCode(String stockCode);



    /**
     * 查询多只股票某一天的全部指标
     * */

    @Select("<script>"
            + "select * from stock_info where trade_date = #{date} and stock_code in "
            + "<foreach item='item' index='index' collection='stockCodes' open='(' separator=',' close=')'>"
            +       "#{item}"
            + "</foreach>"
            + "</script>")
    List<StockIndicator> selectByStockCodesAndDate(@Param("stockCodes") List<String> stockCodes, @Param("date") String date);


}
