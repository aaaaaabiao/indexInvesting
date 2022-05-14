package com.abiao.data;

import com.abiao.model.StockPrice;

import java.util.Date;
import java.util.List;

/**
 * 市场数据的抽象接口
 * */
public interface DataInterface {

    List<StockPrice> getStockPrices(Date startTime, Date endTime);
}
