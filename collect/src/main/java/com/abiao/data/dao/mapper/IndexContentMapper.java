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
