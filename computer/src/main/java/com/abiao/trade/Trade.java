package com.abiao.trade;

import com.abiao.data.DataInterface;
import com.abiao.wallet.Wallet;
import com.abiao.strategy.TradeStrategy;

/**
 * 交易接口
 * */
public interface Trade {

    void execute(TradeStrategy tradeStrategy,
                 DataInterface dataInterface,
                 Wallet wallet);
}
