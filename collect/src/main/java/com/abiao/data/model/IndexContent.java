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
     *
     * */
    private String indexCode;

    /**
     * 交易日
     * */
    private Date tradeDate;

    /**
     * 指数成分股
     * */
    private List<String> contentStock;
}
