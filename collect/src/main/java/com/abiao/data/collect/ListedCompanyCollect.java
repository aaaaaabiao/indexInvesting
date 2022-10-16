package com.abiao.data.collect;

import com.abiao.data.constant.AKDict;
import com.abiao.data.model.ListedCompany;
import com.abiao.data.model.StockIndicator;
import com.abiao.data.util.AKUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.helper.DataUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 上市公司基本信息采集
 * */
@Slf4j
public class ListedCompanyCollect implements Collect<ListedCompany> {



    @Override
    public List<ListedCompany> collect() {
        List<ListedCompany> listedCompanies = new ArrayList<>();
        try {
            List<Map<String, Object>> stockList = AKUtil.get(AKDict.stockInfoACodeName, null);
            for (Map<String, Object> stockMap : stockList) {
                String code = stockMap.get("code") + "";
                String name = stockMap.get("name") + "";
                Map<String, Object> params = new HashMap<>();
                params.put("symbol", code);
                List<Map<String, Object>> stockInfo = AKUtil.get(AKDict.stockIndividualInfo, params);
                String industry = null;
                Date inMarket = null;
                for (Map<String, Object> info : stockInfo) {
                    String item = info.get("item") + "";
                    String value = info.get("value") + "";
                    switch (item) {
                        case "行业":
                            industry = value;
                            break;
                        case "上市时间":
                            inMarket = DateUtils.parseDate(value, "yyyyMMdd");
                            break;
                        default:

                    }
                }
                ListedCompany listedCompany = new ListedCompany();
                listedCompany.setStockCode(code);
                listedCompany.setStockName(name);
                listedCompany.setIndustry(industry);
                listedCompany.setInMarketDate(DateFormatUtils.format(inMarket, "yyyy-MM-dd"));
                listedCompanies.add(listedCompany);
            }

            //收集上交所和深交所退市的股票

            List<ListedCompany> szseDelisting = distinct(szseDelisting());
            List<ListedCompany> sseDelisting = distinct(sseDelisting());
            listedCompanies.addAll(szseDelisting);
            listedCompanies.addAll(sseDelisting);


        } catch (Exception e) {
            log.error("ListedCompanyCollect exception: e:{}", e);
        }
        return listedCompanies;
    }



    /**
     * 深交所退市公司
     * */
    public List<ListedCompany> szseDelisting() throws IOException {

        Map<String, Object> params = new HashMap<>();
        params.put("indicator", "终止上市公司");

        List<Map<String, Object>> infos = AKUtil.get(AKDict.szseDelisting, params);

        List<ListedCompany> listedCompanies = new ArrayList<>();
        for (Map<String, Object> info : infos) {
            String stockCode = info.get("证券代码") + "";
            String stockName = info.get("证券简称") + "";
            String inMarketDate = info.get("上市日期") + "";
            String outMarketDate = info.get("终止上市日期") + "";

            ListedCompany listedCompany = new ListedCompany();
            listedCompany.setStockCode(stockCode);
            listedCompany.setStockName(stockName);
            listedCompany.setInMarketDate(inMarketDate);
            listedCompany.setOutMarketDate(outMarketDate);
            listedCompanies.add(listedCompany);
        }

        return listedCompanies.stream()
                .filter(item -> !(item.getStockCode().startsWith("900") || item.getStockCode().startsWith("200")))
                .collect(Collectors.toList());
    }

    /**
     * 上交所退市公司
     * */
    public List<ListedCompany> sseDelisting() throws IOException {

        List<Map<String, Object>> infos = AKUtil.get(AKDict.sseDelisting, null);

        List<ListedCompany> listedCompanies = new ArrayList<>();
        for (Map<String, Object> info : infos) {

            String stockCode = info.get("公司代码") + "";
            String stockName = info.get("公司简称") + "";
            String inMarketDate = info.get("上市日期") + "";
            String outMarketDate = info.get("暂停上市日期") + "";
//            inMarketDate = DateFormatUtils.format(DateUtils.parseDate(inMarketDate, "yyyyMMdd"), "yyyy-MM-dd");
//            outMarketDate = DateFormatUtils.format(DateUtils.parseDate(outMarketDate, "yyyyMMdd"), "yyyy-MM-dd");
            ListedCompany listedCompany = new ListedCompany();
            listedCompany.setStockCode(stockCode);
            listedCompany.setStockName(stockName);
            listedCompany.setInMarketDate(inMarketDate);
            listedCompany.setOutMarketDate(outMarketDate);
            listedCompanies.add(listedCompany);
        }

        return listedCompanies.stream()
                .filter(item -> !(item.getStockCode().startsWith("900") || item.getStockCode().startsWith("200")))
                .collect(Collectors.toList());
    }



    public List<ListedCompany> distinct(List<ListedCompany> listedCompanies) {

        List<ListedCompany> ret = new ArrayList<>();
        Set<String> stockCodeSet = new HashSet<>();
        for (ListedCompany listedCompany : listedCompanies) {
            String stockCode = listedCompany.getStockCode();
            if (!stockCodeSet.contains(listedCompany.getStockCode())) {
                ret.add(listedCompany);
                stockCodeSet.add(stockCode);
            }
        }
        return ret;
    }
}
