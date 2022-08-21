package com.abiao.data;

import com.abiao.data.data.DataInterface;
import com.abiao.data.data.DataInterfaceImpl;
import com.abiao.data.strategy.TradeStrategy;
import com.abiao.data.strategy.GridTradeStrategy;
import com.abiao.data.trade.MockTrade;
import com.abiao.data.trade.Trade;
import com.abiao.data.wallet.FixedWallet;
import com.abiao.data.wallet.Wallet;

public class Runner {
    public static void main(String[] args) {
        DataInterface dataInterface = new DataInterfaceImpl();
        TradeStrategy tradeStrategy = new GridTradeStrategy();
        Wallet wallet = new FixedWallet();
        Trade trade = new MockTrade();
        trade.execute(tradeStrategy, dataInterface, wallet);
    }
}
