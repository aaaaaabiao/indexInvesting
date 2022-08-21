package com.abiao.data.trade;

import com.abiao.data.data.DataInterface;
import com.abiao.data.strategy.TradeStrategy;
import com.abiao.data.wallet.Wallet;

/**
 * 交易接口
 * */
public interface Trade {

    void execute(TradeStrategy tradeStrategy,
                 DataInterface dataInterface,
                 Wallet wallet);
}
