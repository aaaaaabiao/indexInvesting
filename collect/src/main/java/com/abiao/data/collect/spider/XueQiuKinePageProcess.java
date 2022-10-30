package com.abiao.data.collect.spider;

import com.abiao.data.model.StockIndicator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 雪球k线数据
 * https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=SZ000971&begin=1665915382646&period=day&type=before&count=-10&indicator=kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance
 * */
@Slf4j
public class XueQiuKinePageProcess implements PageProcessor {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private Site site = Site.me().setRetrySleepTime(3).setSleepTime(1000).setTimeOut(10000)
            .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36")
            .addHeader("cookie", "device_id=0667a293ccc5e4278186e6537c064958; s=dc1548rf55; Hm_lvt_1db88642e346389874251b5a1eded6e3=1664696087,1666422891; xq_a_token=ae5fc472c3ac4a0910f1d64f4c84e313e3c62d82; xqat=ae5fc472c3ac4a0910f1d64f4c84e313e3c62d82; xq_r_token=126b64f24abcc1b2d0b168de6cfeecdd2220d891; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTY2OTU5NDUxMywiY3RtIjoxNjY3MDMxOTMyMzA1LCJjaWQiOiJkOWQwbjRBWnVwIn0.l2yB4lVfvFhGZxJ4GZWyV5G_isY2dP1geDeLK8eqP4SVoWXAZKtJ5_-Z_pRiA_Hg_YJk8efFbcXoeURaNh5a4TcxYlA5MNEYPdS1HX9Fxhx4wTfcgFwiTodgoOLYKAxlvaEDQBOGQrztQ4efCQi9qCpWzOMyp8GLmcTaJmJBnd3WURrYNdKSLnnaADYoiTLVuNN7c3_UtXvOWM5PryhdwEkgLxXeY1Cyo7OI7ByfcgJt_24Nup3zpnvE1R3XVZQAihQHAh25e76iGlIWq1_bA-sWQCTMdOCMy1xSHLjuaNGZdBkhvpEYlB3rEDjxL16NoiuLwTIPXfBWQkMCaRwbbQ; u=611667031990425; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1667031994");

    @Override
    public void process(Page page) {
        String raw = page.getRawText();
        List<Map<String, Object>> infos = new ArrayList<>();
        JSONObject rawJsonObj = JSON.parseObject(raw);

        if (rawJsonObj.containsKey("data")) {
            JSONObject data = rawJsonObj.getJSONObject("data");
            if (data.containsKey("column") && data.containsKey("item")) {
                JSONArray array = data.getJSONArray("column");
                String[] column = array.toArray(new String[array.size()]);

                JSONArray item = data.getJSONArray("item");
                for (Object obj : item) {
                    JSONArray values = (JSONArray) obj;
                    Map<String, Object> itemMap = new HashMap<>();
                    for (int i = 0; i < column.length; i++) {
                        itemMap.put(column[i], values.get(i));
                    }
                    infos.add(itemMap);
                }
            }
        }

        List<StockIndicator> stockIndicators = infos.stream().map(info -> {
            StockIndicator stockIndicator = new StockIndicator();
            stockIndicator.setPeTTM(((BigDecimal)info.get("pe")).doubleValue());
            stockIndicator.setPb(((BigDecimal)info.get("pb")).doubleValue());
            stockIndicator.setPsTTM(((BigDecimal)info.get("ps")).doubleValue());
            stockIndicator.setTotalMV(((BigDecimal)info.get("market_capital")).doubleValue() / 10000);
            Long timestamp = (Long) info.get("timestamp");
            Date date = new Date(timestamp);
            stockIndicator.setTradeDate(sdf.format(date));
            return stockIndicator;
        }).collect(Collectors.toList());

        if (stockIndicators.size() == 0) {
            log.error("xueqiu k line spider task exception! url:{}", page.getRequest().getUrl());
        }
        page.putField("stockIndicators", stockIndicators);
    }

    @Override
    public Site getSite() {
        return site;
    }
}
