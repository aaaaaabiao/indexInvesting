package com.abiao.data.dao.mapper;

import com.abiao.data.model.IndexContent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 指数成分股mapper
 * */
public interface IndexContentMapper{




    @Select("select * from index_content where stock_code=#{stockCode}")
    List<IndexContent> selectIndexContentByCode(String stockCode);



    /**
     * 获取某个指数最早的时间
     * */
    @Select("select in_date from index_content where index_code=#{indexCode} order by in_date limit 1")
    String selectIndexEarliestDateByIndexCode(String indexCode);

    /**
     * 获取某个指数的成分股(by特定时间)
     * */
    @Select("select * from index_content where index_code=#{indexCode} and in_date <= #{tradeDate} and (out_date > #{tradeDate} or out_date is null)")
    List<IndexContent> selectIndexContentByIndexCodeAndTradeDate(@Param("indexCode")String indexCode, @Param("tradeDate")String tradeDate);

    /**
     * 插入指数成分股
     * */
    @Insert("<script>"
            + "insert into index_content (index_code, index_name, stock_code, in_date, out_date, update_time) values "
            + "<foreach item='item' index='index' collection='indexContents' separator=','>"
            +       "(#{item.indexCode}, #{item.indexName}, #{item.stockCode}, #{item.inDate},#{item.outDate}, #{item.updateTime})"
            + "</foreach>"
            + "</script>")
    void batchInsert(@Param("indexContents") List<IndexContent> indexContents);





}
