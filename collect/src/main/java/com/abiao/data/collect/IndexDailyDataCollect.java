package com.abiao.data.collect;

import com.abiao.data.constant.AKDict;
import com.abiao.data.model.IndexDailyData;
import com.abiao.data.model.ListedCompany;
import com.abiao.data.util.AKUtil;
import com.abiao.data.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 指数行情数据
 * */
@Slf4j
public class IndexDailyDataCollect implements Collect<IndexDailyData> {

    private String indexCode;

    private String startDate;

    private String endDate;

    public IndexDailyDataCollect(String indexCode) {
        this.indexCode = indexCode;
    }

    public IndexDailyDataCollect(String indexCode, String startDate, String endDate) {
        this(indexCode);
        this.startDate = startDate;
        this.endDate = endDate;
    }



    @Override
    public List<IndexDailyData> collect() {
        List<IndexDailyData> indexDailyDataList = new ArrayList<>();
        try {
            indexDailyDataList = akCollect();
        } catch (Exception e) {
            log.error("IndexDailyDataCollect exception: e:{}, indexCode:{}", e, indexCode);
        }
        return indexDailyDataList;
    }


    public List<IndexDailyData> akCollect() throws IOException {

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", indexCode);
        params.put("period", "daily");

        if (StringUtils.isNotBlank(startDate)) params.put("start_date", startDate);
        if (StringUtils.isNotBlank(endDate)) params.put("start_date", endDate);

        List<Map<String, Object>> infos = AKUtil.get(AKDict.indexZhAHist, params);
        List<IndexDailyData> indexDailyDataList = infos.stream().map(item -> {
            IndexDailyData indexDailyData = new IndexDailyData();

            indexDailyData.setIndexCode(indexCode);
            indexDailyData.setTradeDate(item.get("日期") + "");
            indexDailyData.setOpen(CommonUtil.bigDecimalToDouble(item.get("开盘")));
            indexDailyData.setClose(CommonUtil.bigDecimalToDouble(item.get("收盘")));
            indexDailyData.setHigh(CommonUtil.bigDecimalToDouble(item.get("最高")));
            indexDailyData.setLow(CommonUtil.bigDecimalToDouble(item.get("最低")));
            indexDailyData.setVolume(((Integer)item.get("成交量")));
            indexDailyData.setAmount(CommonUtil.bigDecimalToDouble(item.get("成交额")));
            return indexDailyData;
        }).collect(Collectors.toList());

        return indexDailyDataList;

    }


}



