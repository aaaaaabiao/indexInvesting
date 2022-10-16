package com.abiao.data.model;

import lombok.Data;


import java.util.Date;
import java.util.List;

/**
 * 指数成分股
 * */
@Data
public class IndexContent {

    /**
     * 指数代码
     * */
    private String indexCode;

    /**
     * 指数名称
     * */
    private String indexName;

    /**
     * 股票代码
     * */
    private String stockCode;


    /**
     * 加入时间
     * */
    private String inDate;


    /**
     * 退出时间
     * */
    private String outDate = "";


    /**
     * 更新时间
     * */
    private long updateTime = System.currentTimeMillis();


    public IndexContent(String indexCode, String indexName, String stockCode, String inDate, String outDate) {
        this.indexCode = indexCode;
        this.indexName = indexName;
        this.stockCode = stockCode;
        this.inDate = inDate;
        this.outDate = outDate;
    }
}
