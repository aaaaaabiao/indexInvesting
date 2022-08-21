package com.abiao.data.model;

import lombok.Data;
import lombok.ToString;

import java.sql.Date;


/**
 * 股票基本指标信息(市盈率、市净率、股息率等)
 * */

@Data
@ToString
public class StockIndicator {
    /**
     * 股票代码
     * */
    private String stockCode;

    /**
     * 交易日
     * */
    private Date tradeDate;

    /**
     * 市盈率
     * */
    private Double pe;


    /**
     * 滚动市盈率
     * */
    private Double peTTM;


    /**
     * 市净率
     * */
    private Double pb;


    /**
     * 市销率
     * */
    private Double ps;

    /**
     * 滚动市销率
     * */
    private Double psTTM;


    /**
     * 股息率
     * */
    private Double dvRatio;

    /**
     * 滚动股息率
     * */
    private Double dvTTM;

    /**
     * 总市值
     * */
    private Double totalMV;
}
