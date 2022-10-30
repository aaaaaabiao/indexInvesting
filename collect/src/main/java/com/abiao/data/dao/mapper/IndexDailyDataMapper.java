package com.abiao.data.dao.mapper;

import com.abiao.data.model.IndexDailyData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 股票指标mapper
 * */
public interface IndexDailyDataMapper {

    @Select("select * from index_data_daily where index_code=#{indexCode}")
    List<IndexDailyData> selectIndexDailyDataByIndexCode(String indexCode);



//    /**
//     * 查询多只股票某一天的全部指标
//     * */
//
//    @Select("<script>"
//            + "select * from index_data_daily where trade_date = #{date} and stock_code in "
//            + "<foreach item='item' index='index' collection='stockCodes' open='(' separator=',' close=')'>"
//            +       "#{item}"
//            + "</foreach>"
//            + "</script>")
//    List<IndexDailyData> selectByStockCodesAndDate(@Param("stockCodes") List<String> stockCodes, @Param("date") String date);


//    @Select("select trade_date from stock_indicator_daily order by trade_date desc limit 1")
//    String selectLatestTradeDate();


    /**
     * 插入指数成分股
     * */
    @Insert("<script>"
            + "insert into index_data_daily (index_code, trade_date, open, close, high, low, volume, amount, update_time) values "
            + "<foreach item='item' index='index' collection='IndexDailyDataList' separator=','>"
            +       "(#{item.indexCode}, #{item.tradeDate}, #{item.open}, #{item.close},#{item.high}, #{item.low}, #{item.volume},#{item.amount}, #{item.updateTime})"
            + "</foreach>"
            + "</script>")
    void batchInsertIndexDailyData(@Param("IndexDailyDataList") List<IndexDailyData> indexDailyDataList);




}
