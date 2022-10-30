package com.abiao.data.model;

import lombok.Data;

/**
 * 关注的指数
 * */
@Data
public class FollowIndex {

    /**
     * 指数代码
     * */
    private String indexCode;

    /**
     * 指数名称
     * */
    private String indexName;


    /**
     * 加入时间
     * */
    private String inDate;


    /**
     * 是否添加关注
     * */
    private Integer follow;


    /**
     * 更新时间
     * */
    private long updateTime = System.currentTimeMillis();

}
