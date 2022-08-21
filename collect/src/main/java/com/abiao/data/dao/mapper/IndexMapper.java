package com.abiao.data.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IndexMapper {


    /**
     * 获取某个指数某一天的所有成分股
     * */

    @Select("select stock_code from index_latest_content where in_date < #{date} and index_code = #{indexCode}"
            + " union "
            + "select stock_code from index_history_content where in_date < #{date} and out_date > #{date} and index_code = #{indexCode}")
    List<String> selectContentByIndexCodeAndDate(@Param("indexCode") String indexCode, @Param("date") String date);
}
