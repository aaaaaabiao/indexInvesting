package com.abiao;

import com.abiao.data.DataInterface;
import com.abiao.data.DataInterfaceImpl;
import com.abiao.strategy.GridTradeStrategy;
import com.abiao.strategy.TradeStrategy;
import com.abiao.trade.MockTrade;
import com.abiao.trade.Trade;
import com.abiao.wallet.FixedWallet;
import com.abiao.wallet.Wallet;

public class Runner {
    public static void main(String[] args) {
        DataInterface dataInterface = new DataInterfaceImpl();
        TradeStrategy tradeStrategy = new GridTradeStrategy();
        Wallet wallet = new FixedWallet();
        Trade trade = new MockTrade();
        trade.execute(tradeStrategy, dataInterface, wallet);
    }
}
