package com.abiao.data.collect;

import com.abiao.data.collect.spider.SinaStockIndexContentPageProcess;
import com.abiao.data.model.IndexContent;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;

import java.util.List;

public class IndexContentCollect implements Collect<IndexContent> {

    private static String sinaStockIndexContentApi = "https://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpXiangGuan/stockid/%s.phtml";

    private String stockCode;

    private Spider spider;

    public IndexContentCollect(String stockCode) {
        this.stockCode = stockCode;
        spider = Spider.create(new SinaStockIndexContentPageProcess());
    }


    @Override
    public List<IndexContent> collect() {
        ResultItems resultItems = spider.get(String.format(sinaStockIndexContentApi, stockCode));
        List<IndexContent> indexContents = resultItems.get("indexContents");
        indexContents.forEach(item -> item.setStockCode(stockCode));
        spider.close();
        return indexContents;
    }
}
