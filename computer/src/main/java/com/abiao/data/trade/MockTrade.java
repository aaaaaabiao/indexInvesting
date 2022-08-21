package com.abiao.data.trade;

import com.abiao.data.data.DataInterface;
import com.abiao.data.model.StockPrice;
import com.abiao.data.strategy.TradeStrategy;
import com.abiao.data.wallet.Wallet;
import com.abiao.data.model.TradeResult;

import java.util.Date;
import java.util.List;

public class MockTrade extends AbstractTrade{

    private String name;

    //交易开始时间
    private Date startTime;

    //交易结束时间
    private Date endTime;


    public MockTrade() {

    }

    public MockTrade(String name) {
        this.name = name;
    }


    public String name() {
        return name;
    }
    /**
     * 执行交易
     * */
    public void execute(TradeStrategy tradeStrategy,
                        DataInterface dataInterface,
                        Wallet wallet) {
        List<StockPrice> stockPrices = dataInterface.getStockPrices(startTime, endTime);
        for (StockPrice price : stockPrices) {
            TradeResult buyResult = tradeStrategy.buy(price, wallet);
            TradeResult sellResult = tradeStrategy.sell(price, wallet);
        }
    }


    public static void main(String[] args) {

    }
}
