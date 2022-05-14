package com.abiao.strategy;

import com.abiao.wallet.Wallet;
import com.abiao.model.StockPrice;
import com.abiao.model.TradeResult;

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
