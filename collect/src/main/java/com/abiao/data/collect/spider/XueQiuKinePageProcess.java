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
            .addHeader("cookie", "device_id=0667a293ccc5e4278186e6537c064958; Hm_lvt_1db88642e346389874251b5a1eded6e3=1664696087; xq_a_token=dd874a8d08edaeb9cdc493d9239c447738426b96; xqat=dd874a8d08edaeb9cdc493d9239c447738426b96; xq_r_token=7a97f1ad16a7e6074c744f820621875ef03d3f3d; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTY2ODI5ODQ3NywiY3RtIjoxNjY1ODMxOTYzMjU0LCJjaWQiOiJkOWQwbjRBWnVwIn0.RZH26cpd-zM-hksNid5o0MrmxvIADkbZ__yRmkz5nb8_ii5FfJdGPQ9D_z-6Dq3G3SqlKPI1KU4VIIHQTs1Dnyt5H7LCMyaMIzTraJsy5B5swAFDNTVtewBb9RE1TpCU61klThStSCp607RxkiXB8qahtufIRFaMxgub5j9ouDq5MFVdqHk1UGdLg5aY-tYaD1XUKo6TupA4WEeEqvep1fL3MFfmGgETFG_DGs_Hxy9JgeFvUF6daXWfZx-N_1ioSmOIhf6ACASAWkFfV4tKRU8t38dU3pJnHIDYNoHpoJQrjz84aJh0n36CtIXx1Q772MACZQ9_nQi0BFujag2w4g; u=851665831995554; s=dc1548rf55; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1665912587");;

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
            stockIndicator.setTotalMV(((BigDecimal)info.get("market_capital")).doubleValue());
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
