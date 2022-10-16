package com.abiao.data.model;

import lombok.Data;

/**
 * 指数估值
 * */
@Data
public class IndexValuation {

    String indexCode;

    String tradeDate;

    String pe;

    String peTTM;

    String pb;

}
