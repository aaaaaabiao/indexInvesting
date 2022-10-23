package com.abiao.data.dao.mapper;



import com.abiao.data.model.IndexValuation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 指数估值mapper
 * */
public interface IndexValuationMapper {



    @Select("select trade_date from index_valuation_indicator where index_code = #{indexCode} order by trade_date desc limit 1;")
    String selectLatestTradeDateByIndexCode(String indexCode);


    /**
     * 插入指数成分股
     * */
    @Insert("<script>"
            + "insert into index_valuation_indicator (index_code, trade_date, pe_ew, update_time) values "
            + "<foreach item='item' index='index' collection='indexValuations' separator=','>"
            +       "(#{item.indexCode}, #{item.tradeDate}, #{item.peEw}, #{item.updateTime})"
            + "</foreach>"
            + "</script>")
    void batchInsert(@Param("indexValuations") List<IndexValuation> indexValuations);



    /**
     * 插入指数成分股
     * */
    @Insert("<script>"
            + "replace into index_valuation_indicator (index_code, trade_date, pe_ew, update_time) values "
            + "<foreach item='item' index='index' collection='indexValuations' separator=','>"
            +       "(#{item.indexCode}, #{item.tradeDate}, #{item.peEw}, #{item.updateTime})"
            + "</foreach>"
            + "</script>")
    void batchInsertOrUpdate(@Param("indexValuations") List<IndexValuation> indexValuations);

}
