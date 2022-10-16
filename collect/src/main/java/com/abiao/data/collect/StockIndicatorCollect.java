package com.abiao.data.collect;

import com.abiao.data.collect.spider.XueQiuKinePageProcess;
import com.abiao.data.constant.AKDict;
import com.abiao.data.model.StockIndicator;
import com.abiao.data.util.AKUtil;
import com.abiao.data.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
 * 股票指标信息采集
 */
@Slf4j
public class StockIndicatorCollect implements Collect<StockIndicator> {

    private String stockCode;

    public StockIndicatorCollect(String stockCode) {
        this.stockCode = stockCode;
    }

    @Override
    public List<StockIndicator> collect() {
        List<StockIndicator> ret = new ArrayList<>();
        try {
            ret = akCollect();
        } catch (Exception akEx) {
            log.error("get stock indicator info fail, stock_code:{}, akEx:{}", stockCode, akEx);
            try {
                ret = xueqiuCollect();
            }catch (Exception xqEx) {
                log.error("get stock indicator info fail, stock_code:{}, xqEx:{}", stockCode, xqEx);
            }
        }
        return ret;
    }


    /**
     * 通过ak tool获取数据
     */
    private List<StockIndicator> akCollect() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", stockCode);
        List<StockIndicator> ret = new ArrayList<>();

        List<Map<String, Object>> infos = AKUtil.get(AKDict.stockIndicator, params);
        List<StockIndicator> stockIndicators = new ArrayList<>();
        for (Map<String, Object> info : infos) {
            String tradeDate = (info.get("trade_date") + "").split("T")[0];
            Object pe = info.get("pe");
            Object peTTM = info.get("pe_ttm");
            Object pb = info.get("pb");
            Object ps = info.get("ps");
            Object psTTM = info.get("ps_ttm");
            Object dvRatio = info.get("dv_ratio");
            Object dvTTM = info.get("dv_ttm");
            Object totalMv = info.get("total_mv");


            StockIndicator stockIndicator = new StockIndicator();
            stockIndicator.setStockCode(stockCode);
            stockIndicator.setTradeDate(tradeDate);
            stockIndicator.setPe(pe != null ? ((BigDecimal) pe).doubleValue() : null);
            stockIndicator.setPeTTM(peTTM != null ? ((BigDecimal) peTTM).doubleValue() : null);
            stockIndicator.setPb(pb != null ? ((BigDecimal) pb).doubleValue() : null);
            stockIndicator.setPs(ps != null ? ((BigDecimal) ps).doubleValue() : null);
            stockIndicator.setPsTTM(psTTM != null ? ((BigDecimal) psTTM).doubleValue() : null);
            stockIndicator.setDvRatio(dvRatio != null ? ((BigDecimal) dvRatio).doubleValue() : null);
            stockIndicator.setDvTTM(dvTTM != null ? ((BigDecimal) dvTTM).doubleValue() : null);
            stockIndicator.setTotalMV(totalMv != null ? ((BigDecimal) totalMv).doubleValue() : null);
            stockIndicators.add(stockIndicator);
        }
        ret = stockIndicators;
        return ret;
    }

    private List<StockIndicator> xueqiuCollect() {

        String xueqiuAPI = "https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=%s&begin=%s&period=day&type=before&count=-30&indicator=kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance";
        Spider spider = Spider.create(new XueQiuKinePageProcess());
        String request = String.format(xueqiuAPI, CommonUtil.getFullStockCode(stockCode) , System.currentTimeMillis());
        ResultItems resultItems = spider.get(request);
        List<StockIndicator> stockIndicators = resultItems.get("stockIndicators");
        stockIndicators.forEach(item -> item.setStockCode(stockCode));
        spider.close();
        return stockIndicators;
    }

}
