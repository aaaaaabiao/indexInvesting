package com.abiao.data.dao.mapper;

import com.abiao.data.model.StockIndicator;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.sql.Date;
import java.util.List;


/**
 * 股票指标mapper
 * */
public interface StockIndicatorsMapper {

    @Select("select * from stock_indicator_daily where stock_code=#{stockCode}")
    List<StockIndicator> selectStockIndicatorsByCode(String stockCode);



    /**
     * 查询多只股票某一天的全部指标
     * */

    @Select("<script>"
            + "select * from stock_indicator_daily where trade_date = #{date} and stock_code in "
            + "<foreach item='item' index='index' collection='stockCodes' open='(' separator=',' close=')'>"
            +       "#{item}"
            + "</foreach>"
            + "</script>")
    List<StockIndicator> selectByStockCodesAndDate(@Param("stockCodes") List<String> stockCodes, @Param("date") String date);



    /**
     * 插入指数成分股
     * */
    @Insert("<script>"
            + "insert into stock_indicator_daily (stock_code, trade_date, pe, pe_ttm, pb, ps, ps_ttm, dv_ratio, dv_ttm, total_mv) values "
            + "<foreach item='item' index='index' collection='stockIndicators' separator=','>"
            +       "(#{item.stockCode}, #{item.tradeDate}, #{item.pe}, #{item.peTTM},#{item.pb}, #{item.ps}, #{item.psTTM},#{item.dvRatio},#{item.dvTTM},#{item.totalMV})"
            + "</foreach>"
            + "</script>")
    void batchInsertStockIndicator(@Param("stockIndicators") List<StockIndicator> stockIndicators);
}
