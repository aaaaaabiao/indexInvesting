package com.abiao.trade;

import com.abiao.data.DataInterface;
import com.abiao.wallet.Wallet;
import com.abiao.model.StockPrice;
import com.abiao.model.TradeResult;
import com.abiao.strategy.TradeStrategy;

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
