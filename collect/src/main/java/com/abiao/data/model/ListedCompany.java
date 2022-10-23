package com.abiao.data.model;

import lombok.Data;
import lombok.ToString;



/**
 * 上市公司
 * */
@Data
@ToString
public class ListedCompany {

    /**
     * 股票代码
     * */
    private String stockCode;

    /**
     * 股票名称
     * */
    private String stockName;


    /**
     * 所属行业
     * */
    private String industry;


    /**
     * 上市时间
     * */
    private String inMarketDate;


    /**
     * 退市时间
     * */
    private String outMarketDate;


    /**
     * 创建时间
     * */
    private long updateTime = System.currentTimeMillis();


}
