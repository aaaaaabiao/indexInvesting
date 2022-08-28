package com.abiao.data.dao.mapper;

import com.abiao.data.model.IndexContent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IndexMapper {


     String indexContent = "index_content";

    /**
     * 获取某个指数某一天的所有成分股
     * */

    @Select("select stock_code from index_latest_content where in_date < #{date} and index_code = #{indexCode}"
            + " union "
            + "select stock_code from index_history_content where in_date < #{date} and out_date > #{date} and index_code = #{indexCode}")
    List<String> selectContentByIndexCodeAndDate(@Param("indexCode") String indexCode, @Param("date") String date);



    /**
     * 插入指数成分股
     * */
    @Insert("insert into index_content(index_code, index_name, stock_code, in_date, out_date) " +
            "values (#{indexCode}, #{indexName}, #{stockCode}, #{inDate}, #{outDate})")
    void insertIndexContext(IndexContent indexContent);
}
