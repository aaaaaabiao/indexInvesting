package com.abiao.data.dao.mapper;

import com.abiao.data.model.FollowIndex;
import com.abiao.data.model.IndexContent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 指数成分股mapper
 * */
public interface FollowIndexMapper {


    /**
     * 获取关注的指数
     * */
    @Select("select * from follow_index where follow = 1")
    List<FollowIndex> selectFollowIndex();

}
