package com.abiao.data;

import com.abiao.model.StockPrice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataInterfaceImpl implements DataInterface{
    public List<StockPrice> getStockPrices(Date startTime, Date endTime) {
        return new ArrayList<StockPrice>();
    }
}
