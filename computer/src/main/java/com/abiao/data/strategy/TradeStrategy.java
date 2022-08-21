package com.abiao.data.strategy;

import com.abiao.data.wallet.Wallet;
import com.abiao.data.model.StockPrice;
import com.abiao.data.model.TradeResult;

/**
 * 交易策略
 * */
public interface TradeStrategy {

    /**
     * 买
     * */
    TradeResult buy(StockPrice price, Wallet wallet);


    /**
     * 卖
     * */
    TradeResult sell(StockPrice price, Wallet wallet);

}
