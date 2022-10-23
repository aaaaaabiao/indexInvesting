package com.abiao.data.model;

import lombok.Data;

/**
 * 指数估值
 * */
@Data
public class IndexValuation {


    /**
     * 自增ID
     * */
    private Integer id;

    /**
     * 指数代码
     * */
    private String indexCode;


    /**
     * 交易日
     * */
    private String tradeDate;


    /**
     * 等全PE
     * */
    private Double peEw;


    /**
     * 等权PB
     * */
    private Double pbEw;


    /**
     * 更新时间
     * */

    private Long updateTime = System.currentTimeMillis();

}
