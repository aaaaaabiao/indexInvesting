package com.abiao.data.model;


import lombok.Data;

/**
 * 指数行情数据
 * */
@Data
public class IndexDailyData {

    /**
     * 指数代码
     * */
    private String indexCode;

    /**
     * 交易日
     * */
    private String tradeDate;

    /**
     * 开盘
     * */
    private Double open;

    /**
     * 收盘
     * */
    private Double close;

    /**
     * 最高
     * */
    private Double high;

    /**
     * 最低
     * */
    private Double low;

    /**
     * 成交量
     * */
    private Integer volume;

    /**
     * 成交额
     * */
    private Double amount;


    private Long updateTime = System.currentTimeMillis();

}
